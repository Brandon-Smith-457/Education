#
# CODE WRITTEN IN PYTHON 3.7 ON WINDOWS.  I see no reason why it shouldn't run on Linux or Mac but alas I must be completely safe
#

#Helper Function Author: Padraic Cunningham
def replaceNth(line, sub, new, n):
    find = line.find(sub)
    i = find != -1
    while find != -1 and i != n:
        find = line.find(sub, find + 1)
        i += 1
    if i == n:
        return line[:find]+new+line[find + len(sub):]
    return line

def addMutant(ss, mutationCounts, lineNumber, origLine, mutantLine, mutantType):
    mutationCounts.update({mutantType : mutationCounts.get(mutantType) + 1})
    ss += str(lineNumber) + "\n"
    ss += origLine
    ss += mutantType + "\n"
    ss += mutantLine + "\n"
    return ss, mutationCounts

def searchForMutants(ss, mutationCounts, lineNumber, line, realType):
    types = {"+", "-", "*", "/"}
    i = line.find(realType)
    n = 1
    while (i != -1):
        for type in types:
            if (type != realType):
                mutantLine = replaceNth(line, realType, type, n)
                ss, mutationCounts = addMutant(ss, mutationCounts, lineNumber, line, mutantLine, type)
        i = line.find(realType, i+1)
        n += 1
    return ss, mutationCounts

while (True):
    try:
        inputName = input("Please input a file relative to the currend working directory: ")
        outputName = inputName
        outputName = outputName.split(".py")[0]
        outputName = outputName.split(".cpp")[0]
        outputName = outputName.split(".h")[0]
        outputName = outputName.split(".java")[0]
        outputName = outputName.split(".cs")[0]
        outputName = outputName.split(".txt")[0]
        outputName = outputName + "_MutationLib.txt"
        
        input = open(inputName, "r")
        output = open(outputName, "w+")
        break
    except Exception as e:
        print("Not a valid file or file path")

ss = ""
mutationCounts = {
    "+" : 0,
    "-" : 0,
    "*" : 0,
    "/" : 0
}
lineCount = 1

for line in input:
    index = 0
    c = line[index]
    while (c == ' '):
        index += 1
        c = line[index]
    if (c == '#'):
        lineCount += 1
        continue
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "+")
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "-")
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "*")
    ss, mutationCounts = searchForMutants(ss, mutationCounts, lineCount, line, "/")
    lineCount += 1

#ss, mutationCounts = addMutant(ss, mutationCounts, 1, "Test1\n", "Test2\n", "+")
ss += "Total mutation counts:\n"
ss += "+ : " + str(mutationCounts.get("+")) + "\n"
ss += "- : " + str(mutationCounts.get("-")) + "\n"
ss += "* : " + str(mutationCounts.get("*")) + "\n"
ss += "/ : " + str(mutationCounts.get("/")) + "\n"
print("Writing to file ", outputName)
output.write(ss)
print("Done writing to file ", outputName)

input.close()
output.close()