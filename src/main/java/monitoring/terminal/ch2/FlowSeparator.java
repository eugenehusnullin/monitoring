package monitoring.terminal.ch2;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class FlowSeparator extends ReplayingDecoder<Void> {
	private static final Logger logger = LoggerFactory.getLogger(FlowSeparator.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (true) {
			int startIndex = in.readerIndex();
			byte b1 = in.readByte();
			byte b2 = in.readByte();

			if (b1 == 0x23 && b2 != 0x23) {
				while (in.readByte() != 0x23) {
				}

				int endIndex = in.readerIndex();
				in.readerIndex(startIndex + 1);
				ByteBuf buf = in.readBytes(endIndex - startIndex - 2);
				in.readByte();
				String body = buf.toString(Charset.forName("ASCII"));
				out.add(body);

				if (logger.isDebugEnabled()) {
					logger.debug(body);
				}

				break;
			} else {
				if (b2 == 0x23) {
					in.readerIndex(in.readerIndex() - 1);
				}
			}
		}
	}

	// @Override
	// public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
	// throws Exception {
	// logger.debug("disconnect");
	// super.disconnect(ctx, promise);
	// }

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelActive");
		super.channelActive(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelRegistered");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelUnregistered");
		super.channelUnregistered(ctx);
	}
}
