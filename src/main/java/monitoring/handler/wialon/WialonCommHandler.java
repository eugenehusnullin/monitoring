package monitoring.handler.wialon;

import java.util.HashMap;
import java.util.Map;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;
import monitoring.handler.MessageFormatter;

public class WialonCommHandler implements Handler {

	private Map<Long, WialonIoHandler> connectionsMap = new HashMap<Long, WialonIoHandler>();
	private String wialonb3Host;
	private Integer wialonb3Port;
	private WialonMessageFormatter wialonMessageFormatter;

	@Override
	public void handle(Message message, HandlerStrategy strategy) {

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
		return new WialonIoHandler(terminalId, strategy, wialonb3Host, wialonb3Port);
	}

}
