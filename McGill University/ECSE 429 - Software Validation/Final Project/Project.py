#
# CODE WRITTEN IN PYTHON 3.7 ON WINDOWS.  I see no reason why it shouldn't run on Linux or Mac but alas I must be completely safe
#

import os
import shutil
import SimMutants

# -------------- Function Definitions -----------------

# Replace the "n"th occurence of substring "sub" in string "line" with string "new".  Returns the new line.
def replaceNth(line, sub, new, n):
    find = line.find(sub)
    i = find != -1
    while find != -1 and i != n:
        find = line.find(sub, find + 1)
        i += 1
    if i == n:
        return line[:find]+new+line[find + len(sub):]
    return line

# Add the mutant information to the string ss in the self explanatory format, and update the mutation counts.
def addMutant(ss, mutationCounts, lineNumber, origLine, mutantLine, mutantType):
    mutationCounts.update({mutantType : mutationCounts.get(mutantType) + 1})
    ss += str(lineNumber) + "\n"
    ss += origLine
    ss += mutantType + "\n"
    ss += mutantLine + "\n"
    return ss, mutationCounts

# Returns true iff the position "pos" in the string "line" is surrounded by quotes.
def inQuotation(pos, line):
    # Check double quotes
    j = line.find("\"")
    while j != -1:
        k = line.find("\"", j + 1)
        if j < pos and pos < k:
            return True
        j = line.find("\"", k + 1)

    # Check single quotes
    j = line.find("\'")
    while j != -1:
        k = line.find("\'", j + 1)
        if j < pos and pos < k:
            return True
        j = line.find("\'", k + 1)

    return False

# Search for all mathematical operators in a given string "line" and add a mutant to the fault list corresponding to every found operator.
def searchForMutants(ss, mutationCounts, lineNumber, line, realType):
    types = {"+", "-", "*", "/"}
    i = line.find(realType)
    n = 1
    while (i != -1):
        # Only add to the fault list if the found operator is not between quotation marks.
        if not inQuotation(i, line):
            for type in types:
                if (type != realType):
                    mutantLine = replaceNth(line, realType, type, n)
                    ss, mutationCounts = addMutant(ss, mutationCounts, lineNumber, line, mutantLine, type)
        i = line.find(realType, i+1)
        n += 1
    return ss, mutationCounts

# Returns the character index corresponding to the start of the line with line number "n"
def findCharacterIndex(s, n):
    characterIndex = 0
    for i in range(n - 1):
        while s[characterIndex] != "\n":
            characterIndex = characterIndex + 1
        characterIndex = characterIndex + 1
    return characterIndex

# -------------- End of Function Definitions -----------------

# --------------------- Input ----------------------
while (True):
    try:
        inputFilePath = input("Please input the file path of the original code that generated the mutant list (relative to the current working directory): ")
        k = inputFilePath.rfind(".")
        inputFileExtension = inputFilePath[k:]
        inputFileName = inputFilePath[:k]
        mutantListFilePath = inputFileName + "_MutationLib.txt"
        
        input = open(inputFilePath, "r")
        output = open(mutantListFilePath, "w+")
        break
    except Exception as e:
        print("Not a valid file or file path")

# ------------------ Fault List Generation -----------------------
ss = ""
mutationCounts = {
    "+" : 0,
    "-" : 0,
    "*" : 0,
    "/" : 0
}
lineCount = 1

for line in input:
    # Skipping white space and any lines with python comments (can remove the comments thing if we want to allow for C and C++ defines in the SUT).
    index = 0
    c = line[index]
    while (c == ' '):
        index += 1
        c = line[index]
    if (c == '#'):
        lineCount += 1
        continue
    # Search for each type of operator and add mutants for each operator found.
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "+")
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "-")
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "*")
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "/")
    lineCount += 1

# After all lines in the SUT have been traversed, append the mutationCounts to the fault list.
ss += "Total mutation counts:\n"
ss += "+ : " + str(mutationCounts.get("+")) + "\n"
ss += "- : " + str(mutationCounts.get("-")) + "\n"
ss += "* : " + str(mutationCounts.get("*")) + "\n"
ss += "/ : " + str(mutationCounts.get("/")) + "\n"

print("Writing to file ", mutantListFilePath)
output.write(ss)
print("Done writing to file ", mutantListFilePath)

input.close()
output.close()

# ------------------ Mutant Files Generation ---------------------
# Kinda redundant seen as we just closed the same input file so we might consider just rewinding the input if we want to be really strict on efficiency.
try:
    input = open(inputFilePath, "r")
    originalFileString = input.read()
    input.close()
except Exception as e:
    print(inputFilePath, "does not correspond to a valid file.")
    exit()

# Open the fault list for reading.
try:
    input = open(mutantListFilePath, "r")
except Exception as e:
    print(mutantListFilePath, "does not correspond to a valid file.")
    exit()

# Create a new directory if it does not already exist with the following name:
outputDirectory = "/Mutated_Source_Codes_" + inputFileName +"/"
try:
    cwd = os.getcwd()
    if os.path.isdir(cwd + outputDirectory):
        shutil.rmtree(cwd + outputDirectory)
    os.makedirs(os.path.dirname(cwd + outputDirectory), exist_ok=True)
except Exception as e:
    print("Could not create directory", outputDirectory)
    exit()

j = 0
lineNumber = 0
originalLine = ""
mutantType = ""
lineToInsert = ""
numberOfGeneratedFiles = 0
newFileString = ""
fileNameDict = {
    "+" : "Plus",
    "-" : "Minus",
    "*" : "Multiply",
    "/" : "Divide"
}

for line in input:
    # Just so we can log the information regarding the number of files generated.
    if line == "Total mutation counts:\n":
        for i in range(4):
            numberOfGeneratedFiles = numberOfGeneratedFiles + int(input.readline().split(" : ")[1].split("\n")[0])
        break
    
    # Read in the data from the fault-list
    lineNumber = int(line.split("\n")[0])
    originalLine = input.readline()
    mutantType = input.readline().split("\n")[0]
    lineToInsert = input.readline()
    line = input.readline() # This one is just reading the blank line between each fault

    # Finding the index in the originalFileString where the original line should be removed and the mutant line "lineToInsert" should be inserted.
    characterIndex = findCharacterIndex(originalFileString, lineNumber)
    newFileString = originalFileString[:characterIndex] + lineToInsert + originalFileString[characterIndex + len(originalLine):]

    # Naming convention for the mutant files.
    outputName = outputDirectory[1:] + "MutationNumber_" + str(j) + "_" + inputFileName + "_Mutant_" + fileNameDict[mutantType] + "_LineNumber_" + str(lineNumber) + inputFileExtension
    j = j + 1

    try:
        output = open(outputName, "w+")
        print("Writing to file:", outputName)
        output.write(newFileString)
    except Exception as e:
        print("Failed to create/write to file:", outputName)
    output.close()

input.close()
print(numberOfGeneratedFiles, "mutant files created!")

# ------------------ Run Simulations ---------------------------
SimMutants.run(inputFilePath, outputDirectory)