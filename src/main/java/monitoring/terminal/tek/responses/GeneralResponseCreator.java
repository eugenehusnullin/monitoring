package monitoring.terminal.tek.responses;

import java.nio.ByteBuffer;

import monitoring.terminal.tek.messages.domain.TekMessage;

class GeneralResponseCreator extends ResponseCreator {

	@Override
	int getResponseBodySize() {
		return 5;
	}

	@Override
	short getResponseMessageId() {
		return (short) 0xb003;
	}

	@Override
	void initResponseBody(TekMessage message, ByteBuffer buffer) {
		// message body
		buffer.putShort(message.getSerialNumber());
		buffer.putShort(message.getMessageId());
		buffer.put((byte) 0);
	}

}
