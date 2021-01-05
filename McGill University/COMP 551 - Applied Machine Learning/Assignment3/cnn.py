import torch
import torchvision
import torchvision.transforms as transforms
import matplotlib.pyplot as plt
import numpy as np
import math
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import time


########################## This block is reserved for all the hyperparameters (except the optimizer, which is found right after the definition of the Net) for easy lookup ############################
SGD_batch_size = 8
convolutional_parameters_array = [[12,12,15,15,20,20],[4,2,4,2,4,2],[1,2,1,2,1,2],[3,1,3,1,3,1]] # params_array is a matrix of 4 rows containing the parameters for the convolution layers of the network. The columns represent the layers in sequential order (with repetitions, say, for each time we re-execute a max pooling): row 1: out_channels; row 2: kernel sizes; row 3: strides; row 4: padding. In pooling layers, we keep the same number of out_channels as the previous convolution layer
network_nonlinearity = F.leaky_relu # We define the nonlinear activation function used across the entire network
criterion = nn.CrossEntropyLoss() # We define our loss function: Cross-Entropy
early_stopping_tolerance = 0.001 # if the validation loss decreases by less than this value after "number_of_epochs_for_loss_improvement" epochs, we halt the training
number_of_epochs_for_loss_improvement = 3
epoch_number = 24 # Pre-defining the maximal number of epochs for the train loop

########################## End of hyperparameters block ##################################################################

# Importing the dataset and normalizing it:
transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))])
old_trainset = torchvision.datasets.CIFAR10(root='./data', train=True, download=True, transform=transform)
testset = torchvision.datasets.CIFAR10(root='./data', train=False, download=True, transform=transform)
testloader = torch.utils.data.DataLoader(testset, batch_size=SGD_batch_size, shuffle=False, num_workers=2)
classes = ('plane', 'car', 'bird', 'cat','deer', 'dog', 'frog', 'horse', 'ship', 'truck')
trainset, valset = torch.utils.data.random_split(old_trainset, [40000, 10000]) # We split the train set into 40 000 instances for train (80%) and 10 000 for validation (20%)
trainloader = torch.utils.data.DataLoader(trainset, batch_size=SGD_batch_size, shuffle=True, num_workers=2) # train set
valloader = torch.utils.data.DataLoader(valset, batch_size=SGD_batch_size, shuffle=False, num_workers=2) # validation set


# We write a helper function to find the number of input features for the linear layer as a function of the parameters for the convolutions
def calculate_total_output(params_array):
    params_array = np.array(params_array) # Converting to numpy array for convenience here
    width = 32
    height = 32
    size = np.shape(params_array)
    j = size[1]
    for m in range(j):
        width = math.floor(((width+2*params_array[3,m]-params_array[1,m])/params_array[2,m])) + 1
        height = math.floor(((height+2*params_array[3,m]-params_array[1,m])/params_array[2,m])) + 1
    depth = params_array[0,j-1]
    total = width*height*depth
    return total


