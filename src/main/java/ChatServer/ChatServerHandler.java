package ChatServer;

import org.jboss.netty.channel.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tess on 3/5/14.
 */
public class ChatServerHandler extends SimpleChannelHandler {

    private final ChatServerListener chatServerListener;
    private final static Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);

    public ChatServerHandler(ChatServerListener chatServerListener){
        this.chatServerListener = chatServerListener;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        Channel channel = ctx.getChannel();
        Throwable t = e.getCause();
        logger.debug("exception caught at :{},exception :{}", channel, t);
        chatServerListener.onException(channel, t);
        //super.exceptionCaught(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        this.chatServerListener.onClientConnectedIn(channel);
        super.channelConnected(ctx, e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        logger.info("channel disconnected :{}", channel);
        this.chatServerListener.onClientDisconnected(channel);
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        Channel channel = ctx.getChannel();
        ChannelPipeline pipeline = channel.getPipeline();

        pipeline.addFirst("channel handler", new SimpleChannelHandler() {
            @Override
            public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                    throws Exception {
                Channel channel = e.getChannel();
                logger.info("channel disconnected :{}", channel);
                chatServerListener.onClientDisconnected(channel);
                super.channelDisconnected(ctx, e);
            }
        });

        super.messageReceived(ctx, e);
    }
}
