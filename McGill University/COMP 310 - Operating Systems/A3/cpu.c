#include <stdlib.h>
#include <stdio.h>
#include "shell.h"
FILE *IP = NULL;
char IR[1000];
int offset;

int checkCPUAvailable() {
	if (IP == NULL) return 1;
	else return 0;
}

FILE* runCPU(int quanta, int *eofFlag, int *pageFaultFlag) {
	for (int i = 0; i < quanta; i++) {
		char args[100][100];
		if (fgets(IR, 999, IP) == NULL) {
			*eofFlag = 1;
			break;
		}
		printf("$ %s", IR);
		if (prompt(IR, args) == 99) {
			IP = NULL;
			break;
		}
		offset++;
		if (offset >= 4) {
			*pageFaultFlag = 1;
			break;
		}
	}
	return IP;
}

int setCPUInstructionPointer(FILE *PC) {
	if (PC == NULL) return 6;
	IP = PC;
	return 0;
}

void clearIP() {
	IP = NULL;
}

void clearIR() {
	int i = 0;
	while (IR[i] != '\0') {
		IR[i++] = '\0';
	}
}

FILE* getIP() {
	return IP;
}