# Defining a convolutional neural network. This is the most important part.
class Net(nn.Module):
    def __init__(self, params_array, nonlin):
        super(Net, self).__init__()
        self.nonlin = nonlin
        self.params_array = params_array
        self.conv1 = nn.Conv2d(in_channels = 3, out_channels = self.params_array[0][0], kernel_size = self.params_array[1][0], stride = self.params_array[2][0], padding = self.params_array[3][0]) # First layer. First argument is the number of channels of the input image (3 since they are colored in this case). Second argument: Number of output channels of the layer. Third argument : width and height of the square convolution filter 
        self.pool = nn.MaxPool2d(kernel_size = self.params_array[1][1], stride = self.params_array[2][1], padding = self.params_array[3][1]) # max pooling layer 
        self.conv2 = nn.Conv2d(in_channels = self.params_array[0][0], out_channels = self.params_array[0][2], kernel_size = self.params_array[1][2], stride = self.params_array[2][2], padding = self.params_array[3][2]) # Second convolution layer. The number of input channels matches the number of output channels from the first convolution layer
        self.conv3 = nn.Conv2d(in_channels = self.params_array[0][2], out_channels = self.params_array[0][4], kernel_size = self.params_array[1][4], stride = self.params_array[2][4], padding = self.params_array[3][4]) # I inserted an additional convolutional layer myself
        self.total = calculate_total_output(self.params_array)
        self.fc1 = nn.Linear(in_features = self.total, out_features = 1000) # These are just linear transformations to the inputs (y = Ax+b) where A is an (out_features x in_features) matrix, like in MLP's 
        self.fc2 = nn.Linear(1000, 100)
        self.fc3 = nn.Linear(100,50)
        self.fc4 = nn.Linear(50, 10)
        self.dropout = nn.Dropout(0.5) # Introducing dropout as a regularization technique. Probability of resetting an unit to 0 is p = 0.5

    def forward(self, x): # This is the sequential transformation that the input features undergo. It's also here where we define the activation function:
        x = self.pool(self.nonlin(self.conv1(x))) # First we feed the raw input image into the first convolutional layer. Then we apply the relu nonlinearity to the output of the convolution (with the biases included) and we apply a max pooling to reduce the dimension of the output and to increase the likelihood of detection of some important features in the image
        x = self.pool(self.nonlin(self.conv2(x))) # Repeat previous step
        x = self.pool(self.nonlin(self.conv3(x))) # This is with my own added layer
        x = x.view(-1, self.total) # Change the dimensions of x in order to feed it into the linear layers
        x = self.nonlin(self.fc1(x)) # now we feed the outputs into the equivalent of an MLP with custom hidden layers and user defined activation function in each layer
        x = self.dropout(self.nonlin(self.fc2(x)))
        x = self.nonlin(self.fc3(x))
        x = self.fc4(x)
        return x


net = Net(params_array = convolutional_parameters_array, nonlin = network_nonlinearity)
optimizer = optim.SGD(net.parameters(), lr=0.001, momentum = 0.9) # This is the only hyperparameter not at the beginning of the file, because I need to define the net before
# optimizer = optim.Adam(net.parameters(), lr=0.001) # This is the only hyperparameter not at the beginning of the file, because I need to define the net before

# Transferring the net to cuda device (to make computations faster)
device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
net.to(device)




# Just to see how much time it takes to train
time_before_training = time.time()

# Training and plotting section:
x_axis = []
accuracies = [] # Note: the variable "accuracies" stores accuracies on the test set, not on the validation set 
accuracies_on_validation_set = [] # This stores accuracies on the validation set, the actual metric used for hyperparameter tuning
validation_losses = []
plt.figure(figsize=(13,5))
plt.xlabel('Number of epochs used')
plt.ylabel('Accuracy (%)')
plt.title("Accuracy of the CNN on the test and validation sets\nas a function of the number of epochs used for training")

