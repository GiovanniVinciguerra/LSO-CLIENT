package com.maven.trismaster.entity;

public class Step {
	int index = -1;
	char seed = '\0';
	
	public Step(int index, char seed) {
		this.setIndex(index);
		this.setSeed(seed);
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public char getSeed() {
		return this.seed;
	}

	public void setSeed(char seed) {
		this.seed = seed;
	}

	@Override
	public String toString() {
		return String.format("Step [index = %s, seed = %s]", this.getIndex(), this.getSeed());
	}
}
