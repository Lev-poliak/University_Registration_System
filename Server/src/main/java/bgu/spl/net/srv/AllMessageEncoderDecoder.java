package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Messages.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;
import java.util.function.Supplier;

public class AllMessageEncoderDecoder implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int opcode = 0;
    private Message sendMessage ;
    private static  Vector<Supplier<Message>> messageCreator;
    private final HashSet<Short> OnlyCourseNumMessage = new HashSet<Short>(){{
        add((short) 5);
        add((short) 6);
        add((short) 7);
        add((short) 9);
        add((short) 10);
    }};
    public AllMessageEncoderDecoder(){
        messageCreator = new Vector<Supplier<Message>>();
        Supplier<Message> h= ()->new Message1();
        messageCreator.add(h);
        h= ()->new Message2();
        messageCreator.add(h);
        h= ()->new Message3();
        messageCreator.add(h);
        h= ()->new Message4();
        messageCreator.add(h);
        h= ()->new Message5();
        messageCreator.add(h);
        h= ()->new Message6();
        messageCreator.add(h);
        h= ()->new Message7();
        messageCreator.add(h);
        h= ()->new Message8();
        messageCreator.add(h);
        h= ()->new Message9();
        messageCreator.add(h);
        h= ()->new Message10();
        messageCreator.add(h);
        h= ()->new Message11();
        messageCreator.add(h);
        h= ()->new Message12();
        messageCreator.add(h);
        h= ()->new Message13();
        messageCreator.add(h);
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (opcode == 0 ) {
            pushByte(nextByte);
            if (len == 2) {
                opcode = bytesToShort(bytes);
                sendMessage = messageCreator.get(opcode).get();
            }
            return null;
        }

        sendMessage.setOpcode(opcode);
        if (1 <= opcode & opcode <= 3) {
            if (nextByte == '\0') {
                sendMessage.addUserNameAndPassword(popString());
                if (sendMessage.getUserNameAndPassword().size() == 2) {
                    return copyClearMessage();
                }
            }
        }

        if (OnlyCourseNumMessage.contains(opcode)){
            if(len==1){
              pushByte(nextByte);
              sendMessage.setCourseNumOrOpcode((int) bytesToShort(bytes))  ;
                return copyClearMessage();
            }
        }

        if (opcode == 8) {
            if (nextByte == '\0') {
                sendMessage.addUserNameAndPassword(popString());
                return copyClearMessage();
            }
        }

        if (opcode == 4 | opcode == 11){
            return copyClearMessage();
        }

        pushByte(nextByte);
        return null; //not a complete command yet
    }
        @Override
        public byte[] encode (Message message){
            byte[] messageOpcode = shortToBytes((short)(message.getOpcode()));
            byte[] replyToOpcode = shortToBytes((short)(message.getCourseNumOrOpcode()));
            if (message.getOpcode() == 12){
                byte[] stringByte = message.getReply().getBytes();
                byte[] temp = new byte[replyToOpcode.length + 4];
                ByteBuffer buf = ByteBuffer.wrap(temp);
                buf.put(messageOpcode);
                buf.put(replyToOpcode);
                buf.put(stringByte);
                return buf.array();
            }
            if (message.getOpcode() == 13){
                byte[] temp = new byte[ 4];
                ByteBuffer buf = ByteBuffer.wrap(temp);
                buf.put(messageOpcode);
                buf.put(replyToOpcode);
                return buf.array();
            }
            return new byte[0];
        }

        private void pushByte ( byte nextByte){
            if (len >= bytes.length) {
                bytes = Arrays.copyOf(bytes, len * 2);
            }

            bytes[len++] = nextByte;
        }

        private String popString () {
            //notice that we explicitly requesting that the string will be decoded from UTF-8
            //this is not actually required as it is the default encoding in java.
            String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
            len = 0;
            return result;
        }

        public byte[] shortToBytes ( short num)
        {
            byte[] bytesArr = new byte[2];
            bytesArr[0] = (byte) ((num >> 8) & 0xFF);
            bytesArr[1] = (byte) (num & 0xFF);
            return bytesArr;
        }

        public short bytesToShort ( byte[] byteArr)
        {
            short result = (short) ((byteArr[0] & 0xff) << 8);
            result += (short) (byteArr[1] & 0xff);
            len = 0;
            return result;
        }

        private void clearData(){
            len=0;
            opcode = 0;
            sendMessage = null;
        }

        private Message copyClearMessage() {
            Message tempMessage = sendMessage;
            clearData();
            return tempMessage;
        }

        public void messageFactory(Vector<Supplier<Message>> messageCreator){
        //TODO check if possible to optimize Vector of suppliers
            
            Supplier<Message> h= ()->new Message1();
            messageCreator.add(h);
            h= ()->new Message2();
            messageCreator.add(h);
            h= ()->new Message3();
            messageCreator.add(h);
            h= ()->new Message4();
            messageCreator.add(h);
            h= ()->new Message5();
            messageCreator.add(h);
            h= ()->new Message6();
            messageCreator.add(h);
            h= ()->new Message7();
            messageCreator.add(h);
            h= ()->new Message8();
            messageCreator.add(h);
            h= ()->new Message9();
            messageCreator.add(h);
            h= ()->new Message10();
            messageCreator.add(h);
            h= ()->new Message11();
            messageCreator.add(h);
            h= ()->new Message12();
            messageCreator.add(h);
            h= ()->new Message13();
            messageCreator.add(h);
        }
}