# Actual training loop:
terminate_training = False
for epoch in range(epoch_number):  
    if terminate_training == True:
      break
    running_loss = 0.0
    x_axis.append(epoch+1)
    for i, data in enumerate(trainloader, 0):

        # get the inputs; data is a list of [inputs, labels]
        # inputs, labels = data
        inputs, labels = data[0].to(device), data[1].to(device)
        # zero the parameter gradients
        optimizer.zero_grad()

        # forward + backward + optimize
        outputs = net(inputs)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()

        # print statistics
        running_loss += loss.item()
        if i % 2000 == 1999:    # print every 2000 mini-batches
            print('[%d, %5d] loss: %.3f' %
                  (epoch + 1, i + 1, running_loss / 2000))
            running_loss = 0.0

    # Between the epochs, we make predictions on the test set and append the accuracy to the plot. This is NOT used for hyperparameter tuning, but rather because of the project instructions demands it:
    correct = 0
    total = 0
    with torch.no_grad():
        for data in testloader:
            # images, labels = data
            images, labels = data[0].to(device), data[1].to(device)
            outputs = net(images)
            _, predicted = torch.max(outputs.data, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()
    accuracies.append(100*correct/total)

    # We make predictions on the validation set and append the accuracy to the plot. This IS the metric used for hyperparameter tuning:
    correct = 0
    total = 0
    with torch.no_grad():
        for data in valloader:
            # images, labels = data
            images, labels = data[0].to(device), data[1].to(device)
            outputs = net(images)
            _, predicted = torch.max(outputs.data, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()
    accuracies_on_validation_set.append(100*correct/total)
    

    # Also, between the epochs, we make predictions on the validation set and append the validation loss to the second plot:
    validation_loss = 0.0
    for i, data in enumerate(valloader, 0):
        # get the inputs; data is a list of [inputs, labels]        
        inputs, labels = data[0].to(device), data[1].to(device)       
        outputs = net(inputs)
        loss = criterion(outputs, labels)        
        validation_loss += loss.item()

    validation_loss = validation_loss/10000 # normalizing
    validation_losses.append(validation_loss) # adding to the plot
    if epoch > number_of_epochs_for_loss_improvement - 1:
        last_loss = validation_losses[-1]
        loss_to_improve = validation_losses[-1 - number_of_epochs_for_loss_improvement]
        if loss_to_improve - last_loss < early_stopping_tolerance:
            terminate_training = True # We terminate the training loop






plt.plot(x_axis, accuracies, 'go', label = 'Test', markersize = 3)
plt.plot(x_axis, accuracies_on_validation_set, 'ro', label = 'Validation', markersize = 3)
plt.legend(loc="upper left", bbox_to_anchor = (1,0.5))
plt.savefig("plots/AccuracyVsEpochs.png",dpi=100)
plt.figure(figsize=(10,5))
plt.plot(x_axis, validation_losses, 'bo', markersize = 5)
plt.title("Loss on the validation set for each training epoch of the CNN\nwith early stop tolerance of " + str(early_stopping_tolerance) +" applicable over "+str(number_of_epochs_for_loss_improvement)+ " epochs")
plt.xlabel('epoch number')
plt.ylabel('loss normalized by the size of the validation set')
plt.savefig("plots/ValidationLossVsEpochs.png",dpi=100)

print('Finished Training')
time_finished_training = time.time()
print('Training time: ', time_finished_training - time_before_training)


# Summary section:
best_accuracy = max(accuracies_on_validation_set) # Stores the best recorded accuracy on the validation set
index_of_best_num_epochs = accuracies_on_validation_set.index(best_accuracy)
best_num_epochs = x_axis[index_of_best_num_epochs] # Stores the number of training epochs for which we got the best accuracy on the validation set
print('The best accuracy of the network on the validation set is '+str(best_accuracy)+' % and was obtained after '+ str(best_num_epochs)+ ' training epochs.')

best_true_accuracy = accuracies[index_of_best_num_epochs] # We look into the test accuracies array for the entry corresponding to the number of epochs that yielded the best validation accuracy
print('The best accuracy of the network on the test set is '+str(best_true_accuracy)+' % and was obtained after '+ str(best_num_epochs)+ ' training epochs.')

# Getting statistics about the performance per class (note that this is after the last training epoch, which might not necessarily be the best one):
class_correct = list(0. for i in range(10))
class_total = list(0. for i in range(10))
with torch.no_grad():
    for data in testloader:
        # images, labels = data
        images, labels = data[0].to(device), data[1].to(device)
        outputs = net(images)
        _, predicted = torch.max(outputs, 1)
        c = (predicted == labels).squeeze()
        for i in range(4):
            label = labels[i]
            class_correct[label] += c[i].item()
            class_total[label] += 1


for i in range(10):
    print('Accuracy of %5s : %2d %%' % (
        classes[i], 100 * class_correct[i] / class_total[i]))
