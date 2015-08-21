package monitoring.terminal.ch2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MessageHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		super.channelRead(ctx, msg);
	}

}
