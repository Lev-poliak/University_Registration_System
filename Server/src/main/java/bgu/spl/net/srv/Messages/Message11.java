package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message11 extends Message{
    public Message11(){
        super();
        setOpcode(11);
    }
    public Message excuteCommand(BGRsProtocol pro) {
        //my courses student
        if (pro.isLogIn() && !pro.getDataBase().isAdmin(pro.getUserName())){
            String mycourses = pro.getDataBase().studentCourses(pro.getUserName());
            return new Message12(11,mycourses);
        }
        return new Message13(11);
    }

}
