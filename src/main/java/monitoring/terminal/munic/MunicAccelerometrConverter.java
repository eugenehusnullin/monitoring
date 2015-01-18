package monitoring.terminal.munic;

import monitoring.domain.Message;
import monitoring.handler.AccelerometrConverter;
import monitoring.handler.accelerometer.Accelerometr;

public class MunicAccelerometrConverter implements AccelerometrConverter {

	@Override
	public Accelerometr convert(Message message) {
		if (!(message instanceof MunicMessage)) {
			return null;
		}

		MunicMessage municMessage = (MunicMessage) message;
		if (!municMessage.isTrackEvent()) {
			return null;
		}

		if (!municMessage.hasBehave()) {
			return null;
		}

		Integer accX = municMessage.getAccX();
		Integer accY = municMessage.getAccY();
		Integer accZ = municMessage.getAccZ();
		
		if (accX == null || accY == null || accZ == null) {
			return null;
		}
		
		Accelerometr accelerometr = new Accelerometr();
		accelerometr.setTerminalId(municMessage.getTerminalId());
		accelerometr.setDate(municMessage.getRecordedAt());
		accelerometr.setAccX(accX);
		accelerometr.setAccY(accY);
		accelerometr.setAccZ(accZ);
		return accelerometr;
	}

}
