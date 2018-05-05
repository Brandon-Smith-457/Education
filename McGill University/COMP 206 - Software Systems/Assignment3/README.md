# Assignment3

This is the source code repository that will be used as a starting point for Assignment 3. You should edit this file (README.md) in order to write your responses for Question 2.

# Question 2

## Part 1
1. **main**
	1. **load_one_preference**
		1. For as many iterations as there are fields: **LOAD_FIELD** / **input_fcn_array**. A function pointer array that depending on the field number will call the correct input function of the following: **input_string**; **input_double**; **input_animal**;
	1. If compiled with -DDEBUG: **print_preference**
		1. For as many iterations as there are fields: **PRINT_FIELD** / **output_fcn_array**. A function pointer array that depending on the field number will call the correct output function of the following: **output_string**; **output_double**; **output_animal**;
	1. For as many iterations as there are input data: **load_one_preference**
		1. For as many iterations as there are fields: **LOAD_FIELD** / **input_fcn_array**. A function pointer array that depending on the field number will call the correct input function of the following: **input_string**; **input_double**; **input_animal**;
	1. If compiled with -DDEBUG: **print_preference**
		1. For as many iterations as there are fields: **PRINT_FIELD** / **output_fcn_array**. A function pointer array that depending on the field number will call the correct output function of the following: **output_string**; **output_double**; **output_animal**;
	1. For as many iterations as there are input data: **compute_difference_numeric**
	1. For as many iterations as there are input data: **compute_difference_alphabetic**
	1. If compiled with -DDEBUG: **print_preference**
		1. For as many iterations as there are fields: **PRINT_FIELD** / **output_fcn_array**. A function pointer array that depending on the field number will call the correct output function of the following: **output_string**; **output_double**; **output_animal**;
		
## Part 2
LOAD_FIELD is pre-compiled into input_fcn_array. input_fcn_array is an array of function pointers of length NUM_FIELDS that take a void pointer and a char pointer as arguments.
This means that we can typecast the void pointer in the function into whatever datatype we need, and by accessing the field using the field_number, coupled with the field_offsets, LOAD_FIELD/input_fcn_array can set the specific fields to any of the three possible inputs: input_string; input_double; input_animal.

## Part 3
A struct stores memory in multiples of 4 (or 8 depending on the working system) bytes. What this means is that any data that cannot fit within the current multiple of 4 will be pushed to the next multiple, while the current multiple is padded with null bytes.
field_offsets uses offsetof which is the correct method to employ for determining the offset of each individual field beccause the function returns the byte corresponding to the start of the specific field passed as input.