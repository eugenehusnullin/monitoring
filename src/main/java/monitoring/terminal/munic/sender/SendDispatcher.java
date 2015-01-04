package monitoring.terminal.munic.sender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import monitoring.terminal.munic.domain.MunicItemRawData;
import monitoring.terminal.munic.processing.LowService;
import monitoring.terminal.munic.processing.utils.ItemRawDataJson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(SendDispatcher.class);

	private Map<Long, Sender> connectionsMap = new HashMap<Long, Sender>();

	private String wialonb3Host;
	private Integer wialonb3Port;
	private LowService lowService;

	private Sender createSender() {
		return new Sender(wialonb3Host, wialonb3Port, lowService, logger);
	}

	public void send(List<MunicItemRawData> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		for (MunicItemRawData municItemRawData : list) {
			ItemRawDataJson itemRawDataJson = new ItemRawDataJson(municItemRawData.getItemRawData());
			Long imei = Long.parseLong(itemRawDataJson.getAsset());

			Sender sender = null;
			synchronized (connectionsMap) {
				sender = connectionsMap.get(imei);
				if (sender == null) {
					sender = createSender();
					connectionsMap.put(imei, sender);
				}
				connectionsMap.notifyAll();
			}
			municItemRawData.setItemRawDataJson(itemRawDataJson);
			sender.addItem(municItemRawData);
		}
	}
}
