package ru.gm.munic.service.sender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.gm.munic.domain.MunicItemRawData;
import ru.gm.munic.service.processing.LowService;
import ru.gm.munic.service.processing.utils.ItemRawDataJson;

@Service
public class SendDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(SendDispatcher.class);

	private Map<Long, Sender> connectionsMap = new HashMap<Long, Sender>();

	@Value("#{mainSettings['wialonb3.host']}")
	private String wialonb3Host;
	@Value("#{mainSettings['wialonb3.port']}")
	private Integer wialonb3Port;
	@Value("#{mainSettings['wialonb3.enabled']}")
	private Integer wialonb3Enabled = 1;
	@Autowired
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
