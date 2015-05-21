package monitoring.protocol.nis;

import monitoring.protocol.nis.ws.PutCoord;
import monitoring.protocol.nis.ws.PutCoordResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class NisEndpoint {
	private static final String NAMESPACE_URI = "http://monitoring/protocol/nis/ws/";
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "PutCoord")
	//@PayloadRoot(localPart = "PutCoord")
	@ResponsePayload
	public PutCoordResponse putCoord(@RequestPayload PutCoord request) {
		PutCoordResponse response = new PutCoordResponse();
		response.setObjectID(request.getObjectID());

		return response;
	}
}

//public class NisEndpoint implements EndpointInterceptor {
//
//	@Override
//	public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//}
