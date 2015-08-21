package monitoring.terminal.ch2;

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

public class Ch2Handler {
	private static final Logger logger = LoggerFactory.getLogger(Ch2Handler.class);

	private String host;
	private int port;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap serverBootstrap;
	private ChannelFuture channelFuture;

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
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
						ch.pipeline().addLast(new FlowSeparator(), new MessageDecoder(), new MessageHandler());
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
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();

		logger.info("Ch2 socket stoped.");
	}
}
