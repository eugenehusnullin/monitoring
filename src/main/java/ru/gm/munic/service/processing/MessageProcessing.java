package ru.gm.munic.service.processing;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.gm.munic.domain.Message;
import ru.gm.munic.service.sender.Client;

@Service
public class MessageProcessing {
	private static final Logger logger = LoggerFactory.getLogger(MessageProcessing.class);

	@Value("#{mainSettings['messageprocessing.threads.count']}")
	private Integer threadsCount = 1;

	@Value("#{mainSettings['wialonb3.enabled']}")
	private Integer wialonb3Enabled = 1;
	
	@Value("#{mainSettings['wialonb3.host']}")
	private String wialonb3Host;
	
	@Value("#{mainSettings['wialonb3.port']}")
	private Integer wialonb3Port;

	class RecieverMessageRunnable implements Runnable {
		@Override
		public void run() {
			while (processing) {
				try {
					Message message = null;
					synchronized (queue) {
						if (queue.isEmpty()) {
							try {
								queue.wait();
							} catch (InterruptedException e) {
								break;
							}
						}
						message = queue.poll();
					}

					if (message != null) {
						MesssageRunnable messageRunnable = new MesssageRunnable(message);
						executor.execute(messageRunnable);
					}
				} catch (Exception e) {
					logger.error("MessageProcessing exception in RecieverMessageRunnable.", e);
				}
			}
		}
	}

	class MesssageRunnable implements Runnable {
		private Message message;

		public MesssageRunnable(Message message) {
			this.message = message;
		}

		@Override
		public void run() {
			Client client = new Client(wialonb3Host, wialonb3Port, message);
			client.send();
		}
	}

	private Queue<Message> queue;
	private Thread mainThread;
	private volatile boolean processing = true;
	private ExecutorService executor;

	public MessageProcessing() {
		queue = new ArrayDeque<Message>();
	}

	@PostConstruct
	public void startProcessing() {
		executor = Executors.newFixedThreadPool(threadsCount, new ThreadFactorySecuenceNaming(
				"MessageProcessing EXECUTOR #"));

		Runnable processRunnable = new RecieverMessageRunnable();
		mainThread = new Thread(processRunnable);
		mainThread.setName("MessageProcessing MAIN THREAD");
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

	public void add(Message message) {
		if (wialonb3Enabled == 1) {
			synchronized (queue) {
				queue.add(message);
				queue.notifyAll();
			}
		}
	}
}
