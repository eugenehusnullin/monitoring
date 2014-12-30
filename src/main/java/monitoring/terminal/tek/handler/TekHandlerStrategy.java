package monitoring.terminal.tek.handler;

import monitoring.handler.HandlerStrategy;
import monitoring.handler.position.PositionConverter;

public class TekHandlerStrategy implements HandlerStrategy {

	private TekPositionConverter tekPositionConverter;
	
	@Override
	public PositionConverter getPositionConverter() {
		return tekPositionConverter;
	}

}
