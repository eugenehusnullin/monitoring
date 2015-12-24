package monitoring.handler;

/**
 * An adapter class for {@link HandlerStrategy}. You can extend this class and selectively override required methods
 * only. All methods do nothing by default.
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
	public TerminalSession getTerminalSession(long terminalId) {
		return null;
	}

	@Override
	public AccelerometrConverter getAccelerometrConverter() {
		return null;
	}

	@Override
	public GyroConverter getGyroConverter() {
		return null;
	}

}
