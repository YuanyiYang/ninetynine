package edu.nyu.smg.ninetyNine.client;

import java.util.ArrayList;
import com.google.gwt.thirdparty.guava.common.base.Objects;

public class Player {

	/*
	 * Represent the cards the player have
	 */
	private ArrayList<Card> cards;
	/*
	 * Determine whether it is the player's move
	 */
	private boolean isYourMove;
	/*
	 * Determine whether this player is still in the player or not. This should be used in the 
	 * future when this game is implemented in elimination
	 */
	private boolean isStillPlaying;
	/*
	 * unique ID representing the player
	 */
	private int playerID;
	
	public Player(){
		cards = new ArrayList<Card>();
		isYourMove = false;
		isStillPlaying = false;
		playerID = 0;
	}
	
	public Player(ArrayList<Card> cards, boolean isYourMove, boolean isStillPlaying, int playerID){
		this.cards = cards;
		this.isYourMove = isYourMove;
		this.isStillPlaying = isStillPlaying;
		this.playerID = playerID;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null) return false;
		if(!(obj instanceof Player)) {
			return false;
		}else{
			return this.playerID == ((Player)obj).getPlayerID();
		}
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(this.playerID);
	}
	
	@Override
	public String toString(){
		return "The player is " + this.getPlayerID();
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
	public boolean isYourMove() {
		return isYourMove;
	}
	public void setYourMove(boolean isYourMove) {
		this.isYourMove = isYourMove;
	}
	public boolean isStillPlaying() {
		return isStillPlaying;
	}
	public void setStillPlaying(boolean isStillPlaying) {
		this.isStillPlaying = isStillPlaying;
	}
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	
	
}
