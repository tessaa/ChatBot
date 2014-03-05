package ChatClient;

import org.jboss.netty.channel.Channel;

/**
 * Created by tess on 3/5/14.
 */
public interface ChatClientListener {

    public void onConnected(Channel channel);

    public void onDisconnected(Channel channel);

    public void onException(Channel channel, Throwable t);
}
