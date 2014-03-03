package edu.nyu.smg.ninetyNine.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.nyu.smg.ninetyNine.client.GameApi;
import edu.nyu.smg.ninetyNine.client.GameApi.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@RunWith(JUnit4.class)
public class GameApiTest {
	Map<String, Object> strToObj = ImmutableMap.<String, Object> of("playerId",
			3);
	ImmutableList<Map<String, Object>> playersInfo = ImmutableList
			.<Map<String, Object>> of(strToObj, strToObj);
	Map<String, Object> state = ImmutableMap.<String, Object> of("key", 34,
			"key2", "dsf");
	Map<String, Object> lastState = ImmutableMap.<String, Object> of("bc", 34,
			"key2", true);

	Set set = new Set("k", "sd");
	SetRandomInteger setRandomInteger = new SetRandomInteger("xcv", 23, 54);
	List<Operation> operations = Arrays.asList(set, setRandomInteger, set);

	List<Message> messages = Arrays.<Message> asList(
			new UpdateUI(42, playersInfo, state, lastState, operations, 12,
					ImmutableMap.of(42, 1)), new VerifyMove(playersInfo, state,
					lastState, operations, 23, ImmutableMap.of(42, 33)), set,
			setRandomInteger, new EndGame(32), new SetVisibility("sd"),
			new Shuffle(Lists.newArrayList("xzc", "zxc")), new GameReady(),
			new MakeMove(operations), new VerifyMoveDone(), new VerifyMoveDone(
					23, "asd"), new RequestManipulator(), new ManipulateState(
					state), new ManipulationDone(operations));

	@Test
	public void testSerialization() {
		for (Message equality : messages) {
			assertEquals(equality,
					Message.messageToHasEquality(equality.toMessage()));
		}
	}

	@Test
	public void testEquals() {
		for (Message equality : messages) {
			for (Message equalityOther : messages) {
				if (equality != equalityOther) {
					assertNotEquals(equality, equalityOther);
				}
			}
		}
	}

	@Test
	public void testLegalCheckHasJsonSupportedType() {
		GameApi.checkHasJsonSupportedType(null);
		GameApi.checkHasJsonSupportedType(34);
		GameApi.checkHasJsonSupportedType(-342323);
		GameApi.checkHasJsonSupportedType(5.23);
		GameApi.checkHasJsonSupportedType("string");
		GameApi.checkHasJsonSupportedType(true);
		GameApi.checkHasJsonSupportedType(ImmutableList.of());
		GameApi.checkHasJsonSupportedType(ImmutableList.of(true, 1));
		GameApi.checkHasJsonSupportedType(ImmutableMap.of("key1", 1, "key2",
				false));
		GameApi.checkHasJsonSupportedType(ImmutableMap.of("key1", 1, "key2",
				ImmutableList.of(true, 1)));
	}

	@Test
	public void testIllegalCheckHasJsonSupportedType() {
		checkIllegalJsonSupportedType(45L);
		checkIllegalJsonSupportedType(new Date());
		// checkIllegalJsonSupportedType(Color.B);
		// checkIllegalJsonSupportedType(ImmutableList.of(true, Color.B));
		checkIllegalJsonSupportedType(ImmutableMap.of(true, 1));
		// checkIllegalJsonSupportedType(ImmutableMap.of("key1", 1, "key2",
		// Color.B));
	}

	private void checkIllegalJsonSupportedType(Object object) {
		try {
			GameApi.checkHasJsonSupportedType(object);
			fail();
		} catch (Exception expected) {
			// expected exception
		}
	}
}