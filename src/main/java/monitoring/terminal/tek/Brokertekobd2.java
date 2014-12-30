package monitoring.terminal.tek;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Brokertekobd2 {

	private static final Logger logger = LoggerFactory.getLogger(Brokertekobd2.class);
	private IoAcceptor acceptor;
	private String host;
	private Integer port;

	private Handlertekobd2 handler;

	public void start() {
		try {
			logger.info("Starting tekobd2 acceptor.");

			acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast("tekobd2", new ProtocolCodecFilter(new CodecFactory()));
			acceptor.setDefaultLocalAddress(new InetSocketAddress(host, port));
			acceptor.setHandler(handler);
			acceptor.bind();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		logger.info("tekobd2 acceptor started.");
	}

	public void stop() {
		logger.info("Stoping tekobd2 acceptor");
		acceptor.unbind();
		acceptor.dispose();
		logger.info("tekobd2 acceptor stoped.");
	}
}
