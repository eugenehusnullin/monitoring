package monitoring.terminal.ch2;

import java.util.Date;

import monitoring.domain.Message;
import monitoring.handler.MessageFormatter;
import monitoring.handler.wialon.DataPacket;
import monitoring.terminal.munic.utils.PositionHelper;
import monitoring.utils.DateUtils;

public class Ch2WialonMessageFormatter implements MessageFormatter {

	@Override
	public DataPacket fromTerminalFormatt(Message message) {

		if (message instanceof Ch2Message) {
			Ch2Message ch2Message = (Ch2Message) message;

			DataPacket dataPacket = new DataPacket();
			PositionHelper.initPosition(dataPacket, ch2Message);
			return dataPacket;

		} else if (message instanceof Ch2Response) {
			Ch2Response r = (Ch2Response) message;

			DataPacket dataPacket = new DataPacket();
			dataPacket.setTerminalId(r.getTerminalId());
			dataPacket.setDate(DateUtils.localTimeToOtherTimeZone(new Date(), DateUtils.TIMEZONEID_UTC));
			dataPacket.getParams().put("response", r.getResponse());
			return dataPacket;

		} else {
			return null;
		}
	}

	@Override
	public Message toTerminalFormatt(Message message) {
		return message;
		// WialonMessage wm = (WialonMessage) message;
	}

}
