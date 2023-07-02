package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message10 extends Message{
    public Message10(){
        super();
        setOpcode(10);
    }
    public Message executeCommand(BGRsProtocol pro) {
        //unregister
        if (pro.isLogIn() && !pro.getDataBase().isAdmin(pro.getUserName())){
            if (pro.getDataBase().unRegister(pro.getUserName(),getCourseNumOrOpcode())){
                return new Message12(10);
            }
        }
        return new Message13(10);

    }

}
