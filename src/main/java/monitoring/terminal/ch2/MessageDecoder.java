package monitoring.terminal.ch2;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import monitoring.utils.ByteUtilities;

public class MessageDecoder extends ChannelHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
	private static final float lsb = 7.81f;
	private List<Long> terminalIds;
	private Long deviceId = null;

	public MessageDecoder(List<Long> terminalIds) {
		setTerminalIds(terminalIds);
	}

	public void setTerminalIds(List<Long> terminalIds) {
		this.terminalIds = terminalIds;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String s = (String) msg;
		if (logger.isDebugEnabled()) {
			logger.debug(s);
		}

		String[] arr = s.split(",");
		// String id = arr[0];
		String cmd = arr[1];
		int shift = 0;

		if (cmd.equals("PWR saved") || cmd.equals("Sorry Not Support")) {
			cmd = arr[3];
			shift = 2;
		} else if (cmd.equals(" ERROR!")) {
			cmd = "CMD-T";
		}

		Ch2Message m = null;
		Ch2Response r = null;

		switch (cmd) {
		case "CMD-T":
		case "CMD-D":
			m = fill(arr, shift, cmd, s);
			break;

		case "CMD-Z":
			r = fillResponse(arr, s);
			logger.info("Response: " + s);
			break;

		default:
			if (deviceId != null) {
				m = new Ch2Message();
				m.setTerminalId(deviceId);
				m.setRaw(s);
			}

			logger.warn("Don`t know how to decode this message of Ch2 device: " + s);
			break;
		}

		if (m != null) {
			super.channelRead(ctx, m);
		} else {
			super.channelRead(ctx, r);
		}
	}

	private Ch2Response fillResponse(String[] arr, String msg) {
		// 860719028547457,CMD-Z,$296=XW8AN2NE9FH018999
		// 860719028547457,CMD-Z,$300=3.48,328.95,30286
		// 860719028533101,CMD-Z,85 00 00 72 94
		// 860719028533101,CMD-Z,88 58 57 38 41 4E 32 4E 45 39 46 48 30 31 38 39 39 39
		Ch2Response r = new Ch2Response();
		r.setRaw(msg);
		r.setTerminalId(Long.parseLong(arr[0]));
		if (deviceId == null) {
			deviceId = r.getTerminalId();
		}
		int index = msg.indexOf("CMD-Z", 0) + 6;
		r.setResponse(msg.substring(index));

		if (msg.charAt(index) == '$') {
			r.setResponseType(msg.substring(index + 1, index + 4));

			if (r.getResponseType().equals("296")) {
				r.setResponse("VIN-" + msg.substring(index + 5));
			}
		} else {
			r.setResponseType(msg.substring(index, index + 2));

			if (r.getResponseType().equals("88")) {
				if (!r.getResponse().startsWith("88 00 00 00 00 00 00 00 00 00")) {
					String vin = r.getResponse().substring(3);
					vin = vin.replace(" ", "");
					vin = ByteUtilities.hexToAscii(vin);
					r.setResponse("VIN-" + vin);
				}
			} else if (r.getResponseType().equals("85")) {
				String odom = r.getResponse().substring(3);
				odom = odom.replace(" ", "");
				r.setResponse("ODOM-" + Long.parseLong(odom, 16));
			}
		}
		return r;
	}

	public Ch2Message fill(String[] arr, int shift, String cmd, String msg) {
		// 860719028553836,CMD-T,A,DATE:151113,TIME:100131,LAT:55.7923483N,LOT:037.7523600E,Speed:040.1,1-0-0-0-99-31,010,25002-1E17-4F03,10,0.98,0,-21,18,122,-1,-1,-1
		// 860719028553836,CMD-T,V,DATE:151208,TIME:111333,LAT:55.1664883N,LOT:061.3897166E,Speed:001.1,1-1-0-0-81-22,000,25002-1CE9-765A,3,,0,46,-28,214,-1,-1,-1
		// 867273021510909,Sorry Not Support,,CMD-T,A,DATE:160301,TIME:155102,LAT:55.8110733N,LOT:037.7410283E,Speed:000.0,1-0-0-0-59-31,000,25002-1E64-705E,8,1.28,0,15,30,-4,-64,-96,54
		// 867273021510339,Z6FMXXESWMDK75664,16% 78^C 847rpm 0km 0km/h 14V 0 6 2L/h 0L/100Km,CMD-T,A,DATE:160304,TIME:123859,LAT:55.7759783N,LOT:037.5831400E,Speed:000.0,1-0-0-0-72-24,000,25002-1E3F-3422,8,1.25,0,-33,-3,0,-28,134,-40
		Ch2Message m = new Ch2Message();
		m.setRaw(msg);
		m.setTerminalId(Long.parseLong(arr[0]));
		if (deviceId == null) {
			deviceId = m.getTerminalId();
		}
		m.setCmd(cmd);
		m.setValidLocation(arr[shift + 2].equals("A"));

		// DATE TIME
		String date = arr[shift + 3].substring(arr[shift + 3].indexOf(":") + 1);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2000 + Integer.parseInt(date.substring(0, 2)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(2, 4)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(4)));

		String time = arr[shift + 4].substring(arr[shift + 4].indexOf(":") + 1);
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(2, 4)));
		cal.set(Calendar.SECOND, Integer.parseInt(time.substring(4)));

		m.setDate(cal.getTime());

		// LATITUDE
		String lat = arr[shift + 5].substring(arr[shift + 5].indexOf(":") + 1, arr[shift + 5].length() - 1);
		m.setLatitude(Double.parseDouble(lat));

		// LONGITUDE
		String lon = arr[shift + 6].substring(arr[shift + 6].indexOf(":") + 1, arr[shift + 6].length() - 1);
		m.setLongitude(Double.parseDouble(lon));

		// SPEED
		String speed = arr[shift + 7].substring(arr[shift + 7].indexOf(":") + 1);
		m.setSpeed(Double.parseDouble(speed));
		
		// PLUGED
		String state = arr[shift + 8];
		m.setPluged(state.substring(0, 1).equals("1"));

		// course
		try {
			m.setCourse(Integer.parseInt(arr[shift + 9]));
		} catch (Exception e) {
			logger.error("Get course failure.", e);
		}

		// laccid
		try {
			if (!arr[shift + 10].isEmpty()) {
				String[] laccidArr = arr[shift + 10].split("-");
				String laccid = laccidArr[1].concat(laccidArr[2]);
				m.setLaccid(laccid);
			}
		} catch (Exception e) {
			logger.error("Get laccid failure.", e);
		}

		if (!arr[shift + 11].isEmpty()) {
			m.setGpsSignal(arr[shift + 11]);
		}

		if (!arr[shift + 12].isEmpty()) {
			m.setHdop(Double.parseDouble(arr[shift + 12]));
		}

		// is have accelerometer and gyroscope
		if (arr.length > (shift + 15)) {
			m.setAx(Math.round(Integer.parseInt(arr[shift + 14]) * lsb));
			m.setAy(Math.round(Integer.parseInt(arr[shift + 15]) * lsb));
			m.setAz(Math.round(Integer.parseInt(arr[shift + 16]) * lsb));

			if (terminalIds != null && terminalIds.contains(m.getTerminalId())) {
				m.setAx(m.getAx() + 250);
				m.setAy(m.getAy() + 50);
				m.setAz(m.getAz() - 20);
			}

			m.setGx(Integer.parseInt(arr[shift + 17]));
			m.setGy(Integer.parseInt(arr[shift + 18]));
			m.setGz(Integer.parseInt(arr[shift + 19]));
		}

		return m;
	}
}
