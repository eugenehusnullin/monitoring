package monitoring.utils;

import org.postgresql.util.Base64;

public class Base64Utils {

	public static int decodeBase64Integer(String value) {
		int result = 0;
		if (value != null && value != "") {
			byte[] values = Base64.decode(value);
			if (values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					result <<= 8;
					result += values[i] < 0 ? 256 + values[i] : values[i];
				}
			}
		}
		return result;
	}

	public static String decodeBase64String(String value) {
		String result = "";
		if (value != null && value != "") {
			byte[] values = Base64.decode(value);
			for (int i = 0; i < values.length; i++) {
				result += (char) values[i];
			}
		}
		return result;
	}

	public static boolean decodeBase64Boolean(String value) {
		boolean result = false;
		if (value != null && value != "") {
			byte[] values = Base64.decode(value);
			result = values[0] != 0;
		}
		return result;
	}
}
