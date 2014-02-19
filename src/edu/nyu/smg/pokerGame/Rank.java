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

	public int getNumberValue() {
		return this.ordinal() + 1;
	}

	/*
	 * Map ACE to 1, TWO to 2, THREE to 3.....TEN to 10, JACK to J, QUEEN to Q,
	 * KING to K
	 */
	public String getFirstLetter() {
		int rank = this.ordinal() + 1;
		return rank <= 9 ? String.valueOf(rank) : rank == 10 ? "T"
				: rank == 11 ? "J" : rank == 12 ? "Q" : "K";
	}

	public static Rank fromFirstLetter(String rankString) {
		int rankIndex = rankString.equals("T") ? 10
				: rankString.equals("J") ? 11 : rankString.equals("Q") ? 12
						: rankString.equals("K") ? 13 : Integer
								.valueOf(rankString);
		return VALUES[rankIndex - 1];
	}

}
