package monitoring.terminal.tek;

import java.util.List;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TekIoHandler extends IoHandlerAdapter {

	// private static final Logger logger = LoggerFactory.getLogger(TekIoHandler.class);
	private TekTerminalSessionKeeper terminalSessionKeeper;
	private List<Handler> handlers;
	private HandlerStrategy strategy;

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		terminalSessionKeeper.setTerminalSession((Message) message, session);

		for (Handler handler : handlers) {
			handler.handle((Message) message, strategy);
		}

		session.write(message);
	}

	public void setTerminalSessionKeeper(TekTerminalSessionKeeper terminalSessionKeeper) {
		this.terminalSessionKeeper = terminalSessionKeeper;
	}

	public void setHandlers(List<Handler> handlers) {
		this.handlers = handlers;
	}

	public void setStrategy(HandlerStrategy strategy) {
		this.strategy = strategy;
	}

}
