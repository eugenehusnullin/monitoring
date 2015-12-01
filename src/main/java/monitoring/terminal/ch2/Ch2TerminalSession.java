package monitoring.terminal.ch2;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import monitoring.domain.Message;
import monitoring.handler.TerminalSession;
import monitoring.handler.wialon.WialonMessage;

public class Ch2TerminalSession implements TerminalSession {
	private static final Logger logger = LoggerFactory.getLogger(Ch2TerminalSession.class);

	private ChannelHandlerContext ctx;

	public Ch2TerminalSession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void write(Message message) {
		WialonMessage wm = (WialonMessage) message;

		logger.info("Send command to ch2 block: " + wm.getStrMessage());
		ByteBuf b = ctx.alloc().buffer(wm.getStrMessage().length());
		try {
			b.writeBytes(wm.getStrMessage().getBytes("ASCII"));
			ctx.write(b);
			ctx.flush();
		} catch (UnsupportedEncodingException e) {
			logger.error("Send to terminal error.", e);
		}
	}

	public void setSession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
}