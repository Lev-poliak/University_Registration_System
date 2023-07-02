//
// Created by spl211 on 05/01/2021.
//

#include <semaphore.h>
#include <list>
#include "../include/connectionHandler.h"
#include "../include/ServerRespondHandler.h"

#ifndef CLIENT_DATAHANDLER_H
#define CLIENT_DATAHANDLER_H

enum Protocol{
    ADMIN_REG = 1,
    STUDENT_REG = 2,
    LOG_IN = 3,
    LOG_OUT = 4,
    COURSE_REG = 5,
    KDAM_CHECK = 6,
    COURSE_STATS = 7,
    STUDENT_STATS = 8,
    IS_REG = 9,
    UN_REG = 10,
    MY_COURSES = 11,
    ACK = 12,
    ERR = 13
};

class DataHandler{

public:

    static short stringToShort(const std::string &shortAsString);
    static int stringToInt(const std::string &intAsString);
    static std::string shortToString(short num);

    static std::string getDataAndMove(std::string &message, char deli);
    static int getIntAndMove(std::string &message, char deli);
    static short getShortAndMove(std::string &message, char deli);
    static std::string getStringAndMove(std::string &message, char deli);
    static std::list<std::string> getStringListAndMove(std::string &listAsSingleString, char dataDeli, char listDeli);
};

#endif //CLIENT_DATAHANDLER_H
