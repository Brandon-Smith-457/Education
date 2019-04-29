int checkCPUAvailable();
FILE* runCPU(int quanta, int *eofFlag);
int setCPUInstructionPointer(FILE* PC);
void clearIP();
void clearIR();
FILE* getIP();