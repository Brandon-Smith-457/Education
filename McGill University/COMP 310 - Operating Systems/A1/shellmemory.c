#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "shell.h"

//Linked list for "infinite" possibilities
struct SHELLMEMORY {
	char* var;
	char* val;
	struct SHELLMEMORY* next;
} *shellMemory = NULL;

//Currently neither setting nor printing from the linked list are optimal via sorting

int setMem(char* var, char* val) {
	//If it's the first entry
	if (shellMemory == NULL) {
		shellMemory = malloc(sizeof(struct SHELLMEMORY));
		if (shellMemory == NULL) return ERR_ERR;
		shellMemory->var = strdup(var);
		shellMemory->val = strdup(val);
		shellMemory->next = NULL;
		return ERR_SET;
	}
	
	//If there already exists some variables
	else {
		struct SHELLMEMORY* current = shellMemory;
		struct SHELLMEMORY* previous = NULL;
		while (current != NULL) {
			if (strcmp(current->var, var) == 0) {
				current->val = strdup(val);
				return ERR_SET;
			}
			else {
				previous = current;
				current = current->next;
			}
		}
		//The previous variable is necessary for when it comes time to malloc (otherwise you would malloc on a temporary pointer)
		previous->next = malloc(sizeof(struct SHELLMEMORY));
		current = previous->next;
		if (current == NULL) return ERR_ERR;
		current->var = strdup(var);
		current->val = strdup(val);
		current->next = NULL;
		return ERR_SET;
	}
	return ERR_ERR;
}

int printMem(char* var) {
	struct SHELLMEMORY* current = shellMemory;
	while (current != NULL) {
		if (strcmp(current->var, var) == 0) {
			printf("%s = %s\n", current->var, current->val);
			return ERR_PRINT;
		}
		else {
			current = current->next;
		}
	}
	printf("Variable %s does not exist\n", var);
	return ERR_PRINT;
}

