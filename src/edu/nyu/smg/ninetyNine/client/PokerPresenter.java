package edu.nyu.smg.ninetyNine.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.nyu.smg.ninetyNine.client.GameApi.*;

/**
 * The presenter that controls the poker99 graphics. We use the MVP pattern: the
 * model is {@link PokerState}, the view will have the poker99 graphics and it
 * will implement {@link PokerPresenter.View}, and the presenter is
 * {@link PokerPresenter}.
 */

public class PokerPresenter {

	/**
	 * The possible poker messages. INVISIBLE: the game will simply go on and
	 * the player should choose a card to play. This card should be a numerical
	 * value card or wild card except the minus card. NEXT_MOVE_SUB: the player
	 * should choose card with Rank 10 or face card Queen serving as a negative
	 * value card. HAS_WINNER: to suggest that we have a winner right now.
	 */
	public enum PokerMessage {
		INVISIBLE, NEXT_MOVE_SUB, HAS_WINNER;
	}

	public interface View {
		/**
		 * Sets the presenter. The viewer will call certain methods on the
		 * presenter, e.g., when a card is selected ({@link #cardSelected}),
		 * when selection is done ({@link #finishedSelectingCards}), etc.
		 * 
		 * The process of playing a card looks as follows to the viewer: 1)
		 * (Optional)The viewer will set IS_SUB field; If the IS_SUB field is
		 * set to be true, the view would receive the
		 * {@link PokerMessage#NEXT_MOVE_SUB} message 2) The viewer calls
		 * {@link #cardSelected} a couple of times to select the cards to drop
		 * to the middle pile 3) The viewer calls
		 * {@link #finishedSelectingCards} to finalize his selection
		 */
		void setPresenter(PokerPresenter pokerPresenter);

		/** Sets the state for a viewer, i.e., not one of the players. */
		void setViewerState(int numberOfWhiteCards, int numberOfBlackCards,
				int numberOfCardsInUsedPile, int numberOfCardsInUnusedPile,
				int point, boolean isClockwise, PokerMessage pokerMessage);

		/**
		 * Sets the state for a player (whether the player has the turn or not).
		 */
		void setPlayerState(int numberOfOpponentCards,
				int numberOfCardsInUsedPile, int numberOfCardsInUnusedPile,
				List<Card> myCards, int point, boolean isClockwise,
				PokerMessage pokerMessage);

		/**
		 * Asks the player to choose the next card or finish his selection. We
		 * pass what cards are selected (those cards will be dropped to the
		 * middle pile), and what cards will remain in the player hands. The
		 * user can either select a card (by calling {@link #cardSelected), or
		 * finish selecting (by calling {@link #finishedSelectingCards}; only
		 * allowed if selectedCards.size==1). If the user selects a card from
		 * selectedCards, then it moves that card to remainingCards. If the user
		 * selects a card from remainingCards, then it moves that card to
		 * selectedCards.
		 */
		void chooseNextCard(List<Card> selectedCards, List<Card> remainingCards);

		/**
		 * Asks the player to set the IS_SUB field in the game state. If
		 * selected, the next move will be playing a minus wild card.
		 */
		void chooseNextMoveSub(boolean isSub);
	}

	/*
	 * playerIds is elicited from playersInfo(Map(<String>,<Obejct>)), thus it
	 * only contains the actual players Id.
	 */
	private View pokerView;
	private GameLogic gameLogic;
	private Container container;
	private Optional<ColorOfPlayer> myColor;
	/*
	 * The state don't contain info about which player has the turn. Since every
	 * move contains the SetTurn() operation, we can get the turn via lastMove.
	 */
	private List<Integer> playerIds = null;
	private ColorOfPlayer thisTurnColor = null;
	private PokerState pokerState = null;
	private List<Card> selectedCards = null;

	public PokerPresenter(Container container, View pokerView) {
		gameLogic = new GameLogic();
		this.container = container;
		this.pokerView = pokerView;
		pokerView.setPresenter(this);
	}

