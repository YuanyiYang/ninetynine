/**
 * Suit class given in the lecture. 
 */
package edu.nyu.smg.pokerGame;
import java.util.Comparator;

public enum Suit {
	CLUBS, DIAMONDS, HEARTS, SPADES;

	  public static final Suit[] VALUES = values();
	 
	  public static final Comparator<Suit> DDZ_COMPARATOR = new Comparator<Suit>() {    
	    @Override
	    public int compare(Suit o1, Suit o2) {
	      return 0;
	    }
	  };

	  public Suit getNext() {
	    if (this == VALUES[VALUES.length - 1]) {
	      return VALUES[0];      
	    }
	    return values()[ordinal() + 1];
	  }

	  public Suit getPrev() {
	    if (this == VALUES[0]) {
	      return VALUES[VALUES.length - 1];      
	    }
	    return values()[ordinal() - 1];
	  }
}
