package com.challenge.beans;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MessageList {
	private UserMessages userMessages;
	private List<UserMessages> followersMessages;
	
	public UserMessages getUserMessages() {
		return userMessages;
	}
	
	public void setUserMessages(UserMessages userMessages) {
		this.userMessages = userMessages;
	}
	
	public List<UserMessages> getFollowersMessages() {
		return followersMessages;
	}
	
	public void setFollowersMessages(List<UserMessages> followersMessages) {
		this.followersMessages = followersMessages;
	}
	
}
