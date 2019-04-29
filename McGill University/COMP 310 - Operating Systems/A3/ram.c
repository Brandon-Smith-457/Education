#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include "kernel.h"
#include "interpreter.h"

//Checking if two filestreams are equivalent courtesy of nneonneo on Stacked Exchange
//https://stackoverflow.com/questions/12502552/can-i-check-if-two-file-or-file-descriptor-numbers-refer-to-the-same-file
int same_file(int fd1, int fd2) {
    struct stat stat1, stat2;
    if(fstat(fd1, &stat1) < 0) return -1;
    if(fstat(fd2, &stat2) < 0) return -1;
    return (stat1.st_dev == stat2.st_dev) && (stat1.st_ino == stat2.st_ino);
}

int addToRAM(FILE** ram, FILE *p) {
	int k, exists = 0;
	for (k = 0; k < 10; k++) {
		if (ram[k] != NULL)
			if (same_file(fileno(p), fileno(ram[k]))) exists = 1;
	}
	if (exists) {
		printf("One of the scripts already exists in RAM");
		return -2;
	}
	for (k = 0; k < 10; k++) {
		if (ram[k] == NULL) {
			ram[k] = p;
			break;
		}
	}
	if (k == 10) {
		printf("RAM is full, could not add file\n");
		return -1;
	}
	return k;
}

FILE** makeRAM(int size) {
	FILE **ram = (FILE**) malloc(sizeof(FILE*)*size);
	for (int i = 0; i < size; i++) {
		ram[i] = NULL;
	}
	return ram;
}

void removeFromRAM(FILE** ram, int k) {
	if (k < 0 || k > 9) {
		//printf("Invalid RAM address\n");
		return;
	}
	if (ram[k] == NULL) {
		//printf("RAM address already empty\n");
		return;
	}
	ram[k] = NULL;
	//printf("RAM address %d removed\n", k);
}

FILE* getFromRAM(FILE** ram, int k) {
	if (k < 0 || k > 9) {
		//printf("Invalid RAM address\n");
		return NULL;
	}
	if (ram[k] == NULL) {
		//printf("RAM address empty\n");
		return NULL;
	}
	return ram[k];
}
