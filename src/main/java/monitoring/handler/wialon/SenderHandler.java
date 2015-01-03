package monitoring.handler.wialon;

import java.util.HashMap;
import java.util.Map;

import monitoring.domain.Message;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

public class SenderHandler implements Handler {

	private Map<Long, Sender> connectionsMap = new HashMap<Long, Sender>();
	private String wialonb3Host;
	private Integer wialonb3Port;

	@Override
	public void handle(Message message, HandlerStrategy strategy) {

		MessageFormatter messageFormatter = strategy.getWialonMessageFormatter();
		if (messageFormatter != null) {
			WialonMessage wialonMessage = (WialonMessage) messageFormatter.formatt(message);

			if (wialonMessage != null) {
				send(wialonMessage);
			}
		}
	}

	private Sender createSender(long terminalId) {
		return new Sender(terminalId, wialonb3Host, wialonb3Port);
	}

	private void send(WialonMessage wialonMessage) {
		long terminalId = wialonMessage.getTerminalId();

		synchronized (connectionsMap) {
			Sender sender = connectionsMap.get(terminalId);
			if (sender == null) {
				sender = createSender(terminalId);
				connectionsMap.put(terminalId, sender);
			}
			sender.addItem(wialonMessage);
			connectionsMap.notifyAll();
		}
	}

}
