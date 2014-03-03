package edu.nyu.smg.ninetyNine.client;

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
	
	public static void main(String[] args) {
		for(ColorOfPlayer c : ColorOfPlayer.values()){
			System.out.println(c.name());
		}
	}
}
