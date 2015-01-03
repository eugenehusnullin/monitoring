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
public class Handler extends IoHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);
	
	private Map<Long, IoSession> sessionMap = new HashMap<Long, IoSession>();

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		logger.info("tekobd2. messageReceived");
		
		Message m = (Message) message;
		synchronized (sessionMap) {
			sessionMap.put(m.getTerminalId(), session);
			sessionMap.notifyAll();
		}

		session.write(message);
	}
	
	public IoSession getSession(long terminalId) {
		synchronized (sessionMap) {
			IoSession session = sessionMap.get(terminalId);
			sessionMap.notifyAll();
			return session;
		}
	}
}
