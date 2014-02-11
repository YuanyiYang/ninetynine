package edu.nyu.smg.pokerGame;

import com.google.common.base.*;
public class Card {

	private Rank cardRank;
	private CardType cardType;
	private Suit cardSuit;
	private boolean isMinus;

	protected Card() {
		cardRank = null;
		cardType = null;
		cardSuit = null;
		isMinus = false;
	}

	public Card(Rank cardRank, Suit cardSuit) {
		this.cardRank = cardRank;
		this.cardSuit = cardSuit;
		this.cardType = null;
		this.isMinus = false;
	}

	public Card(Rank cardRank, Suit cardSuit, boolean isMinus) {
		this(cardRank, cardSuit);
		this.isMinus = isMinus;
	}


	@Override
	public boolean equals(Object obj){
		if(obj==null){
			return false;
		}else if(!(obj instanceof Card)){
			return false;
		}else{
			return Objects.equal(this.cardRank, ((Card)obj).getCardRank()) &&
					Objects.equal(this.cardSuit, ((Card)obj).getCardSuit());
		}
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(this.cardRank,this.cardSuit);
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

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public Suit getCardSuit() {
		return cardSuit;
	}

	public void setCardSuit(Suit cardSuit) {
		this.cardSuit = cardSuit;
	}

	public boolean isMinus() {
		return isMinus;
	}

	public void setMinus(boolean isMinus) {
		this.isMinus = isMinus;
	}
	
}
