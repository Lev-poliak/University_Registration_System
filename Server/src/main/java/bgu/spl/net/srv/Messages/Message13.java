package bgu.spl.net.srv.Messages;

public class Message13 extends Message {

    public Message13(){
        super();
        setOpcode( 13);
    }

    public Message13(Integer errorOpcode){
        super();
        setOpcode( 13);
        setCourseNumOrOpcode(errorOpcode);
    }

}
