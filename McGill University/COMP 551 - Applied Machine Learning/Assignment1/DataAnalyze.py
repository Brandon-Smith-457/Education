import numpy as np
from numpy import genfromtxt

#use to analyze the data of file "Ionosphere.data"
class IonosphereAnalyze():
	#X stores a 2d array of all inputs
	X = np.empty((351, 34))
	#Y stores an array of results, where 1 means g, -1 means b
	Y = np.empty((351))


	def __init__ (self):
		file = open("ionosphere/ionosphere.data", "r")
		lines = file.readlines()
		j = 0;
		for line in lines:
			values = line.split(",")
			y = values.pop(34)
			self.X[j] = values;
			if y == 'g\n':
				self.Y[j] = 1
			else:
				self.Y[j] = 0
			j = j+1

	#Analyze data, clean unuseful datas etc...
	def analyzeData(self):
		stillHave = True
		initialN, initialD= self.X.shape
		while(stillHave):
			stillHave = False
			N,D = self.X.shape
			for i in range(0,N):
				if self.X[i][0] == 0.0:
					X_del= np.delete(self.X,i,0)
					self.X = X_del
					Y_del = np.delete(self.Y,i,0)
					self.Y = Y_del
					stillHave = True
					break

#use to analyze the data of file "Adult.data"
class AdultAnalyze():
	X = []
	Y = []

	def __init__(self):
		myData = genfromtxt('Adult/adult.data', delimiter=',')

		file = open("Adult/adult.data", "r")
		myLines = file.readlines()

		# Dictionary for Categorical Variable[1] (WorkClass)
		dictWorkClass = {' Private': 0, ' Self-emp-not-inc': 1, ' Self-emp-inc': 2,
						 ' Federal-gov': 3, ' Local-gov': 4, ' State-gov': 5,
						 ' Without-pay': 6, ' Never-worked': 7, ' ?': 99
						 }

		# Dictionary for Categorical Variable[3] (Education)
		dictEducation = {' Bachelors': 0, ' Some-college': 1, ' 11th': 2, ' HS-grad': 3,
						 ' Prof-school': 4, ' Assoc-acdm': 5, ' Assoc-voc': 6,
						 ' 9th': 7, ' 7th-8th': 8, ' 12th': 9, ' Masters': 10,
						 ' 1st-4th': 11, ' 10th': 12, ' Doctorate': 13,
						 ' 5th-6th': 14, ' Preschool': 15, ' ?': 99
						 }

		# Dictionary for Marital Status
		dictMaritalStatus = {' Married-civ-spouse': 0, ' Divorced': 1, ' Never-married': 2, ' Separated': 3,
							 ' Widowed': 4, ' Married-spouse-absent': 5, ' Married-AF-spouse': 6, ' ?': 99
							 }
		# Dictionary for occupation
		dictOccupation = {' Tech-support': 0, ' Craft-repair': 1, ' Other-service': 2, ' Sales': 3,
						  ' Exec-managerial': 4,
						  ' Prof-specialty': 5, ' Handlers-cleaners': 6, ' Machine-op-inspct': 7, ' Adm-clerical': 8,
						  ' Farming-fishing': 9, ' Transport-moving': 10, ' Priv-house-serv': 11,
						  ' Protective-serv': 12,
						  ' Armed-Forces': 13, ' ?': 99
						  }

		# Dictionary for Relationship
		dictRelationship = {' Wife': 0, ' Own-child': 1, ' Husband': 2, ' Not-in-family': 3, ' Other-relative': 4,
							' Unmarried': 5, ' ?': 99}

		# Dictionary for Race
		dictRace = {' White': 0, ' Asian-Pac-Islander': 1, ' Amer-Indian-Eskimo': 2, ' Other': 3, ' Black': 4, ' ?': 5}

		# Dictionary for Sex
		dictSex = {' Female': 0, ' Male': 1, ' ?': 99}

		# Dictionary for Native Country
		dictNatCountry = {' United-States': 0, ' Cambodia': 1, ' England': 2, ' Puerto-Rico': 3, ' Canada': 4,
						  ' Germany': 5, ' Outlying-US(Guam-USVI-etc)': 6,
						  ' India': 7, ' Japan': 8, ' Greece': 9, ' South': 10, ' China': 11, ' Cuba': 12, ' Iran': 13,
						  ' Honduras': 14, ' Philippines': 15,
						  ' Italy': 16, ' Poland': 17, ' Jamaica': 18, ' Vietnam': 19, ' Mexico': 41, ' Portugal': 20,
						  ' Ireland': 21, ' France': 22,
						  ' Dominican-Republic': 23, ' Laos': 24, ' Ecuador': 25, ' Taiwan': 26, ' Haiti': 27,
						  ' Columbia': 28, ' Hungary': 29, ' Guatemala': 30,
						  ' Nicaragua': 31, ' Scotland': 32, ' Thailand': 33, ' Yugoslavia': 34, ' El-Salvador': 35,
						  ' Trinadad&Tobago': 36, ' Peru': 37, ' Hong': 38,
						  ' Holand-Netherlands': 39, ' ?': 99
						  }

		# Dictionary for income
		dictIncome = {' <=50K\n': 0, ' >50K\n': 1, ' ?': 99}

		i = 0
		myDataList = []
		for line in myLines:
			i += 1
			# print(i)
			if i > 32561:
				break

			entry = line.split(",")

			# Int encoding workclass
			if entry[1] in dictWorkClass:
				entry[1] = str(dictWorkClass[entry[1]])
			else:
				entry[1] = " ERRORRRRRRRR"

			# Int encoding education
			if entry[3] in dictEducation:
				entry[3] = str(dictEducation[entry[3]])
			else:
				entry[3] = " ERRORRRRRRRR"

			# Int encoding marital status
			if entry[5] in dictMaritalStatus:
				entry[5] = str(dictMaritalStatus[entry[5]])
			else:
				entry[5] = " ERRRORRRRRRR"

			# Int encoding Occupation
			if entry[6] in dictOccupation:
				entry[6] = str(dictOccupation[entry[6]])
			else:
				entry[6] = " ERRRORRRRRRR"

			# Int encoding Relationship
			if entry[7] in dictRelationship:
				entry[7] = str(dictRelationship[entry[7]])
			else:
				entry[7] = " ERRRRROOOORRRR"

			# Int encoding Race
			if entry[8] in dictRace:
				entry[8] = str(dictRace[entry[8]])
			else:
				entry[8] = " ERRORRRRRRRRRRRRRRRR"

			# Int encoding Sex
			if entry[9] in dictSex:
				entry[9] = str(dictSex[entry[9]])
			else:
				entry[9] = " ERRORRRRRRRRRRRRRRRR"

			# Int encoding Native Country
			if entry[13] in dictNatCountry:
				entry[13] = str(dictNatCountry[entry[13]])
			else:
				entry[13] = " ERRORRRRRRRRRRRRRRRR"

			# Int encoding Income
			if entry[14] in dictIncome:
				entry[14] = str(dictIncome[entry[14]])
			else:
				entry[14] = " ERRORRRRRRRRRRRRRRRR"

			entry = list(map(int, entry))

			delFlag = 0
			for arrayValue in entry:
				# print(arrayValue)
				if arrayValue == 99:
					del entry[:]
					delFlag = 1
					break

			if delFlag != 1:
				myDataList.append(entry)

		#myDataList = sorted(myDataList, key=itemgetter(2))
		i = 0
		self.X = np.empty((len(myDataList), 14))
		self.Y = np.empty((len(myDataList)))
		for entry in myDataList:
			self.X[i] = entry[0:14]
			self.Y[i] = entry[14]
			i+=1


	#Analyze data, clean unuseful datas etc...
	def analyzeData(self):
		total = 0
		totalbad = 0
		for dataSet in self.X:
			total +=1;
			for data in dataSet:
				if(data == 99):
					totalbad = totalbad+1
					break
		print("Total of " + str(total) +" data, there are " + str(totalbad) + " data with \"?\" inside " )

	def getY(self):
		return self.Y

