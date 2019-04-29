#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "interpreter.h"
#include "shellmemory.h"
#include "shell.h"

//Input size to dynamically grow the total size of possible userInputs
int inputSize, errorCode, loop = 1;
char *name = "Brandon Smith";
char *prompt = "$";

//Input because it gives more control to read character by character
char *userInput = NULL, input;
char** parsedInput = NULL;

//Stream will be used when switching between stdin and a file
//Linked list typedef in header file
STREAMMEMORY *streamMemory = NULL;

int parse();
int freeInputs();
int memRealloc();

int main (int argc, char* argv[]) {	
	printf("Welcome to the %s shell!\nVersion 1.0 Created January 2019\n", name);
	
	//Setting the stream to stdin at start
	streamMemory = malloc(sizeof(STREAMMEMORY));
	streamMemory->stream = stdin;
	streamMemory->next = NULL;
	streamMemory->previous = NULL;
	
	//If there is a piped file in stdin then create a streamMemory link.
	if ((fseek(stdin, 0, SEEK_END), ftell(stdin)) > 0) {
		rewind(stdin);
		streamMemory->previous = malloc(sizeof(STREAMMEMORY));
		streamMemory->previous->next = streamMemory;
		streamMemory->previous->previous = NULL;
		streamMemory->previous->stream = stdin;
	}
	
	while (loop) {
		//Initializing the inputSize to one MB
		inputSize = ONE_MB;
		
		//Reserving inputSize space for userInput (in loop so that the last userInput's size is not kept)
		userInput = (char*) realloc(userInput, inputSize);
		
		//Print the prompt for the user if they have not switched the input stream by calling a run command
		if (streamMemory->stream == stdin && streamMemory->previous == NULL) printf(" %s ", prompt);

		//Stripping un-necessary leading spaces and tabs
		do input = fgetc(streamMemory->stream);
		while (input == ' ' || input == '\t');
		
		int i = 0;
		//The if statement here is to divert flow to switch the stream back to stdin
		if (input != EOF) {
			//Loop through all the characters, storing them into userInput
			while (input != '\n' && input != '\r' && input != EOF) {
				*(userInput + i) = input;
				i++;
				//When input gets larger than one MB, increase userInput size
				if (i >= inputSize) memRealloc();
				input = fgetc(streamMemory->stream);
				
				//Clear any trailing tabs (trailing spaces are currently a problem)
				//Can't remove trailing spaces here.
				while (input == '\t') input = fgetc(streamMemory->stream);
			}
			//Handling Windows CRLF line ending annoyances
			if (input == '\r') input = fgetc(streamMemory->stream);
		}
		else {
			if (streamMemory->stream != stdin) fclose(streamMemory->stream);
			if (streamMemory->previous != NULL) {
				streamMemory = streamMemory->previous;
				free(streamMemory->next);
				streamMemory->next = NULL;
			}
			if (streamMemory->previous == NULL) {
				//Redirect stdin back to the terminal
				if (!freopen("/dev/tty", "r", stdin)) {
					perror("/dev/tty");
					exit(1);
				}
			}
		}
		
		//Printing the commands read from a script
		//YOU CAN COMMENT OUT THIS IF BLOCK IF YOU DON'T WANT THE COMMANDS TO BE PRINTED
		if (streamMemory->previous != NULL) {
			int k = strlen(userInput);
			while (*(userInput + (k - 1)) == ' ') k--;
			putc('\t', stdout);
			for (i = 0; i < k; i++) putc(*(userInput + i), stdout);
			printf(" :\n");
		}

		errorCode = parse(userInput);
		//Currently need to ask Prof about Error codes overall.
		switch (errorCode) {
			case ERR_NONE :
				break;
			case ERR_QUIT :
				loop = 0;
				break;
			case ERR_HELP :
				break;
			case ERR_SET :
				break;
			case ERR_PRINT :
				break;
			case ERR_RUN :
				break;
			case ERR_ERR :
				loop = 0;
				printf("Error\n");
				break;
			case ERR_ARGS :
				printf("Incorrect number of arguments\n");
				break;
			default :
				printf("Unexpected\n");
		}
		
		//Deal with the previous inputs being longer than the current one "hello" followed by "help" would result in "helpo"
		if(userInput != NULL) freeInputs();
	}
	//Free-ing the memory allocation for posterities sake (just in case Linux decides it doesn't wanna garbage collect anymore
	free(userInput);
	free(parsedInput);
}

int parse() {
	int words = 0;
	
	//If the user just hit enter
	if (*(userInput) == '\0') return ERR_NONE;
	
	//Tokenize the input by ' ' and store the result in parsedInput 
	char* temp = strtok(userInput, " ");

	while (temp) {
		words++;
		parsedInput = realloc(parsedInput, sizeof(char*) * words);
		if (parsedInput == NULL) return ERR_ERR;
		parsedInput[words - 1] = temp;
		temp = strtok (NULL, " ");
	}
	parsedInput = realloc (parsedInput, sizeof(char*) * (words + 1));
	parsedInput[words] = 0;
	return interpreter(words, parsedInput);
}

int memRealloc() {
	//increasing by inputSize to avoid overly excessive calls to this function if a user has overly long variable names
	inputSize += inputSize;
	userInput = realloc(userInput, inputSize);
	return 0;
}

//Set userInput and parsedInput to '\0' for any previously written characters
int freeInputs() {
	int i = 0;
	while (*(userInput + i) != '\0') {
		*(userInput + i) = '\0';
		i++;
	}
	i = 0;
	if (parsedInput != NULL) {
		while (*(parsedInput + i) != '\0') {
			int j = 0;
			while (*(*(parsedInput + i) + j) != '\0') {
				*(*(parsedInput + i) + j) = '\0';
				j++;
			}
			i++;
		}
	}
}





