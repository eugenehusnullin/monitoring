package ru.gm.munic.cguard.wialon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Dispatcher {
	//private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

	private Map<Long, Sender> connectionsMap = new ConcurrentHashMap<Long, Sender>();

	@Value("#{mainSettings['wialonb3.cguard.host']}")
	private String host;
	@Value("#{mainSettings['wialonb3.cguard.port']}")
	private Integer port;

	public void send(Long imei, String message) {
		if (message == null || message.isEmpty()) {
			return;
		}

		Sender sender = connectionsMap.get(imei);
		if (sender == null) {
			sender = new Sender(host, port);
			connectionsMap.put(imei, sender);
		}

		sender.addItem(message);
	}
}
