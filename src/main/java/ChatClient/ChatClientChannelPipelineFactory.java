package ChatClient;

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

/**
 * Created by tess on 3/5/14.
 */
public class ChatClientChannelPipelineFactory implements ChannelPipelineFactory {

    protected final static Logger logger = LoggerFactory.getLogger(ChatClientChannelPipelineFactory.class);
    protected final ChatClientListener chatClientListener;

    public ChatClientChannelPipelineFactory(
            ChatClientListener chatClientListener) {
        super();
        this.chatClientListener = chatClientListener;
    }


    @Override
    public ChannelPipeline getPipeline() throws Exception {
        logger.info("Creating pipeline");
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addFirst("delimiter decoder", new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));

        pipeline.addLast("string decoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("string encoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast("chat client handler", new ChatClientHandler(chatClientListener));
        return pipeline;
    }
}
