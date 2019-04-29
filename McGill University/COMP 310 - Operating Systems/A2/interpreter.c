#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "shellmemory.h"
#include "shell.h"
#include "kernel.h"

int run(char *filename) {
	FILE *ptr;
	char buffer[1000], bufs[100][100];
	int result = 0;

	ptr = fopen(filename,"rt");
	if (ptr == NULL) return 4; // file not found

	fgets(buffer,999,ptr);
	while(!feof(ptr)) {
		printf("$ %s", buffer);
		if (strlen(buffer)>1) result = prompt(buffer, bufs);
		if (result == 99) break;
		fgets(buffer,999,ptr);
	}

	fclose(ptr);

	if (result == 99) return 99;

	return 0;
}

int exec(char args[][100], int words) {
	//savePreviousCPUState();
	char openedFilesNames[10][100];
	FILE *openedFiles[10];
	int openedFilesWords = 0;
	int result = 0;
	for (int i = 1; i < words; i++) {
		if (openedFilesWords == 0) {
			openedFiles[openedFilesWords] = fopen(args[i], "rt");
			if (openedFiles[openedFilesWords] == NULL) printf("Script %s does not exist\n", args[i]);
			else {
				strcpy(openedFilesNames[openedFilesWords], args[i]);
				openedFilesWords++;
			}
		} else {
			for (int j = 0; j < openedFilesWords; j++) {
				if (strcmp(openedFilesNames[j], args[i]) == 0) result = 1;
			}
			if (result == 1) {
				printf("Script %s is already loaded into RAM\n", args[i]);
				result = 0;
			} else {
				openedFiles[openedFilesWords] = fopen(args[i], "rt");
				if (openedFiles[openedFilesWords] == NULL) printf("Script %s does not exist\n", args[i]);
				else {
					strcpy(openedFilesNames[openedFilesWords], args[i]);
					openedFilesWords++;
				}
			}
		}
	}
	int schedule = 0;
	if (head == NULL) schedule = 1;
	result = myinit(openedFiles, openedFilesWords);
	if (result == 6) return result;
	if (schedule) result = scheduler();
	return result;
}

int interpreter(char args[][100]) {
	int result = 0; // no errors
	int words = 0, i = 0;
	while (1) {
		if (*args[i] != '\0') words++;
		else break;
		i++;
	}

	if (strcmp(args[0],"help")==0) {
		if (words == 1) {
			printf("Legal commands:\n");
			printf("help              display this help\n");
			printf("quit              exits the shell\n");
			printf("set VAR STRING    assign STRING to VAR\n");
			printf("print VAR         display contents of VAR\n");
			printf("run SCRIPT.TXT    interpret SCRIPT.TXT\n");
			printf("exec p1 p2 p3     Executes concurrent programs\n");

			result = 0;
		} else result = 5;
	}
	else if (strcmp(args[0],"quit")==0) {
		if (words == 1) result = 99; // exit shell code
		else result = 5;
	}
	else if (strcmp(args[0],"set")==0) {
		if (words == 3) {
			if (strlen(args[1])<1 || strlen(args[2])<1) return 1; // set error
			add(strdup(args[1]), strdup(args[2]));
		} else result = 5;
	}
	else if (strcmp(args[0],"print")==0) {
		if (words == 2) {
			if (strlen(args[1])<1) return 2; // print error
		
			char *temp = get(args[1]);

			if (temp != NULL) printf("%s\n", temp);
			else printf("The variable %s does not exist\n", args[1]);
		} else result = 5;

	}
	else if (strcmp(args[0],"run")==0) {
		if (words == 2) {
			if (strlen(args[1])<1) return 3; // run error
			result = run(args[1]);
		} else result = 5;
	}
	else if (strcmp(args[0],"exec")==0) {
		if (words >= 2 && words <= 11) {
			for (int i = 0; i < words; i++) {
				if (strlen(args[i])<1) return 6; //exec error
			}
			result = exec(args, words);
		} else result = 5;
	}
	else {
		result = 98; // command does not exist
	}

	return result;
}

