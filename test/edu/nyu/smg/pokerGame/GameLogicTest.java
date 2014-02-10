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

	  private final int wId = 41;
	  private final int bId = 42;
	  private final String playerId = "playerId";
	  private final String turn = "turn"; // turn of which player (either W or B)
	  private static final String W = "W"; // White hand
	  private static final String B = "B"; // Black hand
	  private static final String USED = "USED"; // Used pile
	  private static final String UNUSED = "UNUSED"; //Unused pile
	  private static final String C = "C"; // Card key (C1 .. C52)
	  private static final String P = "Points"; 
//	  private final String claim = "claim"; // a claim has the form: [3cards, rankK]	  
	  private final List<Integer> visibleToW = ImmutableList.<Integer>of(wId);
	  private final List<Integer> visibleToB = ImmutableList.<Integer>of(bId);
	  private final List<Integer> visibleToNull = ImmutableList.<Integer>of();
	  private final Map<String, Object> wInfo = ImmutableMap.<String, Object>of(playerId, wId);
	  private final Map<String, Object> bInfo = ImmutableMap.<String, Object>of(playerId, bId);
	  private final List<Map<String, Object>> playersInfo = ImmutableList.of(wInfo, bInfo);
	  private final Map<String, Object> emptyState = ImmutableMap.<String, Object>of();
	  private final Map<String, Object> nonEmptyState = ImmutableMap.<String, Object>of("k", "v");
	  
	  private final Map<String, Object> turnOfWEmptyUsed = ImmutableMap.<String, Object>builder()
				.put(turn,W)
				.put(W, getCardsInRange(1, 5))
				.put(B, getCardsInRange(6, 10))
				.put(USED, ImmutableList.of())
				.put(UNUSED, getCardsInRange(11, 52))
				.put(P, 0)
				.build();
	
	private final Map<String, Object> turnOfBEmptyUsed = ImmutableMap.<String, Object>builder()
			.put(turn,B)
			.put(W, getCardsInRange(1, 5))
			.put(B, getCardsInRange(6, 10))
			.put(USED, ImmutableList.of())
			.put(UNUSED, getCardsInRange(11, 52))
			.put(P, 0)
			.build();

	private final Map<String, Object> WPlayANormalCard = ImmutableMap.<String, Object>builder()
			.put(turn, B)
			.put(W, concat(getCardsInRange(1, 4), getCard(11)))
			.put(B, getCardsInRange(6, 10))
			.put(USED, getCard(5))
			.put(UNUSED, getCardsInRange(12, 52))
			.put(P, "RankInC5")
			.build();
	
	private final Map<String, Object> BPlayAExchangeCard = ImmutableMap.<String, Object>builder()
			.put(turn, W)
			.put(W, getCardsInRange(7, 10))
			.put(B, concat(getCardsInRange(1, 4), getCard(11)))
			.put(USED, getCardsInRange(5, 6))
			.put(UNUSED, getCardsInRange(12, 52))
			.put(P, "RankInC5")
			.build();
	
	private final Map<String, Object> WPlayDrawOtherCard = ImmutableMap.<String, Object>builder()
			.put(turn, B)
			.put(W, getCardsInRange(8, 11))
			.put(B, getCardsInRange(1, 4))
			.put(USED, getCardsInRange(5, 7))
			.put(UNUSED, getCardsInRange(12, 52))
			.put(P, "RankInC5")
			.build();
	
	private final Map<String,Object> illegalWPlayDrawMoreThanOne = 
			ImmutableMap.<String, Object>builder().put(turn, B)
			.put(W, getCardsInRange(9, 13))
			.put(B, getCardsInRange(1, 4))
			.put(USED, getCardsInRange(5, 8))
			.put(UNUSED, getCardsInRange(14, 52))
			.put(P, "RankInC5C8").build();
	
	private final Map<String, Object> illegalBDrawFromOtherAndPile = 
			ImmutableMap.<String, Object>builder().put(turn, W)
			.put(W, getCardsInRange(9, 11))
			.put(B, concat(getCard(12),concat(getCardsInRange(1, 3), getCard(8))))
			.put(USED, getCardsInRange(4, 7))
			.put(UNUSED, getCardsInRange(13, 52))
			.put(P, "p")
			.build();
			
			
	private List<String> getCardsInRange(int fromInclusive, int toInclusive){  
		List<String> cards = Lists.newArrayList();
		for(int i=fromInclusive;i<=toInclusive;i++){
			cards.add(C+i);
		}
		return cards;
	}
	
	private List<String> getCard(int cardNumber){
		List<String> card = Lists.newArrayList();
		card.add(C+cardNumber);
		return card;
	}
	
	private List<String> concat(List<String> a, List<String> b){
		return Lists.newArrayList(Iterables.concat(a,b));
	}
	

	private void assertMoveOk(VerifyMove verifyMove) {
	    VerifyMoveDone verifyDone = new GameLogic().verify(verifyMove);
	    assertEquals(new VerifyMoveDone(), verifyDone);
	  }

	  private void assertHacker(VerifyMove verifyMove) {
	    VerifyMoveDone verifyDone = new GameLogic().verify(verifyMove);
	    assertEquals(new VerifyMoveDone(verifyMove.getLastMovePlayerId(), "Hacker found"), verifyDone);
	  }
	  
	  
}
