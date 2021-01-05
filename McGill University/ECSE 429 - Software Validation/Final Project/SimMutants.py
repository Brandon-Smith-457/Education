import sys
import subprocess
import os
import threading

def run(inputFilePath, outputDir):
	# Generate the test vectors
	vectors = [
	    [ 0,  0,  0,  0,  0],
	    [ 0,  0,  0,  0,  1],
	    [ 0,  0,  0,  0, -1],
	    [ 0,  0,  0,  1,  0],
	    [ 0,  0,  0,  1,  1],
	    [ 0,  0,  0,  1, -1],
	    [ 0,  0,  0, -1,  0],
	    [ 0,  0,  0, -1,  1],
	    [ 0,  0,  0, -1, -1],
	    [ 0,  0,  1,  0,  0],
	    [ 0,  0,  1,  0,  1],
	    [ 0,  0,  1,  0, -1],
	    [ 0,  0,  1,  1,  0],
	    [ 0,  0,  1,  1,  1],
	    [ 0,  0,  1,  1, -1],
	    [ 0,  0,  1, -1,  0],
	    [ 0,  0,  1, -1,  1],
	    [ 0,  0,  1, -1, -1],
	    [ 0,  0, -1,  0,  0],
	    [ 0,  0, -1,  0,  1],
	    [ 0,  0, -1,  0, -1],
	    [ 0,  0, -1,  1,  0],
	    [ 0,  0, -1,  1,  1],
	    [ 0,  0, -1,  1, -1],
	    [ 0,  0, -1, -1,  0],
	    [ 0,  0, -1, -1,  1],
	    [ 0,  0, -1, -1, -1],
	    [ 0,  1,  0,  0,  0],
	    [ 0,  1,  0,  0,  1],
	    [ 0,  1,  0,  0, -1],
	    [ 0,  1,  0,  1,  0],
	    [ 0,  1,  0,  1,  1],
	    [ 0,  1,  0,  1, -1],
	    [ 0,  1,  0, -1,  0],
	    [ 0,  1,  0, -1,  1],
	    [ 0,  1,  0, -1, -1],
	    [ 0,  1,  1,  0,  0],
	    [ 0,  1,  1,  0,  1],
	    [ 0,  1,  1,  0, -1],
	    [ 0,  1,  1,  1,  0],
	    [ 0,  1,  1,  1,  1],
	    [ 0,  1,  1,  1, -1],
	    [ 0,  1,  1, -1,  0],
	    [ 0,  1,  1, -1,  1],
	    [ 0,  1,  1, -1, -1],
	    [ 0,  1, -1,  0,  0],
	    [ 0,  1, -1,  0,  1],
	    [ 0,  1, -1,  0, -1],
	    [ 0,  1, -1,  1,  0],
	    [ 0,  1, -1,  1,  1],
	    [ 0,  1, -1,  1, -1],
	    [ 0,  1, -1, -1,  0],
	    [ 0,  1, -1, -1,  1],
	    [ 0,  1, -1, -1, -1],
	    [ 0, -1,  0,  0,  0],
	    [ 0, -1,  0,  0,  1],
	    [ 0, -1,  0,  0, -1],
	    [ 0, -1,  0,  1,  0],
	    [ 0, -1,  0,  1,  1],
	    [ 0, -1,  0,  1, -1],
	    [ 0, -1,  0, -1,  0],
	    [ 0, -1,  0, -1,  1],
	    [ 0, -1,  0, -1, -1],
	    [ 0, -1,  1,  0,  0],
	    [ 0, -1,  1,  0,  1],
	    [ 0, -1,  1,  0, -1],
	    [ 0, -1,  1,  1,  0],
	    [ 0, -1,  1,  1,  1],
	    [ 0, -1,  1,  1, -1],
	    [ 0, -1,  1, -1,  0],
	    [ 0, -1,  1, -1,  1],
	    [ 0, -1,  1, -1, -1],
	    [ 0, -1, -1,  0,  0],
	    [ 0, -1, -1,  0,  1],
	    [ 0, -1, -1,  0, -1],
	    [ 0, -1, -1,  1,  0],
	    [ 0, -1, -1,  1,  1],
	    [ 0, -1, -1,  1, -1],
	    [ 0, -1, -1, -1,  0],
	    [ 0, -1, -1, -1,  1],
	    [ 0, -1, -1, -1, -1],
	    [ 1,  0,  0,  0,  0],
	    [ 1,  0,  0,  0,  1],
	    [ 1,  0,  0,  0, -1],
	    [ 1,  0,  0,  1,  0],
	    [ 1,  0,  0,  1,  1],
	    [ 1,  0,  0,  1, -1],
	    [ 1,  0,  0, -1,  0],
	    [ 1,  0,  0, -1,  1],
	    [ 1,  0,  0, -1, -1],
	    [ 1,  0,  1,  0,  0],
	    [ 1,  0,  1,  0,  1],
	    [ 1,  0,  1,  0, -1],
	    [ 1,  0,  1,  1,  0],
	    [ 1,  0,  1,  1,  1],
	    [ 1,  0,  1,  1, -1],
	    [ 1,  0,  1, -1,  0],
	    [ 1,  0,  1, -1,  1],
	    [ 1,  0,  1, -1, -1],
	    [ 1,  0, -1,  0,  0],
	    [ 1,  0, -1,  0,  1],
	    [ 1,  0, -1,  0, -1],
	    [ 1,  0, -1,  1,  0],
	    [ 1,  0, -1,  1,  1],
	    [ 1,  0, -1,  1, -1],
	    [ 1,  0, -1, -1,  0],
	    [ 1,  0, -1, -1,  1],
	    [ 1,  0, -1, -1, -1],
	    [ 1,  1,  0,  0,  0],
	    [ 1,  1,  0,  0,  1],
	    [ 1,  1,  0,  0, -1],
	    [ 1,  1,  0,  1,  0],
	    [ 1,  1,  0,  1,  1],
	    [ 1,  1,  0,  1, -1],
	    [ 1,  1,  0, -1,  0],
	    [ 1,  1,  0, -1,  1],
	    [ 1,  1,  0, -1, -1],
	    [ 1,  1,  1,  0,  0],
	    [ 1,  1,  1,  0,  1],
	    [ 1,  1,  1,  0, -1],
	    [ 1,  1,  1,  1,  0],
	    [ 1,  1,  1,  1,  1],
	    [ 1,  1,  1,  1, -1],
	    [ 1,  1,  1, -1,  0],
	    [ 1,  1,  1, -1,  1],
	    [ 1,  1,  1, -1, -1],
	    [ 1,  1, -1,  0,  0],
	    [ 1,  1, -1,  0,  1],
	    [ 1,  1, -1,  0, -1],
	    [ 1,  1, -1,  1,  0],
	    [ 1,  1, -1,  1,  1],
	    [ 1,  1, -1,  1, -1],
	    [ 1,  1, -1, -1,  0],
	    [ 1,  1, -1, -1,  1],
	    [ 1,  1, -1, -1, -1],
	    [ 1, -1,  0,  0,  0],
	    [ 1, -1,  0,  0,  1],
	    [ 1, -1,  0,  0, -1],
	    [ 1, -1,  0,  1,  0],
	    [ 1, -1,  0,  1,  1],
	    [ 1, -1,  0,  1, -1],
	    [ 1, -1,  0, -1,  0],
	    [ 1, -1,  0, -1,  1],
	    [ 1, -1,  0, -1, -1],
	    [ 1, -1,  1,  0,  0],
	    [ 1, -1,  1,  0,  1],
	    [ 1, -1,  1,  0, -1],
	    [ 1, -1,  1,  1,  0],
	    [ 1, -1,  1,  1,  1],
	    [ 1, -1,  1,  1, -1],
	    [ 1, -1,  1, -1,  0],
	    [ 1, -1,  1, -1,  1],
	    [ 1, -1,  1, -1, -1],
	    [ 1, -1, -1,  0,  0],
	    [ 1, -1, -1,  0,  1],
	    [ 1, -1, -1,  0, -1],
	    [ 1, -1, -1,  1,  0],
	    [ 1, -1, -1,  1,  1],
	    [ 1, -1, -1,  1, -1],
	    [ 1, -1, -1, -1,  0],
	    [ 1, -1, -1, -1,  1],
	    [ 1, -1, -1, -1, -1],
	    [-1,  0,  0,  0,  0],
	    [-1,  0,  0,  0,  1],
	    [-1,  0,  0,  0, -1],
	    [-1,  0,  0,  1,  0],
	    [-1,  0,  0,  1,  1],
	    [-1,  0,  0,  1, -1],
	    [-1,  0,  0, -1,  0],
	    [-1,  0,  0, -1,  1],
	    [-1,  0,  0, -1, -1],
	    [-1,  0,  1,  0,  0],
	    [-1,  0,  1,  0,  1],
	    [-1,  0,  1,  0, -1],
	    [-1,  0,  1,  1,  0],
	    [-1,  0,  1,  1,  1],
	    [-1,  0,  1,  1, -1],
	    [-1,  0,  1, -1,  0],
	    [-1,  0,  1, -1,  1],
	    [-1,  0,  1, -1, -1],
	    [-1,  0, -1,  0,  0],
	    [-1,  0, -1,  0,  1],
	    [-1,  0, -1,  0, -1],
	    [-1,  0, -1,  1,  0],
	    [-1,  0, -1,  1,  1],
	    [-1,  0, -1,  1, -1],
	    [-1,  0, -1, -1,  0],
	    [-1,  0, -1, -1,  1],
	    [-1,  0, -1, -1, -1],
	    [-1,  1,  0,  0,  0],
	    [-1,  1,  0,  0,  1],
	    [-1,  1,  0,  0, -1],
	    [-1,  1,  0,  1,  0],
	    [-1,  1,  0,  1,  1],
	    [-1,  1,  0,  1, -1],
	    [-1,  1,  0, -1,  0],
	    [-1,  1,  0, -1,  1],
	    [-1,  1,  0, -1, -1],
	    [-1,  1,  1,  0,  0],
	    [-1,  1,  1,  0,  1],
	    [-1,  1,  1,  0, -1],
	    [-1,  1,  1,  1,  0],
	    [-1,  1,  1,  1,  1],
	    [-1,  1,  1,  1, -1],
	    [-1,  1,  1, -1,  0],
	    [-1,  1,  1, -1,  1],
	    [-1,  1,  1, -1, -1],
	    [-1,  1, -1,  0,  0],
	    [-1,  1, -1,  0,  1],
	    [-1,  1, -1,  0, -1],
	    [-1,  1, -1,  1,  0],
	    [-1,  1, -1,  1,  1],
	    [-1,  1, -1,  1, -1],
	    [-1,  1, -1, -1,  0],
	    [-1,  1, -1, -1,  1],
	    [-1,  1, -1, -1, -1],
	    [-1, -1,  0,  0,  0],
	    [-1, -1,  0,  0,  1],
	    [-1, -1,  0,  0, -1],
	    [-1, -1,  0,  1,  0],
	    [-1, -1,  0,  1,  1],
	    [-1, -1,  0,  1, -1],
	    [-1, -1,  0, -1,  0],
	    [-1, -1,  0, -1,  1],
	    [-1, -1,  0, -1, -1],
	    [-1, -1,  1,  0,  0],
	    [-1, -1,  1,  0,  1],
	    [-1, -1,  1,  0, -1],
	    [-1, -1,  1,  1,  0],
	    [-1, -1,  1,  1,  1],
	    [-1, -1,  1,  1, -1],
	    [-1, -1,  1, -1,  0],
	    [-1, -1,  1, -1,  1],
	    [-1, -1,  1, -1, -1],
	    [-1, -1, -1,  0,  0],
	    [-1, -1, -1,  0,  1],
	    [-1, -1, -1,  0, -1],
	    [-1, -1, -1,  1,  0],
	    [-1, -1, -1,  1,  1],
	    [-1, -1, -1,  1, -1],
	    [-1, -1, -1, -1,  0],
	    [-1, -1, -1, -1,  1],
	    [-1, -1, -1, -1, -1]
	]
	
	
	
	# obtain all mutant filenames
	files = os.listdir(outputDir[1:])
	results = [ 0 for vector in vectors ]
	killedMutants = [ False for file in files ] # all mutants are not killed by default 

	print()
	print("---------------------------------------------")
	print ("Executing fault free program . . .")
	print("---------------------------------------------")

	lock = threading.Lock()

	def subProcess(vectors, count):
		for vector in vectors:
			returnValuesInBytes = subprocess.check_output([sys.executable, inputFilePath, str(vector[0]), str(vector[1]), str(vector[2]), str(vector[3]), str(vector[4])]) # return values stored in bytes
			lock.acquire()
			results[count] = returnValuesInBytes.decode('utf-8')
			lock.release()
			count += 1

	vec = []
	count = []
	for i in range(3):
		x = len(vectors)
		modu = x%3
		startat = int(i * (x/3))
		endat = int((i + 1) * (x/3))
		if modu == 0:
			vec.append(vectors[startat:endat])
			count.append(startat)
		elif modu == 1:
			if i == 2:
				vec.append(vectors[startat:endat+1])
				count.append(startat)
			else:
				vec.append(vectors[startat:endat])
				count.append(startat)
		else:
			if i == 2:
				vec.append(vectors[startat+1:endat+2])
				count.append(startat + 1)
			elif i == 1:
				vec.append(vectors[startat:endat+1])
				count.append(startat)
			else:
				vec.append(vectors[startat:endat])
				count.append(startat)

	t1 = threading.Thread(target=subProcess, args=(vec[0], count[0],))
	t2 = threading.Thread(target=subProcess, args=(vec[1], count[1],))
	t3 = threading.Thread(target=subProcess, args=(vec[2], count[2],))
	t1.start()
	t2.start()
	t3.start()
	t1.join()
	t2.join()
	t3.join()

	
	print()
	print("---------------------------------------------")
	print("Executing mutated programs . . .")
	print("---------------------------------------------")

	totalCount = [0] # Lists because for some reason Python doesn't recognize them inside of mutantSubProcess if they're just a values
	mutantsKillCount = [0]

	def mutantSubProcess(mutantFiles, mutantCount):
		# for each mutant, run test vectors
		vectorCount = 0

		for mutant in mutantFiles:
			for vector in vectors:
				try:
					mutatedReturnValuesInBytes = subprocess.check_output([sys.executable, outputDir[1:]+mutant, str(vector[0]), str(vector[1]), str(vector[2]), str(vector[3]), str(vector[4])], stderr=subprocess.STDOUT) # return values stored in bytes
					mutatedReturnValue = mutatedReturnValuesInBytes.decode('utf-8')
					if (mutatedReturnValue != results[vectorCount]):
						lock.acquire()
						print("Mutant ", mutant, " killed by test vector (", vector[0], ",", vector[1], ",", vector[2], ",", vector[3], ",", vector[4], ")")
						killedMutants[mutantCount] = True
						mutantsKillCount[0] += 1
						lock.release()
						break # move on to next mutant
					else:
						vectorCount += 1 # continue with next vector
				except:
					lock.acquire()
					print("Mutant ", mutant, " killed by test vector (", vector[0], ",", vector[1], ",", vector[2], ",", vector[3], ",", vector[4], ")")
					killedMutants[mutantCount] = True
					mutantsKillCount[0] += 1
					lock.release()
					break # move on to next mutant
			vectorCount = 0
			lock.acquire()
			mutantCount += 1
			totalCount[0] += 1 
			lock.release()
	
	vec = []
	count = []
	for i in range(3):
		x = len(files)
		modu = x%3
		startat = int(i * (x/3))
		endat = int((i + 1) * (x/3))
		if modu == 0:
			vec.append(files[startat:endat])
			count.append(startat)
		elif modu == 1:
			if i == 2:
				vec.append(files[startat:endat+1])
				count.append(startat)
			else:
				vec.append(files[startat:endat])
				count.append(startat)
		else:
			if i == 2:
				vec.append(files[startat+1:endat+2])
				count.append(startat + 1)
			elif i == 1:
				vec.append(files[startat:endat+1])
				count.append(startat)
			else:
				vec.append(files[startat:endat])
				count.append(startat)

	t1 = threading.Thread(target=mutantSubProcess, args=(vec[0], count[0],))
	t2 = threading.Thread(target=mutantSubProcess, args=(vec[1], count[1],))
	t3 = threading.Thread(target=mutantSubProcess, args=(vec[2], count[2],))
	t1.start()
	t2.start()
	t3.start()
	t1.join()
	t2.join()
	t3.join()

	print("Mutants killed = ", killedMutants)
	if mutantsKillCount[0] > 0:
		print("Mutant Ratio = ", mutantsKillCount[0]/totalCount[0])
	print("Mutants not killed:")
	for i in range(len(files)):
		if not killedMutants[i]:
			print(files[i])