package monitoring.protocol.nis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public class NisSaajSoapMessageFactory extends SaajSoapMessageFactory {
	@Override
	public SaajSoapMessage createWebServiceMessage(InputStream inputStream) throws IOException {
		String str = IOUtils.toString(inputStream);
		str = str.replace("xmlns:env=", "xmlns:soapenv=");
		//str = str.replace("ws:PutCoord", "PutCoord");
		str = str.replace("ObjectID", "ws:ObjectID");
		str = str.replace("<Coord", "<ws:Coord");
		str = str.replace("/Coord>", "/ws:Coord>");
		str = str.replace("\"http://schemas.xmlsoap.org/soap/envelope\"",
				"\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://monitoring/protocol/nis/ws/\"");
		inputStream = IOUtils.toInputStream(str);

		return super.createWebServiceMessage(inputStream);
	}
}
