package ChatServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by tess on 3/5/14.
 */
public class ChatServer {

    protected final static Logger logger = LoggerFactory.getLogger(ChatServer.class);


    public static void main(String args[]){
        try {
            ChatServerAgent serverAgent = new ChatServerAgent();
            serverAgent.start(new InetSocketAddress("localhost", 1338));
        } catch (Exception e) {
            logger.info("ERROR: " + e.getMessage());
        }
    }
}
