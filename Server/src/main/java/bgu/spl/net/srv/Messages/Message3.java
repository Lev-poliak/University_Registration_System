package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

import java.util.Vector;

public class Message3 extends Message{

    public Message3(){
        super();
        setOpcode(3);
    }
    public Message executeCommand(BGRsProtocol pro){

        if (!pro.isLogIn()){
            Vector<String> nameAndPassword = this.getUserNameAndPassword();
            boolean logged = pro.getDataBase().loginAccount(nameAndPassword.get(0),nameAndPassword.get(1));
            if (logged){
                pro.setLogIn(true);
                pro.setUserName(nameAndPassword.get(0));
                return new Message12(3);
            }
        }
        return new Message13(3);
    }

}
