package monitoring.terminal.munic.processing;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import monitoring.terminal.munic.controller.domain.MunicItemRawData;
import monitoring.terminal.munic.controller.domain.MunicRawData;
import monitoring.terminal.munic.processing.utils.ThreadFactorySecuenceNaming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MunicRawDataProcessing {
	private static final Logger logger = LoggerFactory.getLogger(MunicRawDataProcessing.class);

	@Value("#{mainSettings['municrawdataprocessing.threads.count']}")
	private Integer threadsCount = 1;

	class RecieverMunicRawDataRunnable implements Runnable {
		@Override
		public void run() {
			while (processing) {
				try {
					MunicRawData municRawData = null;
					synchronized (queue) {
						if (queue.isEmpty()) {
							try {
								queue.wait();
							} catch (InterruptedException e) {
								break;
							}
						}
						municRawData = queue.poll();
					}

					if (municRawData != null) {
						MunicRawDataRunnable messageRunnable = new MunicRawDataRunnable(municRawData);
						executor.execute(messageRunnable);
					}
				} catch (Exception e) {
					logger.error("MunicRawDataProcessing exception in RecieverMunicRawDataRunnable.", e);
				}
			}
		}
	}

	class MunicRawDataRunnable implements Runnable {
		private MunicRawData municRawData;

		public MunicRawDataRunnable(MunicRawData municRawData) {
			this.municRawData = municRawData;
		}

		@Override
		public void run() {
			try {
				List<MunicItemRawData> list = lowService.processMunicRawData(municRawData);
				municItemRawDataProcessing.add(list);
			} catch (Exception e) {
				logger.error("Error processing MunicRawData with value: " + municRawData.getRawData(), e);
			}
		}
	}

	private Queue<MunicRawData> queue;
	private Thread mainThread;
	private volatile boolean processing = true;
	private ExecutorService executor;
	@Autowired
	private LowService lowService;
	@Autowired
	private MunicItemRawDataProcessing municItemRawDataProcessing;

	public MunicRawDataProcessing() {
		queue = new ArrayDeque<MunicRawData>();
	}

	@PostConstruct
	public void startProcessing() {
		executor = Executors.newFixedThreadPool(threadsCount, new ThreadFactorySecuenceNaming(
				"MunicRawDataProcessing EXECUTOR #"));

		Runnable processRunnable = new RecieverMunicRawDataRunnable();
		mainThread = new Thread(processRunnable);
		mainThread.setName("MunicRawDataProcessing MAIN THREAD");
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

	public void add(MunicRawData municRawData) {
		synchronized (queue) {
			queue.add(municRawData);
			queue.notifyAll();
		}
	}
	
	public void addList(List<MunicRawData> list) {
		synchronized (queue) {
			for (MunicRawData municRawData : list) {
				queue.add(municRawData);
			}
			queue.notifyAll();
		}
	}
}
