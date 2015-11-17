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
			
			if(ch2Message.getAx() != null) {
				dataPacket.getParams().put("ax", ch2Message.getAx().toString());
				dataPacket.getParams().put("ay", ch2Message.getAy().toString());
				dataPacket.getParams().put("az", ch2Message.getAz().toString());
				
				dataPacket.getParams().put("gx", ch2Message.getGx().toString());
				dataPacket.getParams().put("gy", ch2Message.getGy().toString());
				dataPacket.getParams().put("gz", ch2Message.getGz().toString());
			}
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
