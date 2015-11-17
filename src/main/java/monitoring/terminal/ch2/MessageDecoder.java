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
		Ch2Response r = null;

		switch (cmd) {
		case "CMD-T":
		case "CMD-D":
			m = fill(arr);
			break;

		case "CMD-Z":
			r = fillResponse(arr);
			logger.info("Response: " + s);
			break;

		default:
			logger.warn("Don`t know how to decode this message of Ch2 device: " + s);
			break;
		}

		if (m != null) {
			super.channelRead(ctx, m);
		} else {
			super.channelRead(ctx, r);
		}
	}

	private Ch2Response fillResponse(String[] arr) {
		Ch2Response r = new Ch2Response();
		r.setTerminalId(Long.parseLong(arr[0]));
		r.setResponse(arr[2]);
		return r;
	}

	private Ch2Message fill(String[] arr) {
		// 860719028553836,CMD-T,A,DATE:151113,TIME:100131,LAT:55.7923483N,LOT:037.7523600E,Speed:040.1,1-0-0-0-99-31,010,25002-1E17-4F03,10,0.98,0,-21,18,122,-1,-1,-1
		Ch2Message m = new Ch2Message();
		m.setTerminalId(Long.parseLong(arr[0]));
		m.setCmd(arr[1]);
		m.setValidLocation(arr[2].equals("A"));

		// DATE TIME
		String date = arr[3].substring(arr[3].indexOf(":") + 1);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2000 + Integer.parseInt(date.substring(0, 2)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(2, 4)));
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(4)));

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

		// course
		try {
			m.setCourse(Integer.parseInt(arr[9]));
		} catch (Exception e) {
			logger.error("Get course failure.", e);
		}
		
		// is have accelerometer and gyroscope
		if (arr.length > 15) {
			m.setAx(Integer.parseInt(arr[14]));
			m.setAy(Integer.parseInt(arr[15]));
			m.setAz(Integer.parseInt(arr[16]));
			
			m.setGx(Integer.parseInt(arr[17]));
			m.setGy(Integer.parseInt(arr[18]));
			m.setGz(Integer.parseInt(arr[19]));
		}

		return m;
	}
}
