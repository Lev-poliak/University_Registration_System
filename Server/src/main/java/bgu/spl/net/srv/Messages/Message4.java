package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message4 extends Message{

    public Message4(){
        super();
        setOpcode(4);
    }
    public Message executeCommand(BGRsProtocol pro){
        if (pro.isLogIn()){
            pro.getDataBase().logOut(pro.getUserName());
            return  new Message12(4);
        }
        return  new Message13(4);
    }


    }
