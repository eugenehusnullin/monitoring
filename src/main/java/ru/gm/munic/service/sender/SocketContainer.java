package ru.gm.munic.service.sender;

import java.util.UUID;

import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class SocketContainer {
	private NioSocketConnector connector;
	private UUID uuid;

	public SocketContainer(NioSocketConnector connector) {
		this.connector = connector;
		this.uuid = UUID.randomUUID();
	}

	@Override
	public boolean equals(Object obj) {
		return uuid.equals(((SocketContainer) obj).uuid);
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	public NioSocketConnector getConnector() {
		return connector;
	}
}
