package edu.nyu.smg.ninetyNine.client;

import static org.junit.Assert.*;

import com.google.common.collect.*;

import edu.nyu.smg.ninetyNine.client.Card;
import edu.nyu.smg.ninetyNine.client.Rank;
import edu.nyu.smg.ninetyNine.client.Suit;

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

	
	/*
	 * The same test to test immutableMap.
	 */

	
}
