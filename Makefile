BENCHMARK = main

CC = gcc

EXE = chatsd

SRC = $(BENCHMARK).c

CFLAGS = -lpthread 

.PHONY: all exe clean

all : exe

exe : $(EXE)

$(EXE) : $(SRC)
	$(CC) $(CFLAGS) $^ -o $@

clean :
	-rm chatsd
