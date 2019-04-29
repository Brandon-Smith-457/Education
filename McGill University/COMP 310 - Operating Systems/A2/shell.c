#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "interpreter.h"
#include "shellmemory.h"

int parse(char buffer[], char args[][100]) {
	int i = 0, j, k = 0;

	// skip white space
	

	while (k < 100) {
		while(i<1000 && buffer[i]==' ') i++;
		j = 0;
		while(i<1000 && buffer[i]!=' ' && buffer[i]!='\n' && buffer[i]!='\r') {
			args[k][j] = buffer[i];
			j++; i++;
		}
		args[k][j] = '\0';
		k++;
	}

	while(i<1000 && buffer[i]==' ') i++;

	// debug code: printf("[%s] [%s] [%s]\n", arg0, arg1, arg2);

	// check if there is more data (illegal)
	
	if (i==1000 || buffer[i]=='\n' || buffer[i]=='\r' || buffer[i]=='\0')
		return 1; // completed successfully (true)
	else
		return 0;
}

int prompt(char buffer[], char bufs[][100]) {
	int result = 0;

	result = parse(buffer,bufs);

	if (!result) printf("Error: Command %s has too many characters\n",buffer);
	else result = interpreter(bufs);

	if (result == 99) return 99;
	if (result == 98) printf("Command does not exist\n");
	if (result ==  1) printf("Wrong number of set parameters\n");
	if (result ==  2) printf("Wrong number of print parameters\n");
	if (result ==  3) printf("Run is  missing filename\n");
	if (result ==  4) printf("Script filename not found\n");
	if (result ==  5) printf("Incorrect number of arguments\n");
	if (result ==  6) printf("Exec error\n");

	return 0;
}

int mainLoop() {
	char buffer[1000], bufs[100][100];
	int done = 0, result = 0;

	initShellMemory();

	printf("Welcome to the Brandon Smith shell!\n");
	printf("Version 1.0 Created January 2019\n");

	while(!done) {
		printf("$ ");
		if (fgets(buffer, 999, stdin) == NULL) {
			if (!freopen("/dev/tty", "r", stdin)) {
				perror("/dev/tty");
				exit(1);
			}
		} else {
			if (!isatty(fileno(stdin))) printf("%s", buffer);
			result = prompt(buffer, bufs);
			if (result == 99) done = 1;
		}
	}

	printf("Good bye.\n");
	return 0;
}

