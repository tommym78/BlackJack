/**
 * @author Marek Tomaszewicz
 * This class is used to represent a playing card
 */
public class Card {
	// These final arrays are used to correlate the parameters value and suit of a card to a name
	private final String[] TYPE = new String[]{"Spades", "Clubs", "Diamonds", "Hearts"}, PRICE = new String[] {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	private int suit, value;
	public Card(int value, int suit) {
		this.value = value;
		this.suit = suit;
	}
	// All of these are accessor methods
	public String toString() {
		return PRICE[value - 1] + " of " + TYPE[suit];
	}
	public int getValue() {
		if(value > 9) return 10;
		return value;
	}
	public boolean isAce() {
		return value == 1;
	}
}
