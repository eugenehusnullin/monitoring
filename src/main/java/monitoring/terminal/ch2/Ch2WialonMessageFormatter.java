package monitoring.terminal.ch2;

import monitoring.domain.Message;
import monitoring.handler.MessageFormatter;
import monitoring.handler.wialon.DataPacket;
import monitoring.terminal.munic.utils.PositionHelper;

public class Ch2WialonMessageFormatter implements MessageFormatter {

	@Override
	public DataPacket fromTerminalFormatt(Message message) {
		if (!(message instanceof Ch2Message)) {
			return null;
		}

		Ch2Message ch2Message = (Ch2Message) message;

		DataPacket dataPacket = new DataPacket();
		PositionHelper.initPosition(dataPacket, ch2Message);

		return dataPacket;
	}

	@Override
	public Message toTerminalFormatt(Message message) {
		return null;
	}

}
