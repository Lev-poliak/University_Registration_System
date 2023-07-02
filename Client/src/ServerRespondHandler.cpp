#include "../include/ServerRespondHandler.h"
#include "../include/DataHandler.h"
#include <list>

void ServerRespondHandler::operator()(){
    while(true){
        char accOrErrAsChars[2];
        char opcodeSentAsChars[2];
        if (!connectionHandler.getBytes(accOrErrAsChars, 2)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if (!connectionHandler.getBytes(opcodeSentAsChars, 2)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        short accOrErr = DataHandler::stringToShort(accOrErrAsChars);
        short opcode = DataHandler::stringToShort(opcodeSentAsChars);
        if (accOrErr == ACK){
            std::string message;
            if (!connectionHandler.getLine(message)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            manageAck(opcode, message);
        } else { //opcode == ERR
            manageErr(opcode);
        }
    }
}

void ServerRespondHandler::manageErr(short errOpcode) {
    std::cout << "ERROR " << errOpcode << std::endl;
}

void ServerRespondHandler::manageAck(short opcode, const std::string& message) {
    switch(opcode) {
        case LOG_OUT: //log out
            logOut();
            break;
        case KDAM_CHECK: //kdam check
            printCourseKdam(message);
            break;
        case COURSE_STATS:
            printCourseStats(message);
            break;
        case STUDENT_STATS:
            printStudentsStats(message);
            break;
        case IS_REG:
            getIsRegistered(message);
            break;
        case MY_COURSES:
            printMyCourses(message);
            break;
        case ADMIN_REG:
        case STUDENT_REG:
        case LOG_IN:
        case COURSE_REG:
        case UN_REG:
        default:
            break;
    }
}

void ServerRespondHandler::logOut() { exit(0); }

void ServerRespondHandler::printCourseKdam(const std::string& message){
    std::string coursesList;
    std::string tempMessage = message;
    if (message[0] != '\0'){
        return;
    }
    short courseNum = DataHandler::getShortAndMove(tempMessage, DATA_DELI);
    coursesList += std::to_string(courseNum);
    while(tempMessage[0] != '\0'){
        courseNum = DataHandler::getShortAndMove(tempMessage, DATA_DELI);
        coursesList += std::to_string(courseNum);
    }
    std::cout << "[" << coursesList << "]" << std::endl;
}

void ServerRespondHandler::printCourseStats(const std::string &message) {
    // get the stats
    std::string tmpMessage = message;
    short courseNum = DataHandler::getShortAndMove(tmpMessage, DATA_DELI);
    std::string courseName = DataHandler::getStringAndMove(tmpMessage, DATA_DELI);
    int seatsAvailable = DataHandler::getIntAndMove(tmpMessage, DATA_DELI);
    int maxNumOfSeats = DataHandler::getIntAndMove(tmpMessage, DATA_DELI);
    std::list<std::string> studentsRegList = DataHandler::getStringListAndMove(tmpMessage, DATA_DELI, LIST_DELI);
    std::string studentsRegString = stringListToProperString(studentsRegList);

    // print the stats
    std::cout << "Course: (" << courseNum << " " << courseName << std::endl;
    std::cout << "Seats Available: " << seatsAvailable << "\\" << maxNumOfSeats << std::endl;
    std::cout << "Students Registered: [" + studentsRegString + "]" << std::endl;
}

void ServerRespondHandler::printStudentsStats(const std::string &message) {
    // get the stats
    std::string tmpMessage = message;
    std::string userName = DataHandler::getStringAndMove(tmpMessage, DATA_DELI);
    std::list<std::string> coursesList = DataHandler::getStringListAndMove(tmpMessage, DATA_DELI, LIST_DELI);
    std::string coursesString = stringListToProperString(coursesList);

    // print stats
    std::cout << "Student: " << userName << std::endl;
    std::cout << "Courses: [" << coursesString << "]" << std::endl;
}

void ServerRespondHandler::getIsRegistered(const std::string &message) {
    bool isReg = message[0];
    if (isReg){
        std::cout << "REGISTERED" << std::endl;
    } else {
        std::cout << "NOT REGISTERED" << std::endl;
    }
}

void ServerRespondHandler::printMyCourses(const std::string &message) {
    std::string tmpMessage = message;
    std::list<std::string> coursesList = DataHandler::getStringListAndMove(tmpMessage, DATA_DELI, LIST_DELI);
    std::string coursesString = stringListToProperString(coursesList);

    // print stats
    std::cout << "[" << coursesString << "]" << std::endl;
}

std::string ServerRespondHandler::stringListToProperString(const std::list<std::string> &stringList){
    std::string properString;
    for(const std::string &stringIter: stringList){
        properString += stringIter + ',';
    }
    // remove last comma
    properString = properString.substr(0, properString.size() - 1);
    return properString;
}





