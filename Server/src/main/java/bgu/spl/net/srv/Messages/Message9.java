package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message9 extends Message{

    public Message9(){
        super();
        setOpcode(9);
    }
    public Message executeCommand(BGRsProtocol pro) {
        if(pro.isLogIn() && !pro.getDataBase().isAdmin(pro.getUserName())){
            if (pro.getDataBase().isRegisterd(pro.getUserName(),getCourseNumOrOpcode())){
                return new Message12(9,new String("REGISTERED"));
            }
            else {
                return new Message12(9,new String("NOT REGISTERED"));
            }
        }
        return new Message13(9);
    }

}
