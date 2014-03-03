package edu.nyu.smg.ninetyNine.client;

import java.util.Arrays;
import java.util.Comparator;

public class Card extends Equality implements Comparable<Card> {

	private Rank cardRank;
	private Suit cardSuit;

	protected Card() {
		cardRank = null;
		cardSuit = null;
	}

	public Card(Rank cardRank, Suit cardSuit) {
		this.cardRank = cardRank;
		this.cardSuit = cardSuit;
	}
	
	/**
	 * @return a string representing the information of the card. The format of the string is 
	 * subject to change. You should consider it carefully if your implementation is based on the
	 * format of the string.
	 */
	@Override
	public String toString(){
		return "This card is " + cardRank + " in " + cardSuit ;
	}

	public Rank getCardRank() {
		return cardRank;
	}

	public void setCardRank(Rank cardRank) {
		this.cardRank = cardRank;
	}


	public Suit getCardSuit() {
		return cardSuit;
	}

	public void setCardSuit(Suit cardSuit) {
		this.cardSuit = cardSuit;
	}

	public static final Comparator<Card> COMPARATOR = new Comparator<Card>() {

		@Override
		public int compare(Card o1, Card o2) {
			int rank = o1.cardRank.compareTo(o2.cardRank);
			int suit = o1.cardSuit.compareTo(o2.cardSuit);
			return rank == 0 ? suit : rank;
		}
	};
	
	@Override
	public int compareTo(Card o) {
		return COMPARATOR.compare(this, o);
	}

	@Override
	public Object getId() {
		return Arrays.asList(getCardRank(),getCardSuit());
	}
	
}
