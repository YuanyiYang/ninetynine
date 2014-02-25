package edu.nyu.smg.pokerGame;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * Representation of the cheat game state. The game state uses these keys: turn,
 * W, B, Used, UnUsed, C0...C51, Point, direction ,isSub and isGameOver which
 * are mapped to these fields: turn, white, black, usedPile, unUsedPile, cards,
 * points, direction of the game, whether this card will add up to the points or
 * minus from the points and do we have a winner right now
 */

public class PokerState {
	private final ColorOfPlayer turn;
	private final ImmutableList<Integer> playerIds;
	/**
	 * Note that some of the entries will have null, meaning the card is not
	 * visible to us.
	 */
	private final ImmutableList<Optional<Card>> cards;

	/**
	 * Index of the white cards, each integer is in the range [0-52).
	 */
	private final ImmutableList<Integer> white;
	private final ImmutableList<Integer> black;
	private final ImmutableList<Integer> used;
	private final ImmutableList<Integer> unused;
	private final boolean isSub;
	private final Integer points;
	private final DirectionsOfTurn direction;
	private final boolean isGameOver;

	public PokerState(ColorOfPlayer turn, ImmutableList<Optional<Card>> cards,
			ImmutableList<Integer> playerIds, ImmutableList<Integer> white,
			ImmutableList<Integer> black, ImmutableList<Integer> used,
			ImmutableList<Integer> unused, boolean isSub, Integer points,
			DirectionsOfTurn direction, boolean isGameOver) {
		this.turn = checkNotNull(turn);
		this.playerIds = checkNotNull(playerIds);
		this.cards = checkNotNull(cards);
		this.white = checkNotNull(white);
		this.black = checkNotNull(black);
		this.used = checkNotNull(used);
		this.unused = checkNotNull(unused);
		this.isSub = isSub;
		this.points = points;
		this.direction = checkNotNull(direction);
		this.isGameOver = isGameOver;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public DirectionsOfTurn getDirection() {
		return direction;
	}

	public ColorOfPlayer getTurn() {
		return turn;
	}

	public ImmutableList<Optional<Card>> getCards() {
		return cards;
	}

	public ImmutableList<Integer> getWhite() {
		return white;
	}

	public ImmutableList<Integer> getBlack() {
		return black;
	}

	public ImmutableList<Integer> getUsed() {
		return used;
	}

	public ImmutableList<Integer> getUnused() {
		return unused;
	}

	public boolean isSub() {
		return isSub;
	}

	public Integer getPoints() {
		return points;
	}

	public ImmutableList<Integer> getPlayerIds() {
		return playerIds;
	}

	public int getPlayerId(ColorOfPlayer color) {
		return playerIds.get(color.ordinal());
	}

	public ImmutableList<Integer> getWhiteOrBlack(ColorOfPlayer color) {
		return color.isWhite() ? white : black;
	}
}
