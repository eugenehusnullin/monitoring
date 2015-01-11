package monitoring.terminal.cguard.wialon;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetranslateSender extends IoHandlerAdapter implements IoFutureListener<ConnectFuture> {
	private static final Logger logger = LoggerFactory.getLogger(RetranslateSender.class);

	private String wialonb3Host;
	private Integer wialonb3Port;

	private Queue<String> queue;
	private volatile boolean processed;

	private IoSession ioSession;
	private String currentItem;
	private NioSocketConnector connector;
	private int errorsCount;

	public RetranslateSender(String wialonb3Host, Integer wialonb3Port) {
		this.wialonb3Host = wialonb3Host;
		this.wialonb3Port = wialonb3Port;

		queue = new LinkedList<String>();
		processed = false;
		currentItem = null;
		ioSession = null;
		connector = null;

		errorsCount = 0;

		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(10000);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), "\n", "\n")));
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
			ioSession.write(currentItem);
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

	public void send(String message) {
		synchronized (queue) {
			queue.add(message);

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
		errorsCount = 0;
		sendNextItem();
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
}
