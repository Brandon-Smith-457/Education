struct PCB* makePCB(FILE *p);

typedef struct PCB {
	FILE *PC;
	int ramAddress;
} PCB;