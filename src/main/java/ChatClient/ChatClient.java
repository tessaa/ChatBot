package ChatClient;

import java.net.InetSocketAddress;

/**
 * Created by tess on 3/5/14.
 */
public class ChatClient {

    public final ChatClientAgent clientAgent;
    public final InetSocketAddress address;

    public ChatClient(String host, int port){
        this.clientAgent = new ChatClientAgent();
        this.address = new InetSocketAddress(host, port);
        clientAgent.connect(address);
    }

    public static void main(String arg[]){
        ChatClient client = new ChatClient("localhost", 1338);
    }
}
