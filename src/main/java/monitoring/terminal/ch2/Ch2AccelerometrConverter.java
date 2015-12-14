package monitoring.terminal.ch2;

import monitoring.domain.Message;
import monitoring.handler.AccelerometrConverter;
import monitoring.handler.accelerometer.Accelerometr;

public class Ch2AccelerometrConverter implements AccelerometrConverter {

	@Override
	public Accelerometr convert(Message message) {
		if (!(message instanceof Ch2Message)) {
			return null;
		}

		Ch2Message ch2Message = (Ch2Message) message;
		if (ch2Message.getAx() == null || ch2Message.getAy() == null || ch2Message.getAz() == null) {
			return null;
		}

		if (ch2Message.getAx() >= 180 || ch2Message.getAx() <= -210
				|| ch2Message.getAy() <= -180 || ch2Message.getAy() >= 180) {

			Accelerometr accelerometr = new Accelerometr();
			accelerometr.setTerminalId(ch2Message.getTerminalId());
			accelerometr.setDate(ch2Message.getDate());
			accelerometr.setAccX(ch2Message.getAx());
			accelerometr.setAccY(ch2Message.getAy());
			accelerometr.setAccZ(ch2Message.getAz());
			return accelerometr;
		} else {
			return null;
		}
	}

}
