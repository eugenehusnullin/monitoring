package ru.gm.munic.service.processing;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.gm.munic.domain.MunicData;
import ru.gm.munic.domain.MunicItemRawData;
import ru.gm.munic.domain.TopMapzone;
import ru.gm.munic.service.processing.utils.ItemRawDataJson;
import ru.gm.munic.service.processing.utils.ThreadFactorySecuenceNaming;
import ru.gm.munic.service.sender.SendDispatcher;

@Service
public class MunicItemRawDataProcessing {
	private static final Logger logger = LoggerFactory.getLogger(MunicItemRawDataProcessing.class);

	@Value("#{mainSettings['messageprocessing.threads.count']}")
	private Integer threadsCount = 1;

	class RecieverMunicItemRawDataRunnable implements Runnable {
		@Override
		public void run() {
			while (processing) {
				try {
					List<MunicItemRawData> list = null;
					synchronized (queue) {
						if (queue.isEmpty()) {
							try {
								queue.wait();
							} catch (InterruptedException e) {
								break;
							}
						}
						list = queue.poll();
					}

					if (list != null) {
						MunicItemRawDataRunnable messageRunnable = new MunicItemRawDataRunnable(list);
						executor.execute(messageRunnable);
					}
				} catch (Exception e) {
					logger.error("MessageProcessing exception in RecieverMessageRunnable.", e);
				}
			}
		}
	}

	class MunicItemRawDataRunnable implements Runnable {
		private List<MunicItemRawData> list;

		public MunicItemRawDataRunnable(List<MunicItemRawData> list) {
			this.list = list;
		}

		@Override
		public void run() {
			sendDispatcher.send(list);
			
			for (MunicItemRawData item : list) {
				ItemRawDataJson itemRawDataJson = new ItemRawDataJson(item.getItemRawData());
				MunicData municData = itemRawDataJson.getMunicData();
				if (municData != null) {
					municData.setMunicItemRawData(item);
					
					if (municData.hasLocation()) {
						TopMapzone mapzone = geoService.checkMapzone(municData.getLat(), municData.getLon());
						if (mapzone != null) {
							municData.setMapzoneId(mapzone.getId());
							municData.setMapzoneType(mapzone.getType());
						}
					}
					
					lowService.saveMunicData(municData);
				}
			}
		}
	}

	private Queue<List<MunicItemRawData>> queue;
	private Thread mainThread;
	private volatile boolean processing = true;
	private ExecutorService executor;
	@Autowired
	private SendDispatcher sendDispatcher;
	@Autowired
	private LowService lowService;
	@Autowired
	private GeoService geoService;

	public MunicItemRawDataProcessing() {
		queue = new ArrayDeque<List<MunicItemRawData>>();
	}

	@PostConstruct
	public void startProcessing() {
		executor = Executors.newFixedThreadPool(threadsCount, new ThreadFactorySecuenceNaming(
				"MunicItemRawDataProcessing EXECUTOR #"));

		Runnable processRunnable = new RecieverMunicItemRawDataRunnable();
		mainThread = new Thread(processRunnable);
		mainThread.setName("MunicItemRawDataProcessing MAIN THREAD");
		mainThread.start();
	}

	@PreDestroy
	public void stopProcessing() {
		processing = false;
		mainThread.interrupt();
		executor.shutdown();
		try {
			mainThread.join();
		} catch (InterruptedException e) {
		}
	}

	public void add(List<MunicItemRawData> list) {
		synchronized (queue) {
			queue.add(list);
			queue.notifyAll();
		}
	}
}
