package ChatServer;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by tess on 3/5/14.
 */
public class ChatServerAgent implements IChatServerAgent{

    protected final static Logger logger = LoggerFactory.getLogger(ChatServerAgent.class);
    protected final ChannelGroup channelGroup = new DefaultChannelGroup();
    protected final ServerBootstrap serverBootstrap;
    protected final ChatServerListener chatServerListener;

    public ChatServerAgent(){
        super();
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.setFactory(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.chatServerListener = new ChatServerListenerIMPL();
        this.serverBootstrap.setPipelineFactory(
                new ChatServerChannelPipelineFactory(
                        chatServerListener));
    }

    @Override
    public void start(SocketAddress streamAddress) {
        logger.info("Server started :{}", streamAddress);
        Channel channel = serverBootstrap.bind(streamAddress);
        channelGroup.add(channel);
    }

    @Override
    public void stop() {
        logger.info("server is stopping");
        channelGroup.close();
        serverBootstrap.releaseExternalResources();
    }


    private class ChatServerListenerIMPL implements ChatServerListener {

        @Override
        public void onClientConnectedIn(Channel channel) {
            //here we just start to stream when the first client connected in
            channelGroup.add(channel);
            logger.info("current connected clients :{}", channelGroup.size());
        }

        @Override
        public void onClientDisconnected(Channel channel) {
            channelGroup.remove(channel);
            int size = channelGroup.size();
            logger.info("current connected clients :{}", size);
        }

        @Override
        public void onException(Channel channel, Throwable t) {
            channelGroup.remove(channel);
            channel.close();
            int size = channelGroup.size();
            logger.info("current connected clients :{}", size);
        }

    }
}
