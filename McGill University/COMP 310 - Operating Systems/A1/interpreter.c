#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "shell.h"
#include "shellmemory.h"

int help();
int quit();
int set(char* var, char* val);
int print(char* var);
int run(char* script);

//Check for command words in parsedInput[0] and check for correct number of arguments
int interpreter(int words, char** parsedInput) {
	if (strcmp(parsedInput[0], "help") == 0) {
		if (words == 1) return help();
		else return ERR_ARGS;
	}
	if (strcmp(parsedInput[0], "quit") == 0) {
		if (words == 1) return quit();
		else return ERR_ARGS;
	}
	if (strcmp(parsedInput[0], "set") == 0) {
		if (words == 3) return set(parsedInput[1], parsedInput[2]);
		else return ERR_ARGS;
	}
	if (strcmp(parsedInput[0], "print") == 0) {
		if (words == 2) return print(parsedInput[1]);
		else return ERR_ARGS;
	}
	if (strcmp(parsedInput[0], "run") == 0) {
		if (words == 2) return run(parsedInput[1]);
		else return ERR_ARGS;
	}
	
	//If it's none of the above commands and is not the empty string inform the user of Unknown command
	if (strcmp(parsedInput[0], "") != 0) printf("Unknown Command: %s\n", parsedInput[0]);
	return ERR_NONE;
}

int help() {
	printf("COMMAND\t\t\tDESCRIPTION\n");
	printf("help\t\t\tDisplays all the commands\n");
	printf("quit\t\t\tExits / terminates the shell with \"Bye!\"\n");
	printf("set VAR STRING\t\tAssigns a value to shell memory\n");
	printf("print VAR\t\tPrints the STRING assigned to VAR\n");
	printf("run SCRIPT.TXT\t\tExecutes the file SCRIPT.TXT\n");
	return ERR_HELP;
}

int quit() {
	printf("Bye!\n");
	return ERR_QUIT;
}

int set(char* var, char* val) {
	return setMem(var, val);
}

int print(char* var) {
	return printMem(var);
}

int run(char* script) {
	FILE* fp = fopen(script, "r");
	
	//If the file doesn't exist inform the user
	if (fp == NULL) {
		printf("Script not found\n");
		return ERR_NONE;
	}
	
	//Otherwise set the stream to the script and keep track of the previous ones
	streamMemory->next = malloc(sizeof(STREAMMEMORY));
	streamMemory->next->stream = fp;
	streamMemory->next->previous = streamMemory;
	streamMemory = streamMemory->next;
	return ERR_RUN;
}






