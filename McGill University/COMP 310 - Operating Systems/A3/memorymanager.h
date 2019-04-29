#include "pcb.h"

int launcher(FILE *p, char fileName[100]);
int countTotalPages(FILE *p);
FILE* findPage(int pageNumber, FILE *p, char* fileName);
int findFrame();
int findVictim(PCB *pcb);
int updateFrame(int frameNumber, int victimFrame, FILE *page);
int updatePageTable(PCB *pcb, int pageNumber, int frameNumber, int victimFrame);
