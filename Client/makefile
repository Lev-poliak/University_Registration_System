CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread

all: EchoClient
	g++ -o bin/echoExample bin/connectionHandler.o bin/echoClient.o bin/ServerRespondHandler.o \
	bin/KeyboardHandler.o bin/DataHandler.o $(LDFLAGS)

EchoClient: bin/connectionHandler.o bin/ServerRespondHandler.o bin/echoClient.o  bin/KeyboardHandler.o bin/DataHandler.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/DataHandler.o: src/DataHandler.cpp
	g++ $(CFLAGS) -o bin/DataHandler.o src/DataHandler.cpp

bin/ServerRespondHandler.o: src/ServerRespondHandler.cpp
	g++ $(CFLAGS) -o bin/ServerRespondHandler.o  src/ServerRespondHandler.cpp

bin/KeyboardHandler.o: src/KeyboardHandler.cpp
	g++ $(CFLAGS) -o bin/KeyboardHandler.o src/KeyboardHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
