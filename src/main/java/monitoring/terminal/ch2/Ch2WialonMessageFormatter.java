package monitoring.terminal.ch2;

import java.util.Date;
import java.util.Map;

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
				Map<String, String> params = dataPacket.getParams();
				params.put("ax", ch2Message.getAx().toString());
				params.put("ay", ch2Message.getAy().toString());
				params.put("az", ch2Message.getAz().toString());
				
				params.put("gx", ch2Message.getGx().toString());
				params.put("gy", ch2Message.getGy().toString());
				params.put("gz", ch2Message.getGz().toString());
				
				params.put("laccid", ch2Message.getLaccid());
				params.put("gpssignal", ch2Message.getGpsSignal());
				params.put("hdop", Double.toString(ch2Message.getHdop()));
				
				if(ch2Message.getVin() != null && !ch2Message.getVin().isEmpty()) {
					params.put("vin", ch2Message.getVin());
				}
				
				params.put("raw", ch2Message.getRaw());
			}
			return dataPacket;

		} else if (message instanceof Ch2Response) {
			Ch2Response r = (Ch2Response) message;

			DataPacket dataPacket = new DataPacket();
			dataPacket.setTerminalId(r.getTerminalId());
			dataPacket.setDate(DateUtils.localTimeToOtherTimeZone(new Date(), DateUtils.TIMEZONEID_UTC));
			dataPacket.getParams().put("response", r.getResponse().replace("=", "~"));
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
