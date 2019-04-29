typedef struct READYQUEUE {
	struct PCB *pcb;
	struct READYQUEUE* next;
} READYQUEUE;

int myinit(FILE *files[], int words);
int scheduler();
void savePreviousCPUState();
struct READYQUEUE *head;