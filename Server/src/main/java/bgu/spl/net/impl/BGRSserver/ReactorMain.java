package bgu.spl.net.impl.BGRSserver;
import bgu.spl.net.srv.AllMessageEncoderDecoder;
import bgu.spl.net.srv.BGRsProtocol;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {

        Server.reactor(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]), //port
                 () ->  new BGRsProtocol(), //protocol factory
                AllMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }
}
