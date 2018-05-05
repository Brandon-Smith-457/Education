# Days spent alive addition loop!
	.data		#Data segment
YEARS:	.word 10	#A label that stores 10 in word size memory
DAYS:	.word 		#A label for storing the number of days alive

	.text
	.globl main
main:
	lw $t0, YEARS	#Load the value at YEARS into register t0
	li $t2, 0	#Load the value 0 into t1
	li $s0, 0	#Load the value 0 into s0
while:	beq $t2, $t0, done	#If t1 and t0 are equal go to done
	addi $s0, $s0, 365	#In each iteration of while add 365 to s0
	addi $t2, $t2, 1	#Increment t2 by +1
	j while		#Jump to while
done:	sw $s0, DAYS	#Save the value in s0 into the address of DAYS
	li $v0, 10	#Tell the OS to use library 10 (Exit Program)
	syscall		#Call the library function
