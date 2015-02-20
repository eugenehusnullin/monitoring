package monitoring.utils;

import org.junit.Test;

public class Base64UtilsTest {
	
	@Test
	public void accxTest() {
		System.out.println("accx begin - " + Base64Utils.decodeBase64Integer("////OA=="));
		System.out.println("accx peak - " + Base64Utils.decodeBase64Integer("////IQ=="));
		System.out.println("accx end - " + Base64Utils.decodeBase64Integer("////QA=="));
		
		System.out.println("accy begin - " + Base64Utils.decodeBase64Integer("////3Q=="));
		System.out.println("accy peak - " + Base64Utils.decodeBase64Integer("////3g=="));
		System.out.println("accy end - " + Base64Utils.decodeBase64Integer("////3w=="));
		
		System.out.println("accz begin - " + Base64Utils.decodeBase64Integer("///7/A=="));
		System.out.println("accz peak - " + Base64Utils.decodeBase64Integer("///7/w=="));
		System.out.println("accz end - " + Base64Utils.decodeBase64Integer("///8Fg=="));
	}

}
