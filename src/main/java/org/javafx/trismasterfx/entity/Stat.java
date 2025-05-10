package org.javafx.trismasterfx.entity;

public class Stat {
	private String player_1 = null, player_2 = null, result = null;
	
	public Stat(String player_1, String player_2, String result) {
		this.setPlayer_1(player_1);
		this.setPlayer_2(player_2);
		this.setResult(result);
	}

	public String getPlayer_1() {
		return this.player_1;
	}

	public void setPlayer_1(String player_1) {
		this.player_1 = player_1;
	}

	public String getPlayer_2() {
		return this.player_2;
	}

	public void setPlayer_2(String player_2) {
		this.player_2 = player_2;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return String.format("Stat [player_1 = %s, player_2 = %s, result = %s]", this.getPlayer_1(), this.getPlayer_2(), this.getResult());
	}
}
