#include "../include/DataHandler.h"


std::string DataHandler::getDataAndMove(std::string &message, char deli){
    size_t deliLocation = message.find(deli);
    std::string results = message.substr(0, deliLocation);
    message = (strchr(message.c_str(), deli)) + 1;
    return results;
}

std::string DataHandler::getStringAndMove(std::string &message, char deli) {
    return getDataAndMove(message, deli);
}

std::list<std::string> DataHandler::getStringListAndMove(std::string &listAsSingleString, char dataDeli, char listDeli){
    std::__cxx11::list<std::string> stringList;
    while(listAsSingleString[0] != dataDeli){
        stringList.push_back(getDataAndMove(listAsSingleString, listDeli));
    }
    return stringList;
}

short DataHandler::getShortAndMove(std::string &message, char deli){
    std::string shortAsString = getStringAndMove(message, deli);
    return stringToShort(shortAsString);
}

short DataHandler::stringToShort(const std::string &shortAsString){
    return ((shortAsString[1] << 8) | shortAsString[0]);
}

std::string DataHandler::shortToString(short num)
{
    std::string shortAsString;
    shortAsString[0] = ((num >> 8) & 0xFF);
    shortAsString[1] = (num & 0xFF);
    return shortAsString;
}

int DataHandler::getIntAndMove(std::string &message, char deli){
    std::string intAsString = getStringAndMove(message, deli);
    return stringToInt(intAsString);
}

int DataHandler::stringToInt(const std::string &intAsString){
    return ((intAsString[3] << 24) | (intAsString[2] << 16) | (intAsString[1] << 8) | intAsString[0]);
}
