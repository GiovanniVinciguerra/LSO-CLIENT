package com.maven.trismaster.entity;

import com.maven.trismaster.App;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Match {
	private class MatchSteps {
		private char[][] steps = new char[3][3];
		private int lastRow = 0, lastCol = 0;
		private boolean turn = false;
		
		public MatchSteps() {
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++)
					this.steps[i][j] = '\0';
		}
		
		public boolean isYourTurn() {
			return this.turn;
		}
		
		public void setTurn(boolean turn) {
			this.turn = turn;
		}
		
		public void setStep(int row, int col, char value) {
			this.steps[row][col] = value;
			this.lastRow = row;
			this.lastCol = col;
		}
		
		public char getStep(int row, int col) {
			return this.steps[row][col];
		}
		
		public String getStepAsText(int row, int col) {
			int index = ((row * 3) + col) + 1;
			char value = this.getStep(row, col);
			return new String(index + "" + value);
		}
		
		public boolean isWinner() {
			/* Scorro sulla riga */
			if(steps[lastRow][lastCol] == steps[lastRow][(lastCol + 1) % 3] && steps[lastRow][lastCol] == steps[lastRow][(lastCol + 2) % 3])
				return true;
			/* Scorro sulla colonna */
			else if(steps[lastRow][lastCol] == steps[(lastRow + 1) % 3][lastCol] && steps[lastRow][lastCol] == steps[(lastRow + 2) % 3][lastCol])
				return true;
			/* Scorre la diagonale principale */
			if(lastRow == lastCol)
				if(steps[lastRow][lastCol] == steps[(lastRow + 1) % 3][(lastCol + 1) % 3] && steps[lastRow][lastCol] == steps[(lastRow + 2) % 3][(lastCol + 2) % 3])
					return true;
			/* Scorre la diagonale secondaria */
			else if((lastRow + lastCol) == 2)
				if(steps[lastRow][lastCol] == steps[(lastRow + 1) % 3][(lastCol + 2) % 3] && steps[lastRow][lastCol] == steps[(lastRow + 2) % 3][(lastCol + 1) % 3])
					return true;
			
			return false;
		}
		
		public void translate(String step)  {
			int index = Character.getNumericValue(step.charAt(0));
			int row = Math.ceilDiv(index - 1, 3);
			int col = (index - 1) % 3;
			char value = step.charAt(1);
			this.setStep(row, col, value);
		}
		
		public int getStepSize() {
			int count = 0;
			
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++)
					if(this.steps[i][j] != '\0')
						count++;
			
			return count;
		}
	}
	
	private StringProperty player_1 = new SimpleStringProperty(), player_2 = new SimpleStringProperty(), literalStatus = new SimpleStringProperty(), seed = new SimpleStringProperty();
	private String result = "0", status = "4";
	private int match_id = -1;
	private boolean player_2Null = true;
	private MatchSteps steps = new MatchSteps();
	
	private final String UNDEFINED = "undefined";
	private final String STATUS_PROGRESS = "status.progress", STATUS_WAITING = "status.waiting", STATUS_NEW = "status.new", STATUS_VALIDATION = "status.validation", STATUS_FINISH = "status.finish";
	
	public Match(int match_id, String player_1, String player_2, String status) {
		this.setMatch_id(match_id);
		this.setPlayer_1(player_1);
		this.setPlayer_2(player_2);
		this.setStatus(status);
	}
	
	public void translate(String step) {
		this.steps.translate(step);
	}
	
	public int getStepSize() {
		return this.steps.getStepSize();
	}
	
	public String getStepAsText(int row, int col) {
		return this.steps.getStepAsText(row, col);
	}
	
	public boolean isYourturn() {
		return this.steps.isYourTurn();
	}
	
	public void setTurn(boolean turn) {
		this.steps.setTurn(turn);
	}
	
	public char getStep(int row, int col) {
		return this.steps.getStep(row, col);
	}
	
	public boolean isWinner() {
		return this.steps.isWinner();
	}
	
	public void setStep(int row, int col, char value) {
		this.steps.setStep(row, col, value);
	}
	
	public StringProperty getPlayer_1Property() {
		return this.player_1;
	}

	public String getPlayer_1() {
		return this.player_1.get();
	}

	public void setPlayer_1(String player_1) {
		if(player_1 == null || player_1.isEmpty() || player_1.isBlank())
			this.player_1.set(App.get_resource_key(UNDEFINED));
		else
			this.player_1.set(player_1);
	}
	
	public StringProperty getPlayer_2Property() {
		return this.player_2;
	}

	public String getPlayer_2() {
		return this.player_2.get();
	}

	public void setPlayer_2(String player_2) {
		if(player_2 == null || player_2.isEmpty() || player_2.isBlank()) {
			this.player_2.set(App.get_resource_key(UNDEFINED));
			this.player_2Null = true;
		} else {
			this.player_2.set(player_2);
			this.player_2Null = false;
		}
	}
	
	public boolean isPlayer2Null() {
		return this.player_2Null;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.setLiteralStatus(status);
	}
	
	public StringProperty getLiteralStatusProperty() {
		return this.literalStatus;
	}
	
	public String getLiteralStatus() {
		return this.literalStatus.get();
	}
	
	private void setLiteralStatus(String status) {
		if(status.compareTo("0") == 0)
			this.literalStatus.set(App.get_resource_key(STATUS_FINISH));
		else if(status.compareTo("1") == 0)
			this.literalStatus.set(App.get_resource_key(STATUS_PROGRESS));
		else if(status.compareTo("2") == 0)
			this.literalStatus.set(App.get_resource_key(STATUS_WAITING));
		else if(status.compareTo("3") == 0)
			this.literalStatus.set(App.get_resource_key(STATUS_VALIDATION));
		else if(status.compareTo("4") == 0)
			this.literalStatus.set(App.get_resource_key(STATUS_NEW));
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public StringProperty getSeedProperty() {
		return this.seed;
	}
	
	public String getSeed() {
		return this.seed.get();
	}
	
	public void setSeed(String seed) {
		if(seed == null || seed.isEmpty() || seed.isBlank())
			this.seed.set(App.get_resource_key(UNDEFINED));
		else
			this.seed.set(seed);
	}

	public int getMatch_id() {
		return this.match_id;
	}

	public void setMatch_id(int match_id) {
		this.match_id = match_id;
	}

	
}
