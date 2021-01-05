import sys
import math
#
# this program finds the intersection points between a quadratic function and a line
# it returns the latter point, if none exist or 0 value functions then returns error
#

# Please input the quadratic, ax^2+bx+c, and linear,dx+e, functions in the following format a,b,c,d,e:
def findRoots(input1, input2, input3, input4, input5):
    input1 = float(input1)
    input2 = float(input2)
    input3 = float(input3)
    input4 = float(input4)
    input5 = float(input5)

    a = input1
    b = input2 - input4
    c = input3 - input5
    
    if a != 0:
        mid = b*b-4*a*c
        if mid < 0:
            x1 = "No intersection"
            x2 = "No intersection"
        else:
            x1 = (-b + math.sqrt(mid)) / (2*a)
            x2 = (-b - math.sqrt(mid)) / (2*a)

    # If the quadratic input is actually just a line.
    else:
        if input4 == input2 and input5 == input3:
            x1 = "Anywhere on the line " + str(input4) + "x + " + str(input5)
            x2 = "Anywhere on the line " + str(input4) + "x + " + str(input5)
        elif input4 == input2:
            x1 = "No intersection"
            x2 = "No intersection"
        else:
            x1 = (input3 - input5) / (input4 - input2)
            x2 = (input5 - input3) / (input2 - input4)
    
    print (x1, x2)

if __name__ == "__main__":
    findRoots(sys.argv[1],sys.argv[2],sys.argv[3],sys.argv[4],sys.argv[5])

