package edu.nyu.smg.pokerGame;

import static com.google.common.base.Preconditions.checkArgument;

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
	 * How to handle the Is_Sub situation? Here is my approach. First use a Set
	 * operation to set the is_sub field in the state to be true. It represents
	 * the next move is subtract ten or twenty. After the subtraction, use a
	 * Delete operation to set the is_sub date field to be false(default).
	 */

	/*
	 * The entries used in the poker game are: turn:W/B, W, B, Used, Unused,
	 * points, C0...C51, direction:clockwise, isSub:false, isGameOver:false
	 */

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

	public VerifyMoveDone verify(VerifyMove verifyMove) {
		try {
			checkMoveIsLegal(verifyMove);
			return new VerifyMoveDone();
		} catch (Exception e) {
			return new VerifyMoveDone(verifyMove.getLastMovePlayerId(),
					e.getMessage());
		}
	}

	void checkMoveIsLegal(VerifyMove verifyMove) {
		List<Operation> lastMove = verifyMove.getLastMove();
		Map<String, Object> lastState = verifyMove.getLastState();
		// Map<String, Object> currentState = verifyMove.getState();
		List<Operation> expectedOperations = getExpectedOperations(verifyMove);
		// List<Operation> expectedOperations = getExpectedOperations(lastState,
		// currentState, lastMove, verifyMove.getPlayerIds(),
		// verifyMove.getLastMovePlayerId());
		check(expectedOperations.equals(lastMove), expectedOperations, lastMove);
		// We use SetTurn, so we don't need to check that the correct player did
		// the move. However, we do need to check the first move is done by the
		// white player (and then in the first MakeMove we'll send SetTurn which
		// will guarantee the correct player send MakeMove).
		if (lastState.isEmpty()) {
			check(verifyMove.getLastMovePlayerId() == verifyMove.getPlayerIds()
					.get(0));
		}
	}

	/*
	 * This function should be used when try to verify opponent's lastMove. In
	 * the parameter, we pass in current state(which is not valid) to find out
	 * the actual value of the card which opponent's played in the last move.
	 * Parameters used in the past: Map<String, Object> lastApiState,
	 * Map<String, Object> currentApiState, List<Operation> lastMove,
	 * List<Integer> playerIds, int lastMovePlayerId
	 */
	@SuppressWarnings("unchecked")
	List<Operation> getExpectedOperations(VerifyMove verifyMove) {
		List<Operation> lastMove = verifyMove.getLastMove();
		Map<String, Object> lastApiState = verifyMove.getLastState();
		Map<String, Object> currentApiState = verifyMove.getState();
		List<Integer> playerIds = verifyMove.getPlayerIds();
		int lastMovePlayerId = verifyMove.getLastMovePlayerId();
		if (lastApiState.isEmpty()) {
			return getInitialMove(playerIds);
		}
		ColorOfPlayer lastTurnPlayer = ColorOfPlayer.values()[playerIds
				.indexOf(lastMovePlayerId)];
		if (lastMove.contains(new Set(IS_SUB, YES))) {
			return getNextMoveSub(lastTurnPlayer, playerIds);
		}
		/*
		 * We use last player's Id to make sure that the game state has
		 * information about in the last state, which player has the turn.
		 */
		PokerState lastState = gameApiStateToPokerState(lastApiState,
				lastTurnPlayer, playerIds);
		PokerState currentState = gameApiStateToPokerState(currentApiState,
				lastTurnPlayer.getOppositeColor(), playerIds);
		List<Integer> lastUnUsed = lastState.getUnused();
		List<Integer> lastUsed = lastState.getUsed();
		if (lastUnUsed.isEmpty()) {
			return getNewRoundOperations(lastState, lastTurnPlayer, playerIds);
		}
		/*
		 * SetTurn, Set(W), Set(B), Set(Used)...
		 */
		Set setUsedPile = (Set) lastMove.get(3);
		List<Integer> newPile = (List<Integer>) setUsedPile.getValue();
		List<Integer> diffUsedPile = subtract(newPile, lastUsed);
		/*
		 * Now we try to get the actual card value played in the last move from
		 * the resulting state(current state). In this state, this card should
		 * be set to be visible to all.
		 */
		Card card = currentState.getCards().get(diffUsedPile.get(0)).get();
		/*
		 * It seems there is no need to find out whether last move contains the
		 * Delete operation or not. The Delete 
		 */
		return getExpectedOperations(diffUsedPile, lastState, playerIds, card);
	}

	List<Operation> getNextMoveSub(ColorOfPlayer lastTurnPlayer,
			List<Integer> playerIds) {
		return ImmutableList.<Operation> of(
				new SetTurn(playerIds.get(lastTurnPlayer.ordinal())), new Set(
						IS_SUB, YES));
	}

	/*
	 * If the unused pile in the last state is empty, this pile has to be reused
	 * and shuffled. These operations only need to change the used pile and
	 * unused pile. Shuffle these cards and change their visibility, making them
	 * invisible to all. Set(used), Set(unused), Shuffle(),
	 * SetVisibility(unused)
	 */
	List<Operation> getNewRoundOperations(PokerState pokerState,
			ColorOfPlayer lastTurnPlayer, List<Integer> playerIds) {
		List<Integer> lastUsed = pokerState.getUsed();
		List<Integer> newUsed = ImmutableList.<Integer> of();
		List<Integer> newUnUsed = lastUsed;
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(playerIds.get(lastTurnPlayer.ordinal())));
		operations.add(new Set(USED, newUsed));
		operations.add(new Set(UNUSED, newUnUsed));
		List<String> unusedNewCards = Lists.newArrayList();
		for (Integer newCardIndex : newUnUsed) {
			unusedNewCards.add(C + newCardIndex);
		}
		operations.add(new Shuffle(unusedNewCards));
		for (Integer newCardIndex : newUnUsed) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of()));
		}
		return ImmutableList.<Operation> copyOf(operations);
	}

	/**
	 * This method will call different methods according to the actual value of
	 * the poker card. It should be called in the following scenario: when the
	 * current player makes a move or the player tries to verify opponent's
	 * move. When the current player tries to make a move, the actual value of
	 * the card can be elicited from lastState(pokerState here). When trying to
	 * verify opponent's move, the actual value of card shall be passed in as a
	 * parameter. The caller of this method should get the actual value of the
	 * card from the current state(personal opinion). This method used to
	 * represent that a player plays a card.
	 * 
	 * @param cardList
	 *            the card played by the player. A list contains only one
	 *            integer representing the card.
	 * @param pokerState
	 *            the last game state, converted from gameApiState, which stored
	 *            in the server and recognized by both player as the valid state
	 * @param playerIds
	 * @param card
	 *            if this field is null, it means that the actual card value can
	 *            be elicited from the last state, which stands for that it is
	 *            the current player tries to make a move. If this field is not
	 *            null, it means that it's the player trying to verify
	 *            opponent's move. The actual value of this card should be given
	 *            by the caller method, which is elicited from the resulting
	 *            state after the opponent's move.
	 * @return desired list of operations
	 */
	public List<Operation> getExpectedOperations(List<Integer> cardList,
			PokerState pokerState, List<Integer> playerIds, Card card) {
		ColorOfPlayer lastTurnColor = pokerState.getTurn();
		if (pokerState.getUnused().isEmpty()) {
			return getNewRoundOperations(pokerState, lastTurnColor, playerIds);
		}
		// First convert the INT id(0...51) into a string like "1c", "ks"
		Integer cardId = cardList.get(0);
		Card currentCard;
		/*
		 * Here since the player can see the card value, so the get() method of
		 * Optional should return the instance; otherwise something goes wrong,
		 * it will throw an illegalStateException
		 */
		if (card == null) {
			currentCard = pokerState.getCards().get(cardId).get();
		} else {
			currentCard = card;
		}
		Rank cardRank = currentCard.getCardRank();
		int cardValue = cardRank.getNumberValue();
		/*
		 * Here what we need to pay attention is that in order to get the card
		 * played from last move, this move must have an uniform operations. So
		 * we define here that the first four operation is SetTurn(),
		 * Set(W)/Set(B), Set(Used). So that in the index 3 position of
		 * operations, we could get the new used pile.
		 */
		switch (cardRank) {
		case FOUR: {
			return playAReverseCard(pokerState, cardList, playerIds);
		}
		case SEVEN: {
			return playAExchangeCard(pokerState, cardList, playerIds);
		}
		case TEN: {
			if (pokerState.isSub()) {
				// Through the last operation, this date field is set to be
				// true, meaning that this move should subtract from the total
				// value
				return playAMinusCard(pokerState, cardList, -10, playerIds);
			} else {
				return playANormalCard(pokerState, cardList, 10, playerIds);
			}
		}
		case JACK: {
			return playADrawOtherCard(pokerState, cardList, playerIds);
		}
		case QUEEN: {
			if (pokerState.isSub()) {
				return playAMinusCard(pokerState, cardList, -20, playerIds);
			} else {
				return playANormalCard(pokerState, cardList, 20, playerIds);
			}
		}
		case KING: {
			return playAddedToMaxCard(pokerState, cardList, playerIds);
		}
		default:
			return playANormalCard(pokerState, cardList, cardValue, playerIds);
		}
	}

	/**
	 * This method will return the expected operations when a player played a
	 * card which only adds up to the total points. The wild card which adds up
	 * to point 99 can be deemed as a value card with value 99. It has to figure
	 * out whether the game will be over or not. If over, set the state and add
	 * the EndGame operation
	 * 
	 * @param pokerState
	 *            the last game state, converted from gameApiState, which stored
	 *            in the server and recognized by both player as the valid state
	 * @param cardList
	 *            the card played by the player. A list contains only one
	 *            integer representing the card
	 * @param cardValue
	 *            if the value is 99, it means that the player has played a wild
	 *            card and the total points should be 99. Otherwise, it should
	 *            be the card value.
	 * @param playerIds
	 * @return the desired operations
	 */
	List<Operation> playANormalCard(PokerState pokerState,
			List<Integer> cardList, int cardValue, List<Integer> playerIds) {
		check(!pokerState.isGameOver());
		check(!pokerState.isSub());
		ColorOfPlayer turnOfColor = pokerState.getTurn();
		/*
		 * play a normal value card. Only add up to the value. Suppose W played
		 * a card here: operations will be: SetTurn(); Set(W)/Set(B), Set(Used),
		 * Set(Unused), Set(P), SetVisiblity(Set the card played visible to all
		 * and new card drawn to the current player), Set(IsGameOver),
		 * EndGame(). No need to set the direction here.
		 */
		List<Integer> lastUsed = pokerState.getUsed();
		List<Integer> lastUnUsed = pokerState.getUnused();
		// return the List<Integer> representing the cards in the current
		// player's hand
		List<Integer> lastHandOfCurrentPlayer = pokerState
				.getWhiteOrBlack(turnOfColor);
		List<Integer> lastHandOfOpponent = pokerState
				.getWhiteOrBlack(turnOfColor.getOppositeColor());
		int lastPoint = pokerState.getPoints();
		// get the new card which will be added to current player. It is the
		// first element in the unused pile
		List<Integer> newCardAddedToCurrentPlayer = lastUnUsed.subList(0, 1);
		List<Integer> newUsed = concat(lastUsed, cardList);
		List<Integer> newUnUsed = subtract(lastUnUsed,
				newCardAddedToCurrentPlayer);
		List<Integer> newHandOfCurrentPlayer = concat(
				subtract(lastHandOfCurrentPlayer, cardList),
				newCardAddedToCurrentPlayer);
		int newPoint = 0;
		if (cardValue == 99) {
			newPoint = 99;
		} else {
			newPoint = lastPoint + cardValue;
		}
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(playerIds.get(turnOfColor.getOppositeColor()
				.ordinal())));
		operations.add(new Set(turnOfColor.name(), newHandOfCurrentPlayer));
		operations.add(new Set(turnOfColor.getOppositeColor().name(),
				lastHandOfOpponent));
		operations.add(new Set(USED, newUsed));
		operations.add(new Set(UNUSED, newUnUsed));
		operations.add(new Set(P, newPoint));
		operations.add(new SetVisibility(C + cardList.get(0)));
		operations.add(new SetVisibility(
				C + newCardAddedToCurrentPlayer.get(0), ImmutableList
						.<Integer> of(playerIds.get(turnOfColor.ordinal()))));
		if (newPoint > 99) {
			operations.add(new Set(IS_GAMEOVER, YES));
			operations.add(new GameApi.EndGame(playerIds.get(turnOfColor
					.getOppositeColor().ordinal())));
		}
		return ImmutableList.<Operation> copyOf(operations);
	}

	/**
	 * The current player played a reverse card. Change the direction of the
	 * game.
	 * 
	 * @param pokerState
	 *            the last game state
	 * @param cardList
	 *            the card played by the player. A list contains only one
	 *            integer representing the card
	 * @param playerIds
	 * @return desired operations
	 */
	List<Operation> playAReverseCard(PokerState pokerState,
			List<Integer> cardList, List<Integer> playerIds) {
		// make sure that the default value of isSub() is false
		check(!pokerState.isGameOver());
		check(!pokerState.isSub());
		ColorOfPlayer turnOfColor = pokerState.getTurn();
		/*
		 * play a change direction card. This card will not lead to an end of
		 * the game. Suppose W played a card here: operations will be:
		 * SetTurn(); Set(W)/Set(B), Set(Used), Set(Unused), SetVisiblity(Set
		 * the card played visible to all and new card drawn to the current
		 * player), set(Direction).
		 */
		List<Integer> lastHandOfPlayer = pokerState
				.getWhiteOrBlack(turnOfColor);
		List<Integer> lastHandOfOpponent = pokerState
				.getWhiteOrBlack(turnOfColor.getOppositeColor());
		List<Integer> lastUsedPile = pokerState.getUsed();
		List<Integer> lastUnUsedPile = pokerState.getUnused();
		DirectionsOfTurn lastDirection = pokerState.getDirection();

		List<Integer> newCardAddedToPlayer = lastUnUsedPile.subList(0, 1);
		List<Integer> newHandOfPlayer = concat(
				subtract(lastHandOfPlayer, cardList), newCardAddedToPlayer);
		List<Integer> newUsedPile = concat(lastUsedPile, cardList);
		List<Integer> newUnUsedPile = subtract(lastUnUsedPile,
				newCardAddedToPlayer);
		DirectionsOfTurn newDirection = lastDirection.getOppositeDirection();
		List<Operation> desiredOperations = ImmutableList
				.<Operation> builder()
				.add(new SetTurn(playerIds.get(turnOfColor.getOppositeColor()
						.ordinal())))
				.add(new Set(turnOfColor.name(), newHandOfPlayer))
				.add(new Set(turnOfColor.getOppositeColor().name(),
						lastHandOfOpponent))
				.add(new Set(USED, newUsedPile))
				.add(new Set(UNUSED, newUnUsedPile))
				.add(new SetVisibility(C + cardList.get(0)))
				.add(new SetVisibility(C + newCardAddedToPlayer.get(0),
						ImmutableList.<Integer> of(playerIds.get(turnOfColor
								.ordinal()))))
				.add(new Set(DIRECTION, newDirection.name())).build();
		return desiredOperations;
	}

	/**
	 * This current player exchange cards with the other player and neither
	 * player will get a new card from the unused pile. This action will not
	 * lead to the end of the game, but has a lot to do with the visibility.
	 * 
	 * @param pokerState
	 *            the game state of the current player derived from the gameApi.
	 * @param cardList
	 *            a list contains only one card representing the card played
	 * @param playerIds
	 * @return desired operations
	 */
	List<Operation> playAExchangeCard(PokerState pokerState,
			List<Integer> cardList, List<Integer> playerIds) {
		check(!pokerState.isGameOver());
		check(!pokerState.isSub());
		ColorOfPlayer turnOfColor = pokerState.getTurn();
		/*
		 * the operations should be: setTurn(), set(W)/set(B), set(Used),
		 * setVisibility(the cards in the current player), setVisibility(the
		 * cards in the opponent's hand), setVisibility(cardList)
		 */
		List<Integer> lastUsedPile = pokerState.getUsed();
		List<Integer> lastHandOfCurrentPlayer = pokerState
				.getWhiteOrBlack(turnOfColor);
		List<Integer> lastHandOfOpponent = pokerState
				.getWhiteOrBlack(turnOfColor.getOppositeColor());

		List<Integer> newUsedPile = concat(lastUsedPile, cardList);
		List<Integer> newHandOfCurrentPlayer = lastHandOfOpponent;
		List<Integer> newHandOfOpponent = subtract(lastHandOfCurrentPlayer,
				cardList);

		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(playerIds.get(turnOfColor.getOppositeColor()
				.ordinal())));
		operations.add(new Set(turnOfColor.name(), newHandOfCurrentPlayer));
		operations.add(new Set(turnOfColor.getOppositeColor().name(),
				newHandOfOpponent));
		operations.add(new Set(USED, newUsedPile));
		for (Integer newCardIndex : newHandOfCurrentPlayer) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of(playerIds.get(turnOfColor.ordinal()))));
		}
		for (Integer newCardIndex : newHandOfOpponent) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of(playerIds.get(turnOfColor.getOppositeColor()
							.ordinal()))));
		}
		operations.add(new SetVisibility(C + cardList.get(0)));
		return ImmutableList.<Operation> copyOf(operations);
	}

	/*
	 * After this move, the date field Is_Sub in the game state should be set to
	 * false. This operation would not lead to an end of the game.
	 */
	List<Operation> playAMinusCard(PokerState pokerState,
			List<Integer> cardList, int cardValue, List<Integer> playerIds) {
		check(!pokerState.isGameOver());
		check(pokerState.isSub());
		ColorOfPlayer turnOfColor = pokerState.getTurn();
		/*
		 * play a negative value card. SetTurn(); Set(W)/Set(B), Set(Used),
		 * Set(Unused), Set(P), SetVisiblity(Set the card played visible to all
		 * and new card drawn to the current player), Delete()
		 */
		List<Integer> lastUsed = pokerState.getUsed();
		List<Integer> lastUnUsed = pokerState.getUnused();
		List<Integer> lastHandOfCurrentPlayer = pokerState
				.getWhiteOrBlack(turnOfColor);
		List<Integer> lastHandOfOpponent = pokerState
				.getWhiteOrBlack(turnOfColor.getOppositeColor());
		int lastPoint = pokerState.getPoints();
		// get the new card which will be added to current player. It is the
		// first element in the unused pile
		List<Integer> newCardAddedToCurrentPlayer = lastUnUsed.subList(0, 1);
		List<Integer> newUsed = concat(lastUsed, cardList);
		List<Integer> newUnUsed = subtract(lastUnUsed,
				newCardAddedToCurrentPlayer);
		List<Integer> newHandOfCurrentPlayer = concat(
				subtract(lastHandOfCurrentPlayer, cardList),
				newCardAddedToCurrentPlayer);
		int newPoint = lastPoint + cardValue;
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(playerIds.get(turnOfColor.getOppositeColor()
				.ordinal())));
		operations.add(new Set(turnOfColor.name(), newHandOfCurrentPlayer));
		operations.add(new Set(turnOfColor.getOppositeColor().name(),
				lastHandOfOpponent));
		operations.add(new Set(USED, newUsed));
		operations.add(new Set(UNUSED, newUnUsed));
		operations.add(new Set(P, newPoint));
		operations.add(new SetVisibility(C + cardList.get(0)));
		operations.add(new SetVisibility(
				C + newCardAddedToCurrentPlayer.get(0), ImmutableList
						.<Integer> of(playerIds.get(turnOfColor.ordinal()))));
		operations.add(new Delete(IS_SUB));
		return ImmutableList.<Operation> copyOf(operations);
	}

	/*
	 * Currently the card drawn from the opponent is given default as the first
	 * card in the opponent's hand. There might be two ways to improve: first is
	 * use a random number; Second is let the player choose, which requires
	 * interaction with UI and it may require a further move.
	 */
	List<Operation> playADrawOtherCard(PokerState pokerState,
			List<Integer> cardList, List<Integer> playerIds) {
		/*
		 * In this move, it may lead to an end of the game. If the player drawa
		 * a card from the opponent which is the last card of the opponent, this
		 * will lead to the win of the current player. SetTurn(), Set(W)/Set(B),
		 * Set(used), SetVisibility(the card played by the player),
		 * SetVisibility(the card drawn from the opponent), shuffle(the cards in
		 * the current player) Set(isGameOver), EndGame()
		 */
		check(!pokerState.isGameOver());
		check(!pokerState.isSub());
		ColorOfPlayer turnOfColor = pokerState.getTurn();
		List<Integer> lastHandOfCurrentPlayer = pokerState
				.getWhiteOrBlack(turnOfColor);
		List<Integer> lastHandOfOpponent = pokerState
				.getWhiteOrBlack(turnOfColor.getOppositeColor());
		List<Integer> lastUnsed = pokerState.getUsed();
		List<Integer> cardFromOpponent = lastHandOfOpponent.subList(0, 1);

		List<Integer> newHandOfCurrentPlayer = concat(
				subtract(lastHandOfCurrentPlayer, cardList), cardFromOpponent);
		// pay special attention to the order here, this order will also
		// influence the shuffle order
		List<Integer> newHandOfOpponent = subtract(lastHandOfOpponent,
				cardFromOpponent);
		List<Integer> newUsedPile = concat(lastUnsed, cardList);
		List<String> newCardsInCurrentPlayer = Lists.newArrayList();
		for (Integer cardIndex : newHandOfCurrentPlayer) {
			newCardsInCurrentPlayer.add(C + cardIndex);
		}
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(playerIds.get(turnOfColor.getOppositeColor()
				.ordinal())));
		operations.add(new Set(turnOfColor.name(), newHandOfCurrentPlayer));
		operations.add(new Set(turnOfColor.getOppositeColor().name(),
				newHandOfOpponent));
		operations.add(new Set(USED, newUsedPile));
		operations.add(new SetVisibility(C + cardList.get(0)));
		operations
				.add(new SetVisibility(C + cardFromOpponent.get(0),
						ImmutableList.<Integer> of(playerIds.get(turnOfColor
								.ordinal()))));
		operations.add(new Shuffle(newCardsInCurrentPlayer));
		if (newHandOfOpponent.isEmpty()) {
			operations.add(new Set(IS_GAMEOVER, YES));
			operations.add(new EndGame(playerIds.get(turnOfColor.ordinal())));
		}
		return ImmutableList.<Operation> copyOf(operations);
	}

	List<Operation> playAddedToMaxCard(PokerState pokerState,
			List<Integer> cardList, List<Integer> playerIds) {
		return playANormalCard(pokerState, cardList, 99, playerIds);
	}

	List<Operation> getInitialMove(List<Integer> playerIds) {
		int whitePlayerId = playerIds.get(0);
		int blackPlayerId = playerIds.get(1);
		List<Operation> operations = Lists.newArrayList();
		// The order of operations: turn, W, B, Used, UnUsed , C0...C51, Points,
		// direction, isSub, isGameOver
		operations.add(new SetTurn(whitePlayerId));
		operations.add(new Set(W, getIndicesInRange(0, 4)));
		operations.add(new Set(B, getIndicesInRange(5, 9)));
		operations.add(new Set(USED, ImmutableList.of()));
		operations.add(new Set(UNUSED, getIndicesInRange(10, 51)));
		// sets all 52 cards: set(C0,1c), set(C1,1d), ... , set(C51,Ks)
		// It means that add the entry into the state and visible to all
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
			operations.add(new SetVisibility(C + i, ImmutableList
					.<Integer> of())); // to question
		}
		operations.add(new Set(P, 0));
		operations.add(new Set(DIRECTION, CLOCKWISE));
		// operations.add(new Set(IS_SUB, FALSE));
		// we don't need to do this action now
		// If the gameApiState don't contain this key, it's false
		return operations;
	}

	@SuppressWarnings("unchecked")
	PokerState gameApiStateToPokerState(Map<String, Object> gameApiState,
			ColorOfPlayer turnOfPlayer, List<Integer> playerIds) {
		List<Optional<Card>> cards = Lists.newArrayList();
		for (int i = 0; i < 52; i++) {
			// Here we get the string: the actual value of the card ie 1c, ...,
			// ks, maybe in a completely different order
			/*
			 * Here seems that if the value is null, it simply means that you
			 * cannot see the card. implementation detail require further
			 * consideration Some cards are null means that you cannot see the
			 * card. Otherwise you can see the value of the card
			 */
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
				turnOfPlayer,
				ImmutableList.copyOf(cards),
				ImmutableList.copyOf(playerIds),
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

	/*
	 * 0->1c, 1->1d, 2->1h, 3->1s,......,51->ks
	 */
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
