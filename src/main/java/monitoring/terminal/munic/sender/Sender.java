package monitoring.terminal.munic.sender;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;

import monitoring.terminal.munic.domain.MunicItemRawData;
import monitoring.terminal.munic.processing.LowService;
import monitoring.terminal.munic.processing.utils.ItemRawDataJson;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;

public class Sender implements IoFutureListener<ConnectFuture>, IoHandler {
	private Logger logger;

	private String wialonb3Host;
	private Integer wialonb3Port;
	private LowService lowService;

	private Queue<MunicItemRawData> queue;
	private volatile boolean processed;

	private IoSession ioSession;
	private MunicItemRawData currentItem;

	private NioSocketConnector connector;

	private int errorsCount;

	private Boolean previousDioIgnition = null;

	public Sender(String wialonb3Host, Integer wialonb3Port, LowService lowService, Logger logger) {
		this.wialonb3Host = wialonb3Host;
		this.wialonb3Port = wialonb3Port;
		this.lowService = lowService;
		this.logger = logger;

		queue = new LinkedList<MunicItemRawData>();
		processed = false;
		currentItem = null;
		ioSession = null;
		connector = null;

		errorsCount = 0;

		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(10000);
		
		connector.setHandler(this);
	}

	private boolean connect() {
		if (ioSession == null || !ioSession.isConnected()) {
			ConnectFuture future = connector.connect(new InetSocketAddress(wialonb3Host, wialonb3Port));
			future.addListener(this);

			return false;
		} else {
			return true;
		}
	}

	private void sendCurrentItem() {
		if (currentItem != null && connect()) {
			ItemRawDataJson itemRawDataJson = currentItem.getItemRawDataJson();
			if (itemRawDataJson.isTrack()) {
				if (itemRawDataJson.getDioIgnition() == null) {
					itemRawDataJson.setDioIgnition(previousDioIgnition);
				} else {
					previousDioIgnition = itemRawDataJson.getDioIgnition();
				}
				ioSession.write(itemRawDataJson.getString4Wialon());
			} else {
				try {
					lowService.setWialonSended(currentItem);
				} finally {
					sendNextItem();
				}
			}
		}
	}

	private void sendNextItem() {
		synchronized (queue) {
			currentItem = queue.poll();
			if (currentItem == null) {
				processed = false;
			} else {
				processed = true;
				sendCurrentItem();
			}
			queue.notify();
		}
	}

	public void addItem(MunicItemRawData municItemRawData) {
		synchronized (queue) {
			queue.add(municItemRawData);

			if (!processed) {
				sendNextItem();
			}

			queue.notify();
		}
	}

	@Override
	public void operationComplete(ConnectFuture future) {
		if (!future.isConnected()) {
			errorsCount++;

			if (future.getException() != null) {
				logger.error(future.getException().toString());
			}

			try {
				Thread.sleep(Math.min(errorsCount * 1000, 60000));
			} catch (InterruptedException e) {
			}

			sendCurrentItem();
		}
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		errorsCount = 0;
		ioSession = session;
		sendCurrentItem();
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		try {
			lowService.setWialonSended(currentItem);
		} finally {
			errorsCount = 0;
			sendNextItem();
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error(cause.toString());

		session.close(true);
		session = null;

		errorsCount++;
		Thread.sleep(Math.min(errorsCount * 1000, 60000));
		sendCurrentItem();
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
	}
}
