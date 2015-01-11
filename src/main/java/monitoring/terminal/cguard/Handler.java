package monitoring.terminal.cguard;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import monitoring.handler.position.Position;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Handler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	private final String NAN = "NAN";
	private final String ID = "ID";

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("cguard. sessionCreated");

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("cguard. sessionOpened");

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error(cause.toString());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message == null) {
			return;
		}

		String strMessage = (String) message;
		logger.info("cguard. messageReceived. " + strMessage);

		if (strMessage == "") {
			return;
		}

		int i = strMessage.indexOf(":", 0);
		if (i <= 0) {
			return;
		}

		String[] messArr = strMessage.split(":");
		String commandType = messArr[0];

		Date messageDate = null;
		if (messArr.length > 1
				&& (commandType.equals("NAV") || commandType.equals("NV") || commandType.equals("BC") || commandType
						.equals("EV"))) {

			String firstElement = messArr[1].trim();
			if (Pattern.matches(firstElement, "\\d{6}\\s\\d{6}")) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Integer.parseInt(firstElement.substring(0, 2)),
						Integer.parseInt(firstElement.substring(2, 4)), Integer.parseInt(firstElement.substring(4, 6)),
						Integer.parseInt(firstElement.substring(6, 8)),
						Integer.parseInt(firstElement.substring(8, 10)), Integer.parseInt(firstElement.substring(10)));
			} else if (firstElement.equals("U") && (commandType.equals("BC") || commandType.equals("EV"))) {
				messageDate = new Date();
			} else {
				return;
			}
		}

		// ������ ����������� ���������� � ���������������
		// ������ ����������� ���������� � ���������������� ������
		// "������ ������"
		Long imei;
		if (commandType.equals("ID") || commandType.equals("IDRO")) {
			imei = Long.parseLong(messArr[1].trim());
			session.setAttribute(ID, imei);

			// �������� ������������� ����������.
		} else if (commandType.equals("NAV") || commandType.equals("NV")) {
			imei = (Long) session.getAttribute(ID);
			if (imei == null || messageDate == null || messArr.length < 5) {
				return;
			}

			Position position = new Position();
			position.setTerminalId(imei);
			position.setDate(messageDate);
			position.setLat(Double.parseDouble(messArr[2].trim()));
			position.setLon(Double.parseDouble(messArr[3].trim()));
			position.setSpeed(Double.parseDouble(messArr[4].trim()));

			int shift = 0;
			if (commandType.equals("NV")) {
				shift = 1;
				if (!messArr[5].trim().equals(NAN)) {
					position.setAccuracy(Double.parseDouble(messArr[5].trim()));
				}
			}
			if (!messArr[shift + 5].trim().equals(NAN)) {
				position.setCourse(Double.parseDouble(messArr[shift + 5].trim()));
				if (position.getCourse() > 359 || position.getCourse() < 0) {
					position.setCourse(0D);
				}
			}
			if (!messArr[shift + 6].trim().equals(NAN)) {
				position.setAltitude(Double.parseDouble(messArr[shift + 6].trim()));
			}

			if (position.getLat() != 0 && position.getLon() != 0) {
				// TODO: persist position to db
			}

		} else if (commandType.equals("BC")) { // �������� �������� � ����������

		}
	}

}
