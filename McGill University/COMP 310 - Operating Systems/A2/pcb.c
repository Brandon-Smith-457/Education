#include <stdio.h>
#include <stdlib.h>
#include "pcb.h"

struct PCB* makePCB(FILE *p) {
	struct PCB *tempPCB = malloc(sizeof(struct PCB));
	tempPCB->PC = p;
	return tempPCB;
}