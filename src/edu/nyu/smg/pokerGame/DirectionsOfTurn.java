/**
 * Determines the direction of the games
 * @author yuanyiyang
 *
 */

package edu.nyu.smg.pokerGame;

public enum DirectionsOfTurn {
	Clockwise, AntiClockwise;

	public boolean isClockwise() {
		return this == Clockwise;
	}

	public boolean isAntiClockwise() {
		return this == AntiClockwise;
	}

	public DirectionsOfTurn getOppositeDirection() {
		return this == Clockwise ? AntiClockwise : Clockwise;
	}
}
