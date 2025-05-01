package com.maven.trismaster.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.maven.trismaster.App;

public class SettingsDaoService {
	private final String PATH_FILE = System.getProperty("user.dir") + "/target/settings.txt";
	private final String DELIMITER = ";";
	
	public void retrieve() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(PATH_FILE));
		
		String line = reader.readLine();
		
		if(line != null) {
			String[] tokens = line.split(DELIMITER);
			
			App.setConnectionType(tokens[0]);
		}
		
		reader.close();
	}
	
	public void write_and_store() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_FILE, false));
		
		writer.write(App.getConnectionType());
		writer.newLine();
		
		writer.close();
	}
}
