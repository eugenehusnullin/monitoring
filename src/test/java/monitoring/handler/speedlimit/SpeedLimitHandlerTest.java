package monitoring.handler.speedlimit;

import java.util.Formatter;
import java.util.Locale;

import org.junit.Test;

public class SpeedLimitHandlerTest {

	@Test
	public void hereTest() {
		Double lat = 55.75249;
		Double lon = 37.58121;

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);

		sb.append("http://legacy.route.cit.api.here.com/routing/6.2/getlinkinfo.json");
		sb.append('?');
		sb.append("app_id=g54Qc1ApYhkO2HpAwTEZ");
		sb.append('&');
		sb.append("app_code=TkrxmdbiVk05WxNZrPE3pA");
		sb.append('&');
		formatter.format("waypoint=%.6f,%.6f", lat, lon);
		sb.append('&');
		sb.append("linkattributes=speedCategory");
		formatter.close();

		String url = sb.toString();
		System.out.println(url);
	}
	
	@Test
	public void speedLimitTest() {
		SpeedLimitHandler slh = new SpeedLimitHandler();
		Integer s = slh.getSpeedLimit(55.75249, 37.58121);
		System.out.println(s);
	}

}
