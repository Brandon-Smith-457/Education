#include <stdio.h>
#include <stdlib.h>
#include "shell.h"
#include "ram.h"
#include "cpu.h"
#include "kernel.h"
#include "memorymanager.h"

struct READYQUEUE *head = NULL;
struct READYQUEUE *tail = NULL;
FILE **ram;

int QUANTA = 2;

void cycleReadyQueue();
void removeHeadFromReady();
void clearReadyQueue();
int boot();

int main() {
	if (boot()) {
		printf("Kernel 2.0 loaded!\n");
		mainLoop();
	}
}

int boot() {
	int ramSize = 10;
	ram = makeRAM(ramSize);
	if (system("[ -d BackingStore ]") == 0) {
		system("rm -rf BackingStore");
	}
	system("mkdir BackingStore");
	return 1;
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
	fclose(head->pcb->PC);
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

//Pointless function now
/*int myinit(FILE *files[], int words) {
	int result = 0;
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
}*/

void removePagesFromRam() {
	for (int i = 0; i < 10; i++) {
		if (head->pcb->pageTable[i] != -1) {
			removeFromRAM(ram, head->pcb->pageTable[i]);
		}
	}
}

void clearReadyQueue() {
	while (head != NULL) {
		removePagesFromRam();
		removeHeadFromReady();
	}
}

void pageFault() {
	head->pcb->PC_page++;
	if (head->pcb->PC_page >= head->pcb->pages_max) {
		removePagesFromRam();
	} else {
		if (head->pcb->pageTable[head->pcb->PC_page] != -1) {
			head->pcb->PC = getFromRAM(ram, head->pcb->pageTable[head->pcb->PC_page]);
			head->pcb->PC_offset = 0;
		} else {
			head->pcb->PC = findPage(head->pcb->PC_page, head->pcb->PC, head->pcb->fileName);
			int frameNumber = findFrame();
			int victimFrame = -1;
			if (frameNumber == -1) {
				victimFrame = findVictim(head->pcb);
			}
			updateFrame(frameNumber, victimFrame, head->pcb->PC);
			updatePageTable(head->pcb, head->pcb->PC_page, frameNumber, victimFrame);
			head->pcb->PC_offset = 0;
		}
	}
}

int scheduler() {
	int result = 0, eofFlag = 0, pageFaultFlag = 0;
	while (head != NULL) {
		if (checkCPUAvailable() == 1) {
			result = setCPUInstructionPointer(head->pcb->PC);
			offset = head->pcb->PC_offset;
			if (result == 6) {
				clearReadyQueue();
				break;
			}
			FILE *newPC = runCPU(QUANTA, &eofFlag, &pageFaultFlag);
			if (newPC == NULL) return 99;
			clearIP();
			clearIR();
			if (!eofFlag && !pageFaultFlag) {
				head->pcb->PC = newPC;
				head->pcb->PC_offset = offset;
				cycleReadyQueue();
			} else if (eofFlag) {
				removePagesFromRam();
				removeHeadFromReady();
				eofFlag = 0;
			} else if (pageFaultFlag) {
				pageFault();
				cycleReadyQueue();
				pageFaultFlag = 0;
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






















