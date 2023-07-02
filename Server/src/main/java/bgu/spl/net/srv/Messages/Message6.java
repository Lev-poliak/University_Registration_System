package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message6 extends Message{

    public Message6(){
        super();
        setOpcode(6);
    }
    public Message executeCommand(BGRsProtocol pro){
        if (pro.getDataBase().isCourseExist(this.getCourseNumOrOpcode())) {
            String kdamCourses = pro.getDataBase().kdamCoursesString(this.getCourseNumOrOpcode());
            return new Message12(6, kdamCourses);
        }
        return new Message13(6);
    }
}



