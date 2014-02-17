package edu.nyu.smg.pokerGame;

import static org.junit.Assert.*;
import edu.nyu.smg.pokerGame.GameApi.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class GameLogicTest {

	private GameLogic gameLogic = new GameLogic();

	private void assertHacker(VerifyMove verifyMove) {
		VerifyMoveDone verifyDone = gameLogic.verify(verifyMove);
		assertEquals(verifyMove.getLastMovePlayerId(),
				verifyDone.getHackerPlayerId());
	}

	private void assertMoveOk(VerifyMove verifyMove) {
		gameLogic.checkMoveIsLegal(verifyMove);
	}

	private VerifyMove move(int lastMovePlayerId,
			Map<String, Object> lastState, List<Operation> lastMove,
			Map<String, Object> currentState) {
		return new VerifyMove(playersInfo, currentState, lastState, lastMove,
				lastMovePlayerId, ImmutableMap.<Integer, Integer> of());
	}

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
	private static final String IS_SUB = "isSub";
	private static final String YES = "yes";
	private static final String CLOCKWISE = "Clockwise";
	private static final String ANTICLOCKWISE = "AntiClockwise";
	private static final String IS_GAMEOVER = "isGameOver";
	private final List<Integer> visibleToW = ImmutableList.<Integer> of(wId);
	private final List<Integer> visibleToB = ImmutableList.<Integer> of(bId);
	private final List<Integer> visibleToNone = ImmutableList.<Integer> of();
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
	private final List<Integer> testPlayerIds = ImmutableList.<Integer> of(wId,
			bId);

	private final Map<String, Object> initialMoveState = ImmutableMap
			.<String, Object> builder().put(W, getIndicesInRange(0, 4))
			.put(B, getIndicesInRange(5, 9)).put(USED, ImmutableList.of())
			.put(UNUSED, getIndicesInRange(10, 51)).put(P, 0).put(C + 4, "5s")
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

	private final Map<String, Object> simpleStateBeforeEndGame = ImmutableMap
			.<String, Object> builder().put(TURN, W).put(W, getCardIndex(25))
			.put(B, getCardIndex(26)).put(USED, getIndicesInRange(0, 24))
			.put(UNUSED, getIndicesInRange(27, 51)).put(P, 99)
			.put(DIRECTION, ANTICLOCKWISE).build();

	private final Map<String, Object> gameContinueWithCardTen = ImmutableMap
			.<String, Object> builder().put(TURN, B).put(W, getCardIndex(27))
			.put(B, getCardIndex(26)).put(USED, getIndicesInRange(0, 25))
			.put(UNUSED, getIndicesInRange(28, 51)).put(P, 89)
			.put(DIRECTION, ANTICLOCKWISE).put(IS_SUB, YES).build();

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

	// @Test
	// public void testGameEndWithBWins() {
	// List<Operation> operations = ImmutableList.<Operation> builder()
	// .add(new Set(TURN, B))
	// .add(new Set(W, ImmutableList.<Integer> of()))
	// .add(new Set(B, getCardIndex(26)))
	// .add(new Set(USED, getIndicesInRange(0, 25)))
	// .add(new Set(UNUSED, getIndicesInRange(27, 51)))
	// .add(new Set(P, 100)).add(new Set(DIRECTION, ANTICLOCKWISE))
	// .add(new EndGame(bId)).build();
	// assertMoveOk(move(bId, wId, BWinsTheGame, simpleStateBeforeEndGame,
	// operations));
	// }
	//
	// @Test
	// public void testGameEndWithWWins() {
	// List<Operation> operations = ImmutableList.<Operation> builder()
	// .add(new Set(TURN, B)).add(new Set(W, getCardIndex(26)))
	// .add(new Set(B, ImmutableList.<Integer> of()))
	// .add(new Set(USED, getIndicesInRange(0, 25)))
	// .add(new Set(UNUSED, getIndicesInRange(27, 51)))
	// .add(new Set(P, 99)).add(new Set(DIRECTION, ANTICLOCKWISE))
	// .add(new EndGame(wId)).build();
	// assertMoveOk(move(bId, wId, WWinsTheGame, simpleStateBeforeEndGame,
	// operations));
	// }
	//
	// /*
	// * To test the card represent a minus value(Rank10 and Q) here we simply
	// * assume this card is ten. The total value will minus 10
	// */
	// @Test
	// public void testMinusSucceed() {
	// List<Operation> operations = ImmutableList.<Operation> builder()
	// .add(new Set(TURN, B)).add(new Set(W, getCardIndex(27)))
	// .add(new Set(B, getCardIndex(26)))
	// .add(new Set(USED, getIndicesInRange(0, 25)))
	// .add(new Set(UNUSED, getIndicesInRange(28, 51)))
	// .add(new Set(P, 89)).add(new Set(DIRECTION, ANTICLOCKWISE))
	// .add(new Set(IS_SUB, YES)).build();
	// assertMoveOk(move(bId, wId, gameContinueWithCardTen,
	// simpleStateBeforeEndGame, operations));
	// }

	// Need to test the add up to 99 function

	private List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		return gameLogic.getIndicesInRange(fromInclusive, toInclusive);
	}

	private List<Integer> getCardIndex(int cardNumber) {
		return gameLogic.getCardIndex(cardNumber);
	}

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

	private <T> List<T> subtract(List<T> removeFrom, List<T> elementsToRemove) {
		return gameLogic.subtract(removeFrom, elementsToRemove);
	}

	private List<String> getCardsInRange(int fromInclusive, int toInclusive) {
		return gameLogic.getCardsInRange(fromInclusive, toInclusive);
	}

	private List<Operation> getTestInitialOperation() {
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(wId));
		operations.add(new Set(W, getIndicesInRange(0, 4)));
		operations.add(new Set(B, getIndicesInRange(5, 9)));
		operations.add(new Set(USED, ImmutableList.of()));
		operations.add(new Set(UNUSED, getIndicesInRange(10, 51)));
		for (int i = 0; i < 52; i++) {
			operations.add(new Set(C + i, gameLogic.cardIdToString(i)));
		}
		operations.add(new Shuffle(gameLogic.getCardsInRange(0, 51)));
		for (int i = 0; i < 5; i++) {
			operations.add(new SetVisibility(C + i, ImmutableList.of(wId)));
		}
		for (int i = 5; i < 10; i++) {
			operations.add(new SetVisibility(C + i, ImmutableList.of(bId)));
		}
		for (int i = 10; i < 52; i++) {
			operations.add(new SetVisibility(C + i, ImmutableList
					.<Integer> of())); // to question
		}
		operations.add(new Set(P, 0));
		operations.add(new Set(DIRECTION, CLOCKWISE));
		return operations;
	}

	@Test
	public void testGetInitialOperationsSize() {
		assertEquals(5 + 52 + 52 + 1 + 2, gameLogic.getInitialMove(wId, bId)
				.size());
	}

	/*
	 * In this initial move, the first player shuffles the cards. The resulting
	 * state is pointless. This initial move must be done by the first player in
	 * the players list.
	 */
	@Test
	public void testLegalInitialMove() {
		assertMoveOk(move(wId, emptyState, getTestInitialOperation(),
				initialMoveState));
	}

	@Test
	public void IllegalInitialMoveByWrongPlayer() {
		assertHacker(move(bId, emptyState, getTestInitialOperation(),
				initialMoveState));
	}

	@Test
	public void IllegalInitialMoveFromNonEmptyState() {
		assertHacker(move(wId, nonEmptyState, getTestInitialOperation(),
				initialMoveState));
	}

	@Test
	public void IllegalInitialMoveWithExtraMove() {
		List<Operation> operations = new ArrayList<Operation>(
				getTestInitialOperation());
		operations.add(new Set(USED, ImmutableList.of()));
		assertHacker(move(wId, emptyState, operations, initialMoveState));
	}

	// lastID, lastState, lastMove, currentState
	@Test
	public void testIllegalGetNextMoveSubWithExtraMove() {
		List<Operation> lastMove = ImmutableList.<Operation> of(new Set(IS_SUB,
				YES), new Delete(IS_SUB));
		assertHacker(move(wId, initialMoveState, lastMove, emptyState));
	}

	@Test
	public void testLegalGetNextMoveSub() {
		List<Operation> lastMove = ImmutableList.<Operation> of(new Set(IS_SUB,
				YES));
		assertMoveOk(move(bId, WPlayDrawOtherCard, lastMove, emptyState));
	}

	private final Map<String, Object> needANewRoundShuffleState = ImmutableMap
			.<String, Object> builder().put(W, getIndicesInRange(42, 46))
			.put(B, getIndicesInRange(47, 51))
			.put(USED, getIndicesInRange(0, 41))
			.put(UNUSED, ImmutableList.<Integer> of()).put(P, 0)
			.put(DIRECTION, CLOCKWISE).build();

	private final Map<String, Object> afterANewRoundShuffleState = ImmutableMap
			.<String, Object> builder().put(W, getIndicesInRange(42, 46))
			.put(B, getIndicesInRange(47, 51))
			.put(USED, ImmutableList.<Integer> of())
			.put(UNUSED, getIndicesInRange(0, 41)).put(DIRECTION, CLOCKWISE)
			.put(P, 0).build();

	@Test
	public void testNewRoundShuffle() {
		List<Operation> operations = Lists.newArrayList();
		operations.add(new Set(USED, ImmutableList.<Integer> of()));
		operations.add(new Set(UNUSED, getIndicesInRange(0, 41)));
		operations.add(new Shuffle(getCardsInRange(0, 41)));
		for (Integer newCardIndex : getIndicesInRange(0, 41)) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of()));
		}
		assertMoveOk(move(wId, needANewRoundShuffleState, operations,
				afterANewRoundShuffleState));
	}

	/*
	 * Here we deliberately put in the card value put(C+4, "8s") so that the
	 * game state can get the card
	 */
	private final Map<String, Object> WPlayANormalCard = ImmutableMap
			.<String, Object> builder()
			.put(W, concat(getIndicesInRange(0, 3), getCardIndex(10)))
			.put(B, getIndicesInRange(5, 9)).put(USED, getCardIndex(4))
			.put(UNUSED, getIndicesInRange(11, 51)).put(P, 5).put(C + 4, "5h")
			.put(DIRECTION, CLOCKWISE).build();

	@Test
	public void testANormalValueCard() {
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(bId));
		operations.add(new Set(W, concat(getIndicesInRange(0, 3),
				getCardIndex(10))));
		operations.add(new Set(B, getIndicesInRange(5, 9)));
		operations.add(new Set(USED, getCardIndex(4)));
		operations.add(new Set(UNUSED, getIndicesInRange(11, 51)));
		operations.add(new Set(P, 5));
		operations.add(new SetVisibility(C + 4));
		operations.add(new SetVisibility(C + 10, ImmutableList
				.<Integer> of(wId)));
		/*
		 * In this move, it stands for that the player plays his own card. The
		 * player could see the actual value of the card, so the actual value of
		 * card C4 is in the initialMoveState
		 */
		assertEquals(operations, gameLogic.getExpectedOperations(ImmutableList
				.<Integer> of(4), gameLogic.gameApiStateToPokerState(
				initialMoveState, ColorOfPlayer.W, testPlayerIds),
				testPlayerIds, null));
		/*
		 * In this move, it represents that the player is verifying the
		 * opponent's move. So the actual value of the card should be revealed
		 * in the resulting state.
		 */
		assertMoveOk(move(wId, initialMoveState, operations, WPlayANormalCard));
	}

	private final Map<String, Object> BPlayAExchangeCard = ImmutableMap
			.<String, Object> builder().put(W, getIndicesInRange(6, 9))
			.put(B, concat(getIndicesInRange(0, 3), getCardIndex(10)))
			.put(USED, getIndicesInRange(4, 5))
			.put(UNUSED, getIndicesInRange(11, 51)).put(P, 5).put(C + 5, "7s")
			.put(DIRECTION, CLOCKWISE).build();

	@Test
	public void testAExchageCard() {
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(wId));
		operations.add(new Set(B, concat(getIndicesInRange(0, 3),
				getCardIndex(10))));
		operations.add(new Set(W, getIndicesInRange(6, 9)));
		operations.add(new Set(USED, getIndicesInRange(4, 5)));
		for (Integer newCardIndex : concat(getIndicesInRange(0, 3),
				getCardIndex(10))) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of(bId)));
		}
		for (Integer newCardIndex : getIndicesInRange(6, 9)) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of(wId)));
		}
		operations.add(new SetVisibility(C + 5));
		assertMoveOk(move(bId, WPlayANormalCard, operations, BPlayAExchangeCard));
	}
	
	private final Map<String, Object> BPlayAExchangeCardWithWrongCard = ImmutableMap
			.<String, Object> builder().put(W, getIndicesInRange(6, 9))
			.put(B, concat(getIndicesInRange(0, 3), getCardIndex(10)))
			.put(USED, getIndicesInRange(4, 5))
			.put(UNUSED, getIndicesInRange(11, 51)).put(P, 5).put(C + 5, "4s")
			.put(DIRECTION, CLOCKWISE).build();
	
	@Test
	public void testIllegalExchangeWithWrongCard(){
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(wId));
		operations.add(new Set(B, concat(getIndicesInRange(0, 3),
				getCardIndex(10))));
		operations.add(new Set(W, getIndicesInRange(6, 9)));
		operations.add(new Set(USED, getIndicesInRange(4, 5)));
		for (Integer newCardIndex : concat(getIndicesInRange(0, 3),
				getCardIndex(10))) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of(bId)));
		}
		for (Integer newCardIndex : getIndicesInRange(6, 9)) {
			operations.add(new SetVisibility(C + newCardIndex, ImmutableList
					.<Integer> of(wId)));
		}
		operations.add(new SetVisibility(C + 5));
		assertHacker(move(bId, WPlayANormalCard, operations, BPlayAExchangeCardWithWrongCard));
	}
	/*
	 * Gam play: W->C4, Point:5; B->exchange;
	 */
}
