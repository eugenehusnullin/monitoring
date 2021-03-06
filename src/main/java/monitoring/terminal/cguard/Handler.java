package monitoring.terminal.cguard;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import monitoring.domain.AlarmDeviceDetached;
import monitoring.handler.position.Position;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	private final String NAN = "NAN";
	private final String ID = "ID";

	private Map<Long, Boolean> terminalDetachMap = new HashMap<Long, Boolean>();

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("cguard. sessionCreated");

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("cguard. sessionOpened");

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error(cause.toString());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		try {
			if (message == null) {
				return;
			}

			String strMessage = (String) message;
			logger.info("cguard. messageReceived. " + strMessage);

			if (strMessage.equals("")) {
				logger.info("message empty");
				return;
			}

			int index = strMessage.indexOf(":", 0);
			if (index <= 0) {
				logger.info("index of : is 0");
				return;
			}

			String[] messArr = strMessage.split(":");
			String commandType = messArr[0];

			Date messageDate = null;
			if (messArr.length > 1
					&& (commandType.equals("NAV") || commandType.equals("NV") || commandType.equals("BC") || commandType
							.equals("EV"))) {

				String firstElement = messArr[1].trim();
				if (Pattern.matches("\\d{6}\\s\\d{6}", firstElement)) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(Integer.parseInt(firstElement.substring(0, 2)),
							Integer.parseInt(firstElement.substring(2, 4))-1,
							Integer.parseInt(firstElement.substring(4, 6)),
							Integer.parseInt(firstElement.substring(7, 9)),
							Integer.parseInt(firstElement.substring(9, 11)),
							Integer.parseInt(firstElement.substring(11)));
					messageDate = calendar.getTime();
				} else if (firstElement.equals("U") && (commandType.equals("BC") || commandType.equals("EV"))) {
					messageDate = new Date();
				} else {
					logger.info("date is bad");
					return;
				}
			}

			// ������ ����������� ���������� � ���������������
			// ������ ����������� ���������� � ���������������� ������
			// "������ ������"
			Long imei;
			if (commandType.equals("ID") || commandType.equals("IDRO")) {
				logger.info("set ID");
				imei = Long.parseLong(messArr[1].trim());
				session.setAttribute(ID, imei);

			} else {
				logger.info("get ID");
				imei = (Long) session.getAttribute(ID);
			}

			if (imei == null || messageDate == null) {
				logger.info("imei is null");
				return;
			}

			logger.info("imei = " + imei);
			// �������� ������������� ����������.
			if (commandType.equals("NAV") || commandType.equals("NV")) {
				// NV:150111 150445:55.750710:37.583568:5.26:NAN:32.14
				Position position = new Position();
				position.setTerminalId(imei);
				position.setDate(messageDate);
				position.setLat(Double.parseDouble(messArr[2].trim()));
				position.setLon(Double.parseDouble(messArr[3].trim()));
				position.setSpeed(Double.parseDouble(messArr[4].trim()));

				int shift = 0;
				if (commandType.equals("NV") && messArr.length > 5) {
					shift = 1;
					if (!messArr[5].trim().equals(NAN)) {
						position.setAccuracy(Double.parseDouble(messArr[5].trim()));
					}
				}
				if (commandType.equals("NV") && messArr.length > 6 || commandType.equals("NAV")) {
					if (!messArr[shift + 5].trim().equals(NAN)) {
						position.setCourse(Double.parseDouble(messArr[shift + 5].trim()));
						if (position.getCourse() > 359 || position.getCourse() < 0) {
							position.setCourse(0D);
						}
					}
				}
				if (commandType.equals("NV") && messArr.length > 7 || commandType.equals("NAV")) {
					if (!messArr[shift + 6].trim().equals(NAN)) {
						position.setAltitude(Double.parseDouble(messArr[shift + 6].trim()));
					}
				}

				if (position.getLat() != 0 && position.getLon() != 0) {
					// TODO: persist position to db
				}

			} else if (commandType.equals("BC")) { // �������� �������� �
													// ����������
				logger.info("BC entered");
				for (int i = 2; i + 1 < messArr.length; i += 2) {
					logger.info("BC for i=" + i);

					if (messArr[i].equals("AIN1")) {
						logger.info("AIN1 found");

						Double ain1 = Double.parseDouble(messArr[i + 1]);
						logger.info("AIN1 = " + ain1);
						if (ain1.compareTo(8.0d) < 0) {

							logger.info("AIN1 less than 8.0d");

							synchronized (terminalDetachMap) {
								Boolean stateBoolean = terminalDetachMap.get(imei);
								if (stateBoolean == null) {
									logger.info("put to map");
									terminalDetachMap.put(imei, true);

									logger.info("save to table");
									AlarmDeviceDetached terminalDetach = new AlarmDeviceDetached();
									terminalDetach.setOffDate(messageDate);
									terminalDetach.setTerminalId(imei);

									//lowService.saveTerminalDetach(terminalDetach);
								}
								terminalDetachMap.notifyAll();
							}
						} else {
							logger.info("AIN1 bigger than 8.0d");
							synchronized (terminalDetachMap) {
								terminalDetachMap.remove(imei);
								terminalDetachMap.notifyAll();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
