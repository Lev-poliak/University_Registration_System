package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message7 extends Message{

    public Message7(){
        super();
        setOpcode(7);
    }
    public Message executeCommand(BGRsProtocol pro) {
        if (pro.isLogIn() && pro.getDataBase().isAdmin(pro.getUserName())) {
            String a =pro.getDataBase().courseToString(this.getCourseNumOrOpcode());
            return new Message12(7,a);
        }
        return new Message13(7);
    }
}
