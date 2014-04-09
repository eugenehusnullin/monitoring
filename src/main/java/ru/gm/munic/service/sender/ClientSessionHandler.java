package ru.gm.munic.service.sender;

import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import ru.gm.munic.domain.MunicItemRawData;
import ru.gm.munic.service.processing.LowService;
import ru.gm.munic.service.processing.utils.ItemRawDataJson;

public class ClientSessionHandler extends IoHandlerAdapter {
	private List<MunicItemRawData> list;
	private SocketContainer container;
	private CallbackSender callback;
	private int index = 0;
	private LowService lowService;

	public ClientSessionHandler(List<MunicItemRawData> list, SocketContainer container, CallbackSender callback, LowService lowService) {
		this.list = list;
		this.container = container;
		this.callback = callback;
		this.lowService = lowService;
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		send(session);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		send(session);
	}

	private void send(IoSession session) {
		if (index < list.size()) {
			MunicItemRawData municItemRawData = list.get(index);
			index = index + 1;
			ItemRawDataJson itemRawDataJson = new ItemRawDataJson(municItemRawData.getItemRawData());
			session.write(itemRawDataJson.getString4Wialon());
			
			municItemRawData.setWialonSended(true);
			lowService.updateMunicItemRawData(municItemRawData);
			//TODO: ����� ���
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		} else {
			//TODO: ����� ���
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			session.close(true);
			callback.allsended(container);
		}
	}
}
