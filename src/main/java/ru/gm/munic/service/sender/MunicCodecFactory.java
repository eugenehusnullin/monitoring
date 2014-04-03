package ru.gm.munic.service.sender;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringDecoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringEncoder;

public class MunicCodecFactory implements ProtocolCodecFactory {

	private ProtocolEncoder encoder;
	private ProtocolDecoder decoder;
	private Charset charset;

	public MunicCodecFactory() {
		charset = Charset.forName("ASCII");
		encoder = new PrefixedStringEncoder(charset, 2, 0xFFFF);
		decoder = new PrefixedStringDecoder(charset, 2);
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
