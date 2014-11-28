package ru.gm.munic.cguard;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.TextLineDecoder;

public class CodecFactory implements ProtocolCodecFactory {
	private Encoder encoder;
	private TextLineDecoder decoder;
	
	public CodecFactory() {
		encoder = new Encoder();
		decoder = new TextLineDecoder(Charset.forName("UTF-8"), "\n");
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
