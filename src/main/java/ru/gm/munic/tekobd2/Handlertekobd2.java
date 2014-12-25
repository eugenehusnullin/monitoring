package ru.gm.munic.tekobd2;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.gm.munic.tekobd2.messages.IHasResponse;
import ru.gm.munic.tekobd2.messages.Message;

@Service
public class Handlertekobd2 implements IoHandler {

	private static final Logger logger = LoggerFactory.getLogger(Handlertekobd2.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("tekobd2. sessionCreated");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("tekobd2. sessionOpened");

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error(cause.toString());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		logger.info("tekobd2. messageReceived");

		Message m = (Message) message;
		if (m instanceof IHasResponse) {
			session.write(m);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("tekobd2. messageSend");
	}

	@Override
	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
