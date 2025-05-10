package org.javafx.trismasterfx.entity;

import org.javafx.trismasterfx.App;

public class User {
	/* Attributi della classe */
	private String name = null, surname = null, mail = null, username = null, password = null;
	private int session_id;
	
	/* Limiti dimensioni */
	public static final int USERNAME_MAX = 15, NAME_MAX = 30, SURNAME_MAX = 15, MAIL_MAX = 255, PASSWORD_MAX = 15;
	
	/* Messaggi di errore */
	private final String NULL_USR = "null.usr", BLANK_USR = "blank.usr", USR_SZ = "size.usr";
	private final String NULL_PWD = "null.pwd", BLANK_PWD = "blank.pwd", PWD_SZ = "size.pwd";
	private final String NULL_ML = "null.mail", BLANK_ML = "blank.mail", ML_SZ = "size.mail";
	private final String NULL_NM = "null.name", BLANK_NM = "blank.name", NM_SZ = "size.name";
	private final String NULL_SRM = "null.surname", BLANK_SRM = "blank.surname", SRM_SZ = "size.surname";
	
	private static User usr_inst = new User();
	
	/* Singleton class */
	private User() {}
	
	public static User crt_usr_inst(String username, String password) {
		if(usr_inst == null)
			usr_inst = new User();
		
		usr_inst.setUsername(username);
		usr_inst.setPassword(password);
		
		return usr_inst;
	}
	
	public static User get_usr_inst() {
		return usr_inst;
	}
	
	public static User crt_usr_inst(String name, String surname, String mail, String username, String password) {
		if(usr_inst == null)
			usr_inst = new User();
		
		usr_inst.setName(name);
		usr_inst.setSurname(surname);
		usr_inst.setMail(mail);
		usr_inst.setUsername(username);
		usr_inst.setPassword(password);
		
		return usr_inst;
	}
	
	/* Metodi getter e setter */
	public int getSessionId() {
		return this.session_id;
	}
	
	public void setSessionId(int session_id) {
		this.session_id = session_id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		if(name == null)
			throw new IllegalArgumentException(App.get_resource_key(NULL_NM));
		else if(name.isBlank())
			throw new IllegalArgumentException(App.get_resource_key(BLANK_NM));
		else if(name.length() > NAME_MAX)
			throw new IllegalArgumentException(App.get_resource_key(NM_SZ));
		else
			this.name = name;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public void setSurname(String surname) {
		if(surname == null)
			throw new IllegalArgumentException(App.get_resource_key(NULL_SRM));
		else if(surname.isBlank())
			throw new IllegalArgumentException(App.get_resource_key(BLANK_SRM));
		else if(surname.length() > SURNAME_MAX)
			throw new IllegalArgumentException(App.get_resource_key(SRM_SZ));
		else
			this.surname = surname;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		if(mail == null)
			throw new IllegalArgumentException(App.get_resource_key(NULL_ML));
		else if(mail.isBlank())
			throw new IllegalArgumentException(App.get_resource_key(BLANK_ML));
		else if(mail.length() > MAIL_MAX)
			throw new IllegalArgumentException(App.get_resource_key(ML_SZ));
		else
			this.mail = mail;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		if(username == null)
			throw new IllegalArgumentException(App.get_resource_key(NULL_USR));
		else if(username.isBlank())
			throw new IllegalArgumentException(App.get_resource_key(BLANK_USR));
		else if(username.length() > USERNAME_MAX)
			throw new IllegalArgumentException(App.get_resource_key(USR_SZ));
		else
			this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		if(password == null)
			throw new IllegalArgumentException(App.get_resource_key(NULL_PWD));
		else if(password.isBlank())
			throw new IllegalArgumentException(App.get_resource_key(BLANK_PWD));
		else if(password.length() > PASSWORD_MAX)
			throw new IllegalArgumentException(App.get_resource_key(PWD_SZ));
		else
			this.password = password;
	}
	
	/* toString specifico per la classe User */
	@Override
	public String toString() {
		return String.format("User [session_id = %d name = %s, surname = %s, mail = %s, username = %s, password = %s]", getSessionId(), getName(), getSurname(), getMail(), getUsername(), getPassword());
	}
}
