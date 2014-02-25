package edu.nyu.smg.pokerGame;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.nyu.smg.pokerGame.GameApi.*;
import edu.nyu.smg.pokerGame.PokerPresenter.View;
import edu.nyu.smg.pokerGame.PokerPresenter.PokerMessage;

public class PokerPresenterTest {

	/*
	 * Here I left the cardSelected(Card card) and finishedSelectingCards(). The
	 * view should call these two methods. Since there is no implementation in
	 * the view, these two methods will not be invoked right now.
	 */

	private PokerPresenter pokerPresenter;
	private GameLogic gameLogic = new GameLogic();
	private View mockView;
	private Container mockContainer;

	private static final String W = "W"; // White hand
	private static final String B = "B"; // Black hand
	private static final String USED = "USED"; // Used pile
	private static final String UNUSED = "UNUSED"; // Unused pile
	private static final String C = "C"; // Card key (C1 .. C52)
	private static final String P = "Points"; // the total points of the game
	private static final String IS_SUB = "isSub";
	private static final String IS_GAMEOVER = "isGameOver";
	private static final String DIRECTION = "direction";
	private static final String YES = "yes";
	private static final String CLOCKWISE = "Clockwise";
	private static final String ANTICLOCKWISE = "AntiClockwise";
	private static final String PLAYER_ID = "playerId";
	private static final int view_Id = GameApi.VIEWER_ID;
	private final int wId = 41;
	private final int bId = 42;
	private final ImmutableList<Integer> playerIds = ImmutableList.of(wId, bId);
	private final ImmutableMap<String, Object> wInfo = ImmutableMap
			.<String, Object> of(PLAYER_ID, wId);
	private final ImmutableMap<String, Object> bInfo = ImmutableMap
			.<String, Object> of(PLAYER_ID, bId);
	private final ImmutableList<Map<String, Object>> playersInfo = ImmutableList
			.<Map<String, Object>> of(wInfo, bInfo);

	/* The interesting states that I'll test. */
	private final ImmutableMap<String, Object> emptyState = ImmutableMap
			.<String, Object> of();
	private final ImmutableMap<String, Object> emptyUsedState = createState(5,
			5, 0, 42, 0, false, false, true);
	private final ImmutableMap<String, Object> nonEmptyUsedState = createState(
			5, 5, 10, 32, 50, false, false, false);
	private final ImmutableMap<String, Object> nextMoveSubState = createState(
			10, 10, 10, 22, 40, true, false, true);
	private final ImmutableMap<String, Object> needShuffleState = createState(
			5, 5, 42, 0, 60, false, false, true);
	private final ImmutableMap<String, Object> hasWinnerPointsExceededState = createState(
			5, 5, 20, 22, 100, false, true, true);

	@Before
	public void setUp() throws Exception {
		mockContainer = Mockito.mock(Container.class);
		mockView = Mockito.mock(View.class);
		pokerPresenter = new PokerPresenter(mockContainer, mockView);
		verify(mockView).setPresenter(pokerPresenter);
	}

	@After
	public void tearDown() {
		verifyNoMoreInteractions(mockContainer);
		verifyNoMoreInteractions(mockView);
	}

	@Test
	public void testEmptyStateForW() {
		pokerPresenter.updateUI(createUpdateUI(wId, emptyState, 0)); // turnOfPlayerId==0
		verify(mockContainer).sendMakeMove(gameLogic.getInitialMove(playerIds));
	}

	@Test
	public void testEmptyStateForB() {
		pokerPresenter.updateUI(createUpdateUI(bId, emptyState, 0));
		verify(mockContainer, times(0)).sendMakeMove(
				ImmutableList.<Operation> of());
	}

	@Test
	public void testEmptyStateForViewer() {
		pokerPresenter.updateUI(createUpdateUI(view_Id, emptyState, 0));
		verify(mockContainer, never()).sendMakeMove(
				ImmutableList.<Operation> of());
	}

	@Test
	public void testEmptyUsedStateForViewer() {
		pokerPresenter.updateUI(createUpdateUI(view_Id, emptyUsedState, wId));
		verify(mockView).setViewerState(5, 5, 0, 42, 0, true,
				PokerMessage.INVISIBLE);
	}

	@Test
	public void testEmptyUsedStateForBInWTurn() {
		pokerPresenter.updateUI(createUpdateUI(bId, emptyUsedState, wId));
		verify(mockView).setPlayerState(5, 0, 42, getCards(5, 10), 0, true,
				PokerMessage.INVISIBLE);
	}

	@Test
	public void testEmptyUsedStateForWInWTurn() {
		pokerPresenter.updateUI(createUpdateUI(wId, emptyUsedState, wId));
		verify(mockView).setPlayerState(5, 0, 42, getCards(0, 5), 0, true,
				PokerMessage.INVISIBLE);
		verify(mockView).chooseNextCard(ImmutableList.<Card> of(),
				getCards(0, 5));
	}

