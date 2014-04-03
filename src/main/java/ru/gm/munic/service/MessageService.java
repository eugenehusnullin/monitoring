package ru.gm.munic.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gm.munic.domain.Message;
import ru.gm.munic.service.processing.MessageProcessing;

@Service
public class MessageService {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageProcessing messageProcessing;

	@Transactional
	public void process(Message message) {
		sessionFactory.getCurrentSession().save(message);
		messageProcessing.add(message);
	}
}
