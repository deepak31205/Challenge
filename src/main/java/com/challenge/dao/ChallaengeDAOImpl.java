package com.challenge.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.challenge.beans.Messages;
import com.challenge.beans.People;
import com.challenge.beans.PeopleFollowers;
import com.challenge.daoInterface.ChallengeDaoInterface;
import com.challenge.utlis.AppConfig;
import com.challenge.utlis.PeopleMapper;

@Repository
public class ChallaengeDAOImpl implements ChallengeDaoInterface{
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public ChallaengeDAOImpl() {
	    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new AppConfig().primaryDataSource());
	}
	
	@Override
	public List<People> getFollowing(int id) {
		List<People> result = null;
		try{
			String query = "select p.id,p.handle,p.name from people p inner join followers f on p.id = f.person_id where f.follower_person_id = :personID";
			MapSqlParameterSource namedParameters = new MapSqlParameterSource("personID",id);
			result = namedParameterJdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper(People.class));
		} catch(Exception e){
			e.printStackTrace();
			return result;
		}
		return result; 
    }
	
	@Override
	public List<People> getFollowers(int id) {
		List<People> result = null;
		try{
			String query = "select p.id,p.handle,p.name from people p inner join followers f on p.id = f.follower_person_id where f.person_id = :personID";
			MapSqlParameterSource namedParameters = new MapSqlParameterSource("personID",id);
			result = namedParameterJdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper(People.class));
		} catch(Exception e){
			e.printStackTrace();
			return result;
		}
		return result;
        
    }
	
	@Override
	public List<String> getMyMessages(int personID, String search){
		String query = "select content from messages where person_id = :personID"; 
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("personID", personID);
		if(search != null){
			query = query + " and content like :search";
			namedParameters.addValue("search", "%"+search+"%");
		}
		List<String> myMessageList = namedParameterJdbcTemplate.queryForList(query, namedParameters, String.class);
		return myMessageList;
	}
	
	@Override
	public List<Messages> getFollowerMessages(List<People> peopleList, String search) {
		
		String query = "select person_id, content from messages where person_id in (:ids)";

		List<Integer> idList = new ArrayList<>();
		
		for(People p : peopleList){
			idList.add(p.getId());
		}
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("ids", idList);
		
		if(search != null){
			query = query + " and content like :search";
			namedParameters.addValue("search", "%"+search+"%");
		}

		List<Messages> followingUserMessages = namedParameterJdbcTemplate.query(query, namedParameters,new BeanPropertyRowMapper(Messages.class));
        return followingUserMessages;
    }
	
	@Override
	public List<Messages> searchMessage(String searchInput){
		String query = "select * from messages where content like :searchInput";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("searchInput", "%"+searchInput+"%");
		List<Messages> messageList = namedParameterJdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper(Messages.class));
		return messageList;
	}
	
	@Override
	public String addFollower(int followerId,int personId){
		String checkQuery = "select person_id from followers where person_id =:followerId and follower_person_id = :personId"; 
		String query = "insert into followers (person_id, follower_person_id) values (:followerId,:personId)";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("personId", personId);
		namedParameters.addValue("followerId", followerId);
		boolean found = false;
		try{
			int result = namedParameterJdbcTemplate.queryForObject(checkQuery, namedParameters, Integer.class);
			if(result == followerId)
				found = true;
		} catch(Exception e){
			found = false;
		}
		
		if(found)
			return "Already following";
		else{
			try{
				int result = namedParameterJdbcTemplate.update(query, namedParameters);
				if(result > 0)
					return "Following requested person successfully";
				else
					return "Failed to follow requested person";
			} catch (Exception e){
				e.printStackTrace();
				return "Something went wrong. Internal server error";
			}
		}
	}
	
	public People getPersonFromUserName(String name){
		String query = "select p.id,p.handle,p.name from people p inner join users u on p.id = u.person_id where u.username=:name";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("name",name);
		People people = namedParameterJdbcTemplate.queryForObject(query, namedParameters, new PeopleMapper());
		return people;
	}
	
	@Override
	public String removeFollower(int unfollowId, int personID){
		String checkQuery = "select person_id from followers where person_id =:unfollowId and follower_person_id = :personID"; 
		String query = "delete from followers where person_id =:unfollowId and follower_person_id = :personID";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("personID", personID);
		namedParameters.addValue("unfollowId", unfollowId);

		boolean found = false;
		try{
			int result = namedParameterJdbcTemplate.queryForObject(checkQuery, namedParameters, Integer.class);
			if(result == unfollowId)
				found = true;
		} catch(Exception e){
			found = false;
		}
		
		if(found){
			try{
				int result = namedParameterJdbcTemplate.update(query, namedParameters);
				if(result > 0)
					return "Unfollowing requested person successfully";
				else
					return "Failed to unfollow requested person";
			} catch (Exception e){
				e.printStackTrace();
				return "Something went wrong. Internal server error";
			}
		}
		else{
			return "Not following this user";
		}
	}
	
	@Override
	public List<PeopleFollowers> getPopularFollowers(){
		String query = "select temp1.person_id, max(temp2.follower_id) from (select person_id, max(followers_of_follower) as sumfs from (select  f.person_id as person_id, f.follower_person_id as follower_id, x.counts as followers_of_follower from followers as f ,(select count(1) as counts, person_id from followers group by person_id ) as x where x.person_id = f.follower_person_id) group by person_id) as temp1 , (select  f.person_id as person_id, f.follower_person_id as follower_id, x.counts as followers_of_follower from followers as f ,(select count(1) as counts, person_id from followers group by person_id ) as x where  x.person_id = f.follower_person_id)  as temp2 where temp1.person_id = temp2.person_id and temp1.sumfs = temp2.followers_of_follower group by temp1.person_id";
		try{
			Map<Integer, People> usersDirectory = getAllPeople();
			List<PeopleFollowers> popularPeopleList = (List<PeopleFollowers>) namedParameterJdbcTemplate.execute(query,  new PreparedStatementCallback<List<PeopleFollowers>>() {
		        @Override
		        public List<PeopleFollowers> doInPreparedStatement(PreparedStatement pstmt)
		            throws SQLException, DataAccessException {
		        	List<PeopleFollowers> result = new ArrayList<>();
		            pstmt.execute();
		            ResultSet rs = pstmt.getResultSet();
		            while(rs.next()){
		            	PeopleFollowers entry = new PeopleFollowers();
		            	entry.setPerson(usersDirectory.get(rs.getInt(1)));
		            	entry.setPopularFollower(usersDirectory.get(rs.getInt(2)));
		            	result.add(entry);
		            }
		            return result;
		        }
		    });
			return popularPeopleList;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<Integer, People> getAllPeople(){
		List<People> users = new ArrayList<>();
		String query = "Select id,handle,name from people";
		users = namedParameterJdbcTemplate.query(query, new BeanPropertyRowMapper(People.class));
		Map<Integer, People> usersDirectory = new HashMap<>();
		for(People p: users){
			usersDirectory.put(p.getId(), p);
		}
		return usersDirectory;
	}
}
