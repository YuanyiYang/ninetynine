package edu.nyu.smg.pokerGame;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.nyu.smg.pokerGame.GameApi.*;

public class GameLogic {

	/*
	 * The entries used in the poker game are: turn:W/B, W, B, Used, Unused,
	 * C0...C51, points, isSub:false, direction:clockwise When we send
	 * operations on these keys, it will always be in the above order.
	 */

	private final String TURN = "turn"; // turn of which player (either W or B)
	private static final String W = "W"; // White hand
	private static final String B = "B"; // Black hand
	private static final String USED = "USED"; // Used pile
	private static final String UNUSED = "UNUSED"; // Unused pile
	private static final String C = "C"; // Card key (C1 .. C52)
	private static final String P = "Points"; // the total points of the game
	private static final String IS_SUB = "isSub";
	private static final String IS_GAMEOVER = "isGameOver";
	private static final String DIRECTION = "direction";
	private static final String YES = "yes";
	private static final String CLOCKWISE = "Clockwise";
	private static final String ANTICLOCKWISE = "AntiClockwise";

	public VerifyMoveDone verify(VerifyMove verifyMove) {
		// TODO: I will implement this method in HW2
		return new VerifyMoveDone();
	}

	void checkMoveIsLegal(VerifyMove verfyMove) {
		List<Operation> lastMove = verfyMove.getLastMove();
		Map<String, Object> lastState = verfyMove.getLastState();
	}

	/*
	 * playerIds: id number of players(int)
	 */
	List<Operation> getExpectedOperations(Map<String, Object> lastApiState,
			List<Operation> lastMove, List<Integer> playerIds) {
		if (lastApiState.isEmpty()) {
			return getInitialMove(playerIds.get(0), playerIds.get(1));
		}
		PokerState lastState = gameApiStateToPokerState(lastApiState);
		List<Integer> lastUsed = lastState.getUsed();
		ColorOfPlayer lastTurn = lastState.getTurn();
		// there are many types of move

		return null;
	}

	/*
	 * In this method, the player will play a card. Different methods will be
	 * called according to the type of this card. Here use cardId: C0,,,,C51
	 * And convert the representation into a Integer list
	 */

	public List<Operation> playACard(String cardId, PokerState pokerState) {
		if (cardId == null) {
			return null;
		}
		String str = cardId.substring(0, 1).toUpperCase();
		if (!"C".equals(str)) {
			throw new IllegalArgumentException(
					"The first letter is not C. It's not a card");
		}
		List<Integer> cardList = ImmutableList.<Integer>of(Integer.parseInt(cardId.substring(1)));
		return playACard(cardList, pokerState);
	}

	private List<Operation> playACard(List<Integer> cardId, PokerState pokerState) {
		// First convert the INT id(0...51) into a string like "1c", "ks"
		int cardIdInInt = cardId.get(0);
		String strCardId = cardIdToString(cardIdInInt);
		// change the Id into rank
		Rank cardRank = Rank.fromFirstLetter(strCardId.substring(0, 1)
				.toUpperCase());
		switch (cardRank) {
		case FOUR: {
			return playAReverseCard(pokerState, cardId);
		}
		case SEVEN: {
			return playAExchangeCard(pokerState, cardId);
		}
		case TEN: {
			return playAPlusOrMinusTenOperation(pokerState, cardId);
		}
		case JACK: {
			return playADrawOtherCard(pokerState, cardId);
		}
		case QUEEN: {
			return playAPlusOrMinusTwentyOperation(pokerState, cardId);
		}
		case KING: {
			return playAddedToMaxCard(pokerState, cardId);
		}
		default:
			return playANormalCard(pokerState, cardId);

		}
	}

	/*
	 * Here the logic seems like the function above which uses the current state
	 * and last state to get the list of operation. I think the above function
	 * should be used when to verify move. Thus I should implement several
	 * method to perform the operations. Use the number to generate operations,
	 * have nothing to do with the state
	 */
	public List<Operation> playANormalCard(PokerState pokerState,
			List<Integer> cardList) {
		return null;
	}

	public List<Operation> playAReverseCard(PokerState pokerState,
			List<Integer> cardList) {
		// check the current player and the current direction
		return null;
	}

	public List<Operation> playAExchangeCard(PokerState pokerState,
			List<Integer> cardList) {
		return null;
	}

	public List<Operation> playAPlusOrMinusTenOperation(PokerState pokerState,
			List<Integer> cardList) {
		return null;
	}

	public List<Operation> playAPlusOrMinusTwentyOperation(
			PokerState pokerState, List<Integer> cardList) {
		return null;
	}

	public List<Operation> playADrawOtherCard(PokerState pokerState,
			List<Integer> cardList) {
		// apparently the card drawn from other now is given randomly
		return null;
	}

	public List<Operation> playAddedToMaxCard(PokerState pokerState,
			List<Integer> cardList) {
		return null;
	}

