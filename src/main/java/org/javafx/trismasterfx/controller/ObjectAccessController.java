package org.javafx.trismasterfx.controller;

import org.javafx.trismasterfx.entity.Match;
import org.javafx.trismasterfx.entity.Message;
import org.javafx.trismasterfx.entity.Stat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObjectAccessController {
	private static ObservableList<Message> messages = FXCollections.observableArrayList();
	private static ObservableList<Stat> stats = FXCollections.observableArrayList();
	private static ObservableList<Match> matches = FXCollections.observableArrayList();
	
	public static ObservableList<Message> getMessages() {
		return messages;
	}
	
	public static ObservableList<Stat> getStats() {
		return stats;
	}
	
	public static ObservableList<Match> getMatches() {
		return matches;
	}
	
	public static Match getNewCreationMatch() {
		Match match = null;
		
		for(Match item : matches) {
			if(item.getStatus().compareTo("4") == 0) {
				match = item;
				break;
			}
		}
			
		return match;
	}
	
	public static Match getProgressMatch() {
		Match match = null;
		
		for(Match item : matches) {
			if(item.getStatus().compareTo("1") == 0) {
				match = item;
				break;
			}
		}
			
		return match;
	}
	
	public static Match getValidationMatch() {
		Match match = null;
		
		for(Match item : matches) {
			if(item.getStatus().compareTo("3") == 0) {
				match = item;
				break;
			}
		}
			
		return match;
	}
	
	public static Match getFinishMatch() {
		Match match = null;
		
		for(Match item : matches) {
			if(item.getStatus().compareTo("0") == 0) {
				match = item;
				break;
			}
		}
			
		return match;
	}
}
