package monitoring.service.sender;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringDecoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringEncoder;

public class PrefixedStringCodecFactory implements ProtocolCodecFactory {

	private ProtocolEncoder encoder;
	private ProtocolDecoder decoder;

	public PrefixedStringCodecFactory(Charset charset, int prefixLength) {
		encoder = new PrefixedStringEncoder(charset, prefixLength);
		decoder = new PrefixedStringDecoder(charset, prefixLength);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

}
