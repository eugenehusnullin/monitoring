package monitoring.terminal.ch2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

public class MessageHandler extends ChannelHandlerAdapter {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private List<Handler> handlers;
	private HandlerStrategy strategy;
	private Ch2TerminalsSessionsKeeper terminalsSessionsKeeper;
	
	private Long terminalId = null;

	public MessageHandler(List<Handler> handlers, HandlerStrategy strategy,
			Ch2TerminalsSessionsKeeper terminalsSessionsKeeper) {
		this.handlers = handlers;
		this.strategy = strategy;
		this.terminalsSessionsKeeper = terminalsSessionsKeeper;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message m = (Message) msg;
		if (terminalId == null) {
			terminalId = m.getTerminalId();
		}

		terminalsSessionsKeeper.messageArrived(m, ctx);

		for (Handler handler : handlers) {
			handler.handle(m, strategy);
		}
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (terminalId != null) {
			terminalsSessionsKeeper.deviceDisconnected(terminalId);
		}
		
		super.channelUnregistered(ctx);
	}
}
