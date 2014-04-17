package ru.gm.munic.service.sender;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;

import ru.gm.munic.domain.MunicItemRawData;
import ru.gm.munic.service.processing.LowService;
import ru.gm.munic.service.processing.utils.ItemRawDataJson;

public class Sender extends IoHandlerAdapter implements IoFutureListener<ConnectFuture> {
	private Logger logger;

	private String wialonb3Host;
	private Integer wialonb3Port;
	private LowService lowService;

	private Queue<MunicItemRawData> queue;
	private boolean processed;

	private IoSession ioSession;
	private MunicItemRawData currentItem;

	private NioSocketConnector connector;

	private int errorsCount;

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
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new SenderCodecFactory()));
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
			ItemRawDataJson itemRawDataJson = new ItemRawDataJson(currentItem.getItemRawData());
			if (itemRawDataJson.isTrack()) {
				try {
					Thread.sleep(1000);
					ioSession.write(itemRawDataJson.getString4Wialon());
				} catch (InterruptedException e) {
				}
			} else {
				lowService.setWialonSended(currentItem);
				sendNextItem();
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

	public void addItems(List<MunicItemRawData> list) {
		synchronized (queue) {
			for (MunicItemRawData item : list) {
				queue.add(item);
			}

			if (!processed) {
				sendNextItem();
			}

			queue.notify();
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
		lowService.setWialonSended(currentItem);

		errorsCount = 0;
		sendNextItem();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error(cause.toString());

		session.close(true);
		session = null;

		errorsCount++;
		Thread.sleep(errorsCount * 1000);
		sendCurrentItem();
	}

	@Override
	public void operationComplete(ConnectFuture future) {
		if (!future.isConnected()) {
			errorsCount++;

			if (future.getException() != null) {
				logger.error(future.getException().toString());
			}

			try {
				Thread.sleep(errorsCount * 1000);
			} catch (InterruptedException e) {
			}

			sendCurrentItem();
		}
	}
}
