package monitoring.terminal.tek;

import java.util.HashMap;
import java.util.Map;

import monitoring.domain.Message;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TekIoHandler extends IoHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(TekIoHandler.class);
	
	private Map<Long, TekTerminalSession> sessionMap = new HashMap<Long, TekTerminalSession>();

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		logger.info("tekobd2. messageReceived");
		
		Message m = (Message) message;
		synchronized (sessionMap) {
			long terminalId = m.getTerminalId();
			TekTerminalSession terminalSession = sessionMap.get(terminalId);
			if (terminalSession == null) {
				terminalSession = new TekTerminalSession();
				sessionMap.put(terminalId, terminalSession);
			}
			terminalSession.setSession(session);
			sessionMap.notifyAll();
		}

		session.write(message);
	}
	
	public TekTerminalSession getTerminalSession(long terminalId) {
		synchronized (sessionMap) {
			TekTerminalSession session = sessionMap.get(terminalId);
			sessionMap.notifyAll();
			return session;
		}
	}
}
