#define ERR_ARGS -2
#define ERR_ERR -1
#define ERR_NONE 0
#define ERR_QUIT 1
#define ERR_HELP 2
#define ERR_SET 3
#define ERR_PRINT 4
#define ERR_RUN 5

#define ONE_MB 1024

typedef struct STREAMMEMORY {
	FILE* stream;
	struct STREAMMEMORY* next;
	struct STREAMMEMORY* previous;
} STREAMMEMORY;

extern STREAMMEMORY *streamMemory;