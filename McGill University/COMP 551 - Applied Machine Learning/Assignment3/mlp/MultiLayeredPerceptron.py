import numpy as np

class MultiLayeredPerceptron:
    def __init__(self, mlpShape, activation='Sigmoid', derivative=None, externalActivation=False, cost='L1', lowerWeightBound=-1, upperWeightBound=1, lowerBiasBound=-1, upperBiasBound=1, num_epochs=12, early_stop=3, batch_size=8, learningRate=0.1):
        # Number of Layers and Number of nodes in each layer
        self.mlpShape = mlpShape
        # Array to hold the weights and biases
        self.mlpWeights = []
        self.mlpBiases = []
        # If activation is an internally used activation string then uses pre-built strategies, otherwise activation and derivative MUST contain functions
        self.activation = activation
        self.derivative = derivative
        self.externalActivation = externalActivation
        self.cost = cost
        # Bounds for random weights/biases intialization (used if no initialization strategy is being used)
        self.lowerWeightBound = lowerWeightBound
        self.upperWeightBound = upperWeightBound
        self.lowerBiasBound = lowerBiasBound
        self.upperBiasBound = upperBiasBound

        self.num_epochs = num_epochs
        self.early_stop = early_stop
        self.batch_size = batch_size
        self.learningRate = learningRate

        if (activation == 'Sigmoid'):
            # Initialize the weights and biases to random numbers
            for i in range(self.mlpShape.size - 1):
                self.mlpWeights.append(np.random.uniform(self.lowerWeightBound, self.upperWeightBound, (self.mlpShape[i+1], self.mlpShape[i])))
                self.mlpBiases.append(np.random.uniform(self.lowerBiasBound, self.upperBiasBound, (1, self.mlpShape[i+1])))
        elif (activation == 'ReLu'):
            for i in range(self.mlpShape.size - 1):
                self.mlpWeights.append(np.random.randn(self.mlpShape[i+1], self.mlpShape[i]) * np.sqrt(2/self.mlpShape[i]))
                self.mlpBiases.append(np.zeros((1, self.mlpShape[i+1])))
        elif (activation == 'Softmax'):
            for i in range(self.mlpShape.size - 1):
                self.mlpWeights.append(np.random.uniform(self.lowerWeightBound, self.upperWeightBound, (self.mlpShape[i+1], self.mlpShape[i])))
                self.mlpBiases.append(np.random.uniform(self.lowerBiasBound, self.upperBiasBound, (1, self.mlpShape[i+1])))
        elif (activation == 'Tanh'):
            for i in range(self.mlpShape.size - 1):
                self.mlpWeights.append(np.random.uniform(self.lowerWeightBound, self.upperWeightBound, (self.mlpShape[i+1], self.mlpShape[i])))
                self.mlpBiases.append(np.random.uniform(self.lowerBiasBound, self.upperBiasBound, (1, self.mlpShape[i+1])))
        else:
            for i in range(self.mlpShape.size - 1):
                self.mlpWeights.append(np.random.uniform(self.lowerWeightBound, self.upperWeightBound, (self.mlpShape[i+1], self.mlpShape[i])))
                self.mlpBiases.append(np.random.uniform(self.lowerBiasBound, self.upperBiasBound, (1, self.mlpShape[i+1])))

    # Internally supported activation functions (derivatives are calculated from activated values (ie dsigmoid will be called with x=sigmoid(x)))
    def sigmoid(self, x):
        return 1 / (1 + np.exp(-x))

    def dsigmoid(self, x):
        return x * (1 - x)

    def reLu(self, x):
        return np.maximum(0, x)

    def dreLu(self, x):
        return (x > 0) * 1

    def tanh(self, x):
        return np.tanh(x)

    def dtanh(self, x):
        return 1 / (np.cosh(x)**2)

    # Mapping function to apply activation function to matrix
    def mapActivation(self, x):
        if (self.activation == 'Sigmoid'):
            return self.sigmoid(x)
        elif (self.activation == 'ReLu'):
            return self.reLu(x)
        elif (self.activation == 'Softmax'):
            return self.softmax(x)
        elif (self.activation == 'Tanh'):
            return self.tanh(x)
        elif (self.externalActivation):
            return np.array(list(map(self.activation, x)))
        else:
            print("Undefined Activation Function")
            return None

    # Mapping function to apply derivative of activation function to matrix
    def mapDerivative(self, x):
        if (self.activation == 'Sigmoid'):
            return self.dsigmoid(x)
        elif (self.activation == 'ReLu'):
            return self.dreLu(x)
        elif (self.activation == 'Softmax'):
            return self.dsoftmax(x)
        elif (self.activation == 'Tanh'):
            return self.dtanh(x)
        elif (self.externalActivation):
            return np.array(list(map(self.derivative, x)))
        else:
            print("Undefined Activation Function")
            return None

    # Cost functions
    def cost_l1(self, predicted, actual):
        return (actual - predicted)

    # Mapping function to apply cost function to matrix (1D usually)
    def computeCost(self, predicted, actual):
        if (self.cost == 'L1'):
            return self.cost_l1(predicted, actual)
        else:
            print("Undefined Cost Function")
            return None

    # Feed Forward Prediction
    def predict(self, x):
        if (x.size != self.mlpShape[0]):
            print("Incorrect Size")
            return
        for weights, biases in zip(self.mlpWeights, self.mlpBiases):
            x = np.dot(weights, x)
            # Indexing at 0 cause of how the np.uniform function works for creating 1D Matices
            x = np.add(x, biases[0])
            x = self.mapActivation(x)
        return x

    # Implementation of Mini-Batch gradient descent (inputs = full set of inputs)
    def train(self, trainInputs, trainTargets, validationInputs, validationTargets):
        validationAccuracy = []
        consecutive_failure = 0
        for j in range(self.num_epochs):
            print("Epoch" + str(j) + ":")
            # First loop over all the trainInputs and trainTargets and batch together trainInputs and trainTargets sending them to the batch function every batch_size iterations
            i = 0
            batch_trainInputs = []
            batch_trainTargets = []
            for input, target in zip(trainInputs, trainTargets):
                if (i % self.batch_size == 0):
                    batch_trainInputs = []
                    batch_trainTargets = []
                i = i + 1
                batch_trainInputs.append(input)
                batch_trainTargets.append(target)
                if (i % self.batch_size == 0):
                    print(str(float(i)/float(len(trainInputs))*100) + "%")
                    self.batch(np.array(batch_trainInputs).reshape(self.batch_size, self.mlpShape[0]), np.array(batch_trainTargets).reshape(self.batch_size, self.mlpShape[len(self.mlpShape) - 1]))
            if (not i % self.batch_size == 0):
                print(str(float(i)/float(len(trainInputs))*100) + "%")
                self.batch(np.array(batch_trainInputs).reshape(i % self.batch_size, self.mlpShape[0]), np.array(batch_trainTargets).reshape(i % self.batch_size, self.mlpShape[len(self.mlpShape) - 1]))

            # validationAccuracy
            print("Computing Validation Accuracy for Epoch" + str(j))
            correctCount = 0
            k = 0
            for image, label in zip(validationInputs, validationTargets):
                predictedLabelArray = self.predict(image[0])
                predictedLabel = np.argmax(predictedLabelArray)
                actualLabel = np.argmax(label)
                if (predictedLabel == actualLabel):
                    correctCount = correctCount + 1
                if (k % self.batch_size == 0):
                    print(str(k/len(validationInputs)*100) + "%")
                k = k + 1
            print("Epoch" + str(j) + " Validation Accuracy:")
            print(correctCount/len(validationInputs))
            validationAccuracy.append(correctCount/len(validationInputs))
            if (len(validationAccuracy) > 1):
                if (validationAccuracy[len(validationAccuracy) - 1] - validationAccuracy[len(validationAccuracy) - 2] < 0.05):
                    consecutive_failure = consecutive_failure + 1
                else:
                    consecutive_failure = 0
            if (consecutive_failure >= self.early_stop - 1):
                return validationAccuracy
        return validationAccuracy

    # One epoch of the MLP backpropogation gradient descent (trains on batch of whatever size is input)
    def batch(self, inputs, targets):
        # For each input in the batch, compute a full calculation of the gradients for both weights and biases
        # making sure to store the gradients.  Compute the average of the gradients across each input and then
        # use the average gradients to update all weights and biases.

        # weightArray containing inputs.size weightsGradients
        # weightsGradients is an array of numpy matrices of shapes dependent on the weights in self.mlpWeights
        weightArray = []

        # biasArray containing inputs.size biasesGradients
        # biasesGradients is an array of numpy matrices of shapes dependent on the biases in self.mlpBiases
        biasArray = []

        # Perform the following calculations for each input of the batch one by one storing the weightsGradients and biasesGradients in the above arrays
        for input, target in zip(inputs, targets):
            # Input is going into the outputLayerArray because the "inputLayer" is being considered
            # a layer of nodes so the "inputs" are coming from the "output" of the "inputLayer"
            outputLayerArray = [input]
            inputLayerArray = []
            # Feed Forward a single input from the batch in a loop over all inputs from the batch
            # (storing the pre-activated and post-activated values for each layer in inputLayerArray and outputLayerArray respectively)
            i = 0
            for weights, biases in zip(self.mlpWeights, self.mlpBiases):
                inputToLayer = np.dot(weights, outputLayerArray[i])
                # Indexing at 0 cause of how the np.uniform function works for creating 1D Matices
                inputToLayer = np.add(inputToLayer, biases[0])
                inputLayerArray.append(inputToLayer)
                outputFromLayer = self.mapActivation(inputLayerArray[i])
                outputLayerArray.append(outputFromLayer)
                i = i + 1
            # Compute the errors of the output nodes
            errors = [self.computeCost(outputLayerArray[i], target)]

            # Back Propogation starts here
            weightsGradients = []
            biasesGradients = []
            j = 0
            for weights, biases in zip(reversed(self.mlpWeights), reversed(self.mlpBiases)):
                # Back-propogate the errors
                weightsT = np.transpose(weights)
                errors.append(np.dot(weightsT, errors[j]))
                # Compute the gradients for each layer
                gradient = self.mapDerivative(outputLayerArray[i])
                gradient = np.multiply(gradient, errors[j])
                gradient = np.multiply(gradient, self.learningRate)
                biasesGradients.insert(0, gradient)
                # Use the gradient vector and compute the gradient Matrix of the Weights
                gradient = gradient.reshape(gradient.size, 1)
                outputT = np.transpose(outputLayerArray[i-1])
                outputT = outputT.reshape(1, outputT.size)
                gradientMatrix = np.dot(gradient, outputT)
                weightsGradients.insert(0, gradientMatrix)
                j = j + 1
                i = i - 1
                if (j == len(self.mlpWeights)):
                    break
            weightArray.append(weightsGradients)
            biasArray.append(biasesGradients)

        # Using the computed weightsGradients and biasesGradients and calculating an average over the batch
        averageWeightsGradients = []
        averageBiasesGradients = []
        for i in range(self.mlpShape.size - 1):
            averageWeightsGradients.append(np.zeros((self.mlpShape[i+1], self.mlpShape[i])))
            averageBiasesGradients.append(np.zeros((1, self.mlpShape[i+1])))

        # Sum the weightsGradients
        for weightsGradients in weightArray:
            tempSum = []
            for weights, averages in zip(weightsGradients, averageWeightsGradients):
                tempSum.append(np.add(weights, averages))
            averageWeightsGradients = tempSum
        # Divide the weightsGradients
        tempArray = []
        for weights in averageWeightsGradients:
            tempArray.append(weights / len(weightArray))
        averageWeightsGradients = tempArray
        # Sum the biasesGradients
        for biasesGradients in biasArray:
            tempSum = []
            for biases, averages in zip(biasesGradients, averageBiasesGradients):
                tempSum.append(np.add(biases, averages))
            averageBiasesGradients = tempSum
        # Divide the biasesGradients
        tempArray = []
        for biases in averageBiasesGradients:
            tempArray.append(biases / len(biasArray))
        averageBiasesGradients = tempArray

        # Finally apply the "deltas" to the weights and biases
        self.mlpWeights = np.add(self.mlpWeights, averageWeightsGradients)
        for i in range(len(self.mlpBiases)):
            self.mlpBiases[i] = np.add(self.mlpBiases[i], averageBiasesGradients[i])

    # Debug and Information functions
    def numLayers(self):
        return self.mlpShape.size

    def printWeights(self):
        for weights in self.mlpWeights:
            print(weights.shape)
            print(weights)

    def printBiases(self):
        for biases in self.mlpBiases:
            print(biases.shape)
            print(biases)

    def printActivation(self):
        print(self.activation)
