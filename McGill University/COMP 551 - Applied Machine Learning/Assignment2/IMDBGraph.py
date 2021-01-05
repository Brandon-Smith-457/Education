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
import os

def getIMBD(path):
    posData = []
    posTarget = []
    negData = []
    negTarget = []
    i = 0
    directory = os.path.join(path, "pos")
    for filename in os.listdir(directory):
        f = open(os.path.join(directory, filename), "r", encoding="utf8")
        posData.append(f.read())
        posTarget.append(1)
        f.close()
        i = i + 1
    i = 0
    directory = os.path.join(path, "neg")
    for filename in os.listdir(directory):
        f = open(os.path.join(directory, filename), "r", encoding="utf8")
        negData.append(f.read())
        negTarget.append(0)
        f.close()
        i = i + 1
    return (posData + negData), (posTarget + negTarget)

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
	X_train, y_train = getIMBD("aclImdb\\train")
	X_test, y_test = getIMBD("aclImdb\\test")

	print("There are ", len(X_train), " datas for training, ", len(X_test) , " for test.")

	name_list = ['Logistic Regression','Decision Tree','Linear SVC','AdaBoost','Random Forest']
	#Please add the best parameters in the second *classifierName*
	classifier_list = [
						Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LogisticRegression())
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LogisticRegression(class_weight='balanced', max_iter=1000, solver='sag', tol=0.001))
							]),
						Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', DecisionTreeClassifier())
							]),
    					#43%
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', DecisionTreeClassifier(ccp_alpha=0.0, class_weight='balanced', criterion='gini', max_depth=None, max_features=None, max_leaf_nodes=100, min_impurity_decrease=0.0, min_samples_leaf=10, min_samples_split=0.01, min_weight_fraction_leaf=0.0, random_state=None, splitter='best'))
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.5, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LinearSVC())
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.5, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', LinearSVC(C=0.1, fit_intercept=False, tol=0.001))
							]),
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf', AdaBoostClassifier())
							]),
    					#47%, the n_estimator is not the best parameter, might need to change
    					Pipeline([
							('vect', CountVectorizer(lowercase=True, max_df=0.4, max_features=10000, min_df=3, ngram_range=(1, 3), stop_words='english', strip_accents='ascii')),
							('tfidf', TfidfTransformer()),
							('scale', StandardScaler(with_mean=False)),
							('norm', Normalizer()),
							('clf',  AdaBoostClassifier(base_estimator = DecisionTreeClassifier(max_depth = 17, min_samples_split = 560, ccp_alpha = 0.0001, min_samples_leaf = 12), 
    							learning_rate = 0.1, n_estimators = 100))
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
							('clf', RandomForestClassifier(ccp_alpha=1e-05, class_weight='balanced', criterion='entropy', max_features='sqrt', min_samples_leaf=0.01, min_samples_split=10, oob_score=True))
							])
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
