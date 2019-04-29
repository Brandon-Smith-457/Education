#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pcb.h"

struct PCB* makePCB(FILE *p, char *fileName) {
	struct PCB *tempPCB = malloc(sizeof(struct PCB));
	tempPCB->PC = p;
	tempPCB->PC_offset = 0;
	tempPCB->PC_page = 0;
	tempPCB->fileName = strdup(fileName);
	for (int i = 0; i < 10; i++) {
		tempPCB->pageTable[i] = -1;
	}
	return tempPCB;
}
