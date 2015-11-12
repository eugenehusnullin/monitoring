package monitoring.terminal.ch2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import monitoring.handler.Handler;
import monitoring.handler.HandlerStrategy;

public class Ch2Handler {
	private static final Logger logger = LoggerFactory.getLogger(Ch2Handler.class);

	private String host;
	private int port;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap serverBootstrap;
	private ChannelFuture channelFuture;

	private List<Handler> handlers;
	private HandlerStrategy strategy;
	private Ch2TerminalsSessionsKeeper terminalsSessionsKeeper;

	public void setHandlers(List<Handler> handlers) {
		this.handlers = handlers;
	}

	public void setStrategy(HandlerStrategy strategy) {
		this.strategy = strategy;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public void setTerminalsSessionsKeeper(Ch2TerminalsSessionsKeeper terminalsSessionsKeeper) {
		this.terminalsSessionsKeeper = terminalsSessionsKeeper;
	}

	public void run() throws InterruptedException {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();

		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new FlowSeparator(), new MessageDecoder(),
								new MessageHandler(handlers, strategy, terminalsSessionsKeeper));
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

		channelFuture = serverBootstrap.bind(host, port).sync();
		logger.info("Ch2 socket started, on host - " + host + ", port - " + port + ".");
	}

	public void stop() throws InterruptedException {
		channelFuture.channel().close();
		channelFuture.channel().closeFuture().sync();
		@SuppressWarnings("rawtypes")
		Future fb = bossGroup.shutdownGracefully();
		@SuppressWarnings("rawtypes")
		Future fw = workerGroup.shutdownGracefully();
		
		try {
	        fb.await();
	        fw.await();
	    } catch (InterruptedException ignore) {}

		logger.info("Ch2 socket stoped.");
	}
}
