package monitoring.tek.messages.domain;

import java.nio.ByteBuffer;

public interface HasResponse {
	byte[] makeResponse();

	int getResponseBodySize();

	short getResponseMessageId();

	void initResponseBody(ByteBuffer bb);
}
