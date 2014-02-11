package edu.nyu.smg.pokerGame;

public enum ColorOfPlayer {

	W, B;

	public boolean isWhite() {
		return this == W;
	}

	public boolean isBlack() {
		return this == B;
	}

	public ColorOfPlayer getOppositeColor() {
		return this == W ? B : W;
	}
}
