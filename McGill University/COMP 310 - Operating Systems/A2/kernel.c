#include <stdio.h>
#include <stdlib.h>
#include "shell.h"
#include "ram.h"
#include "pcb.h"
#include "cpu.h"
#include "kernel.h"

struct READYQUEUE *head = NULL;
struct READYQUEUE *tail = NULL;
FILE **ram;

int QUANTA = 2;

int addToReady(struct PCB *newPCB);
void cycleReadyQueue();
void removeHeadFromReady();
void clearReadyQueue();

int main() {
	printf("Kernel 1.0 loaded!\n");
	mainLoop();
}

int addToReady(struct PCB *newPCB) {
	int result = 0;
	if (head == NULL || tail == NULL) {
		head = malloc(sizeof(struct READYQUEUE));
		head->pcb = newPCB;
		head->next = NULL;
		tail = head;
		return result;
	}
	else if (head == tail && head->next == NULL) {
		head->next = malloc(sizeof(struct READYQUEUE));
		head->next->pcb = newPCB;
		head->next->next = NULL;
		tail = head->next;
		return result;
	}
	else if (head != tail) {
		tail->next = malloc(sizeof(struct READYQUEUE));
		tail->next->pcb = newPCB;
		tail->next->next = NULL;
		tail = tail->next;
		return result;
	}
	return 1;
}

void removeHeadFromReady() {
	struct READYQUEUE *temp = head;
	free(head->pcb);
	head = head->next;
	free(temp);
}

void cycleReadyQueue() {
	if (head == NULL || tail == NULL) {
		printf("ReadyQueue is empty\n");
		return;
	}
	else if (head == tail && head->next == NULL) {
		return;
	}
	else if (head != tail) {
		struct READYQUEUE *temp = head;
		head = head->next;
		temp->next = NULL;
		tail->next = temp;
		tail = temp;
		return;
	}
}

int myinit(FILE *files[], int words) {
	int result = 0, ramSize = 10;
	ram = makeRAM(ramSize);
	for (int i = 0; i < words; i++) {
		int k = addToRAM(ram, files[i]);
		if (k == -1) {
			result = 6;
			break;
		}
		if (k != -2) {
			struct PCB *newPCB = makePCB(files[i]);
			if (newPCB == NULL) {
				result = 6;
				break;
			}
			newPCB->ramAddress = k;
			if (addToReady(newPCB) == 1) {
				result = 6;
				break;
			}
		}

	}
	return result;
}

void clearReadyQueue() {
	while (head != NULL) {
		removeFromRAM(ram, head->pcb->ramAddress);
		removeHeadFromReady();
	}
}

int scheduler() {
	int result = 0, eofFlag = 0;
	while (head != NULL) {
		if (checkCPUAvailable() == 1) {
			result = setCPUInstructionPointer(head->pcb->PC);
			if (result == 6) {
				clearReadyQueue();
				break;
			}
			FILE *newPC = runCPU(QUANTA, &eofFlag);
			if (newPC == NULL) return 99;
			clearIP();
			clearIR();
			if (!eofFlag) {
				head->pcb->PC = newPC;
				cycleReadyQueue();
			} else {
				removeFromRAM(ram, head->pcb->ramAddress);
				removeHeadFromReady();
				eofFlag = 0;
			}
		}
	}
	return result;
}

void savePreviousCPUState() {
	FILE* temp = getIP();
	if (temp != NULL) {
		head->pcb->PC = temp;
		clearIP();
		clearIR();
	}
}






















