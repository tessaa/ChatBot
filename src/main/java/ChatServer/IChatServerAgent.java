package ChatServer;

import java.net.SocketAddress;

/**
 * Created by tess on 3/5/14.
 */
public interface IChatServerAgent {

    public void start(SocketAddress streamAddress);

    public void stop();
}
