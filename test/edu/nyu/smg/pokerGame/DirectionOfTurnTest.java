package edu.nyu.smg.pokerGame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
