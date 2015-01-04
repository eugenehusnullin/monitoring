package monitoring.handler;

import monitoring.domain.Message;

public interface TerminalSession {
	void write(Message message);
}
