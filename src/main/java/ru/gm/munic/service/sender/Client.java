package ru.gm.munic.service.sender;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import ru.gm.munic.domain.Message;
import ru.gm.munic.service.processing.MessageJson;

public class Client {

	private int port;
	private String host;
	private Message message;

	public Client(String host, int port, Message message) {
		this.host = host;
		this.port = port;
		this.message = message;
	}

	public void send() {
		String messageString = getMessageString(message);
		if (messageString == null) {
			return;
		}

		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(3000);

		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MunicCodecFactory()));
		connector.setHandler(new ClientSessionHandler(messageString));

		IoSession session;
		ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
		future.awaitUninterruptibly();
		session = future.getSession();

		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}

	public String getMessageString(Message message) {
		MessageJson messageJson = new MessageJson(message);
		if (messageJson.getEvent().equals("track")) {
			return messageJson.getString4Tcp();
		}
		return null;
	}

}
