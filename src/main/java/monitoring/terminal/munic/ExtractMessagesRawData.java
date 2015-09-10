package monitoring.terminal.munic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

class ExtractMessagesRawData implements RawHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExtractMessagesRawData.class);

	private List<Handler> handlers;
	private HandlerStrategy strategy;
	private MunicMessageFactory municMessageFactory;

	private Map<Long, State> stateMap = new HashMap<Long, State>();

	@Override
	public void procces(String message) {
		synchronized (queue) {
			queue.add(message);
			queue.notifyAll();
		}
	}

	private void proccesMessage(String message) {
		JSONTokener jsonTokener = new JSONTokener(message);
		JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
		List<MunicMessage> messagesList = new ArrayList<>();

		// init objects from json
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			MunicMessage municMessage = municMessageFactory.createMessage(jsonObject);
			messagesList.add(municMessage);
		}

		// sort by recorded time
		messagesList = messagesList.stream().sorted((e1, e2) -> e1.getRecordedAt().compareTo(e2.getRecordedAt()))
				.collect(Collectors.toList());

		// process messages
		for (MunicMessage municMessage : messagesList) {
			stateProof(municMessage);
			if (municMessage != null) {
				for (Handler handler : handlers) {
					handler.handle(municMessage, strategy);
				}
			}
		}
	}

	class RecieverMunicRawDataRunnable implements Runnable {
		@Override
		public void run() {
			while (processing) {
				try {
					String message = null;
					synchronized (queue) {
						if (queue.isEmpty()) {
							try {
								queue.wait();
							} catch (InterruptedException e) {
								break;
							}
						}
						message = queue.poll();
						queue.notifyAll();
					}

					if (message != null) {
						proccesMessage(message);
					}
				} catch (Exception e) {
					logger.error("ExtractMessagesRawData exception in RecieverMunicRawDataRunnable.", e);
				}
			}
		}
	}

	private Queue<String> queue = new ArrayDeque<String>();
	private Thread mainThread;
	private volatile boolean processing = true;

	public void startProcessing() {
		Runnable processRunnable = new RecieverMunicRawDataRunnable();
		mainThread = new Thread(processRunnable);
		mainThread.setName("ExtractMessagesRawData MAIN THREAD");
		mainThread.start();
	}

	public void stopProcessing() {
		processing = false;
		mainThread.interrupt();
		try {
			mainThread.join();
		} catch (InterruptedException e) {
		}
	}

	public void setHandlers(List<Handler> handlers) {
		this.handlers = handlers;
	}

	public void setStrategy(HandlerStrategy strategy) {
		this.strategy = strategy;
	}

	public void setMunicMessageFactory(MunicMessageFactory municMessageFactory) {
		this.municMessageFactory = municMessageFactory;
	}

	private void stateProof(MunicMessage municMessage) {
		if (municMessage.isTrackEvent()) {
			synchronized (stateMap) {
				State state = stateMap.get(municMessage.getTerminalId());
				if (state == null) {
					state = new State();
					stateMap.put(municMessage.getTerminalId(), state);
				}

				// ignition
				if (municMessage.getDioIgnition() != null) {
					state.setIgnition(municMessage.getDioIgnition());
				} else {
					Boolean ignitionState = state.getIgnition();
					if (ignitionState != null) {
						municMessage.setDioIgnition(ignitionState);
					}
				}

				// gprmc valid
				if (municMessage.getGprmcValid() != null) {
					state.setGpsValid(municMessage.getGprmcValid());
				} else {
					Boolean gprmcValid = state.getGpsValid();
					if (gprmcValid != null) {
						municMessage.setGprmcValid(gprmcValid);
					}
				}

				// notify all threads
				stateMap.notifyAll();
			}
		}
	}

}