	List<Operation> getInitialMove(int whitePlayerId, int blackPlayerId) {
		List<Operation> operations = Lists.newArrayList();
		// The order of operations: turn, W, B, Used, UnUsed , C0...C51, Points,
		// isSub, direction
		operations.add(new Set(TURN, W));
		// set W and B hands
		operations.add(new Set(W, getIndicesInRange(0, 4)));
		operations.add(new Set(B, getIndicesInRange(5, 9)));
		// middle pile is empty
		operations.add(new Set(USED, ImmutableList.of()));
		operations.add(new Set(UNUSED, getIndicesInRange(10, 51)));
		// sets all 52 cards: set(C0,1c), set(C1,1d), ... , set(C51,Ks)
		for (int i = 0; i < 52; i++) {
			operations.add(new Set(C + i, cardIdToString(i)));
		}
		// shuffle(C0,...,C51)
		operations.add(new Shuffle(getCardsInRange(0, 51)));
		// sets visibility
		for (int i = 0; i < 5; i++) {
			operations.add(new SetVisibility(C + i, ImmutableList
					.of(whitePlayerId)));
		}
		for (int i = 5; i < 10; i++) {
			operations.add(new SetVisibility(C + i, ImmutableList
					.of(blackPlayerId)));
		}
		for (int i = 10; i < 52; i++) {
			operations.add(new SetVisibility(C + i, null)); // to question
		}
		operations.add(new Set(P, 0));
		// operations.add(new Set(IS_SUB, FALSE)); // we don't need to do this
		// action now
		// If the gameApiState don't contain this key, it's false
		// operations.add(new Set(HAS_WINNER, FALSE));
		operations.add(new Set(DIRECTION, CLOCKWISE)); // the default is the
														// clockwise
		return operations;
	}

	@SuppressWarnings("unchecked")
	private PokerState gameApiStateToPokerState(Map<String, Object> gameApiState) {
		List<Optional<Card>> cards = Lists.newArrayList();
		for (int i = 0; i < 52; i++) {
			String cardString = (String) gameApiState.get(C + i);
			Card card;
			if (cardString == null) {
				card = null;
			} else {
				Rank rank = Rank.fromFirstLetter(cardString.substring(0, 1)
						.toUpperCase());
				Suit suit = Suit.fromFirstLetterLowerCase(cardString.substring(
						1).toLowerCase());
				card = new Card(rank, suit);
			}
			cards.add(Optional.fromNullable(card));
		}
		List<Integer> white = (List<Integer>) gameApiState.get(W);
		List<Integer> black = (List<Integer>) gameApiState.get(B);
		List<Integer> used = (List<Integer>) gameApiState.get(USED);
		List<Integer> unused = (List<Integer>) gameApiState.get(UNUSED);

		return new PokerState(
				ColorOfPlayer.valueOf((String) gameApiState.get(TURN)),
				ImmutableList.copyOf(cards),
				ImmutableList.copyOf(white),
				ImmutableList.copyOf(black),
				ImmutableList.copyOf(used),
				ImmutableList.copyOf(unused),
				gameApiState.containsKey(IS_SUB),
				(Integer) gameApiState.get(P),
				(DirectionsOfTurn.valueOf((String) gameApiState.get(DIRECTION))),
				gameApiState.containsKey(IS_GAMEOVER));
	}

	List<String> getCardsInRange(int fromInclusive, int toInclusive) {
		List<String> cards = Lists.newArrayList();
		for (int i = fromInclusive; i <= toInclusive; i++) {
			cards.add(C + i);
		}
		return cards;
	}

	List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		List<Integer> keys = Lists.newArrayList();
		for (int i = fromInclusive; i <= toInclusive; i++) {
			keys.add(i);
		}
		return keys;
	}

	List<String> getCard(int cardNumber) {
		List<String> card = Lists.newArrayList();
		card.add(C + cardNumber);
		return card;
	}

	List<Integer> getCardIndex(int cardNumber) {
		List<Integer> cardIndex = Lists.newArrayList();
		cardIndex.add(cardNumber);
		return cardIndex;
	}

	<T> List<T> concat(List<T> a, List<T> b) {
		return Lists.newArrayList(Iterables.concat(a, b));
	}

	String cardIdToString(int cardID) {
		checkArgument(cardID >= 0 && cardID < 52);
		int rank = (cardID / 4);
		String rankString = Rank.VALUES[rank].getFirstLetter();
		int suit = cardID % 4;
		String suitString = Suit.values()[suit].getFirstLetterLowerCase();
		return rankString + suitString;
	}

	<T> List<T> subtract(List<T> removeFrom, List<T> elementsToRemove) {
		check(removeFrom.containsAll(elementsToRemove), removeFrom,
				elementsToRemove);
		List<T> result = Lists.newArrayList(removeFrom);
		result.removeAll(elementsToRemove);
		check(removeFrom.size() == result.size() + elementsToRemove.size());
		return result;
	}

	private void check(boolean val, Object... debugArguments) {
		if (!val) {
			throw new RuntimeException("We have a hacker! debugArguments="
					+ Arrays.toString(debugArguments));
		}
	}
}
