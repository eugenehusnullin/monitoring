package monitoring.utils;

import org.junit.Test;

public class ByteUtilitiesTest {

	@Test
	public void hexToAsciiTest() {
		String hex = "585738414E324E45394648303138393939";
		System.out.println(ByteUtilities.hexToAscii(hex));
	}
}
