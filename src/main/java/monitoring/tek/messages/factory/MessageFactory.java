package monitoring.tek.messages.factory;

import java.nio.ByteBuffer;

import monitoring.tek.messages.domain.MessageImpl;

public abstract class MessageFactory {

	abstract public MessageImpl createMessage();

	abstract public void initializeMessageBody(MessageImpl message, ByteBuffer buffer);
}
