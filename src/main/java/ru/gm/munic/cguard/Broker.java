package ru.gm.munic.cguard;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.gm.munic.service.processing.LowService;

@Service
public class Broker {

	private static final Logger logger = LoggerFactory.getLogger(Broker.class);
	private IoAcceptor acceptor;
	@Value("#{mainSettings['cguard.host']}")
	private String host;
	@Value("#{mainSettings['cguard.port']}")
	private Integer port;
	@Autowired
	private LowService lowService;

	@PostConstruct
	public void start() {
		try {
			logger.info("Starting cguar acceptor.");

			acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast("cguard", new ProtocolCodecFilter(new CodecFactory()));
			acceptor.setDefaultLocalAddress(new InetSocketAddress(host, port));
			acceptor.setHandler(new Handler(lowService));
			acceptor.bind();
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	@PreDestroy
	public void stop() {
		logger.info("Stoping cguar acceptor");
		acceptor.unbind();
		acceptor.dispose();
	}
}
