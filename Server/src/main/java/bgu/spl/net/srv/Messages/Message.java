package bgu.spl.net.srv.Messages;

import bgu.spl.net.srv.BGRsProtocol;

import java.util.Vector;

public abstract class Message {
    private int opcode = 0;
    private final Vector<String> userNameAndPassword;
    private int courseNumOrOpcode;
    private String reply;

    public  Message (){
        userNameAndPassword = new Vector<>();
    }
    public  Message executeCommand(BGRsProtocol pro){
        return null;
    }

    public void addUserNameAndPassword(String data){
        userNameAndPassword.add(data);
    }

    public int getCourseNumOrOpcode() {
        return courseNumOrOpcode;
    }

    public Vector<String> getUserNameAndPassword() {
        return userNameAndPassword;
    }

    public void setCourseNumOrOpcode(int courseNumOrOpcode) {
        this.courseNumOrOpcode = courseNumOrOpcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getOpcode() {
        return opcode;
    }

    public void addWord(String userOrPassword) {
        this.userNameAndPassword.add(userOrPassword);
    }


}
