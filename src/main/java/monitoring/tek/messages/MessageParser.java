package monitoring.tek.messages;

import java.nio.ByteBuffer;

import monitoring.tek.messages.domain.MessageImpl;
import monitoring.tek.messages.factory.MessageFactory;
import monitoring.tek.messages.factory.MessageFactoryDetector;

import org.apache.mina.core.buffer.IoBuffer;

public class MessageParser {

	private MessageFactoryDetector messageFactoryDetector;

	public MessageImpl parse(IoBuffer in, int length) throws Exception {

		ByteBuffer buffer = MessageUtilities.preprocess(in, length);
		short messageId = MessageUtilities.detectMessageId(buffer);

		MessageFactory messageFactory = messageFactoryDetector.getMessageFactory(messageId);

		MessageImpl message = messageFactory.createTekMessage();

		MessageUtilities.initializeHeader(message, buffer);

		messageFactory.initializeMessageBody(message, buffer);

		MessageUtilities.initializeFooter(message, buffer);

		return message;
	}
}