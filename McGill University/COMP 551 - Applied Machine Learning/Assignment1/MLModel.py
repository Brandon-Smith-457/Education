import numpy as np

#Class that use to do maching learning
class LogisticRegressionModel():
	trainingX = []
	trainingY = []
	testX = []
	testY = []
	W = np.inf

	def __init__ (self, trX, trY, teX, teY):
		self.trainingX = trX
		self.trainingY = trY
		self.testX = teX
		self.testY = teY

	def logistic(self, logit):
		D = len(logit)
		y = np.zeros(D)
		for i in range(0,D):
			y[i] = 1/(1+np.exp(-(logit[i])))
		return y

	def gradient(self, X, Y, w):
		yh = self.logistic(np.dot(X, w))
		grad = np.dot(X.T, yh-Y)
		return grad

	def GradientDescent(self, X, Y, lr, eps, maxIteration):
		N,D = X.shape
		self.W = np.zeros(D)
		w = np.zeros(D)
		g = np.inf
		previous = 0.0
		count = 0
		while(np.linalg.norm(g)>eps and count<maxIteration):
			count = count+1
			g = self.gradient(X,Y,w)
			w = w-lr*g
			trTargetY = self.predictWithW(X, w)
			trAccuracy = self.evaluate_acc(Y, trTargetY)
			#print(trAccuracy)
			if(trAccuracy>previous):
				#print(trAccuracy)
				self.W = w
				previous = trAccuracy

	#Train the model with input X and Y, where X is the input,
	#and Y is the true output of Data
	#using logistic regression if isLogisticRegression is true
	#using naive bayes if isLogisticRegression is false
	def fit(self, learningRate):
		#To-do implment logistic regression
		#print("Training......")
		self.GradientDescent(self.trainingX, self.trainingY, learningRate,0.5,500)
		#print("Training Complete")

	#Give the result of X base on what is trained on fit function,
	#X is the input, and it returns the predict output.
	def predictWithW(self, trueX, w):
		(row, col) = trueX.shape
		Y = np.empty((row))
		Y = self.logistic(np.dot(trueX, w))
		for i in range(0,row):
			if Y[i]>0.5:
				Y[i] = 1
			else:
				Y[i] = 0
		#print("predicting Complete")
		return Y

	def predict(self, trueX):
		#print("Predicting.....")
		(row, col) = trueX.shape
		w = self.W
		Y = np.empty((row))
		Y = self.logistic(np.dot(trueX, w))
		for i in range(0,row):
			if Y[i]>0.5:
				Y[i] = 1
			else:
				Y[i] = 0
		#print("predicting Complete")
		return Y

	#returns a float between 1 and 0 that means the accuracy of the result,
	#1 means 100% match, 0 means 0% match
	def evaluate_acc(self, trueY, targetY):
		num1 = len(trueY)
		num2 = len(targetY)
		if num1!=num2:
			#print("length of 2 inputs are not the same")
			return 0.0
		elif num1 == 0 or num2 == 0:
			#print("inputs are empty")
			return 0.0
		else:
			count = 0
			for i in range(0,num1):
				if trueY[i]==targetY[i]:
					count = count+1
			return (float(count)/float(num1))


class GaussianNaiveBayes():
	trainingX = []
	trainingY = []
	testX = []
	testY = []
	PY = []


	def __init__ (self, trX, trY, teX, teY):
		self.trainingX = trX
		self.trainingY = trY
		self.testX = teX
		self.testY = teY

	def fit(self):
		#print("Training...")
		X = self.trainingX
		Xt = self.testX
		N, D = X.shape
		C = 2
		y = np.zeros((N,2))
		for i in range(0,N):
			if(self.trainingY[i]==1):
				y[i,0] = 1;
				y[i,1] = 0;
			else:
				y[i,0] = 0;
				y[i,1] = 1;
		# First we find the P(Y=1|X)
		mu, s = np.zeros((C,D)), np.zeros((C,D))
		for c in range(C): #calculate mean and std        
			inds = np.nonzero(y[:,c])[0]
			#print(inds)
			mu[c,:] = np.mean(X[inds,:], 0)        
			s[c,:] = np.std(X[inds,:], 0)
			s = s+0.0000001
		log_prior = np.log(np.mean(y, 0))[:,None] 
		log_likelihood = - np.sum( np.log(s[:,None,:]) +.5*(((Xt[None,:,:]- mu[:,None,:])/s[:,None,:])**2), 2)
		log_p= log_prior + log_likelihood #N_text x C
		log_p -= np.max(log_p) #numerical stability
		posterior = np.exp(log_p)  
		posterior /= np.sum(posterior)
		#print(posterior)
		self.PY = posterior
		#print("Training Complete")

	def predict(self, trueX):
		#print("Predicting.....")
		#Compare the P(Y=1|X) and P(Y=0|X)

		Y1 = self.PY
		#print(Y1)
		col, row = Y1.shape
		Y = np.empty((row))
		for i in range(0,row):
			#print(Y1[i])
			if Y1[0,i]>Y1[1,i]:
				Y[i] = 1
			else:
				Y[i] = 0
		#print("predicting Complete")
		return Y

	#returns a float between 1 and 0 that means the accuracy of the result,
	#1 means 100% match, 0 means 0% match
	def evaluate_acc(self, trueY, targetY):
		#print(trueY)
		#print(targetY)
		num1 = len(trueY)
		num2 = len(targetY)
		if num1!=num2:
			#print("length of 2 inputs are not the same")
			return 0.0
		elif num1 == 0 or num2 == 0:
			#print("inputs are empty")
			return 0.0
		else:
			count = 0
			for i in range(0,num1):
				if trueY[i]==targetY[i]:
					count = count+1
			return (float(count)/float(num1))