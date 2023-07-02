package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

import java.util.Vector;

public class Message1  extends Message{


    public Message1(){
        super();
        setOpcode(1);
    }
    @Override
    public Message executeCommand(BGRsProtocol pro){
        Vector<String> nameAndPassword = this.getUserNameAndPassword();
        boolean succeeded = pro.getDataBase().registerUser(nameAndPassword.get(0),nameAndPassword.get(1),true);
        if (succeeded){
            return new Message12(1);
        }
        else
            {
            return new Message13(1);
        }
    }


}
