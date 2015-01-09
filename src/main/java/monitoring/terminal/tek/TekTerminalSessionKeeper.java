package monitoring.terminal.tek;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import monitoring.domain.Message;

public class TekTerminalSessionKeeper {
	private Map<Long, TekTerminalSession> sessionMap = new HashMap<Long, TekTerminalSession>();

	public TekTerminalSession getTerminalSession(long terminalId) {
		synchronized (sessionMap) {
			TekTerminalSession session = sessionMap.get(terminalId);
			sessionMap.notifyAll();
			return session;
		}
	}

	public void setTerminalSession(Message message, IoSession session) {

		synchronized (sessionMap) {
			long terminalId = message.getTerminalId();
			TekTerminalSession terminalSession = sessionMap.get(terminalId);
			if (terminalSession == null) {
				terminalSession = new TekTerminalSession();
				sessionMap.put(terminalId, terminalSession);
			}
			terminalSession.setSession(session);
			sessionMap.notifyAll();
		}
	}
}
