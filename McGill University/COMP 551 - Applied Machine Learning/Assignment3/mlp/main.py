import torch
import torchvision
import torchvision.transforms as transforms
import matplotlib.pyplot as plt
import numpy as np
import random
from MultiLayeredPerceptron import MultiLayeredPerceptron

def main():
    # Download and load in data
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))])
    trainset = torchvision.datasets.CIFAR10(root='./data', train=True, download=True, transform=transform)
    trainloader = torch.utils.data.DataLoader(trainset, batch_size=1, shuffle=True, num_workers=0)
    testset = torchvision.datasets.CIFAR10(root='./data', train=False, download=True, transform=transform)
    testloader = torch.utils.data.DataLoader(testset, batch_size=1, shuffle=True, num_workers=0)
    classes = ('plane', 'car', 'bird', 'cat', 'deer', 'dog', 'frog', 'horse', 'ship', 'truck')

    print("Flattening the images into 1D vectors of size 3072:")
    trainImagesArray = []
    validationImagesArray = []
    trainLabelsArray = []
    validationLabelsArray = []
    i = 0
    for data in trainloader:
        images, labels = data
        for image, label in zip(images, labels):
            labelArray = np.zeros(10)
            labelArray[label] = 1
            if (i < 40000):
                trainImagesArray.append(image.numpy().reshape(1, 3072))
                trainLabelsArray.append(labelArray.reshape(1, 10))
            elif (i < 50000):
                validationImagesArray.append(image.numpy().reshape(1, 3072))
                validationLabelsArray.append(labelArray.reshape(1, 10))
            else:
                break
            i = i + 1

    testImagesArray = []
    testLabelsArray = []
    for data in testloader:
        images, labels = data
        for image, label in zip(images, labels):
            testImagesArray.append(image.numpy().reshape(1, 3072))
            labelArray = np.zeros(10)
            labelArray[label] = 1
            testLabelsArray.append(labelArray.reshape(1, 10))
    print("Done")

    print("Deleting PyTorch Loaders")
    del trainset
    del trainloader
    del testset
    del testloader
    print("Done")

    print("Initialize MLP")
    mlpShape = np.array([3072, 3072, 120, 10])
    mlp = MultiLayeredPerceptron(mlpShape, activation='Sigmoid', derivative=None, externalActivation=False, cost='L1', lowerWeightBound=-1, upperWeightBound=1, lowerBiasBound=-1, upperBiasBound=1, num_epochs=12, early_stop=3, batch_size=8, learningRate=0.1)
    print("Done")

    print("Training MLP")
    validationAccuracyPerEpoch = mlp.train(trainImagesArray, trainLabelsArray, validationImagesArray, validationLabelsArray)
    print("Done")

    print("Validation Accuracy Per Epoch:")
    print(validationAccuracyPerEpoch)

    print("Calculating accuracy on train set")
    correctCount = 0
    for image, label in zip(trainImagesArray, trainLabelsArray):
        predictedLabelArray = mlp.predict(image[0])
        predictedLabel = np.argmax(predictedLabelArray)
        actualLabel = np.argmax(label)
        if (predictedLabel == actualLabel):
            correctCount = correctCount + 1
    print("Accuracy:")
    print(correctCount/len(trainImagesArray))

    print("Calculating accuracy on test set")
    correctCount = 0
    for image, label in zip(testImagesArray, testLabelsArray):
        predictedLabelArray = mlp.predict(image[0])
        predictedLabel = np.argmax(predictedLabelArray)
        actualLabel = np.argmax(label)
        if (predictedLabel == actualLabel):
            correctCount = correctCount + 1
    print("Accuracy:")
    print(correctCount/len(testImagesArray))

    print("mlp.mlpShape")
    print(mlp.mlpShape)
    print("mlp.activation")
    print(mlp.activation)
    print("mlp.cost")
    print(mlp.cost)
    print("mlp.lowerWeightBound")
    print(mlp.lowerWeightBound)
    print("mlp.upperWeightBound")
    print(mlp.upperWeightBound)
    print("mlp.lowerBiasBound")
    print(mlp.lowerBiasBound)
    print("mlp.upperBiasBound")
    print(mlp.upperBiasBound)
    print("mlp.num_epochs")
    print(mlp.num_epochs)
    print("mlp.early_stop")
    print(mlp.early_stop)
    print("mlp.batch_size")
    print(mlp.batch_size)
    print("mlp.learningRate")
    print(mlp.learningRate)

if __name__ == "__main__":
    main()
