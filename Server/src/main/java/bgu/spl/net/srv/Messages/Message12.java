package bgu.spl.net.srv.Messages;

public class Message12  extends Message{


    public Message12(){
        super();
        setOpcode( 12);
    }
    public Message12(Integer opcode,String reply){
        super();
        setOpcode(12);
        setCourseNumOrOpcode(opcode);
        setReply(reply);
    }
    public Message12(Integer opcode){
        super();
        setOpcode(12);
        setCourseNumOrOpcode(opcode);
    }
}
