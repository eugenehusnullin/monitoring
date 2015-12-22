package monitoring.terminal.ch2;

import java.util.Date;
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

	public MessageHandler(List<Handler> handlers, HandlerStrategy strategy,
			Ch2TerminalsSessionsKeeper terminalsSessionsKeeper) {
		this.handlers = handlers;
		this.strategy = strategy;
		this.terminalsSessionsKeeper = terminalsSessionsKeeper;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message m = (Message) msg;
		terminalsSessionsKeeper.putTerminalSession(m.getTerminalId(), ctx);

		for (Handler handler : handlers) {
			handler.handle(m, strategy);
		}

		// demo
		Ch2DemoInfo demoInfo = terminalsSessionsKeeper.getDemoInfo(m.getTerminalId());
		if (demoInfo != null) {
			demoInfo.setDetachAlarmed(false);
			demoInfo.setVinChangeAlarmed(false);

			if (m instanceof Ch2Message) {
				Ch2Message mm = (Ch2Message) m;
				demoInfo.setLastDateCoord(new Date());
				demoInfo.setLat(mm.getLatitude());
				demoInfo.setLon(mm.getLongitude());
			} else if (m instanceof Ch2Response) {
				Ch2Response mr = (Ch2Response) m;
				if (mr.getResponseType().equals("88")) {
					if (!mr.getResponse().startsWith("88 00 00 00 00 00 00 00 00 00")) {
						String vin = mr.getResponse().substring(4);
						demoInfo.setVin(vin);
					}
				}
			}
		}
	}

}
