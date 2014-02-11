package edu.nyu.smg.pokerGame;

import static org.junit.Assert.*;
import edu.nyu.smg.pokerGame.GameApi.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class GameLogicTest {

	private GameLogic gameLogic = new GameLogic();

	private final int wId = 41;
	private final int bId = 42;
	/*
	 * private boolean isSub = false; //whether this card add to the points or
	 * not. define here and implement later
	 */
	private final String playerId = "playerId";
	private final String turn = "turn"; // turn of which player (either W or B)
	private static final String W = "W"; // White hand
	private static final String B = "B"; // Black hand
	private static final String USED = "USED"; // Used pile
	private static final String UNUSED = "UNUSED"; // Unused pile
	private static final String C = "C"; // Card key (C1 .. C52)
	private static final String P = "Points"; // the total points of the game
	private final List<Integer> visibleToW = ImmutableList.<Integer> of(wId);
	private final List<Integer> visibleToB = ImmutableList.<Integer> of(bId);
	private final List<Integer> visibleToNull = ImmutableList.<Integer> of();
	private final Map<String, Object> wInfo = ImmutableMap.<String, Object> of(
			playerId, wId);
	private final Map<String, Object> bInfo = ImmutableMap.<String, Object> of(
			playerId, bId);
	private final List<Map<String, Object>> playersInfo = ImmutableList.of(
			wInfo, bInfo);
	private final Map<String, Object> emptyState = ImmutableMap
			.<String, Object> of();
	private final Map<String, Object> nonEmptyState = ImmutableMap
			.<String, Object> of("k", "v");

	private final Map<String, Object> turnOfWEmptyUsed = ImmutableMap
			.<String, Object> builder().put(turn, W)
			.put(W, getIndicesInRange(1, 5)).put(B, getIndicesInRange(6, 10))
			.put(USED, ImmutableList.of()).put(UNUSED, getIndicesInRange(11, 52))
			.put(P, 0).build();

	private final Map<String, Object> turnOfBEmptyUsed = ImmutableMap
			.<String, Object> builder().put(turn, B)
			.put(W, getIndicesInRange(1, 5)).put(B, getIndicesInRange(6, 10))
			.put(USED, ImmutableList.of()).put(UNUSED, getIndicesInRange(11, 52))
			.put(P, 0).build();

	private final Map<String, Object> WPlayANormalCard = ImmutableMap
			.<String, Object> builder().put(turn, B)
			.put(W, concat(getCardsInRange(1, 4), getCard(11)))
			.put(B, getIndicesInRange(6, 10)).put(USED, getCard(5))
			.put(UNUSED, getIndicesInRange(12, 52)).put(P, "RankInC5").build();

	private final Map<String, Object> BPlayAExchangeCard = ImmutableMap
			.<String, Object> builder().put(turn, W)
			.put(W, getIndicesInRange(7, 10))
			.put(B, concat(getIndicesInRange(1, 4), getCardIndex(11)))
			.put(USED, getIndicesInRange(5, 6))
			.put(UNUSED, getIndicesInRange(12, 52)).put(P, "RankInC5").build();

	private final Map<String, Object> WPlayDrawOtherCard = ImmutableMap
			.<String, Object> builder().put(turn, B)
			.put(W, getIndicesInRange(8, 11)).put(B, getIndicesInRange(1, 4))
			.put(USED, getIndicesInRange(5, 7))
			.put(UNUSED, getIndicesInRange(12, 52)).put(P, "RankInC5").build();

	private final Map<String, Object> illegalWPlayDrawMoreThanOne = ImmutableMap
			.<String, Object> builder().put(turn, B)
			.put(W, getIndicesInRange(9, 13)).put(B, getIndicesInRange(1, 4))
			.put(USED, getIndicesInRange(5, 8))
			.put(UNUSED, getIndicesInRange(14, 52)).put(P, "RankInC5C8").build();

	private final Map<String, Object> illegalBDrawFromOtherAndPile = ImmutableMap
			.<String, Object> builder()
			.put(turn, W)
			.put(W, getIndicesInRange(9, 11))
			.put(B,
					concat(getCardIndex(12),
							concat(getIndicesInRange(1, 3), getCardIndex(8))))
			.put(USED, getIndicesInRange(4, 7))
			.put(UNUSED, getIndicesInRange(13, 52)).put(P, "RankInC5").build();

	private final Map<String, Object> illegalWPlayTwice = ImmutableMap
			.<String, Object> builder().put(turn, W)
			.put(W, getIndicesInRange(8, 11)).put(B, getIndicesInRange(1, 4))
			.put(USED, getIndicesInRange(5, 7))
			.put(UNUSED, getIndicesInRange(12, 52)).put(P, "RankInC5").build();

	private List<String> getCardsInRange(int fromInclusive, int toInclusive) {
		return gameLogic.getCardsInRange(fromInclusive, toInclusive);
	}

	private List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		return gameLogic.getIndicesInRange(fromInclusive, toInclusive);
	}
	
	private List<Integer> getCardIndex(int cardNumber){
		return gameLogic.getCardIndex(cardNumber);
	}

	@Test
	public void testCardsInRange() {
		assertEquals(ImmutableList.of("C3", "C4"),
				gameLogic.getCardsInRange(3, 4));
	}

	private List<String> getCard(int cardNumber) {
		return gameLogic.getCard(cardNumber);
	}

	@Test
	public void testGetCard() {
		assertEquals(ImmutableList.of("C50"), gameLogic.getCard(50));
	}
	
	@Test
	public void testGetCardIndex(){
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

	private void assertMoveOk(VerifyMove verifyMove) {
		VerifyMoveDone verifyDone = new GameLogic().verify(verifyMove);
		assertEquals(new VerifyMoveDone(), verifyDone);
	}

	private void assertHacker(VerifyMove verifyMove) {
		VerifyMoveDone verifyDone = new GameLogic().verify(verifyMove);
		assertEquals(new VerifyMoveDone(verifyMove.getLastMovePlayerId(),
				"Hacker found"), verifyDone);
	}

}
