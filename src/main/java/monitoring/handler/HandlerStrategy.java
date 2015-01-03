package monitoring.handler;

import org.apache.mina.core.session.IoSession;

import monitoring.handler.position.PositionConverter;
import monitoring.handler.wialon.MessageFormatter;

public interface HandlerStrategy {

	PositionConverter getPositionConverter();

	MessageFormatter getWialonMessageFormatter();
	
	IoSession getTerminalIoSession(long terminalId);
}
