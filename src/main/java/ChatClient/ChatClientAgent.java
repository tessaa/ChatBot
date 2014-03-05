package ChatClient;

import ChatServer.IChatServerAgent;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by tess on 3/5/14.
 */
public class ChatClientAgent implements IChatClientAgent {

    protected final static Logger logger = LoggerFactory.getLogger(ChatClientAgent.class);
    protected Channel clientChannel;
    protected final ClientBootstrap clientBootstrap;
    //I just move the stream encoder out of the channel pipeline for the performance
    protected volatile boolean isStreaming;
    protected ScheduledExecutorService timeWorker;
    protected ExecutorService encodeWorker;

    public ChatClientAgent(){
        super();
        this.clientBootstrap = new ClientBootstrap();
        this.clientBootstrap.setFactory(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.clientBootstrap.setPipelineFactory(
                new ChatClientChannelPipelineFactory(new ChatClientListenerIMPL()));
        this.timeWorker = new ScheduledThreadPoolExecutor(1);
        this.encodeWorker = Executors.newSingleThreadExecutor();

    }

    public void connect(SocketAddress graphicsServerAddress) {
        logger.info("going to connect to stream server :{}", graphicsServerAddress);
        clientBootstrap.connect(graphicsServerAddress);
    }

    @Override
    public void stop() {
        logger.info("Stopping");
        clientChannel.close();
        clientBootstrap.releaseExternalResources();
    }

    private class ChatClientListenerIMPL implements ChatClientListener {

        @Override
        public void onConnected(Channel channel) {
            clientChannel = channel;
            if (!isStreaming) {
                isStreaming = true;
            }
        }

        @Override
        public void onDisconnected(Channel channel) {
            logger.info("Disconnected from server");

            if (isStreaming) {
                isStreaming = false;
            }
        }

        @Override
        public void onException(Channel channel, Throwable t) {
            logger.info("Disconnected from server");
            if (isStreaming) {
                isStreaming = false;

            }

        }

    }



}
