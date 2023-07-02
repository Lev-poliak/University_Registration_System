package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

public class Message8 extends Message{

    public Message8(){
        super();
        setOpcode(8);
    }
    public Message executeCommand(BGRsProtocol pro) {
        //student stats
    if (pro.isLogIn() && pro.getDataBase().isAdmin(pro.getUserName())){
        String studentDataAsString = pro.getDataBase().StudentToString(getUserNameAndPassword().get(0));
        return new Message12(8,studentDataAsString);
    }
        return new Message13(8);
    }

    }
