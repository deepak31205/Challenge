package com.challenge.daoInterface;

import java.util.List;

import com.challenge.beans.Messages;
import com.challenge.beans.People;
import com.challenge.beans.PeopleFollowers;

public interface ChallengeDaoInterface {
	
	public List<People> getFollowing(int id);
	public List<People> getFollowers(int id);
	public List<String> getMyMessages(int personID, String search);
	public List<Messages> getFollowerMessages(List<People> peopleList, String search);
	public List<Messages> searchMessage(String searchInput);
	public String addFollower(int followerId,int personId);
	public String removeFollower(int unfollowId, int personID);
	public List<PeopleFollowers> getPopularFollowers();
}