class HabermanAnalyze():

	X = []
	Y = []

	def __init__(self):
		self.habermanAnalysis()

	def habermanAnalysis(self):
		file = open("haberman/haberman.data", "r")
		myLines = file.readlines()

		i = 0
		myDataList = []
		for line in myLines:
			i += 1
			# print(i)
			if i > 32561:
				break

			entry = line.split(",")
			for x in range(0,4):
				entry[x] = int(entry[x])
				# break

	# print(entry[0])
	# entry[0] = entry[0] + 1
	# print(entry)
			myDataList.append(entry)
		countOfRows=0
		for row in myDataList:
			countOfRows += 1
			#print(row, "Entry Number: ", countOfRows)

		self.X = np.empty((len(myDataList), 3))
		self.Y = np.empty((len(myDataList)))
		i = 0
		for entry in myDataList:
			self.X[i] = entry[0:3]
			if entry[3] == 2:
				self.Y[i] = 1
			else:
				self.Y[i] = 0
			i+=1

	def analyzeData(self):
		#print(self.X)
		#print(self.Y)
		print("Nothing need to do, analyzed already")

class breastCancerAnalyze():
	X = []
	y = []

	def __init__(self):
		self.breastCancerAnalyze()

	def analyzeData(self):
		print("Nothing need to do")

	def breastCancerAnalyze(self):
		file = open("BreastCancerWisconsin/breast-cancer-wisconsin(CLEANED).data", "r")
		myLines = file.readlines()

		i = 0
		myDataList = []
		for line in myLines:
			i += 1
			# print(i)
			if i > 683:
				break

			entry = line.split(",")
			for x in range(0, 11):
				entry[x] = int(entry[x])
			# break

			myDataList.append(entry)

		countOfRows = 0
		for row in myDataList:
			countOfRows += 1
			#print(row, "Entry Number: ", countOfRows)
			#print("TYPE: ", row[10].__class__)

		self.X = np.empty((len(myDataList), 10))
		self.Y = np.empty((len(myDataList)))
		i = 0
		for entry in myDataList:
			self.X[i] = entry[0:10]
			if entry[10] == 2:      #2 is benign, 4 is malignant
				self.Y[i] = 1
			elif entry[10] == 4:
				self.Y[i] = 0
			else:
				entry[10] = 999999999999
			i += 1

		#print("Self.X", self.X)
		#print("Self.Y", self.Y)



		

if __name__ == '__main__':
	#ionosphereAnalyze = IonosphereAnalyse() ################ ERROR SO COMMENTED THIS OUT FOR NOW ######################
	adultAnalyze = AdultAnalyze()
	#print(adultAnalyze.Y)
	#adultAnalyze.analyzeData()

	#adultAnalyze.mergedAdultAnalyze()         #Calling
	myBreastCancer = breastCancer()
	myBreastCancer.breastCancerAnalyze()