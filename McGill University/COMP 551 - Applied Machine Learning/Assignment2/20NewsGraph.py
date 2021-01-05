import sklearn as sk
from time import time
from sklearn import metrics
from sklearn.model_selection import GridSearchCV, RandomizedSearchCV
from sklearn.model_selection import train_test_split
from sklearn.model_selection import cross_val_score
from sklearn.datasets import fetch_20newsgroups
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import LinearSVC
from sklearn.linear_model import SGDClassifier
from sklearn.ensemble import AdaBoostClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import Normalizer
from sklearn.preprocessing import StandardScaler
from scipy.stats import uniform
import numpy as np
import dis
import matplotlib.pyplot as plt

def autolabel(rects):
    """
    Attach a text label above each bar displaying its height
    """
    for rect in rects:
        height = rect.get_height()
        ax.text(rect.get_x() + rect.get_width()/2., 1.05*height,
                '%d%%' % int(height*100),
                ha='center', va='bottom')

if __name__ == '__main__':
	trainingData = fetch_20newsgroups(subset='train', remove=(['headers', 'footers', 'quotes']))
	testingData = fetch_20newsgroups(subset='test', remove=(['headers', 'footers', 'quotes']))
	X_train = trainingData.data
	y_train = trainingData.target
	X_test = testingData.data
	y_test = testingData.target

	print("There are ", len(X_train), " datas for training, ", len(X_test) , " for test.")

	name_list = ['Logistic Regression','Decision Tree','Linear SVC','AdaBoost','Random Forest']
	#Please add the best parameters in the second *classifierName*
	classifier_list = [
						Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.3, max_features=100000, min_df=3, ngram_range=(1, 1), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LogisticRegression())
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.3, max_features=100000, min_df=3, ngram_range=(1, 1), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LogisticRegression(class_weight = 'balanced', intercept_scaling = 10, max_iter = 10000, tol = 0.00001, solver = 'sag'))
							]),
						Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=2, ngram_range=(1, 1), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', DecisionTreeClassifier())
							]),
    					#43%
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=2, ngram_range=(1, 1), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', DecisionTreeClassifier(criterion = "gini", max_depth = 300, min_samples_split = 140, max_leaf_nodes = 2300, ccp_alpha = 0.000001))
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.3, max_features=100000, min_df=2, ngram_range=(1, 2), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LinearSVC())
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.3, max_features=100000, min_df=2, ngram_range=(1, 2), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LinearSVC(C=0.1, dual=False, fit_intercept=False, intercept_scaling=10.0, max_iter=100, tol=0.001))
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.5, max_features=10000, min_df=2, ngram_range=(1, 1), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', AdaBoostClassifier())
							]),
    					#47%, the n_estimator is not the best parameter, might need to change
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.5, max_features=10000, min_df=2, ngram_range=(1, 1), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf',  AdaBoostClassifier(base_estimator = DecisionTreeClassifier(criterion = "gini", max_depth = 300, min_samples_split = 140, max_leaf_nodes = 2300, ccp_alpha = 0.000001), 
								learning_rate = 0.01, n_estimators = 100))
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.5, max_features=100000, min_df=2, ngram_range=(1, 2), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', RandomForestClassifier())
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.5, max_features=100000, min_df=2, ngram_range=(1, 2), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', RandomForestClassifier(criterion = "gini", max_depth = 300, min_samples_split = 140, max_leaf_nodes = 2300, ccp_alpha = 0.000001))
							])
						# Keep in mind I got these estimates from training on 400 data points.
						# Then scaled to dataset in batches of 1000, since RAM was capped!! (See RandomForestTester for code)
    					]
	num_list = [0,0,0,0,0]
	num_list1 = [0,0,0,0,0]
	i = 0
	j = 0
	for name in name_list:
		print("Training " + name + " with default parameters")
		start = time()
		classifier_list[i].fit(X_train, y_train)
		predicted = classifier_list[i].predict(X_test)
		print("Search took %.2f seconds." % ((time() - start)))
		num_list[j] = metrics.accuracy_score(predicted, y_test)
		print("Training " + name + " with best parameters")
		start = time()
		classifier_list[i+1].fit(X_train,y_train)
		predicted1 = classifier_list[i+1].predict(X_test)
		print("Search took %.2f seconds." % ((time() - start)))
		num_list1[j] = metrics.accuracy_score(predicted1, y_test)
		i = i+2
		j = j+1
	x =list(range(len(num_list)))
	total_width, n = 0.8, 2
	width = total_width / n
	fig, ax = plt.subplots()
	rects1 = ax.bar(x, num_list, width=width, label='Default',fc = 'y')
	for i in range(len(x)):
		x[i] = x[i] + width
	rects2 = ax.bar(x, num_list1, width=width, label='Best',tick_label = name_list,fc = 'r')
	autolabel(rects1)
	autolabel(rects2)
	plt.ylim(0,1)
	plt.legend()
	plt.show()
