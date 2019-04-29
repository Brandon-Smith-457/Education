typedef struct READYQUEUE {
	struct PCB *pcb;
	struct READYQUEUE* next;
} READYQUEUE;

int scheduler();
int addToReady(struct PCB *newPCB);
void savePreviousCPUState();
struct READYQUEUE *head;
FILE **ram;
