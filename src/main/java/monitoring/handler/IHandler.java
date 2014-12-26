package monitoring.handler;

import monitoring.domain.IMessage;

public interface IHandler {
	void handle(IMessage message);
}