	/** Updates the presenter and the view with the state in updateUI. */
	public void updateUI(UpdateUI updateUI) {
		playerIds = updateUI.getPlayerIds();
		int yourPlayerId = updateUI.getYourPlayerId();
		int yourPlayerIndex = updateUI.getPlayerIndex(yourPlayerId);
		myColor = yourPlayerIndex == 0 ? Optional.of(ColorOfPlayer.W)
				: yourPlayerIndex == 1 ? Optional.of(ColorOfPlayer.B)
						: Optional.<ColorOfPlayer> absent();
		/*
		 * Here a little tricky part: Optional.<ColorOfPlayer>absent(); If there
		 * is not this generic type, this means that <T extends Object>.
		 * However, the ENUM is not a subclass of object.
		 */
		if (updateUI.getState().isEmpty()) {
			// The W player sends the initial setup move.
			if (myColor.isPresent() && myColor.get().isWhite()) {
				sendInitialMove(playerIds);
			}
			return;
		}
		/*
		 * If the current state is empty, it's the initial procedure and the
		 * presenter will send the initial moves(No need to update UI in this
		 * procedure). If the unused pile in the current state is empty, the
		 * presenter should send the operations to shuffle the used pile(Also no
		 * need to update UI right now).
		 */
		List<Operation> lastMove = updateUI.getLastMove();
		for (Operation operation : lastMove) {
			if (operation instanceof SetTurn) {
				int thisTurnPlayerId = ((SetTurn) operation).getPlayerId();
				thisTurnColor = ColorOfPlayer.values()[playerIds
						.indexOf(thisTurnPlayerId)];
			}
		}
		// current game state
		pokerState = gameLogic.gameApiStateToPokerState(updateUI.getState(),
				thisTurnColor, playerIds);
		/*
		 * The shuffle move needs the current state and the turn of player. So
		 * we need to initiate the pokerState, thisTurnColor and playerIds data
		 * field. The UI here will not get updated. And the shuffle operation
		 * should be sent by the player who has the turn. The view and the other
		 * player will do nothing.
		 */
		if (pokerState.getUnused().size() == 0) {
			if (isMyTurn()) {
				sendNextNewRound();
			}
			return;
		} 
		selectedCards = Lists.newArrayList();

		if (updateUI.isAiPlayer()) {
			// container.sendMakeMove(..);
			return;
		}
		if (updateUI.isViewer()) {
			pokerView.setViewerState(pokerState.getWhite().size(), pokerState
					.getBlack().size(), pokerState.getUsed().size(), pokerState
					.getUnused().size(), pokerState.getPoints(), isClockWise(),
					getPokerMessage());
			return;
		}
		// this must be a player
		ColorOfPlayer myC = myColor.get();
		ColorOfPlayer opponentColorOfPlayer = myC.getOppositeColor();
		List<Integer> opponentCards = pokerState
				.getWhiteOrBlack(opponentColorOfPlayer);
		List<Integer> unusedPile = pokerState.getUnused();
		List<Integer> usedPile = pokerState.getUsed();
		pokerView.setPlayerState(opponentCards.size(), usedPile.size(),
				unusedPile.size(), getMyCards(), pokerState.getPoints(),
				isClockWise(), getPokerMessage());
		if (isMyTurn()) {
			// it's your move
			/*
			 * In your move, you might want to choose a card to play or want to
			 * set the IS_SUB field to be true. Here I suppose that in this
			 * updateUI method, the player's next move is to play a card. When
			 * it comes to the set IS_SUB operation, the player will call a
			 * method on the view and the view will call corresponding method in
			 * the presenter. The method in the presented will call the
			 * container to generate the operations.
			 */
			if (opponentCards.size() > 0 && !pokerState.isGameOver()) {
				chooseNextCard();
			}
		}

	}

	/**
	 * Add/Remove card from the {@link #selectedCards}. The view can only call
	 * this method if the presenter called {@link View#chooseNextCard}
	 */
	public void cardSelected(Card card) {
		check(isMyTurn() && !pokerState.isGameOver());
		if (selectedCards.contains(card)) {
			selectedCards.remove(card);
		} else if (!selectedCards.contains(card) && selectedCards.size() == 0) {
			selectedCards.add(card);
		} else if (!selectedCards.contains(card) && selectedCards.size() == 1) {
			selectedCards.clear();
			selectedCards.add(card);
		}
	}

	/**
	 * Finishes the card selection process. The view can only call this method
	 * if the presenter called {@link View#chooseNextCard} and one card was
	 * selected by calling {@link #cardSelected}.
	 */
	public void finishedSelectingCards() {
		check(isMyTurn() && !selectedCards.isEmpty());
		container.sendMakeMove(gameLogic.getExpectedOperations(
				findCardIndex(selectedCards), pokerState, playerIds, null));
	}

	/**
	 * The container will send a series of operations to move the cards in the
	 * used pile to the unused pile to start a new round.
	 */
	void sendNextNewRound() {
		container.sendMakeMove(gameLogic.getNewRoundOperations(pokerState,
				thisTurnColor, playerIds));
	}

	/*
	 * The current player chooses a card. Since he could see the card, the index
	 * of the card could be got from the game state in the presenter.
	 */
	private List<Integer> findCardIndex(List<Card> listCard) {
		checkNotNull(pokerState);
		return ImmutableList.<Integer> of(pokerState.getCards().indexOf(
				listCard.get(0)));
	}

	private boolean isClockWise() {
		checkNotNull(pokerState);
		return pokerState.getDirection().isClockwise();
	}

	/**
	 * The view(i.e the actual player) choose to set the IS_SUB field. This
	 * method should be called by {@link View#chooseNextMoveSub(boolean)}.
	 */
	void pokerViewChooseToSetIsSub() {
		container.sendMakeMove(gameLogic.getNextMoveSub(thisTurnColor,
				playerIds));
	}

	private void chooseNextCard() {
		pokerView.chooseNextCard(ImmutableList.<Card> copyOf(selectedCards),
				gameLogic.subtract(getMyCards(), selectedCards));
	}

	private List<Card> getMyCards() {
		checkNotNull(pokerState);
		List<Card> myCards = Lists.newArrayList();
		ImmutableList<Optional<Card>> cards = pokerState.getCards();
		for (Integer cardIndex : pokerState.getWhiteOrBlack(myColor.get())) {
			myCards.add(cards.get(cardIndex).get());
		}
		return myCards;
	}

	private void sendInitialMove(List<Integer> playersId) {
		container.sendMakeMove(gameLogic.getInitialMove(playersId));
	}

	private PokerMessage getPokerMessage() {
		checkNotNull(pokerState);
		if (pokerState.isSub()) {
			return PokerMessage.NEXT_MOVE_SUB;
		} else if (pokerState.isGameOver()) {
			return PokerMessage.HAS_WINNER;
		} else {
			return PokerMessage.INVISIBLE;
		}
	}

	private boolean isMyTurn() {
		checkNotNull(pokerState);
		return myColor.isPresent() && myColor.get() == pokerState.getTurn();
	}

	private void check(boolean val) {
		if (!val) {
			throw new IllegalArgumentException();
		}
	}
}
