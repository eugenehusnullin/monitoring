package monitoring.terminal.ch2;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public class Ch2TerminalsSessionsKeeper {
	private Map<Long, Ch2TerminalSession> sessionMap = new HashMap<Long, Ch2TerminalSession>();

	public Ch2TerminalSession getTerminalSession(long terminalId) {
		synchronized (sessionMap) {
			Ch2TerminalSession session = sessionMap.get(terminalId);
			sessionMap.notifyAll();
			return session;
		}
	}

	public void putTerminalSession(long terminalId, ChannelHandlerContext ctx) {

		synchronized (sessionMap) {
			Ch2TerminalSession terminalSession = sessionMap.get(terminalId);
			if (terminalSession == null) {
				terminalSession = new Ch2TerminalSession(ctx);
				sessionMap.put(terminalId, terminalSession);
			}
			terminalSession.setSession(ctx);
			sessionMap.notifyAll();
		}
	}

}
