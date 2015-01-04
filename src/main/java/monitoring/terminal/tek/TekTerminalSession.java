package monitoring.terminal.tek;

import monitoring.domain.Message;
import monitoring.handler.TerminalSession;

import org.apache.mina.core.session.IoSession;

public class TekTerminalSession implements TerminalSession {

	private IoSession session;

	@Override
	public void write(Message message) {
		session.write(message);
	}
	
	void setSession(IoSession session) {
		this.session = session;
	}

}
