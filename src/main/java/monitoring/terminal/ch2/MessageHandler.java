package monitoring.terminal.ch2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

public class MessageHandler extends ChannelHandlerAdapter {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private List<Handler> handlers;
	private HandlerStrategy strategy;

	public MessageHandler(List<Handler> handlers, HandlerStrategy strategy) {
		this.handlers = handlers;
		this.strategy = strategy;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Ch2Message m = (Ch2Message) msg;
		for (Handler handler : handlers) {
			handler.handle(m, strategy);
		}
		super.channelRead(ctx, msg);
	}

}
