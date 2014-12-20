package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;

public interface IResponse {
	byte[] makeResponse();

	int getResponseBodySize();

	short getResponseMessageId();

	void initResponseBody(ByteBuffer bb);
}
