package com.challenge.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.challenge.beans.UserMessages;
import com.challenge.beans.MessageList;
import com.challenge.beans.Messages;
import com.challenge.beans.People;
import com.challenge.dao.ChallaengeDAOImpl;


@Service
public class MessageService {
	
	@Autowired
	private ChallaengeDAOImpl challengeDaoImpl;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public MessageList getMessageList(People people, String search){
		MessageList messageList = new MessageList();
		
		try{
			List<People> peopleList = challengeDaoImpl.getFollowing(people.getId());
			List<Messages> messages = challengeDaoImpl.getFollowerMessages(peopleList, search);
			ArrayList<String> myMessages = (ArrayList<String>)challengeDaoImpl.getMyMessages(people.getId(), search);
			ArrayList<UserMessages> list = new ArrayList<>();
			UserMessages messageBean = new UserMessages();
			
			messageBean = applicationContext.getBean(UserMessages.class);
			messageBean.setId(people.getId());
			messageBean.setHandle(people.getHandle());
			messageBean.setName(people.getName());
			messageBean.setMessageList(myMessages);
			
			messageList.setUserMessages(messageBean);
			
			for(People p : peopleList){
				messageBean = applicationContext.getBean(UserMessages.class);
				messageBean.setId(p.getId());
				messageBean.setHandle(p.getHandle());
				messageBean.setName(p.getName());
				ArrayList<String> userMessageList = new ArrayList<>();
				
				for(Messages message : messages){
					if(message.getPerson_id() == p.getId()){
						userMessageList.add(message.getContent());
					}
				}
				messageBean.setMessageList(userMessageList);
				list.add(messageBean);
			}
			
			messageList.setFollowersMessages(list);
		} catch (Exception e){
			return null;
		}
		
		return messageList;
	}
}
