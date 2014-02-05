/**
 * The design principal of this game state is that it don't depend on certain beginning state of the 
 * game. For instance, when initiated, the cards should be given by the caller class.
 */
package edu.nyu.smg.pokerGame;

import java.util.*;

public class State {

	/*
	 * Each player should be initialized with some cards, which is stored as a
	 * data field of Player
	 */
	private ArrayList<Player> players;
	/*
	 * The cards which has been used by the player before the deck is shuffled
	 */
	private ArrayList<Card> usedCards;
	/*
	 * The cards in the deck and will be drawn by the player
	 */
	private ArrayList<Card> unUsedCards;
	/*
	 * The current player
	 */
	private Player currentPlayer;
	/*
	 * The current direction of the game
	 */
	private DirectionsOfTurn directionOfGame;
	/*
	 * Whether the game is over or not
	 */
	private boolean isGameOver;
	/*
	 * The total points of the game
	 */
	private int points;
	
	/*
	 * This method is called when there are no cards in the unUsedCards pile. It will shuffle 
	 * the cards in the usedCards pile into the unUsedCards pile.
	 */
	private void shuffleCardsInTheDeck(){
		
	}
	
	/*
	 * This method is to determine whether it is needed to shuffle the cards. It should be 
	 * called before the shuffleCardsInTheDeck method.
	 */
	private boolean isNeededShuffled(){
		return false;
	}
	
	private void reverseDirection(){
		if(this.directionOfGame.equals(DirectionsOfTurn.Clockwise)){
			this.directionOfGame = DirectionsOfTurn.AntiClockwise;
		}else{
			this.directionOfGame = DirectionsOfTurn.Clockwise;
		}
	}
	
	/*
	 * Try to find the card in the current player's hand. This method should be called before
	 * the card is played.
	 */
	private boolean isCurrentPlayerHoldTheCard(Card card){
		return false;
	}
	
	/*
	 * This method should be called after the current player making a successful move. The token
	 * should be passed to the next player in the player list.
	 */
	private void moveToNextPlayer(Player currentPlayer){
		
	}
	
	/*
	 * This method should be called after a player successfully player a card
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	

	protected State() {
		players = new ArrayList<Player>();
		usedCards = new ArrayList<Card>();
		unUsedCards = new ArrayList<Card>();
		currentPlayer = null;
		directionOfGame = DirectionsOfTurn.Clockwise;
		isGameOver = false;
		points = 0;
	}

	protected State(ArrayList<Player> players, ArrayList<Card> usedCards,
			ArrayList<Card> unUsedCards, Player currentPlayer,
			DirectionsOfTurn direction, boolean isGameOver, int points) {
		this.players = players;
		this.usedCards = usedCards;
		this.unUsedCards = unUsedCards;
		this.currentPlayer = currentPlayer;
		this.directionOfGame = direction;
		this.isGameOver = isGameOver;
		this.points = points;
	}

	public State(ArrayList<Player> players, ArrayList<Card> unUsedCards) {
		this(players, new ArrayList<Card>(), unUsedCards, null,
				DirectionsOfTurn.Clockwise, false, 0);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Card> getUsedCards() {
		return usedCards;
	}

	public void setUsedCards(ArrayList<Card> usedCards) {
		this.usedCards = usedCards;
	}

	public ArrayList<Card> getUnUsedCards() {
		return unUsedCards;
	}

	public void setUnUsedCards(ArrayList<Card> unUsedCards) {
		this.unUsedCards = unUsedCards;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public DirectionsOfTurn getDirectionOfGame() {
		return directionOfGame;
	}

	public void setDirectionOfGame(DirectionsOfTurn directionOfGame) {
		this.directionOfGame = directionOfGame;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
