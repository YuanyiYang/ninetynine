package edu.nyu.smg.pokerGame;

import static org.junit.Assert.*;
import edu.nyu.smg.pokerGame.GameApi.*;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class GameLogicTest {

	private GameLogic gameLogic = new GameLogic();

	private final int wId = 41;
	private final int bId = 42;
	private static final String PLAYER_ID = "playerId";
	private static final String TURN = "turn"; // turn of which player (either W
												// or B)
	private static final String W = "W"; // White hand
	private static final String B = "B"; // Black hand
	private static final String USED = "USED"; // Used pile
	private static final String UNUSED = "UNUSED"; // Unused pile
	private static final String C = "C"; // Card key (C1 .. C52)
	private static final String P = "Points"; // the total points of the game
	private static final String DIRECTION = "direction";
	private static final String IS_SUB = "isSub"; // some card may have
													// different kinds of
													// function
	private static final String YES = "yes";
	private static final String CLOCKWISE = "Clockwise";
	private static final String ANTICLOCKWISE = "AntiClockwise";
	private static final String IS_GAMEOVER = "isGameOver";
	private final List<Integer> visibleToW = ImmutableList.<Integer> of(wId);
	private final List<Integer> visibleToB = ImmutableList.<Integer> of(bId);
	private final Map<String, Object> wInfo = ImmutableMap.<String, Object> of(
			PLAYER_ID, wId);
	private final Map<String, Object> bInfo = ImmutableMap.<String, Object> of(
			PLAYER_ID, bId);
	private final List<Map<String, Object>> playersInfo = ImmutableList.of(
			wInfo, bInfo);
	private final Map<String, Object> emptyState = ImmutableMap
			.<String, Object> of();
	private final Map<String, Object> nonEmptyState = ImmutableMap
			.<String, Object> of("k", "v");

	// Here we don't put the IS_SUB in the map, so that in the future we can
	// simply find out the value of IS_SUB by determining whether the map
	// contains this entry or not
	// In the gameApiState, the cards should be a list of Integer

	private final Map<String, Object> turnOfWEmptyUsed = ImmutableMap
			.<String, Object> builder().put(TURN, W)
			.put(W, getIndicesInRange(0, 4)).put(B, getIndicesInRange(5, 9))
			.put(USED, ImmutableList.of())
			.put(UNUSED, getIndicesInRange(10, 51)).put(P, 0)
			.put(DIRECTION, CLOCKWISE).build();

	private final Map<String, Object> turnOfBEmptyUsed = ImmutableMap
			.<String, Object> builder().put(TURN, B)
			.put(W, getIndicesInRange(0, 4)).put(B, getIndicesInRange(5, 9))
			.put(USED, ImmutableList.of())
			.put(UNUSED, getIndicesInRange(10, 51)).put(P, 0)
			.put(DIRECTION, CLOCKWISE).build();

	private final Map<String, Object> WPlayANormalCard = ImmutableMap
			.<String, Object> builder().put(TURN, B)
			.put(W, concat(getIndicesInRange(0, 3), getCardIndex(10)))
			.put(B, getIndicesInRange(5, 9)).put(USED, getCardIndex(4))
			.put(UNUSED, getIndicesInRange(11, 51)).put(P, "RankInC4")
			.put(DIRECTION, CLOCKWISE).build();

	// the actual points depend on the rank of c4, here we need the shuffle
	// function to shuffle the card and then elicit the number from the card.
	// Should implement after the shuffle function

	private final Map<String, Object> BPlayAExchangeCard = ImmutableMap
			.<String, Object> builder().put(TURN, W)
			.put(W, getIndicesInRange(6, 9))
			.put(B, concat(getIndicesInRange(0, 3), getCardIndex(10)))
			.put(USED, getIndicesInRange(4, 5))
			.put(UNUSED, getIndicesInRange(11, 51)).put(P, "RankInC4")
			.put(DIRECTION, CLOCKWISE).build();

	private final Map<String, Object> WPlayDrawOtherCard = ImmutableMap
			.<String, Object> builder().put(TURN, B)
			.put(W, getIndicesInRange(7, 10)).put(B, getIndicesInRange(0, 3))
			.put(USED, getIndicesInRange(4, 6))
			.put(UNUSED, getIndicesInRange(11, 51)).put(P, "RankInC4")
			.put(DIRECTION, CLOCKWISE).build();

	private final Map<String, Object> BPlayChangeDirection = ImmutableMap
			.<String, Object> builder().put(TURN, W)
			.put(W, getIndicesInRange(7, 10))
			.put(B, concat(getCardIndex(11), getIndicesInRange(0, 2)))
			.put(USED, getIndicesInRange(3, 6))
			.put(UNUSED, getIndicesInRange(12, 51)).put(P, "RankInC4")
			.put(DIRECTION, ANTICLOCKWISE).build();

	/*
	 * To test this illegal state, we use initial state as the last state for
	 * simplicity
	 */
	private final Map<String, Object> illegalWPlayDrawMoreThanOne = ImmutableMap
			.<String, Object> builder().put(TURN, B)
			.put(W, concat(getIndicesInRange(0, 3), getIndicesInRange(10, 11)))
			.put(B, getIndicesInRange(5, 9)).put(USED, getCardIndex(4))
			.put(UNUSED, getIndicesInRange(12, 51)).put(P, "RankInC4")
			.put(DIRECTION, CLOCKWISE).build();

	private final Map<String, Object> illegalBDrawFromOtherAndPile = ImmutableMap
			.<String, Object> builder().put(TURN, W)
			.put(W, getIndicesInRange(0, 3))
			.put(B, concat(getIndicesInRange(4, 8), getCardIndex(10)))
			.put(USED, getCardIndex(9)).put(UNUSED, getIndicesInRange(11, 51))
			.put(P, 0).put(DIRECTION, CLOCKWISE).build();

	private final Map<String, Object> illegalWPlayTwice = ImmutableMap
			.<String, Object> builder().put(TURN, B)
			.put(W, concat(getIndicesInRange(0, 2), getIndicesInRange(10, 11)))
			.put(B, getIndicesInRange(5, 9)).put(USED, getIndicesInRange(3, 4))
			.put(UNUSED, getIndicesInRange(12, 51)).put(P, "RankInC4C5")
			.put(DIRECTION, CLOCKWISE).build();

	@Test
	public void testCardsInRange() {
		assertEquals(ImmutableList.of("C3", "C4"),
				gameLogic.getCardsInRange(3, 4));
	}

	@Test
	public void testGetCard() {
		assertEquals(ImmutableList.of("C50"), gameLogic.getCard(50));
	}

	@Test
	public void testGetCardIndex() {
		assertEquals(ImmutableList.of(50), gameLogic.getCardIndex(50));
	}

	private <T> List<T> concat(List<T> a, List<T> b) {
		return gameLogic.concat(a, b);
	}

	@Test
	public void testCardIdToString() {
		assertEquals("1c", gameLogic.cardIdToString(0));
		assertEquals("1d", gameLogic.cardIdToString(1));
		assertEquals("1h", gameLogic.cardIdToString(2));
		assertEquals("1s", gameLogic.cardIdToString(3));
		assertEquals("2c", gameLogic.cardIdToString(4));
		assertEquals("2d", gameLogic.cardIdToString(5));
		assertEquals("2h", gameLogic.cardIdToString(6));
		assertEquals("2s", gameLogic.cardIdToString(7));
		assertEquals("Ks", gameLogic.cardIdToString(51));
	}

	private List<Operation> getInitialOperations() {
		return gameLogic.getInitialMove(wId, bId);
	}

	@Test
	public void testGetInitialOperationsSize() {
		assertEquals(5 + 52 + 52 + 1 + 2, gameLogic.getInitialMove(wId, bId)
				.size());
	}

	/*
	 * In this initial move, W shuffle the cards. Both W and B will verify the
	 * move and return that the move is OK
	 */
	@Test
	public void testLegalInitialMove() {
		assertMoveOk(move(wId, wId, turnOfWEmptyUsed, emptyState,
				getInitialOperations()));
		assertMoveOk(move(bId, wId, turnOfWEmptyUsed, emptyState,
				getInitialOperations()));
	}

	/*
	 * W plays a normal card. B should verify this move. Here the operations are
	 * generated manually. Should use a method to substitute the operations here
	 * Should be like: List<Operation> getOp(state, List<Integer> cardId)
	 */
	@Test
	public void testWPlayANormalCardFromBeginning() {
		List<Operation> operations = ImmutableList
				.<Operation> builder()
				.add(new Set(TURN, B))
				.add(new Set(W, concat(getIndicesInRange(0, 3),
						getCardIndex(10))))
				.add(new Set(B, getIndicesInRange(5, 9)))
				.add(new Set(USED, getCardIndex(4)))
				.add(new Set(UNUSED, getIndicesInRange(11, 51)))
				.add(new Set(P, "RankInC4")).add(new Set(DIRECTION, CLOCKWISE))
				.build();
		assertMoveOk(move(bId, wId, WPlayANormalCard, turnOfWEmptyUsed,
				operations));
	}

	// W verify move made by B
	@Test
	public void testBPlayAExchageCard() {
		List<Operation> operations = ImmutableList
				.<Operation> builder()
				.add(new Set(TURN, W))
				.add(new Set(W, getIndicesInRange(6, 9)))
				.add(new Set(B, concat(getIndicesInRange(0, 3),
						getCardIndex(10))))
				.add(new Set(USED, getIndicesInRange(4, 5)))
				.add(new Set(UNUSED, getIndicesInRange(11, 51)))
				.add(new Set(P, "RankInC4")).add(new Set(DIRECTION, CLOCKWISE))
				.build();
		assertMoveOk(move(wId, bId, BPlayAExchangeCard, WPlayANormalCard,
				operations));
	}

	@Test
	public void testWPlayDrawOther() {
		List<Operation> operations = ImmutableList.<Operation> builder()
				.add(new Set(TURN, B))
				.add(new Set(W, getIndicesInRange(7, 10)))
				.add(new Set(B, getIndicesInRange(0, 3)))
				.add(new Set(USED, getIndicesInRange(4, 6)))
				.add(new Set(UNUSED, getIndicesInRange(11, 51)))
				.add(new Set(P, "RankInC4")).add(new Set(DIRECTION, CLOCKWISE))
				.build();
		assertMoveOk(move(bId, wId, WPlayDrawOtherCard, BPlayAExchangeCard,
				operations));
	}

	/*
	 * The new direction in the set(Direction) should be determined in the game
	 * logic based on the current game state
	 */
	@Test
	public void testBplayExchangeCard() {
		List<Operation> operations = ImmutableList
				.<Operation> builder()
				.add(new Set(TURN, W))
				.add(new Set(W, getIndicesInRange(7, 10)))
				.add(new Set(B, concat(getIndicesInRange(0, 2),
						getCardIndex(11))))
				.add(new Set(USED, getIndicesInRange(3, 6)))
				.add(new Set(UNUSED, getIndicesInRange(12, 51)))
				.add(new Set(P, "RankInC4"))
				.add(new Set(DIRECTION, ANTICLOCKWISE)).build();
		assertMoveOk(move(wId, bId, BPlayChangeDirection, WPlayDrawOtherCard,
				operations));
	}

	/*
	 * After the initial move, instead of giving the right of the play to B, he
	 * himself made another move
	 */
	@Test
	public void testIllegalWDoubleMove() {
		List<Operation> operations = ImmutableList
				.<Operation> builder()
				.add(new Set(TURN, B))
				.add(new Set(W, concat(getIndicesInRange(0, 2),
						getIndicesInRange(10, 11))))
				.add(new Set(B, getIndicesInRange(5, 9)))
				.add(new Set(USED, getIndicesInRange(3, 4)))
				.add(new Set(UNUSED, getIndicesInRange(12, 51)))
				.add(new Set(P, "RankInC4C5"))
				.add(new Set(DIRECTION, CLOCKWISE)).build();
		assertHacker(move(bId, wId, illegalWPlayTwice, WPlayANormalCard,
				operations));
	}

	@Test
	public void testIllegalMoveDrawTwoCards() {
		List<Operation> operations = ImmutableList
				.<Operation> builder()
				.add(new Set(TURN, B))
				.add(new Set(W, concat(getIndicesInRange(0, 3),
						getIndicesInRange(10, 11))))
				.add(new Set(B, getIndicesInRange(5, 9)))
				.add(new Set(USED, getCardIndex(4)))
				.add(new Set(UNUSED, getIndicesInRange(12, 51)))
				.add(new Set(P, "RankInC4")).add(new Set(DIRECTION, CLOCKWISE))
				.build();
		assertHacker(move(bId, wId, illegalWPlayDrawMoreThanOne,
				turnOfWEmptyUsed, operations));
	}

	@Test
	public void testIllegalBDrawFromOtherAndPile() {
		List<Operation> operations = ImmutableList
				.<Operation> builder()
				.add(new Set(TURN, W))
				.add(new Set(W, getIndicesInRange(0, 3)))
				.add(new Set(B, concat(getIndicesInRange(4, 8),
						getCardIndex(10)))).add(new Set(USED, getCardIndex(9)))
				.add(new Set(UNUSED, getIndicesInRange(11, 51)))
				.add(new Set(P, 0)).add(new Set(DIRECTION, CLOCKWISE)).build();
		assertHacker(move(wId, bId, illegalBDrawFromOtherAndPile,
				turnOfBEmptyUsed, operations));
	}

	@Test
	public void testIllegalInitialMoveNotFromEmptyState() {
		assertHacker(move(wId, wId, turnOfWEmptyUsed, nonEmptyState,
				getInitialOperations()));
	}

	private final Map<String, Object> simpleStateBeforeEndGame = ImmutableMap
			.<String, Object> builder().put(TURN, W).put(W, getCardIndex(25))
			.put(B, getCardIndex(26)).put(USED, getIndicesInRange(0, 24))
			.put(UNUSED, getIndicesInRange(27, 51)).put(P, 99)
			.put(DIRECTION, ANTICLOCKWISE).build();

	private final Map<String, Object> BWinsTheGame = ImmutableMap
			.<String, Object> builder().put(TURN, B)
			.put(W, ImmutableList.<Integer> of()).put(B, getCardIndex(26))
			.put(USED, getIndicesInRange(0, 25))
			.put(UNUSED, getIndicesInRange(27, 51)).put(P, 100)
			.put(DIRECTION, ANTICLOCKWISE).put(IS_GAMEOVER, YES).build();

	private final Map<String, Object> WWinsTheGame = ImmutableMap
			.<String, Object> builder().put(TURN, B).put(W, getCardIndex(26))
			.put(B, ImmutableList.of()).put(USED, getIndicesInRange(0, 25))
			.put(UNUSED, getIndicesInRange(27, 51)).put(P, 99)
			.put(DIRECTION, ANTICLOCKWISE).put(IS_GAMEOVER, YES).build();

	/*
	 * It's W's move. There are three kinds of result after this 1.Value card. W
	 * lose! 2.Jack W wins. 3.Other wild card continue
	 */

	@Test
	public void testGameEndWithBWins() {
		List<Operation> operations = ImmutableList.<Operation> builder()
				.add(new Set(TURN, B))
				.add(new Set(W, ImmutableList.<Integer> of()))
				.add(new Set(B, getCardIndex(26)))
				.add(new Set(USED, getIndicesInRange(0, 25)))
				.add(new Set(UNUSED, getIndicesInRange(27, 51)))
				.add(new Set(P, 100)).add(new Set(DIRECTION, ANTICLOCKWISE))
				.add(new EndGame(bId)).build();
		assertMoveOk(move(bId, wId, BWinsTheGame, simpleStateBeforeEndGame,
				operations));
	}

	@Test
	public void testGameEndWithWWins() {
		List<Operation> operations = ImmutableList.<Operation> builder()
				.add(new Set(TURN, B)).add(new Set(W, getCardIndex(26)))
				.add(new Set(B, ImmutableList.<Integer> of()))
				.add(new Set(USED, getIndicesInRange(0, 25)))
				.add(new Set(UNUSED, getIndicesInRange(27, 51)))
				.add(new Set(P, 99)).add(new Set(DIRECTION, ANTICLOCKWISE))
				.add(new EndGame(wId)).build();
		assertMoveOk(move(bId, wId, WWinsTheGame, simpleStateBeforeEndGame,
				operations));
	}
	
	private void assertHacker(VerifyMove verifyMove) {
		VerifyMoveDone verifyDone = gameLogic.verify(verifyMove);
		assertEquals(verifyMove.getLastMovePlayerId(),
				verifyDone.getHackerPlayerId());
	}

	private void assertMoveOk(VerifyMove verifyMove) {
		assertEquals(0, gameLogic.verify(verifyMove).getHackerPlayerId());
	}

	private VerifyMove move(int yourPlayerId, int lastMovePlayerId,
			Map<String, Object> currentState, Map<String, Object> lastState,
			List<Operation> lastMove) {
		return new VerifyMove(yourPlayerId, playersInfo, currentState,
				lastState, lastMove, lastMovePlayerId);
	}

	private List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		return gameLogic.getIndicesInRange(fromInclusive, toInclusive);
	}

	private List<Integer> getCardIndex(int cardNumber) {
		return gameLogic.getCardIndex(cardNumber);
	}

}
