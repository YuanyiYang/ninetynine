package edu.nyu.smg.ninetyNine.graphics;

import java.util.Arrays;

import edu.nyu.smg.ninetyNine.client.Card;
import edu.nyu.smg.ninetyNine.client.Equality;
import edu.nyu.smg.ninetyNine.client.Rank;
import edu.nyu.smg.ninetyNine.client.Suit;

/**
 * A representation of a card image.
 */
public class CardImage extends Equality {

	enum CardImageKind {
		JOKER, BACK, EMPTY, NORMAL, IS_RANK;
	}

	public static class Factory {
		public static CardImage getBackOfCardImage() {
			return new CardImage(CardImageKind.BACK, null, null);
		}

		public static CardImage getJoker() {
			return new CardImage(CardImageKind.JOKER, null, null);
		}

		public static CardImage getEmpty() {
			return new CardImage(CardImageKind.EMPTY, null, null);
		}

		public static CardImage getRankImage(Rank rank) {
			return new CardImage(CardImageKind.IS_RANK, rank, null);
		}

		public static CardImage getCardImage(Card card) {
			return new CardImage(CardImageKind.NORMAL, null, card);
		}

	}

	public final CardImageKind kind;
	public final Rank rank;
	public final Card card;

	private CardImage(CardImageKind kind, Rank rank, Card card) {
		this.kind = kind;
		this.rank = rank;
		this.card = card;
	}

	@Override
	public Object getId() {
		return Arrays.asList(kind, rank, card);
	}

	private String rank2str(Rank rank) {
		int rankValue = rank.ordinal() + 1;
		return rankValue <= 9 ? String.valueOf(rankValue)
				: rankValue == 10 ? "T" : rankValue == 11 ? "J"
						: rankValue == 12 ? "Q" : rankValue == 13 ? "K" : "ERR";
	}

	private String card2str(Rank rank, Suit suit) {
		return rank2str(rank).toLowerCase()
				+ (suit == Suit.CLUBS ? "c" : suit == Suit.DIAMONDS ? "d"
						: suit == Suit.HEARTS ? "h" : suit == Suit.SPADES ? "s"
								: "ERR");
	}

	@Override
	public String toString(){
		switch(kind){
		case JOKER: 
			return "cards/joker.gif";
		case BACK:
			return "cards/b.gif";
		case EMPTY:
	        return "cards/empty.gif";
		case IS_RANK:
			return "cards/Is" + rank2str(rank) + ".gif";
		case NORMAL:
			return "cards/" + card2str(card.getCardRank(), card.getCardSuit()) + ".gif";
	      default:
	        return "Forgot kind=" + kind;
		}
	}
}
