/** Rank source file given in the lecture. Use the ACE_LOW_COMPARATOR since ACE don't have
 * 	special meaning in the game.
 */

package edu.nyu.smg.pokerGame;

import java.util.Comparator;

public enum Rank {

	ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

	public static final Rank[] VALUES = values();

	public static final Comparator<Rank> ACE_LOW_COMPARATOR = new Comparator<Rank>() {
		@Override
		public int compare(Rank o1, Rank o2) {
			int ord1 = o1.ordinal();
			int ord2 = o2.ordinal();
			return ord1 - ord2;
		}
	};

	public Rank getNext() {
		if (this == VALUES[VALUES.length - 1])
			return VALUES[0];
		return values()[ordinal() + 1];
	}

	public Rank getPrev() {
		if (this == VALUES[0])
			return VALUES[VALUES.length - 1];
		return values()[ordinal() - 1];
	}
	
	public int getNumberValue(){
		return this.ordinal()+1;
	}
	
}
