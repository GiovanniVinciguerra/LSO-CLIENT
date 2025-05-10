package org.javafx.trismasterfx.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Message {
	private String label = null, body = null, timestamp = null;
	
	public Message() {}
	
	public Message(String label, String body, String timestamp) {
		this.setLabel(label);
		this.setBody(body);
		this.setTimestamp(timestamp);
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestamp)), ZoneId.systemDefault());
		time.format(DateTimeFormatter.ISO_DATE_TIME);
		this.timestamp = time.toString();
	}

	@Override
	public String toString() {
		return String.format("Message [label = %c, body = %s, timestamp = %s]", this.getLabel(), this.getBody(), this.getTimestamp());
	}
}
