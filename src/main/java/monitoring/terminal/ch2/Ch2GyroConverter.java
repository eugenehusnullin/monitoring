package monitoring.terminal.ch2;

import monitoring.domain.Message;
import monitoring.handler.GyroConverter;
import monitoring.handler.gyro.Gyro;

public class Ch2GyroConverter implements GyroConverter {

	@Override
	public Gyro convert(Message message) {
		if (!(message instanceof Ch2Message)) {
			return null;
		}

		Ch2Message ch2Message = (Ch2Message) message;
		if (ch2Message.getGx() == null || ch2Message.getGy() == null || ch2Message.getGz() == null) {
			return null;
		}

		if (ch2Message.getGx() < 200 && ch2Message.getGy() < 200 && ch2Message.getGz() < 200) {
			return null;
		}

		Gyro gyro = new Gyro();
		gyro.setTerminalId(ch2Message.getTerminalId());
		gyro.setDate(ch2Message.getDate());
		gyro.setGx(ch2Message.getGx());
		gyro.setGy(ch2Message.getGy());
		gyro.setGz(ch2Message.getGz());
		return gyro;
	}

}
