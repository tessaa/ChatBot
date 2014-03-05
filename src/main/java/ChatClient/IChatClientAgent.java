package ChatClient;

import java.net.SocketAddress;

/**
 * Created by tess on 3/5/14.
 */
public interface IChatClientAgent {
    public void connect(SocketAddress storageServerAddress);

    public void stop();
}
