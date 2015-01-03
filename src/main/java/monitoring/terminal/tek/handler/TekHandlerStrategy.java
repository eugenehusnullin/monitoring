package monitoring.terminal.tek.handler;

import org.apache.mina.core.session.IoSession;

import monitoring.handler.HandlerStrategyAdapter;
import monitoring.handler.position.PositionConverter;
import monitoring.handler.wialon.MessageFormatter;
import monitoring.terminal.tek.Handler;

public class TekHandlerStrategy extends HandlerStrategyAdapter {

	private TekPositionConverter tekPositionConverter;
	private Handler handler;
	
	@Override
	public PositionConverter getPositionConverter() {
		return tekPositionConverter;
	}

	@Override
	public MessageFormatter getWialonMessageFormatter() {
		return null;
	}
	
	@Override
	public IoSession getTerminalIoSession(long terminalId) {
		return handler.getSession(terminalId);
	}

}
