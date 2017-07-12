package com.challenge.utlis;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class JSONConverter {
	
	public String convertToJSON(Object obj){
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonResp = null;
		
		try {
			jsonResp = ow.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonResp;
	}
}
