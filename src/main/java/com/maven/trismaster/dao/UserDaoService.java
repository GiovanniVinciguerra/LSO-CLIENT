package com.maven.trismaster.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.maven.trismaster.entity.User;

public class UserDaoService {
	private final String PATH_FILE = System.getProperty("user.dir") + "/target/user.txt";
	private final String DELIMITER = ";";
	private final int FIELD = 6;
	
	/* Verifica se  esiste un utente già memorizzato; se si lo recupera */
	public boolean check_and_retrieve_usr() throws IOException {
		boolean check = false;
		
		/* Apro il file */
		BufferedReader reader = new BufferedReader(new FileReader(PATH_FILE));
		
		/* Leggo dal file la prima e unica riga */
		String line = reader.readLine();
		
		/* Se l'utente non esiste nel file vuol dire che bisogna accedere;
		 * altrimenti è un utente con una sessione già attiva che non ha bisogno di accedere */
		if(line != null) {
			/* Divide la stringa in token delimitati da ; */
			String[] tokens = line.split(DELIMITER);
			
			/* Se la stringa non contiene il giusto numero di tokens l'accesso deve essere
			 * effettuato nuovamente a causa di qualche problema nella stringa memorizzata */
			if(tokens.length == FIELD) {
				User.get_usr_inst().setSessionId(Integer.parseInt(tokens[0]));
				User.crt_usr_inst(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
				check = true;
			}
		}
		
		/* Chiudo il buffer di lettura */
		reader.close();
		
		return check;
	}
	
	public void write_and_store() throws IOException {
		/* Apro il file */
		BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_FILE, false));
		
		/* Scrivo sul file le informazioni dell'utente in modo da permette il resume rapido
		 * senza accesso */
		writer.write(
				User.get_usr_inst().getSessionId() + ";" +
				User.get_usr_inst().getName() + ";" +
				User.get_usr_inst().getSurname() + ";" +
				User.get_usr_inst().getMail() + ";" +
				User.get_usr_inst().getUsername() + ";" +
				User.get_usr_inst().getPassword()
		);
		writer.newLine();
		
		/* Chiudo il buffer di scrittura */
		writer.close();
	}
	
	public void delete()  throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_FILE, false));
		
		/* Elimino le informazioni dal file */
		writer.write("");
		
		writer.close();
	}
}
