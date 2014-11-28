package ru.gm.munic.cguard;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import ru.gm.munic.domain.Position;
import ru.gm.munic.service.processing.LowService;

public class Handler implements IoHandler {
	private final String NAN = "NAN";
	private final String ID = "ID";
	private LowService lowService;
	
	public Handler(LowService lowService) {
		this.lowService = lowService;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String strMessage = (String)message;
		
		if(strMessage == "") {
			return;
		}
		
		int i = strMessage.indexOf(":", 0);
		if (i <= 0) {
			return;
		}
		
		String commandType = strMessage.substring(0, i-1);
		String commanData = strMessage.substring(i+1, strMessage.length()-1);
		String[] messArr = commanData.split(":");
		
		Date messageDate = null;
		if (messArr.length > 0 && (commandType.equals("NAV") || commandType.equals("NV")
				|| commandType.equals("BC") || commandType.equals("EV"))) {
			
			String firstElement = messArr[0].trim();
			if ( Pattern.matches(firstElement, "\\d{6}\\s\\d{6}") ) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(
						Integer.parseInt(firstElement.substring(0, 1)),
						Integer.parseInt(firstElement.substring(2, 3)),
						Integer.parseInt(firstElement.substring(4, 5)),
						Integer.parseInt(firstElement.substring(7, 8)),
						Integer.parseInt(firstElement.substring(9, 10)),
						Integer.parseInt(firstElement.substring(11, 12))
					);
			} else if (firstElement.equals("U") && (commandType.equals("BC") || commandType.equals("EV"))) {
				messageDate = new Date();
			} else {
				return;
			}
		}
		
		if (commandType.equals("ID") || commandType.equals("IDRO")) {
			session.setAttribute(ID, Long.parseLong(messArr[0].trim()));

		} else if (commandType.equals("NAV") || commandType.equals("NV")) {
			Long imei = (Long) session.getAttribute(ID);
			if (imei == null || messageDate == null || messArr.length < 4) {
				return;
			}
			
			Position position = new Position();
			position.setImei(imei);
			position.setDate(messageDate);
			position.setLat(Double.parseDouble(messArr[1].trim()));
			position.setLon(Double.parseDouble(messArr[2].trim()));
			position.setSpeed(Double.parseDouble(messArr[3].trim()));
			
			int shift = 0;
			if(commandType.equals("NV")) {
				shift = 1;
				if (!messArr[4].trim().equals(NAN)) {
					position.setAccuracy(Double.parseDouble(messArr[4].trim()));
				}
			}
			if (!messArr[shift + 4].trim().equals(NAN)) {
				position.setCourse(Double.parseDouble(messArr[shift + 4].trim()));
				if(position.getCourse() > 359 || position.getCourse() < 0) {
					position.setCourse(0D);
				}
			}
			if (!messArr[shift + 5].trim().equals(NAN)) {
				position.setAltitude(Double.parseDouble(messArr[shift + 5].trim()));
			}
			
			if (position.getLat() != 0 && position.getLon() != 0) {
				lowService.catchPosition(position);
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
