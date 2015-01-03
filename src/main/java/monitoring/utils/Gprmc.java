package monitoring.utils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gprmc {

	public static String makeGprmc(Date recordedAt, double gpsSpeed, double gpsDir, double lat, double lon,
			boolean eastern, boolean northen) {
		// $GPRMC,180933.000,A,5544.0903,N,3736.7949,E,25.00,194.00,160414,0,N,A*15

		SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss.SSS");
		SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyy");

		StringBuffer sb = new StringBuffer();
		sb.append("GPRMC,");
		sb.append(sdfTime.format(recordedAt));
		sb.append(",A,");

		sb.append((int) lat);
		sb.append('.');
		sb.append((int) ((lat - (int) lat) * 10000));
		sb.append(',');
		sb.append(northen ? 'N' : 'S');
		sb.append(',');

		sb.append((int) lon);
		sb.append('.');
		sb.append((int) ((lon - (int) lon) * 10000));
		sb.append(',');
		sb.append(eastern ? 'E' : 'W');
		sb.append(',');

		sb.append((int) gpsSpeed);
		sb.append('.');
		sb.append((int) ((gpsSpeed - (int) gpsSpeed) * 100));
		sb.append(',');

		sb.append((int) gpsDir);
		sb.append('.');
		sb.append((int) ((gpsDir - (int) gpsDir) * 100));
		sb.append(',');

		sb.append(sdfDate.format(recordedAt));
		sb.append(",0,N,A");

		int crc = gprmcCRC(sb.toString().getBytes(Charset.forName("ASCII")));
		sb.insert(0, '$');
		sb.append('*');
		sb.append(Integer.toHexString(crc));

		return sb.toString();
	}

	private static int gprmcCRC(byte[] bytes) {
		int crc = 0x0;
		for (int i = 0; i < bytes.length; i++) {
			crc = crc ^ (bytes[i] < 0 ? 256 + bytes[i] : bytes[i]);
		}
		return crc;
	}
}
