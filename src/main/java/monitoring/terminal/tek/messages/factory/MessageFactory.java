package monitoring.terminal.tek.messages.factory;

import java.nio.ByteBuffer;

import monitoring.terminal.tek.messages.domain.TekMessage;

public abstract class MessageFactory {

	abstract public TekMessage createMessage();

	abstract public void initializeMessageBody(TekMessage message, ByteBuffer buffer);
}
