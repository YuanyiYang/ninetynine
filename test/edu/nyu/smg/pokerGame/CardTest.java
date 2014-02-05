package edu.nyu.smg.pokerGame;

import static org.junit.Assert.*;

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

}
