package com.challenge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.challenge.beans.MessageList;
import com.challenge.beans.People;
import com.challenge.beans.PeopleFollowers;
import com.challenge.dao.ChallaengeDAOImpl;
import com.challenge.service.MessageService;
import com.challenge.utlis.JSONConverter;

@RestController
public class ChallengeController {
	
	@Autowired
	private ChallaengeDAOImpl challengeDAO;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	JSONConverter jsonConverter;
	
	/* Method to get the Person object from user name
	 * when person is logged in
	 */
	public People getCurrentUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
	    People people = challengeDAO.getPersonFromUserName(name);
	    return people;
	}
	
	/* Following endpoint will return a list in JSON format containing
	 * people who follow current logged in user
	 */
	@RequestMapping(value="/challenge/myFollowers", method = RequestMethod.GET, produces = "application/json")
	public String getFollowers(){
	    int personID = getCurrentUser().getId();
		List<People> peopleList = challengeDAO.getFollowers(personID);
		if(peopleList == null){
			return "{\"message\": \"Something went wrong. Internal server error\"}";
		}
		String response = jsonConverter.convertToJSON(peopleList);
		return "{\"followers\": " +response+"}";
	}
	
	/* Following endpoint will return a list in JSON format containing
	 * people followed by current logged in user
	 */
	@RequestMapping(value="/challenge/getFollowing", method = RequestMethod.GET, produces = "application/json")
	public String getFollowing(){
		int personID = getCurrentUser().getId();
		List<People> peopleList = challengeDAO.getFollowing(personID);
		if(peopleList == null){
			return "{\"message\": \"Something went wrong. Internal server error\"}";
		}
		String response = jsonConverter.convertToJSON(peopleList);
		return "{\"following\": " +response+"}";
	}
	
	/* Following endpoint adds a person in the followers table
	 * against the logged in user
	 */
	@RequestMapping(value="/challenge/follow", method = RequestMethod.GET, produces = "application/json")
	public String startFollowing(@RequestParam int id ){
		int personID = getCurrentUser().getId();
		String result = challengeDAO.addFollower(id,personID);
		return "{\"message\": \"" + result +"\"}";
	}
	
	/* Following endpoint removes a person in the followers table
	 * against the logged in user
	 */
	@RequestMapping(value="/challenge/unfollow", method = RequestMethod.GET, produces = "application/json")
	public String unfollow(@RequestParam int id ){
		int personID = getCurrentUser().getId();
		String result = challengeDAO.removeFollower(id,personID);
		return "{\"message\": \"" + result +"\"}";
	}
	
	/* Following endpoint returns a list of messages in JSON format containing a 
	 * current logged in users messages and messages of the people he follows
	 * Additional and optional search parameter if user wants to filter out the 
	 * messages using a search key in the query string
	 */
	@RequestMapping(value="/challenge/getMessages", method = RequestMethod.GET, produces = "application/json")
	public String getMessages(@RequestParam(required=false) String search){
		People people = getCurrentUser();
		MessageList messageList = messageService.getMessageList(people,search);
		if(messageList == null){
			return "{\"message\": \"Something went wrong. Internal server error\"}";
		}
		String response = jsonConverter.convertToJSON(messageList);
		return response;
	}
	
	/* Following endpoint returns a list of pair of users and their popular followers
	 */
	@RequestMapping(value="/challenge/getPopularFollowersList", method = RequestMethod.GET, produces = "application/json")
	public String getMessages(){
		List<PeopleFollowers> popularFollowersList = challengeDAO.getPopularFollowers();
		if(popularFollowersList == null){
			return "{\"message\": \"Something went wrong. Internal server error\"}";
		}
		String response = jsonConverter.convertToJSON(popularFollowersList);
		return "{\"popularFollowersList\": " + response + "}";
	}
}