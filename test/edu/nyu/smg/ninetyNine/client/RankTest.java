package edu.nyu.smg.ninetyNine.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RankTest {

	private Rank rank1 = Rank.ACE;
	private Rank rank2 = Rank.TWO;
	private Rank rank3 = Rank.THREE;
	private Rank rank4 = Rank.FOUR;
	private Rank rank5 = Rank.FIVE;
	private Rank rank6 = Rank.SIX;
	private Rank rank7 = Rank.SEVEN;
	private Rank rank8 = Rank.EIGHT;
	private Rank rank9 = Rank.NINE;
	private Rank rank10 = Rank.TEN;
	private Rank rank11 = Rank.JACK;
	private Rank rank12 = Rank.QUEEN;
	private Rank rank13 = Rank.KING;

	@Test
	public void testGetFirstLetter() {
		assertEquals("1", Rank.VALUES[0].getFirstLetter());
		assertEquals("1", rank1.getFirstLetter());
		assertEquals("2", rank2.getFirstLetter());
		assertEquals("3", rank3.getFirstLetter());
		assertEquals("4", rank4.getFirstLetter());
		assertEquals("5", rank5.getFirstLetter());
		assertEquals("6", rank6.getFirstLetter());
		assertEquals("7", rank7.getFirstLetter());
		assertEquals("8", rank8.getFirstLetter());
		assertEquals("9", rank9.getFirstLetter());
		assertEquals("T", rank10.getFirstLetter());
		assertEquals("J", rank11.getFirstLetter());
		assertEquals("Q", rank12.getFirstLetter());
		assertEquals("K", rank13.getFirstLetter());	
	}
	
	@Test
	public void testFromFirstLetter(){
		assertEquals(rank10, Rank.fromFirstLetter("T"));
		assertEquals(rank9, Rank.fromFirstLetter("9"));
		assertEquals(rank8, Rank.fromFirstLetter("8"));
		assertEquals(rank7, Rank.fromFirstLetter("7"));
		assertEquals(rank6, Rank.fromFirstLetter("6"));
		assertEquals(rank5, Rank.fromFirstLetter("5"));
		assertEquals(rank4, Rank.fromFirstLetter("4"));
		assertEquals(rank3, Rank.fromFirstLetter("3"));
		assertEquals(rank2, Rank.fromFirstLetter("2"));
		assertEquals(rank1, Rank.fromFirstLetter("1"));
		assertEquals(rank11, Rank.fromFirstLetter("J"));
		assertEquals(rank12, Rank.fromFirstLetter("Q"));
		assertEquals(rank13, Rank.fromFirstLetter("K"));
	}

}
