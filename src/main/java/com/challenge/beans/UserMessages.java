package com.challenge.beans;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserMessages {
	private int id;
	private String name;
	private String handle;
	private ArrayList<String> messageList;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getHandle() {
		return handle;
	}
	
	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	public ArrayList<String> getMessageList() {
		return messageList;
	}
	
	public void setMessageList(ArrayList<String> messageList) {
		this.messageList = messageList;
	}
}
