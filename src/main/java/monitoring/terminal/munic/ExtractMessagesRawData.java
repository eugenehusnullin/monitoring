package monitoring.terminal.munic;

import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import monitoring.handler.Handler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ExtractMessagesRawData implements RawHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExtractMessagesRawData.class);

	private List<Handler> handlers;

	public void setHandlers(List<Handler> handlers) {
		this.handlers = handlers;
	}
	
	private void proccesWriter(StringWriter writer) {
		JSONTokener jsonTokener = new JSONTokener(writer.toString());
		JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			// TODO: generate Messages here
			//
			//
		}
	}

	class RecieverMunicRawDataRunnable implements Runnable {
		@Override
		public void run() {
			while (processing) {
				try {
					StringWriter writer = null;
					synchronized (queue) {
						if (queue.isEmpty()) {
							try {
								queue.wait();
							} catch (InterruptedException e) {
								break;
							}
						}
						writer = queue.poll();
						queue.notifyAll();
					}

					if (writer != null) {
						proccesWriter(writer);
					}
				} catch (Exception e) {
					logger.error("ExtractMessagesRawData exception in RecieverMunicRawDataRunnable.", e);
				}
			}
		}
	}

	private Queue<StringWriter> queue = new ArrayDeque<StringWriter>();
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

	@Override
	public void procces(StringWriter writer) {
		synchronized (queue) {
			queue.add(writer);
			queue.notifyAll();
		}
	}

}