	@Test
	public void testEmptyUsedStateForBInBTurn() {
		pokerPresenter.updateUI(createUpdateUI(bId, emptyUsedState, bId));
		verify(mockView).setPlayerState(5, 0, 42, getCards(5, 10), 0, true,
				PokerMessage.INVISIBLE);
		verify(mockView).chooseNextCard(ImmutableList.<Card> of(),
				getCards(5, 10));
	}

	@Test
	public void testEmptyUsedStateForWInBTurn() {
		pokerPresenter.updateUI(createUpdateUI(wId, emptyUsedState, bId));
		verify(mockView).setPlayerState(5, 0, 42, getCards(0, 5), 0, true,
				PokerMessage.INVISIBLE);
	}

	@Test
	public void testNonEmptyUsedStateForViewer() {
		pokerPresenter
				.updateUI(createUpdateUI(view_Id, nonEmptyUsedState, bId));
		verify(mockView).setViewerState(5, 5, 10, 32, 50, false,
				PokerMessage.INVISIBLE);
	}

	@Test
	public void testNonEmptyUsedStateForWTurnOfW() {
		pokerPresenter.updateUI(createUpdateUI(wId, nonEmptyUsedState, wId));
		verify(mockView).setPlayerState(5, 10, 32, getCards(0, 5), 50, false,
				PokerMessage.INVISIBLE);
		verify(mockView).chooseNextCard(ImmutableList.<Card> of(),
				getCards(0, 5));
	}

	@Test
	public void testNonEmptyUsedStateForBTurnOfW() {
		pokerPresenter.updateUI(createUpdateUI(bId, nonEmptyUsedState, wId));
		verify(mockView).setPlayerState(5, 10, 32, getCards(5, 10), 50, false,
				PokerMessage.INVISIBLE);
	}

	@Test
	public void testNonEmptyUsedStateForBTurnOfB() {
		pokerPresenter.updateUI(createUpdateUI(bId, nonEmptyUsedState, bId));
		verify(mockView).setPlayerState(5, 10, 32, getCards(5, 10), 50, false,
				PokerMessage.INVISIBLE);
		verify(mockView).chooseNextCard(ImmutableList.<Card> of(),
				getCards(5, 10));
	}

	@Test
	public void testNonEmptyUsedStateForWTurnOfB() {
		pokerPresenter.updateUI(createUpdateUI(wId, nonEmptyUsedState, bId));
		verify(mockView).setPlayerState(5, 10, 32, getCards(0, 5), 50, false,
				PokerMessage.INVISIBLE);
	}

	@Test
	public void testNeedShuffleStateWTurnOfW() {
		PokerState pokerState = gameLogic.gameApiStateToPokerState(
				needShuffleState, ColorOfPlayer.W, playerIds);
		pokerPresenter.updateUI(createUpdateUI(wId, needShuffleState, wId));
		verify(mockContainer).sendMakeMove(
				gameLogic.getNewRoundOperations(pokerState, ColorOfPlayer.W,
						playerIds));
	}

	@Test
	public void testNeedShuffleStateBTurnOfW() {
		PokerState pokerState = gameLogic.gameApiStateToPokerState(
				needShuffleState, ColorOfPlayer.W, playerIds);
		pokerPresenter.updateUI(createUpdateUI(bId, needShuffleState, wId));
		verify(mockContainer, never()).sendMakeMove(
				gameLogic.getNewRoundOperations(pokerState, ColorOfPlayer.W,
						playerIds));
	}

	@Test
	public void testNeedShuffleStateViewer() {
		PokerState pokerState = gameLogic.gameApiStateToPokerState(
				needShuffleState, ColorOfPlayer.B, playerIds);
		pokerPresenter.updateUI(createUpdateUI(view_Id, needShuffleState, wId));
		verify(mockContainer, never()).sendMakeMove(
				gameLogic.getNewRoundOperations(pokerState, ColorOfPlayer.W,
						playerIds));
	}

	@Test
	public void testNeedShuffleBTurnOfB() {
		PokerState pokerState = gameLogic.gameApiStateToPokerState(
				needShuffleState, ColorOfPlayer.B, playerIds);
		pokerPresenter.updateUI(createUpdateUI(bId, needShuffleState, bId));
		verify(mockContainer).sendMakeMove(
				gameLogic.getNewRoundOperations(pokerState, ColorOfPlayer.B,
						playerIds));
	}

	@Test
	public void testNeedShuffleWTurnOfB() {
		PokerState pokerState = gameLogic.gameApiStateToPokerState(
				needShuffleState, ColorOfPlayer.B, playerIds);
		pokerPresenter.updateUI(createUpdateUI(wId, needShuffleState, bId));
		verify(mockContainer, never()).sendMakeMove(
				gameLogic.getNewRoundOperations(pokerState, ColorOfPlayer.B,
						playerIds));
	}

