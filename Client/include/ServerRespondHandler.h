//
// Created by spl211 on 02/01/2021.
//

#ifndef CLIENT_INPUTHANDLER_H
#define CLIENT_INPUTHANDLER_H


#include "../include/connectionHandler.h"
#include <semaphore.h>
#include <list>

class ServerRespondHandler {

private:
    ConnectionHandler &connectionHandler;

public:

    ServerRespondHandler(ConnectionHandler &connectionHandler): connectionHandler(connectionHandler) {};
    ~ServerRespondHandler() = default;

    void operator()();

    static const char DATA_DELI = '\0';
    static const char LIST_DELI = '|';
private:
    static void manageAck(short opcode, const std::string & respond);
    static void manageErr(short errOpcode);

    static void printCourseKdam(const std::string& message);
    static void printCourseStats(const std::string& message);
    static void printStudentsStats(const std::string& message);
    static void getIsRegistered(const std::string& message);
    static void printMyCourses(const std::string& message);
    static void logOut();

    static std::string stringListToProperString(const std::list<std::string>& stringList);

};


#endif //CLIENT_INPUTHANDLER_H
