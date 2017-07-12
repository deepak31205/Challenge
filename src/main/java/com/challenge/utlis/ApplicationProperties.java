package com.challenge.utlis;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.stereotype.Component;

public class ApplicationProperties {
	
	public String getApplicationProperties(String key){
		InputStream fileReader;
		try {
			Properties prop = new Properties();
			String file = "application.properties";
			fileReader = getClass().getClassLoader().getResourceAsStream(file);
			if (fileReader != null) {
				prop.load(fileReader);
			} 
			else {
				throw new FileNotFoundException( file + "' not found");
			}
			String value = prop.getProperty(key);
			return value;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			return null;
		}
	}
}
