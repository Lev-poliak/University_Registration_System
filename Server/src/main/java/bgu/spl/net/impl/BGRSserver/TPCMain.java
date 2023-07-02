package bgu.spl.net.impl.BGRSserver;

import bgu.spl.net.srv.AllMessageEncoderDecoder;
import bgu.spl.net.srv.BGRsProtocol;
import bgu.spl.net.srv.Server;

public class TPCMain {

    public static void main(String[] args) {
        Server.threadPerClient(
                Integer.parseInt(args[0]),
                () -> new BGRsProtocol(), //protocol factory
                AllMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }

}
