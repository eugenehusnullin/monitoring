package monitoring.handler;

import monitoring.domain.Message;

public interface Handler {
	void handle(Message message, HandlerStrategy strategy);
}
