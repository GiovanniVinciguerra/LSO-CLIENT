package org.javafx.trismasterfx.entity;

import java.util.Objects;
import org.javafx.trismasterfx.App;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Match {
	private class MatchSteps {
		private char[][] steps = new char[3][3];
		private int lastRow = 0, lastCol = 0;
		private double startX = 0, startY = 0, endX = 0, endY = 0; /* Permette di ottenere le coordinate delle celle vincenti */
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
		
		public String getStepAsText(int row, int col, char value) {
			int index = ((row * 3) + col) + 1;
			return new String(index + "" + value);
		}
		
		public double getStartX() {
			return this.startX;
		}
		
		public double getStartY() {
			return this.startY;
		}
		
		public double getEndX() {
			return this.endX;
		}
		
		public double getEndY() {
			return this.endY;
		}
		
		private void calcXY(int minRow, int maxRow, int minCol, int maxCol) {
			this.startX = (double) minCol * 100 + 100 / 2;
			this.startY = (double) minRow * 100 + 100 / 2;
			this.endX = (double) maxCol * 100 + 100 / 2;
			this.endY = (double) maxRow * 100 + 100 / 2;
		}
		
		public boolean isWinner() {
			/* Scorro sulla riga */
			if(steps[lastRow][lastCol] == steps[lastRow][(lastCol + 1) % 3] && steps[lastRow][lastCol] == steps[lastRow][(lastCol + 2) % 3]) {
				/* Calcolo le coordinate delle celle vincitrici */
				int minRow = this.lastRow;
				int minCol = Math.min(this.lastCol, Math.min((this.lastCol + 1) % 3, (this.lastCol + 2) % 3));
				int maxRow = this.lastRow;
				int maxCol = Math.max(this.lastCol, Math.max((this.lastCol + 1) % 3, (this.lastCol + 2) % 3));
				this.calcXY(minRow, maxRow, minCol, maxCol);
				return true;
			}
			/* Scorro sulla colonna */
			else if(steps[lastRow][lastCol] == steps[(lastRow + 1) % 3][lastCol] && steps[lastRow][lastCol] == steps[(lastRow + 2) % 3][lastCol]) {
				int minRow = Math.min(this.lastRow, Math.min((this.lastRow + 1) % 3, (this.lastRow + 2) % 3));
				int minCol = this.lastCol;
				int maxRow = Math.max(this.lastRow, Math.max((this.lastRow + 1) % 3, (this.lastRow + 2) % 3));
				int maxCol = this.lastCol;
				this.calcXY(minRow, maxRow, minCol, maxCol);
				return true;
			}
			/* Scorre la diagonale principale */
			if(lastRow == lastCol) {
				if(steps[lastRow][lastCol] == steps[(lastRow + 1) % 3][(lastCol + 1) % 3] && steps[lastRow][lastCol] == steps[(lastRow + 2) % 3][(lastCol + 2) % 3]) {
					int minRow = Math.min(this.lastRow, Math.min((this.lastRow + 1) % 3, (this.lastRow + 2) % 3));
					int minCol = Math.min(this.lastCol, Math.min((this.lastCol + 1) % 3, (this.lastCol + 2) % 3));
					int maxRow = Math.max(this.lastRow, Math.max((this.lastRow + 1) % 3, (this.lastRow + 2) % 3));
					int maxCol = Math.max(this.lastCol, Math.max((this.lastCol + 1) % 3, (this.lastCol + 2) % 3));
					this.calcXY(minRow, maxRow, minCol, maxCol);
					return true;
				}
			}
			/* Scorre la diagonale secondaria */
			else if((lastRow + lastCol) == 2) {
				if(steps[lastRow][lastCol] == steps[(lastRow + 1) % 3][(lastCol + 2) % 3] && steps[lastRow][lastCol] == steps[(lastRow + 2) % 3][(lastCol + 1) % 3]) {
					int minRow = Math.min(this.lastRow, Math.min((this.lastRow + 1) % 3, (this.lastRow + 2) % 3));
					int minCol = Math.max(this.lastCol, Math.max((this.lastCol + 1) % 3, (this.lastCol + 2) % 3));
					int maxRow = Math.max(this.lastRow, Math.max((this.lastRow + 1) % 3, (this.lastRow + 2) % 3));
					int maxCol = Math.min(this.lastCol, Math.min((this.lastCol + 1) % 3, (this.lastCol + 2) % 3));
					this.calcXY(minRow, maxRow, minCol, maxCol);
					return true;
				}
			}
			
			return false;
		}
		
		public void translate(String step)  {
			int index = Character.getNumericValue(step.charAt(0));
			int row = Math.ceilDiv(index, 3) - 1;
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
	
	public double getStartX() {
		return this.steps.getStartX();
	}
	
	public double getStartY() {
		return this.steps.getStartY();
	}
	
	public double getEndX() {
		return this.steps.getEndX();
	}
	
	public double getEndY() {
		return this.steps.getEndY();
	}
	
	public void translate(String step) {
		this.steps.translate(step);
	}
	
	public int getStepSize() {
		return this.steps.getStepSize();
	}
	
	public String getStepAsText(int row, int col, char value) {
		return this.steps.getStepAsText(row, col, value);
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
	
	

	@Override
	public int hashCode() {
		return Objects.hash(match_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Match other = (Match) obj;
		return this.match_id == other.match_id;
	}

	@Override
	public String toString() {
		return String.format("Match [player_1=%s, player_2=%s, literalStatus=%s, seed=%s, result=%s, match_id=%s]",
				player_1, player_2, literalStatus, seed, result, match_id);
	}
}
