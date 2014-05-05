package edu.nyu.smg.ninetyNine.client;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class AIMove {

	private PokerState pokerState;

	public AIMove(PokerState pokerState) {
		this.pokerState = pokerState;
	}

	public List<Integer> getBestMove() {
		List<Integer> currentHand = pokerState.getWhiteOrBlack(pokerState
				.getTurn());
		List<Integer> currentOppoHand = pokerState.getWhiteOrBlack(pokerState
				.getTurn().getOppositeColor());
		List<Optional<Card>> cards = pokerState.getCards();
		List<Card> myCards = new ArrayList<Card>();
		List<Rank> myRanks = new ArrayList<Rank>();
		Integer point = pokerState.getPoints();
		for (int i = 0; i < currentHand.size(); i++) {
			Card card = cards.get(currentHand.get(i)).get();
			myCards.add(card);
			myRanks.add(card.getCardRank());
		}
		Collections.sort(myRanks, Rank.ACE_LOW_COMPARATOR);
		/*
		 * The best strategy is to get card from opponent
		 */
		if (currentOppoHand.size() == 1) {
			for (Card card : myCards) {
				if (card.getCardRank() == Rank.JACK) {
					return ImmutableList.<Integer> of(cards.indexOf(Optional
							.of(card)));
				}
			}
		}

		if (myRanks.contains(Rank.KING)) {
			for (Card card : myCards) {
				if (card.getCardRank() == Rank.KING) {
					return ImmutableList.<Integer> of(cards.indexOf(Optional
							.of(card)));
				}
			}
		}

		/*
		 * To exchange cards with opponent or draw card from opponent will give
		 * us advantage here
		 */
		if (currentOppoHand.size() >= currentHand.size()) {
			for (Card card : myCards) {
				if (card.getCardRank() == Rank.SEVEN) {
					return ImmutableList.<Integer> of(cards.indexOf(Optional
							.of(card)));
				}
			}
		}

		if (myRanks.contains(Rank.JACK)) {
			for (Card card : myCards) {
				if (card.getCardRank() == Rank.JACK) {
					return ImmutableList.<Integer> of(cards.indexOf(Optional
							.of(card)));
				}
			}
		}

		if (point <= 50) {
			for (int j = myRanks.size() - 1; j >= 0; j--) {
				if (myRanks.get(j) == Rank.FOUR && j >= 1) {
					continue;
				} else {
					return ImmutableList.<Integer> of(cards.indexOf(Optional
							.<Card> of(myCards.get(j))));
				}
			}
		} else {
			int j = 0;
			while (j < myRanks.size() - 1 && myRanks.get(j) == Rank.FOUR) {
				j++;
				continue;
			}
			Rank lowestRank = myRanks.get(j);
			int newPoint = point + lowestRank.getNumberValue();
			if (newPoint <= 99) {
				return ImmutableList.<Integer> of(cards.indexOf(Optional
						.<Card> of(myCards.get(j))));
			} else {
				if (myRanks.contains(Rank.FOUR)) {
					for (Card card : myCards) {
						if (card.getCardRank() == Rank.FOUR) {
							return ImmutableList.<Integer> of(cards
									.indexOf(Optional.of(card)));
						}
					}
				}
			}
		}

		return currentHand.subList(0, 1);
	}

}
