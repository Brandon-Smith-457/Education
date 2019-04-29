struct PCB* makePCB(FILE *p, char *fileName);

typedef struct PCB {
	FILE *PC;
	char *fileName;
	int pageTable[10];
	int PC_page;
	int PC_offset;
	int pages_max;
} PCB;
