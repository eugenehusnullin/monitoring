package monitoring.handler;

import monitoring.domain.Message;
import monitoring.handler.gyro.Gyro;

public interface GyroConverter {
	Gyro convert(Message message);
}
