package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message5 extends Message{

    public Message5(){
        super();
        setOpcode(5);
    }
    //register to course
    public Message executeCommand(BGRsProtocol pro){
        if (pro.isLogIn()){
            if (pro.getDataBase().registerCourse(pro.getUserName(),this.getCourseNumOrOpcode())){
                return new Message12(5);
            }
        }
        return new Message13(5);
    }
}