	@Test
	public void testHasWinnerPointsExceedStateViewer() {
		pokerPresenter.updateUI(createUpdateUI(view_Id,
				hasWinnerPointsExceededState, wId));
		verify(mockView).setViewerState(5, 5, 20, 22, 100, true,
				PokerMessage.HAS_WINNER);
	}

	@Test
	public void testHasWinnerPointsExceedStateWTurnOfW() {
		pokerPresenter.updateUI(createUpdateUI(wId,
				hasWinnerPointsExceededState, wId));
		verify(mockView).setPlayerState(5, 20, 22, getCards(0, 5), 100, true,
				PokerMessage.HAS_WINNER);
	}

	@Test
	public void testHasWinnerPointsExceedStateBTurnOfW() {
		pokerPresenter.updateUI(createUpdateUI(bId,
				hasWinnerPointsExceededState, wId));
		verify(mockView).setPlayerState(5, 20, 22, getCards(5, 10), 100, true,
				PokerMessage.HAS_WINNER);
	}

	@Test
	public void testNextMoveSubViewer() {
		pokerPresenter.updateUI(createUpdateUI(view_Id, nextMoveSubState, wId));
		verify(mockView).setViewerState(10, 10, 10, 22, 40, true,
				PokerMessage.NEXT_MOVE_SUB);
	}

	@Test
	public void testNextMoveSubWTurnOfW() {
		pokerPresenter.updateUI(createUpdateUI(wId, nextMoveSubState, wId));
		verify(mockView).setPlayerState(10, 10, 22, getCards(0, 10), 40, true,
				PokerMessage.NEXT_MOVE_SUB);
		verify(mockView).chooseNextCard(ImmutableList.<Card> of(),
				getCards(0, 10));
	}

	@Test
	public void testNextMoveSubBTurnOfW() {
		pokerPresenter.updateUI(createUpdateUI(bId, nextMoveSubState, wId));
		verify(mockView).setPlayerState(10, 10, 22, getCards(10, 20), 40, true,
				PokerMessage.NEXT_MOVE_SUB);
	}

	private List<Card> getCards(int fromInclusive, int toExclusive) {
		List<Card> cards = Lists.newArrayList();
		for (int i = fromInclusive; i < toExclusive; i++) {
			Rank rank = Rank.values()[i / 4];
			Suit suit = Suit.values()[i % 4];
			cards.add(new Card(rank, suit));
		}
		return cards;
	}

	private ImmutableMap<String, Object> createState(int numberOfWhiteCards,
			int numberOfBlackCards, int numberOfUsedPile,
			int numberOfUnUsedPile, int point, boolean isSub,
			boolean isGameOver, boolean isClockWise) {
		Map<String, Object> gameApiState = Maps.newHashMap();
		gameApiState.put(W,
				gameLogic.getIndicesInRange(0, numberOfWhiteCards - 1));
		gameApiState.put(B, gameLogic.getIndicesInRange(numberOfWhiteCards,
				numberOfBlackCards + numberOfWhiteCards - 1));
		gameApiState.put(
				USED,
				gameLogic.getIndicesInRange(numberOfBlackCards
						+ numberOfWhiteCards, numberOfBlackCards
						+ numberOfWhiteCards + numberOfUsedPile - 1));
		gameApiState.put(UNUSED, gameLogic.getIndicesInRange(numberOfBlackCards
				+ numberOfWhiteCards + numberOfUsedPile, numberOfBlackCards
				+ numberOfWhiteCards + numberOfUsedPile + numberOfUnUsedPile
				- 1)); // i.e 51
		gameApiState.put(P, point);
		if (isSub) {
			gameApiState.put(IS_SUB, YES);
		}
		if (isGameOver) {
			gameApiState.put(IS_GAMEOVER, YES);
		}
		if (isClockWise) {
			gameApiState.put(DIRECTION, CLOCKWISE);
		} else {
			gameApiState.put(DIRECTION, ANTICLOCKWISE);
		}
		int i = 0;
		for (Card card : getCards(0, 52)) {
			gameApiState.put(C + (i++), card.getCardRank().getFirstLetter()
					+ card.getCardSuit().getFirstLetterLowerCase());
		}
		return ImmutableMap.<String, Object> copyOf(gameApiState);
	}

	private UpdateUI createUpdateUI(int yourPlayerId,
			Map<String, Object> state, int turnOfPlayerId) {
		// Our UI only looks at the current state
		// (we ignore: lastState, lastMovePlayerId,
		// playerIdToNumberOfTokensInPot)
		return new UpdateUI(yourPlayerId, playersInfo, state, emptyState,
				ImmutableList.<Operation> of(new SetTurn(turnOfPlayerId)), 0,
				ImmutableMap.<Integer, Integer> of());

	}
}
