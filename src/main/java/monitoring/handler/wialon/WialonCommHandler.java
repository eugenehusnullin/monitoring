package monitoring.handler.wialon;

import java.util.HashMap;
import java.util.Map;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;
import monitoring.handler.MessageFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WialonCommHandler implements Handler {
	private static final Logger logger = LoggerFactory.getLogger(WialonCommHandler.class);
	private Map<Long, WialonIoHandler> connectionsMap = new HashMap<Long, WialonIoHandler>();
	private String host;
	private Integer port;
	private WialonMessageFormatter wialonMessageFormatter = new WialonMessageFormatter();

	@Override
	public void handle(Message message, HandlerStrategy strategy) {
		try {

			MessageFormatter messageFormatter = strategy.getWialonMessageFormatter();
			if (messageFormatter != null) {
				DataPacket dataPacket = messageFormatter.fromTerminalFormatt(message);

				if (dataPacket != null) {
					WialonMessage wialonMessage = wialonMessageFormatter.formatt(dataPacket);
					if (wialonMessage != null) {
						send(wialonMessage, strategy);
					}
				}
			}

		} catch (Exception e) {
			logger.error("WialonCommHandler exception: ", e);
		}
	}

	private void send(WialonMessage wialonMessage, HandlerStrategy strategy) {
		long terminalId = wialonMessage.getTerminalId();

		synchronized (connectionsMap) {
			WialonIoHandler sender = connectionsMap.get(terminalId);
			if (sender == null) {
				sender = createSender(terminalId, strategy);
				connectionsMap.put(terminalId, sender);
			}
			sender.addItem(wialonMessage);
			connectionsMap.notifyAll();
		}
	}

	private WialonIoHandler createSender(long terminalId, HandlerStrategy strategy) {
		return new WialonIoHandler(terminalId, strategy, host, port);
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
