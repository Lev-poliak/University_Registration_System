#ifndef CLIENT_KEYBOARDHANDLER_H
#define CLIENT_KEYBOARDHANDLER_H

#endif //CLIENT_KEYBOARDHANDLER_H

#include <connectionHandler.h>
#include <map>
#include "../include/DataHandler.h"

class KeyboardHandler {

private:
    std::map<Protocol, std::string (*)(std::string&)> protocolFunctionMap = {
            {ADMIN_REG,sendAdminReg},
            {STUDENT_REG,sendStudentReg},
            {LOG_IN,sendLogin},
            {LOG_OUT,sendLogout},
            {COURSE_REG,sendCourseReg},
            {KDAM_CHECK,sendKdamCheck},
            {COURSE_STATS,sendCourseStat},
            {STUDENT_STATS,sendStudentStat},
            {IS_REG,sendIsRegistered},
            {UN_REG,sendUnRegister},
            {MY_COURSES,sendMyCourses}
    };

    std::map<std::string, Protocol> protocolStringMap = {
            {"ADMINREG", ADMIN_REG},
            {"STUDENTREG", STUDENT_REG},
            {"LOGIN", LOG_IN},
            {"LOGOUT", LOG_OUT},
            {"COURSEREG", COURSE_REG},
            {"KDAMCHECK", KDAM_CHECK},
            {"COURSESTAT", COURSE_STATS},
            {"STUDENTSTAT", STUDENT_STATS},
            {"ISREGISTERED", IS_REG},
            {"UNREGISTER", UN_REG},
            {"MYCOURSES", MY_COURSES},
    };

public:

    KeyboardHandler() = default;;
    ~KeyboardHandler() = default;;

    void run(ConnectionHandler &connectionHandler);

private:

    static std::string sendAdminReg(std::string &message);
    static std::string sendStudentReg(std::string &message);
    static std::string sendLogin(std::string &message);
    static std::string sendLogout(std::string &message);
    static std::string sendCourseReg(std::string &message);
    static std::string sendKdamCheck(std::string &message);
    static std::string sendCourseStat(std::string &message);
    static std::string sendStudentStat(std::string &message);
    static std::string sendIsRegistered(std::string &message);
    static std::string sendUnRegister(std::string &message);
    static std::string sendMyCourses(std::string &message);

    static std::string opcodeAndCourseNumMessage(std::string &message, Protocol opcode);
    static std::string opcodeUsernameAndPasswordMessage(std::string &message, Protocol opcode);

};