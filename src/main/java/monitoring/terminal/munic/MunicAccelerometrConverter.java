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

		Accelerometr accelerometr = new Accelerometr();
		accelerometr.setTerminalId(municMessage.getTerminalId());
		accelerometr.setDate(municMessage.getRecordedAt());
		accelerometr.setAccX(municMessage.getAccX());
		accelerometr.setAccY(municMessage.getAccY());
		accelerometr.setAccZ(municMessage.getAccZ());
		return accelerometr;
	}

}
