package monitoring.tek.messages;

import java.nio.ByteBuffer;

import monitoring.tek.messages.domain.TekMessageImpl;
import monitoring.tek.messages.factory.MessageFactory;
import monitoring.tek.messages.factory.MessageFactoryDetector;

import org.apache.mina.core.buffer.IoBuffer;

public class MessageParser {

	private MessageHelper helper;
	private MessageFactoryDetector messageFactoryDetector;

	public TekMessageImpl parse(IoBuffer in, int length) throws Exception {

		ByteBuffer buffer = helper.preprocess(in, length);
		short messageId = helper.detectMessageId(buffer);
		
		
		MessageFactory messageFactory = messageFactoryDetector.getMessageFactory(messageId);
		TekMessageImpl message = messageFactory.initializeMessage(buffer);

		return message;
	}
}