#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <time.h>
#include "ram.h"
#include "kernel.h"
#include "memorymanager.h"

int launcher(FILE *p, char fileName[100]) {
	char name[150] = "BackingStore/";
	FILE *store = NULL;
	strcat(name, fileName);
	//Copying the file into the backing store
	char command[150] = "touch BackingStore/";
	strcat(command, fileName);
	system(command);
	strcpy(command, "BackingStore/");
	strcat(command, fileName);
	store = fopen(command, "w+");
	freopen(command, "a+", store);
	if (store == NULL) return 0;
	char line[1000];
	while(fgets(line, sizeof(line), p)) {
		fputs(line, store);
	}
	//Closing the original
	fclose(p);

	//Opening the file in the backing store
	freopen(command, "r", store);

	//Making the PCB
	PCB *pcb = makePCB(store, name);

	//Launching technique
	int totalPages = countTotalPages(store);
	pcb->pages_max = totalPages;
	pcb->PC = findPage(pcb->PC_page, pcb->PC, pcb->fileName);
	int loadNumber = 0;
	if (totalPages == 1) {
		loadNumber = 1;
	} else if (totalPages >= 2) {
		loadNumber = 2;
	}
	for (int i = 0; i < loadNumber; i++) {
		int frameNumber = findFrame();
		int victimFrame = -1;
		if (frameNumber == -1) {
			victimFrame = findVictim(pcb);
		}
		updateFrame(frameNumber, victimFrame, pcb->PC);
		updatePageTable(pcb, i, frameNumber, victimFrame);
	}
	addToReady(pcb);

	return 1;
}

int countTotalPages(FILE *p) {
	int position = ftell(p);
	rewind(p);
	char line[1000];
	int count = 0;
	while(fgets(line, sizeof(line), p)) {
		count++;
	}
	rewind(p);
	fseek(p, position, SEEK_CUR);
	return count/4 + 1;
}

FILE* findPage(int pageNumber, FILE *p, char* fileName) {
	if (countTotalPages(p) > pageNumber) {
		FILE *p2 = fopen(fileName, "r");
		//fseek(p2, ftell(p), SEEK_CUR);
		char line[1000];
		for (int i = 0; i < 4*pageNumber; i++) {
			fgets(line, sizeof(line), p2);
		}
		return p2;
	}
	printf("Page Number out of range");
	return p;
}

int findFrame() {
	for (int i = 0; i < 10; i++) {
		if (ram[i] == NULL) return i;
	}
	return -1;
}

int findVictim(PCB *pcb) {
	time_t t;
	srand((unsigned) time(&t));
	int victimNumber = rand() % 10;
	for (int i = 0; i < 10; i++) {
		if (victimNumber == pcb->pageTable[i]) {
			victimNumber = (victimNumber + 1) % 10;
			i = 0;
		}
	}
	return victimNumber;
}

int updateFrame(int frameNumber, int victimFrame, FILE *page) {
	int result = 0;
	if (frameNumber == -1) {
		ram[victimFrame] = page;
		result = 2;
	} else {
		ram[frameNumber] = page;
		result = 1;
	}
	return result;
}

PCB* getVictimPCB(int victimFrame) {
	READYQUEUE *temp = head;
	while (temp != NULL) {
		for (int i = 0; i < 10; i++) {
			if (temp->pcb->pageTable[i] == victimFrame) return temp->pcb;
		}
		temp = temp->next;
	}
	return NULL;
}

int updatePageTable(PCB *pcb, int pageNumber, int frameNumber, int victimFrame) {
	int result = 0;
	if (frameNumber == -1) {
		PCB *victim = getVictimPCB(victimFrame);
		if (victim == NULL) return 6;
		pcb->pageTable[pageNumber] = victimFrame;
		victim->pageTable[pageNumber] = -1;
		result = 2;
	} else {
		pcb->pageTable[pageNumber] = frameNumber;
		result = 1;
	}
	return result;
}










