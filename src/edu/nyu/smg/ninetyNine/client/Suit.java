/**
 * Suit class given in the lecture. 
 */
package edu.nyu.smg.ninetyNine.client;

import java.util.Comparator;


public enum Suit {
	CLUBS, DIAMONDS, HEARTS, SPADES;

	public static final Suit[] VALUES = values();

	public static final Comparator<Suit> DDZ_COMPARATOR = new Comparator<Suit>() {
		@Override
		public int compare(Suit o1, Suit o2) {
			return 0;
		}
	};

	public String getFirstLetterLowerCase() {
		return name().substring(0, 1).toLowerCase();
	}

	public static Suit fromFirstLetterLowerCase(String firstLetterLowerCase) {
		for (Suit suit : VALUES) {
			if (suit.getFirstLetterLowerCase().equals(firstLetterLowerCase)) {
				return suit;
			}
		}
		throw new IllegalArgumentException("Did not find firstLetterLowerCase="
				+ firstLetterLowerCase);
	}

	public Suit getNext() {
		if (this == VALUES[VALUES.length - 1]) {
			return VALUES[0];
		}
		return values()[ordinal() + 1];
	}

	public Suit getPrev() {
		if (this == VALUES[0]) {
			return VALUES[VALUES.length - 1];
		}
		return values()[ordinal() - 1];
	}
}
