package ChatClient;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by tess on 3/5/14.
 */
public class ChatClientHandler extends SimpleChannelHandler {
    protected final ChatClientListener chatClientListener;
    protected final static Logger logger = LoggerFactory.getLogger(ChatClientHandler.class);


    public ChatClientHandler(ChatClientListener chatClientListener)
            throws Exception{
        super();
        this.chatClientListener = chatClientListener;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        Throwable t = e.getCause();
        logger.debug("exception caught at :{},exception :{}", channel, t);
        chatClientListener.onException(channel, t);
        //super.exceptionCaught(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        logger.info("channel connected :{}", channel);
        chatClientListener.onConnected(channel);
        super.channelConnected(ctx, e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        logger.info("channel disconnected :{}", channel);
        chatClientListener.onDisconnected(channel);
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        ChannelPipeline pipeline = channel.getPipeline();
        JSONObject header = new JSONObject((String) e.getMessage());
        logger.info(header.toString());
        pipeline.removeFirst();
        pipeline.removeFirst();
        pipeline.removeFirst();
        pipeline.removeFirst();
        pipeline.addFirst("channel handler", new SimpleChannelHandler() {
            @Override
            public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                    throws Exception {
                super.channelConnected(ctx, e);
            }

            @Override
            public void channelDisconnected(ChannelHandlerContext ctx,
                                            ChannelStateEvent e) throws Exception {
                Channel channel = e.getChannel();
                logger.info("channel disconnected! :{}", channel);
                chatClientListener.onDisconnected(channel);
                super.channelDisconnected(ctx, e);
            }
        });
        pipeline.addLast("frame encoder", new LengthFieldPrepender(4, false));

        super.messageReceived(ctx, e);
    }
}
