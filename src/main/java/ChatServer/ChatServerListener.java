package ChatServer;

import org.jboss.netty.channel.Channel;

/**
 * Created by tess on 3/5/14.
 */
public interface ChatServerListener {

    public void onClientConnectedIn(Channel channel);

    public void onClientDisconnected(Channel channel);

    public void onException(Channel channel, Throwable t);
}
