import numpy as np
import MLModel
import DataAnalyze
from sklearn.linear_model import LogisticRegression
from sklearn.naive_bayes import GaussianNB
from sklearn import datasets
import matplotlib.pyplot as plt
import time

class kFoldCrossValidation():

	#Plot a graph of learning rate/running time
	def getTimeOfEvaluation(self, X, Y):
		plotX = []
		plotY = []
		i = 0
		while(i<8):
			plotX.append(i)
			startTime = time.time()
			print("Start training using logistic regression")
			model = MLModel.LogisticRegressionModel(X, Y, X, Y)
			model.fit(0.1**(7-i))
			timeUsed = time.time()-startTime
			plotY.append(timeUsed)
			i = i+1
		plt.plot(plotX, plotY, c = 'r', label='logistic regression')
		plt.xlabel("i such that learningRate = 0.1^(7-i)")
		plt.ylabel("RunningTime(second)")
		plt.title("Performance of LogisticRegression based on learningRate")
		plt.show()

	#plot a graph number of inputs/error rate of logistic regression and naive bayes
	def getResult(self, X, Y, isLogisticRegression, timeStep):
		length = int(len(X)/timeStep);
		plotX = np.zeros((length-1))
		plotY1 = np.zeros((length-1))
		plotY2 = np.zeros((length-1))
		for i in range(1,length):
			plotX[i-1] = i*timeStep
			subX = X[0:i*timeStep,:]
			subY = Y[0:i*timeStep]
			accuracy1 = self.validate(subX, subY, 5, True, 1)
			accuracy2 = self.validate(subX, subY, 5, False, 1)
			plotY1[i-1] = 1-accuracy1
			plotY2[i-1] = 1-accuracy2

		plt.plot(plotX, plotY1, c = 'r', label='logistic regression')
		plt.plot(plotX, plotY2, c = 'b', label='naive bayes')
		plt.xlabel("N")
		plt.ylabel("error")
		plt.legend()
		plt.show()	

	# run the validation
	# X = inputs
	# Y = outputs
	# k = number of se		ments to cut inputs to
	# model = model use to run the validation
	def validate(self, X, Y, k, isLogisticRegression, learningRate):
		(row, col) = X.shape
		numOfSection = int(row/k)
		totalAcc = 0.0
		for a in range(0, k):
			i = a*numOfSection
			Xbefore = []
			Ybefore = []
			if i == 0:
				Xbefore = X[0:0, 0:0]
				Ybefore = Y[0:0]
			else:
				Xbefore = X[0:i-1, 0:col]
				Ybefore = Y[0:i-1]
			subX = X[i:i+numOfSection-1, 0:col]
			subY = Y[i:i+numOfSection-1]
			Xafter = X[i+numOfSection:row, 0:col]
			Yafter = Y[i+numOfSection:row]
			XConc = []
			YConc = []
			if i == 0 :
				Xconc = Xafter
				Yconc = Yafter
			else:
				Xconc = np.concatenate((Xbefore, Xafter))
				Yconc = np.concatenate((Ybefore, Yafter))
			#Use to test the accuracy value from sci-kit
			model = None
			if(isLogisticRegression):
				print("Start training using logistic regression")
				model = MLModel.LogisticRegressionModel(Xconc, Yconc, subX, subY)
				model.fit(learningRate)
			else:
				print("Start training using gaussian naive bayes")
				model = MLModel.GaussianNaiveBayes(Xconc, Yconc, subX, subY)
				model.fit()
			targetY = model.predict(subX)
			acc = model.evaluate_acc(subY, targetY)
			#print("Accuracy is " + str(acc))
			totalAcc = totalAcc + acc
		return (totalAcc/float(k))

if __name__ == '__main__':
	#dataAnalyze = DataAnalyze.IonosphereAnalyze()
	dataAnalyze = DataAnalyze.AdultAnalyze()
	#dataAnalyze = DataAnalyze.HabermanAnalyze()
	#dataAnalyze = DataAnalyze.breastCancerAnalyze()
	dataAnalyze.analyzeData()
	X = dataAnalyze.X
	Y = dataAnalyze.Y
	validation = kFoldCrossValidation()
	#validation.getTimeOfEvaluation(X,Y)
	validation.getResult(X,Y,True, 1000)
	#print("Average accuracy after 5 fold cross validation is: " + str(validation.validate(X,Y,5,False)))
