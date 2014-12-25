package ru.gm.munic.tekobd2.messages.trip;

import java.nio.ByteBuffer;

public interface ISubMessage {
	void parse(ByteBuffer bb, short length);
}
