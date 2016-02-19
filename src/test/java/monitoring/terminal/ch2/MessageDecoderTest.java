package monitoring.terminal.ch2;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class MessageDecoderTest {

	@Test
	public void fillTest() {
		String s = "860719028553836,CMD-T,V,DATE:151208,TIME:111333,LAT:55.1664883N,LOT:061.3897166E,Speed:001.1,1-1-0-0-81-22,000,25002-1CE9-765A,3,,0,46,-28,214,-1,-1,-1";
		
		MessageDecoder md = new MessageDecoder(null);
		Ch2Message m =  md.fill(s.split(","), 0, "CMD-T", s);
		Assert.assertEquals(860719028553836L, m.getTerminalId());
		Calendar c = Calendar.getInstance();
		c.set(2015, 12-1, 8, 11, 13, 33);
		Assert.assertEquals(c.getTime(), m.getDate());
	}

}
