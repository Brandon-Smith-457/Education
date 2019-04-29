CC=gcc
SOURCE = cpu.c interpreter.c kernel.c pcb.c ram.c shell.c shellmemory.c
DEPS = cpu.h interpreter.h kernel.h pcb.h ram.h shell.h shellmemory.h
OBJ = cpu.o interpreter.o kernel.o pcb.o ram.o shell.o shellmemory.o
CFLAGS = -g

all: mykernel

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $<

mykernel: $(OBJ)
	$(CC) -o $@ $^
	
debug:
	$(CC) -o $@ $(SOURCE) $(CFLAGS)
	
clean:
	-rm $(OBJ) mykernel debug