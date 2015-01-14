package monitoring.terminal.cguardbad;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Broker {

	private static final Logger logger = LoggerFactory.getLogger(Broker.class);
	private IoAcceptor acceptor;
	@Value("#{mainSettings['cguard.host']}")
	private String host;
	@Value("#{mainSettings['cguard.port']}")
	private Integer port;
	
	@Autowired
	private Handler handler;
	

	@PostConstruct
	public void start() {
		try {
			logger.info("Starting cguarbad acceptor.");

			acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast("cguard", 
					new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), "\n", "\n")));
			acceptor.setDefaultLocalAddress(new InetSocketAddress(host, port));
			acceptor.setHandler(handler);
			acceptor.bind();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		logger.info("cguarbad acceptor started.");
	}

	@PreDestroy
	public void stop() {
		logger.info("Stoping cguarbad acceptor");
		acceptor.unbind();
		acceptor.dispose();
		logger.info("cguarbad acceptor stoped.");
	}
}
