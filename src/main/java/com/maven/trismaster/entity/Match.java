package com.maven.trismaster.entity;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Match {
	public static class GameValueTable {
		private static char[][] values = new char[3][3];
		private static int lastRow = 0, lastCol = 0;
		
		public GameValueTable(ArrayList<Step> steps) {
			for(Step step : steps) {
				lastRow = Math.ceilDiv(step.getIndex(), 3) - 1;
				lastCol = (step.getIndex() - 1) % 3;
				values[lastRow][lastCol] = step.getSeed();
			}
		}
		
		public boolean isWinner() {
			/* Scorro sulla riga */
			if(values[lastRow][lastCol] == values[lastRow][(lastCol + 1) % 3] && values[lastRow][lastCol] == values[lastRow][(lastCol + 2) % 3])
				return true;
			/* Scorro sulla colonna */
			else if(values[lastRow][lastCol] == values[(lastRow + 1) % 3][lastCol] && values[lastRow][lastCol] == values[(lastRow + 2) % 3][lastCol])
				return true;
			/* Scorre la diagonale principale */
			if(lastRow == lastCol)
				if(values[lastRow][lastCol] == values[(lastRow + 1) % 3][(lastCol + 1) % 3] && values[lastRow][lastCol] == values[(lastRow + 2) % 3][(lastCol + 2) % 3])
					return true;
			/* Scorre la diagonale secondaria */
			else if((lastRow + lastCol) == 2)
				if(values[lastRow][lastCol] == values[(lastRow + 1) % 3][(lastCol + 2) % 3] && values[lastRow][lastCol] == values[(lastRow + 2) % 3][(lastCol + 1) % 3])
					return true;
			
			return false;
		}
	}
	
	private StringProperty player_1 = new SimpleStringProperty(), player_2 = new SimpleStringProperty(), status = new SimpleStringProperty();
	private String result = null, seed = null;
	private ObservableList<Step> steps = FXCollections.observableArrayList();
	private int match_id;
	
	public Match(int match_id, String player_1, String player_2, String status) {
		this.setMatch_id(match_id);
		this.setPlayer_1(player_1);
		this.setPlayer_2(player_2);
		this.setStatus(status);
	}
	
	public boolean player_1_isUndefined() {
		return this.getPlayer_1() == null || this.getPlayer_1().isEmpty() || this.getPlayer_1().isBlank();
	}
	
	public boolean player_2_isUndefined() {
		return this.getPlayer_2() == null || this.getPlayer_2().isEmpty() || this.getPlayer_2().isBlank();
	}
	
	public StringProperty getPlayer_1Property() {
		return this.player_1;
	}

	public String getPlayer_1() {
		return this.player_1.get();
	}

	public void setPlayer_1(String player_1) {
		this.player_1.set(player_1);
	}
	
	public StringProperty getPlayer_2Property() {
		return this.player_2;
	}

	public String getPlayer_2() {
		return this.player_2.get();
	}

	public void setPlayer_2(String player_2) {
		this.player_2.set(player_2);
	}
	
	public StringProperty getStatusProperty() {
		return this.status;
	}

	public String getStatus() {
		return this.status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ObservableList<Step> getSteps() {
		return this.steps;
	}
	
	public String getSeed() {
		return this.seed;
	}
	
	public void setSeed(String seed) {
		this.seed = seed;
	}

	public int getMatch_id() {
		return this.match_id;
	}

	public void setMatch_id(int match_id) {
		this.match_id = match_id;
	}

	@Override
	public String toString() {
		return String.format("Match [match_id = %s, player_1 = %s, player_2 = %s, status = %s, seed = s, result = %s, steps = %s]", this.getMatch_id(), this.getPlayer_1(),
				this.getPlayer_2(), this.getStatus(), this.getSeed(), this.getResult(), this.getSteps());
	}
}
