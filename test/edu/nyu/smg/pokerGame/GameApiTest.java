package edu.nyu.smg.pokerGame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.nyu.smg.pokerGame.GameApi.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@RunWith(JUnit4.class)
public class GameApiTest {
  Map<String, Object> strToObj = ImmutableMap.<String, Object>of("playerId", 3);
  ImmutableList<Map<String, Object>> playersInfo =
      ImmutableList.<Map<String, Object>>of(strToObj, strToObj);
  Map<String, Object> state = ImmutableMap.<String, Object>of("key", 34, "key2", "dsf");
  Set set = new Set("k", "sd");
  SetRandomInteger setRandomInteger = new SetRandomInteger("xcv", 23, 54);
  List<Operation> operations = Arrays.asList(set, setRandomInteger, set);

  List<HasEquality> messages =
      Arrays.<HasEquality>asList(
          new UpdateUI(42, playersInfo, state),
          new VerifyMove(42, playersInfo, state, state, operations, 23),
          set, setRandomInteger,
          new EndGame(32),
          new SetVisibility("sd"),
          new Shuffle(Lists.newArrayList("xzc", "zxc")),
          new GameReady(),
          new MakeMove(operations),
          new VerifyMoveDone(),
          new VerifyMoveDone(23, "asd"),
          new RequestManipulator(),
          new ManipulateState(state),
          new ManipulationDone(operations)
          );

  @Test
  public void testSerialization() {
    for (HasEquality equality : messages) {
      assertEquals(equality, HasEquality.messageToHasEquality(equality.toMessage()));
    }
  }

  @Test
  public void testEquals() {
    for (HasEquality equality : messages) {
      for (HasEquality equalityOther : messages) {
        if (equality != equalityOther) {
          assertNotEquals(equality, equalityOther);
        }
      }
    }

  }
}
