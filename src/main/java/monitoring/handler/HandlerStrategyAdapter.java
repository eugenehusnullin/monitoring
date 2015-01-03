package monitoring.handler;

import org.apache.mina.core.session.IoSession;

import monitoring.handler.position.PositionConverter;
import monitoring.handler.wialon.MessageFormatter;
/**
 * An adapter class for {@link HandlerStrategy}.  You can extend this
 * class and selectively override required methods only.  All
 * methods do nothing by default.
 *
 */
public class HandlerStrategyAdapter implements HandlerStrategy {

	@Override
	public PositionConverter getPositionConverter() {
		return null;
	}

	@Override
	public MessageFormatter getWialonMessageFormatter() {
		return null;
	}

	@Override
	public IoSession getTerminalIoSession(long terminalId) {
		return null;
	}

}
