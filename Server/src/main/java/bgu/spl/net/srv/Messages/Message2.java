package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

import java.util.Vector;

public class Message2 extends Message{

    public Message2(){
        super();
        setOpcode(2);
    }

    public Message executeCommand(BGRsProtocol pro){
        Vector<String> nameAndPassword = this.getUserNameAndPassword();
        boolean succeeded = pro.getDataBase().registerUser(nameAndPassword.get(0),nameAndPassword.get(1),false);
        if (succeeded){
            return new Message12(2);
        }
        else
        {
            return new Message13(2);
        }
    }


}
