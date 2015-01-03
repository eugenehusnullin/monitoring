package monitoring.terminal.tek.messages;

import java.nio.ByteBuffer;

import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.terminal.tek.messages.factory.MessageFactory;
import monitoring.terminal.tek.messages.factory.MessageFactoryDetector;

import org.apache.mina.core.buffer.IoBuffer;

public class MessageParser {

	private MessageFactoryDetector messageFactoryDetector;

	public TekMessage parse(IoBuffer in, int length) throws Exception {

		ByteBuffer buffer = MessageUtilities.preprocessIncoming(in, length);
		short messageId = MessageUtilities.detectMessageId(buffer);
		MessageFactory messageFactory = messageFactoryDetector.getMessageFactory(messageId);
		TekMessage message = messageFactory.createMessage();
		MessageUtilities.initializeHeader(message, buffer);
		messageFactory.initializeMessageBody(message, buffer);
		MessageUtilities.initializeFooter(message, buffer);

		return message;
	}
}