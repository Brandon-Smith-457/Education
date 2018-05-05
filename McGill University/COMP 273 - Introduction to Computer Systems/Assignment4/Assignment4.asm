	.data
array:	.space 40			#Declare the global variable array to have 40 bytes (10 integers) of space
ask:	.asciiz "enter a number: "	#String constant for asking user for input
reply:	.asciiz "the sum is "		#String constant for displaying the sum

	.text
	.globl main
main:
	subi $sp, $sp, 8		#Declare the variables i and sum, placing them on the stack
	sw $zero, 0($sp)		#Initialize sum to be 0
	sw $zero, 4($sp)		#Initialize i to be 0
	lw $t0, 4($sp)			#Put i into $t0
for:	beq $t0, 10, done		#For loop (10 iterations)
	la $a0, ask			#Access string constant ask
	li $v0, 4			#syscall code for printing string
	syscall
	li $v0, 5			#syscall code for reading integer
	syscall
	la $t1, array			#Access array
	mul $t2, $t0, 4			#Compute the offset for index i
	add $t1, $t1, $t2		#Add the offset to the address for array
	sw $v0, 0($t1)			#Save whatever data was input by the user
	addi $t0, $t0, 1		#Increment i
	sw $t0, 4($sp)			#Update the stack variable i
	j for
done:
	la $a0, array			#Getting the address of the start of array
	la $a1, array+40		#Getting the address of the end of array
	jal summation			#Call the summation function
	sw $v0, 0($sp)			#Store the result of summation in sum
	la $a0, reply			#Put the adress of reply into $a0
	li $v0, 4			#syscall code for printing string
	syscall
	lw $a0, 0($sp)			#Put the value at sum in $a0
	li $v0, 1			#syscall code for printing integer
	syscall
	addi $sp, $sp, 8		#Clear stack
	li $v0, 10			#syscall code to terminate process
	syscall
	
summation:
	subi $sp, $sp, 8		#Make space for function arguments
	sw $a0, 4($sp)			#Store array address on stack
	sw $a1, 0($sp)			#Store end of array address on stack
	subi $sp, $sp, 16		#Perserve used registers and return address
	sw $t2, 12($sp)
	sw $t1, 8($sp)
	sw $t0, 4($sp)
	sw $ra, 0($sp)
	subi $sp, $sp, 4		#Declare int result
	sw $zero, 0($sp)		#Initialize result to 0
	lw $t0, 0($sp)			#Put result in $t0
	lw $t1, 0($a0)			#Put current indexed array value into $t1
if:	beq $a0, $a1, exit		#if (a != last)
	addi $a0, $a0, 4		#Index forward once for the next call
	jal summation			#Call summation
	add $t0, $v0, $t1		#Compute result
	sw $t0, 0($sp)			#Store the value into result
exit:
	lw $v0, 0($sp)			#Return result
	addi $sp, $sp, 4		#Pop result
	lw $ra, 0($sp)			#Restore the used registers and return address
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	addi $sp, $sp, 24		#Pop the stack
	jr $ra				#Return
	
	
	
	
	
	
	
	
	
	