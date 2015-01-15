package monitoring.handler;

import monitoring.domain.Message;
import monitoring.handler.accelerometer.Accelerometr;

public interface AccelerometrConverter {
	Accelerometr convert(Message message);
}
