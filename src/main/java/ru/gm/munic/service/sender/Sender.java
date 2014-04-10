package ru.gm.munic.service.sender;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.gm.munic.domain.MunicItemRawData;
import ru.gm.munic.service.processing.LowService;

@Service
public class Sender implements CallbackSender {
	@Value("#{mainSettings['wialonb3.host']}")
	private String wialonb3Host;
	@Value("#{mainSettings['wialonb3.port']}")
	private Integer wialonb3Port;
	@Value("#{mainSettings['wialonb3.enabled']}")
	private Integer wialonb3Enabled = 1;

	@Autowired
	private LowService lowService;
	private Set<SocketContainer> socketContainers;

	public Sender() {
		socketContainers = new HashSet<SocketContainer>();
	}

	public void send(List<MunicItemRawData> list) {
		if (wialonb3Enabled == 1) {
			NioSocketConnector connector = new NioSocketConnector();
			SocketContainer container = new SocketContainer(connector);
			synchronized (socketContainers) {
				socketContainers.add(container);
				socketContainers.notifyAll();
			}
			connector.setConnectTimeoutMillis(3000);
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new SenderCodecFactory()));
			connector.setHandler(new ClientSessionHandler(list, container, this, lowService));

			connector.connect(new InetSocketAddress(wialonb3Host, wialonb3Port));

			// IoSession session;
			// ConnectFuture future = connector.connect(new
			// InetSocketAddress(wialonb3Host, wialonb3Port));
			// future.awaitUninterruptibly();
			// session = future.getSession();
			//
			// session.getCloseFuture().awaitUninterruptibly();
			// connector.dispose();
		}
	}

	@Override
	public void allsended(SocketContainer container) {
		synchronized (socketContainers) {
			socketContainers.remove(container);
			socketContainers.notifyAll();
		}
		container.getConnector().dispose();
	}
}
