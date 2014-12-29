package monitoring.handler;

import monitoring.domain.Message;

public interface IHandler {
	void handle(Message message);
}
