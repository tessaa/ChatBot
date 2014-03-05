package ChatServer;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by tess on 3/5/14.
 */
public class ChatServerChannelPipelineFactory implements ChannelPipelineFactory {

    private final static Logger logger = LoggerFactory.getLogger(ChatServerChannelPipelineFactory.class);
    private final ChatServerListener chatServerListener;

    public ChatServerChannelPipelineFactory(
            ChatServerListener chatServerListener) {
        super();
        this.chatServerListener = chatServerListener;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addFirst("delimiter decoder", new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));

        pipeline.addLast("string decoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("string encoder", new StringEncoder(CharsetUtil.UTF_8));

        pipeline.addLast("stream server handler", new ChatServerHandler(this.chatServerListener));
        return pipeline;
    }
}
