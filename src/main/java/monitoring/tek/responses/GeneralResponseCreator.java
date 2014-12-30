package monitoring.tek.responses;

import java.nio.ByteBuffer;

import monitoring.tek.messages.domain.MessageImpl;

public class GeneralResponseCreator extends ResponseCreator {

	@Override
	int getResponseBodySize() {
		return 5;
	}

	@Override
	short getResponseMessageId() {
		return (short) 0xb003;
	}

	@Override
	void initResponseBody(MessageImpl message, ByteBuffer buffer) {
		// message body
		buffer.putShort(message.getSerialNumber());
		buffer.putShort(message.getMessageId());
		buffer.put((byte) 0);
	}

}
