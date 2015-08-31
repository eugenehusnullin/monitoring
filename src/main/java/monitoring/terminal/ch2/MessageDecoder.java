package monitoring.terminal.ch2;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MessageDecoder extends ChannelHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String s = (String) msg;
		if (logger.isDebugEnabled()) {
			logger.debug(s);
		}
		String[] arr = s.split(",");
		// String id = arr[0];
		String cmd = arr[1];

		Ch2Message m = null;

		switch (cmd) {
		case "CMD-T":
		case "CMD-D":
			m = fill(arr);
			break;

		default:
			logger.warn("Don`t know how to decode this message of Ch2 device: " + s);
			break;
		}

		super.channelRead(ctx, m);
	}

	private Ch2Message fill(String[] arr) {
		Ch2Message m = new Ch2Message();
		m.setTerminalId(Long.parseLong(arr[0]));
		m.setCmd(arr[1]);
		m.setValidLocation(arr[2].equals("A"));

		// DATE TIME
		String date = arr[3].substring(arr[3].indexOf(":") + 1);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(0, 2)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(2, 4)));
		cal.set(Calendar.YEAR, 2000 + Integer.parseInt(date.substring(4)));

		String time = arr[4].substring(arr[4].indexOf(":") + 1);
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(2, 4)));
		cal.set(Calendar.SECOND, Integer.parseInt(time.substring(4)));

		m.setDate(cal.getTime());

		// LATITUDE
		String lat = arr[5].substring(arr[5].indexOf(":") + 1, arr[5].length() - 1);
		m.setLatitude(Double.parseDouble(lat));

		// LONGITUDE
		String lon = arr[6].substring(arr[6].indexOf(":") + 1, arr[6].length() - 1);
		m.setLongitude(Double.parseDouble(lon));

		// SPEED
		String speed = arr[7].substring(arr[7].indexOf(":") + 1);
		m.setSpeed(Double.parseDouble(speed));

		return m;
	}
}
