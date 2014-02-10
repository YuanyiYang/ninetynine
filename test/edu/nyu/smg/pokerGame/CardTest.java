package edu.nyu.smg.pokerGame;

import static org.junit.Assert.*;
import com.google.common.collect.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest {

	private Card card1;
	private Card card2;
	private Card card3;

	@Before
	public void setUp() throws Exception {
		card1 = new Card(Rank.ACE, Suit.CLUBS);
		card2 = new Card(Rank.ACE, Suit.CLUBS);
		card3 = new Card(Rank.EIGHT, Suit.CLUBS);
	}

	@Test
	public void equalsGuavaTest() {
		assertTrue(card1.equals(card2));
		assertFalse(card1.equals(card3));
		assertTrue(card1.hashCode() == card2.hashCode());
		assertTrue(card2.hashCode() != card3.hashCode());
		assertFalse(card3.equals(null));
	}

	/*
	 * This test method is to demonstrate that in the immutableList, the data field of the inner 
	 * object still could be changed.
	 */
	@Test
	public void ImmutableListTest(){
		List<Card> cards = ImmutableList.<Card>of(card1,card2);
		assertEquals(false, cards.get(0).isMinus());
		cards.get(0).setMinus(true);
		assertTrue(cards.get(0).isMinus());
	}
	
	/*
	 * The same test to test immutableMap.
	 */
	@Test
	public void ImmutableMapTest(){
		Map<String, Object> cards = ImmutableMap.<String, Object>of(
				"1",card1,
				"2",card2);
		assertFalse(((Card) cards.get("1")).isMinus());
		((Card)cards.get("1")).setMinus(true);
		assertTrue(((Card) cards.get("1")).isMinus());
	}
	
}
