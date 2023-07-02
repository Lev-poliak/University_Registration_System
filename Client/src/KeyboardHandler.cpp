#include "../include/KeyboardHandler.h"

void KeyboardHandler::run(ConnectionHandler &connectionHandler) {

    bool logout = false;
    while (!logout) {
        const short buffSize = 1024;
        char buf[buffSize];
        std::cin.getline(buf, buffSize);
        std::string message(buf);
        std::string protocolString = DataHandler::getStringAndMove(message, ' ');
        Protocol protocolNum = protocolStringMap[protocolString];
        std::string protocolNumAsString = DataHandler::shortToString(protocolNum);
        std::string (*protocolFunction)(std::string&) = protocolFunctionMap[protocolNum];
        std::string messageToSend = protocolFunction(message);
        if (protocolFunction == sendLogout) { logout = true; }
        if (!connectionHandler.sendBytes(protocolNumAsString.c_str(), 2)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if (!connectionHandler.sendLine(messageToSend)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        int len = message.length();
        std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
    }
}

std::string KeyboardHandler::sendAdminReg(std::string &message) {
    return opcodeUsernameAndPasswordMessage(message, ADMIN_REG);

}

std::string KeyboardHandler::sendStudentReg(std::string &message) {
    return opcodeUsernameAndPasswordMessage(message, STUDENT_REG);

}

std::string KeyboardHandler::sendLogin(std::string &message) {
    return opcodeUsernameAndPasswordMessage(message, LOG_IN);
}

std::string KeyboardHandler::sendLogout(std::string &message) {
    std::string opcodeAsString = DataHandler::shortToString(LOG_OUT);
    std::string sendMessage = opcodeAsString;
    return sendMessage;
}

std::string KeyboardHandler::sendCourseReg(std::string &message) {
    return opcodeAndCourseNumMessage(message, COURSE_REG);

}

std::string KeyboardHandler::sendKdamCheck(std::string &message) {
    return opcodeAndCourseNumMessage(message, KDAM_CHECK);

}

std::string KeyboardHandler::sendCourseStat(std::string &message) {
    return opcodeAndCourseNumMessage(message, COURSE_STATS);

}

std::string KeyboardHandler::sendStudentStat(std::string &message) {
    std::string opcodeAsString = DataHandler::shortToString(STUDENT_STATS);
    std::string studentName = DataHandler::getStringAndMove(message, '\0');
    std::string sendMessage = opcodeAsString + studentName + '\0';
    return sendMessage;
}

std::string KeyboardHandler::sendIsRegistered(std::string &message) {
    return opcodeAndCourseNumMessage(message, IS_REG);

}

std::string KeyboardHandler::sendUnRegister(std::string &message) {
    return opcodeAndCourseNumMessage(message, UN_REG);

}

std::string KeyboardHandler::sendMyCourses(std::string &message) {
    return opcodeAndCourseNumMessage(message, MY_COURSES);
}

std::string KeyboardHandler::opcodeAndCourseNumMessage(std::string &message, Protocol opcode){
    short courseNum = DataHandler::getShortAndMove(message, '\0');
    std::string sendMessage = std::to_string(courseNum);
    return sendMessage;
}


std::string KeyboardHandler::opcodeUsernameAndPasswordMessage(std::string &message, Protocol opcode){
    std::string opcodeAsString = DataHandler::shortToString(opcode);
    std::string username = DataHandler::getStringAndMove(message, ' ');
    std::string password = DataHandler::getStringAndMove(message, '\0');
    std::string sendMessage = opcodeAsString + username + '\0' + password + '\0';
    return sendMessage;
}









