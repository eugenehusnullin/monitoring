package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;

public interface IMessage {
	void parseMessageBody(ByteBuffer bb);
}
