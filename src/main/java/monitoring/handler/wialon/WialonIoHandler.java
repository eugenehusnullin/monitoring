package monitoring.handler.wialon;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Queue;

import monitoring.domain.Message;
import monitoring.handler.HandlerStrategy;
import monitoring.handler.MessageFormatter;
import monitoring.handler.TerminalSession;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringDecoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringEncoder;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WialonIoHandler extends IoHandlerAdapter implements IoFutureListener<ConnectFuture> {
	private static final Logger logger = LoggerFactory.getLogger(WialonIoHandler.class);

	private long terminalId;
	private HandlerStrategy strategy;
	private String host;
	private Integer port;

	private Queue<WialonMessage> queue;
	private volatile boolean processed;

	private IoSession ioSession;
	private WialonMessage currentItem;
	private NioSocketConnector connector;
	private int errorsCount;

	public WialonIoHandler(long terminalId, HandlerStrategy strategy, String wialonb3Host, Integer wialonb3Port) {
		this.terminalId = terminalId;
		this.strategy = strategy;
		this.host = wialonb3Host;
		this.port = wialonb3Port;

		queue = new LinkedList<WialonMessage>();
		processed = false;
		currentItem = null;
		ioSession = null;
		connector = null;

		errorsCount = 0;

		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(10000);
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new PrefixedStringEncoder(Charset.forName("ASCII"), 2),
						new PrefixedStringDecoder(Charset.forName("ASCII"), 2)));
		connector.setHandler(this);
	}

	private boolean connect() {
		if (ioSession == null || !ioSession.isConnected()) {
			ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
			future.addListener(this);

			return false;
		} else {
			return true;
		}
	}

	private void sendCurrentItem() {
		if (currentItem != null && connect()) {
			ioSession.write(currentItem.getStrMessage());
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

	public void addItem(WialonMessage wialonMessage) {
		synchronized (queue) {
			queue.add(wialonMessage);

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

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		TerminalSession terminalSession = strategy.getTerminalSession(terminalId);
		if (terminalSession != null) {
			MessageFormatter messageFormatter = strategy.getWialonMessageFormatter();
			if (messageFormatter != null) {
				WialonMessage wialonMessage = new WialonMessage();
				wialonMessage.setStrMessage((String) message);
				wialonMessage.setTerminalId(terminalId);

				Message m = messageFormatter.toTerminalFormatt(wialonMessage);
				if (m != null) {
					terminalSession.write(m);
				}
			}
		}
	}

}
