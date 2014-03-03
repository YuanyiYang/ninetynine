package edu.nyu.smg.ninetyNine.client;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.nyu.smg.ninetyNine.client.DirectionsOfTurn;

public class DirectionOfTurnTest {

	@Test
	public void valueOftest() {
		assertEquals(DirectionsOfTurn.Clockwise, DirectionsOfTurn.valueOf("Clockwise"));
		assertEquals(DirectionsOfTurn.AntiClockwise, DirectionsOfTurn.valueOf("AntiClockwise"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void valueOfIllegalArgument(){
		DirectionsOfTurn.valueOf("clockwise");
	}
}
