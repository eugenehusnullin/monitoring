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
	private Ch2TerminalsSessionsKeeper terminalsSessionsKeeper;

	public MessageHandler(List<Handler> handlers, HandlerStrategy strategy, Ch2TerminalsSessionsKeeper terminalsSessionsKeeper) {
		this.handlers = handlers;
		this.strategy = strategy;
		this.terminalsSessionsKeeper = terminalsSessionsKeeper;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Ch2Message m = (Ch2Message) msg;
		
		terminalsSessionsKeeper.putTerminalSession(m.getTerminalId(), ctx);
		
		for (Handler handler : handlers) {
			handler.handle(m, strategy);
		}
		super.channelRead(ctx, msg);
	}

}
