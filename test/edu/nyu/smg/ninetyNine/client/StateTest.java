package edu.nyu.smg.ninetyNine.client;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import edu.nyu.smg.ninetyNine.client.Card;
import edu.nyu.smg.ninetyNine.client.Player;

public class StateTest {

	ArrayList<Player> playerList = new ArrayList<Player>();
	Player playerOne = new Player(new ArrayList<Card>(),true, true,1);
	Player playerTwo = new Player(new ArrayList<Card>(),false,true,2);
	
	@Before
	public void setUp() throws Exception {	
		Map<String, Object> strToObj = ImmutableMap.<String, Object>of("playerId", 3);
		
		
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
