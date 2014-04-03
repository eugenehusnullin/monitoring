package ru.gm.munic.service.sender;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientSessionHandler extends IoHandlerAdapter {
	private String message;

	public ClientSessionHandler(String message) {
		this.message = message;
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.write(message);
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		Thread.sleep(3000);
		session.close(true);
	}
	
}
