package edu.nyu.smg.ninetyNine.graphics;

import com.google.gwt.resources.client.ImageResource;

import edu.nyu.smg.ninetyNine.client.Card;
import edu.nyu.smg.ninetyNine.client.Rank;

/**
 * A mapping from Card to its ImageResource. The images are all of size 73x97
 * (width x height).
 */
public class CardImageSupplier {
	private final CardImages cardImages;

	public CardImageSupplier(CardImages cardImages) {
		this.cardImages = cardImages;
	}

	public ImageResource getBackOfCardImage() {
		return cardImages.b();
	}

	public ImageResource getJoker() {
		return cardImages.joker();
	}

	public ImageResource getEmpty() {
		return cardImages.empty();
	}

	public ImageResource getResource(CardImage cardImage){
		switch (cardImage.kind) {
		case BACK:
			return getBackOfCardImage();
		case JOKER:
			return getJoker();
		case EMPTY:
			return getEmpty();
		case IS_RANK:
			return getRankImage(cardImage.rank);
		case NORMAL:
			return getCardImage(cardImage.card);
		default:
			throw new RuntimeException("Forgot kind=" + cardImage.kind);
		}
	}
	
	public ImageResource getRankImage(Rank rank) {
		switch (rank) {
		case ACE:
			return cardImages.rankA();
		case TWO:
			return cardImages.rank2();
		case THREE:
			return cardImages.rank3();
		case FOUR:
			return cardImages.rank4();
		case FIVE:
			return cardImages.rank5();
		case SIX:
			return cardImages.rank6();
		case SEVEN:
			return cardImages.rank7();
		case EIGHT:
			return cardImages.rank8();
		case NINE:
			return cardImages.rank9();
		case TEN:
			return cardImages.rank10();
		case JACK:
			return cardImages.rankJ();
		case QUEEN:
			return cardImages.rankQ();
		case KING:
			return cardImages.rankK();
		default:
			throw new RuntimeException("Forgot rank=" + rank);
		}
	}
	
	public ImageResource getCardImage(Card card){
		switch (card.getCardRank()) {
		case ACE:
			switch (card.getCardSuit()) {
			case CLUBS: return cardImages.ac();
			case DIAMONDS: return cardImages.ad();
	        case HEARTS: return cardImages.ah();
	        case SPADES: return cardImages.as();
	        default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
			}
		case TWO:
			 switch (card.getCardSuit()) {
	          case CLUBS: return cardImages.c2();
	          case DIAMONDS: return cardImages.d2();
	          case HEARTS: return cardImages.h2();
	          case SPADES: return cardImages.s2();
	          default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case THREE:
	        switch (card.getCardSuit()) {
	          case CLUBS: return cardImages.c3();
	          case DIAMONDS: return cardImages.d3();
	          case HEARTS: return cardImages.h3();
	          case SPADES: return cardImages.s3();
	          default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case FOUR:
	        switch (card.getCardSuit()) {
	          case CLUBS: return cardImages.c4();
	          case DIAMONDS: return cardImages.d4();
	          case HEARTS: return cardImages.h4();
	          case SPADES: return cardImages.s4();
	          default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case FIVE:
	        switch (card.getCardSuit()) {
	          case CLUBS: return cardImages.c5();
	          case DIAMONDS: return cardImages.d5();
	          case HEARTS: return cardImages.h5();
	          case SPADES: return cardImages.s5();
	          default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case SIX:
	        switch (card.getCardSuit()) {
	          case CLUBS: return cardImages.c6();
	          case DIAMONDS: return cardImages.d6();
	          case HEARTS: return cardImages.h6();
	          case SPADES: return cardImages.s6();
	          default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case SEVEN:
	        switch (card.getCardSuit()) {
	          case CLUBS: return cardImages.c7();
	          case DIAMONDS: return cardImages.d7();
	          case HEARTS: return cardImages.h7();
	          case SPADES: return cardImages.s7();
	          default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case EIGHT:
	        switch (card.getCardSuit()) {
	          case CLUBS: return cardImages.c8();
	          case DIAMONDS: return cardImages.d8();
	          case HEARTS: return cardImages.h8();
	          case SPADES: return cardImages.s8();
	          default: throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case NINE:
	        switch (card.getCardSuit()) {
	          case CLUBS:
	            return cardImages.c9();
	          case DIAMONDS:
	            return cardImages.d9();
	          case HEARTS:
	            return cardImages.h9();
	          case SPADES:
	            return cardImages.s9();
	          default:
	            throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case TEN:
	        switch (card.getCardSuit()) {
	          case CLUBS:
	            return cardImages.tc();
	          case DIAMONDS:
	            return cardImages.td();
	          case HEARTS:
	            return cardImages.th();
	          case SPADES:
	            return cardImages.ts();
	          default:
	            throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case JACK:
	        switch (card.getCardSuit()) {
	          case CLUBS:
	            return cardImages.jc();
	          case DIAMONDS:
	            return cardImages.jd();
	          case HEARTS:
	            return cardImages.jh();
	          case SPADES:
	            return cardImages.js();
	          default:
	            throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case QUEEN:
	        switch (card.getCardSuit()) {
	          case CLUBS:
	            return cardImages.qc();
	          case DIAMONDS:
	            return cardImages.qd();
	          case HEARTS:
	            return cardImages.qh();
	          case SPADES:
	            return cardImages.qs();
	          default:
	            throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      case KING:
	        switch (card.getCardSuit()) {
	          case CLUBS:
	            return cardImages.kc();
	          case DIAMONDS:
	            return cardImages.kd();
	          case HEARTS:
	            return cardImages.kh();
	          case SPADES:
	            return cardImages.ks();
	          default:
	            throw new RuntimeException("Forgot suit=" + card.getCardSuit());
	        }
	      default:
	        throw new RuntimeException("Forgot rank=" + card.getCardRank());
		}
	}
	
}
