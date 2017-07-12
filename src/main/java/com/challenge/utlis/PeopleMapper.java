package com.challenge.utlis;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.challenge.beans.People;

public class PeopleMapper implements RowMapper<People> {  
	 
	public People mapRow(ResultSet rs, int rowNum) throws SQLException {  
		People people = new People();  
		people.setId(rs.getInt("id"));
		people.setHandle(rs.getString("handle"));
		people.setName(rs.getString("name"));
		return people;  
	 }  
}
