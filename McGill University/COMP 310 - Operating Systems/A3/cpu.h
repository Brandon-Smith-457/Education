int checkCPUAvailable();
FILE* runCPU(int quanta, int *eofFlag, int *pageFaultFlag);
int setCPUInstructionPointer(FILE* PC);
void clearIP();
void clearIR();
FILE* getIP();

int offset;
