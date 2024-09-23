/**
 * @author Marek Tomaszewicz
 * This class simulates the shoe on the blackjack table
 */
import java.util.ArrayList;
public class Shoe {
	private ArrayList<Card> deck, discard;
	private final int nOfDecks = 6, shoeSize = nOfDecks * 52, shufCard = shoeSize / 4;
	public Shoe() {
		discard = new ArrayList<>(shufCard);
		deck = new ArrayList<>(shoeSize);
		for(int i = 0; i < nOfDecks; i++) {
			for(int j = 0; j < 4; j++) {
				for(int k = 0; k < 13; k++) {
					deck.add(new Card(k + 1, j));
				}
			}
		}
		this.shuffle();
	}
	private static int randInt(int fro, int to) {
		return fro + (int)(Math.random() * (to - fro));
	}
	private void deckSwap(int pos0, int pos1) {
		Card m = deck.get(pos0);
		Card n = deck.get(pos1);
		if(pos0 > pos1) {
			deck.add(pos0, n);
			deck.remove(pos0 + 1);
			deck.add(pos1, m);
			deck.remove(pos1 + 1);
			return;
		}
		deck.add(pos1, m);
		deck.remove(pos1 + 1);
		deck.add(pos0, n);
		deck.remove(pos0 + 1);
	}
	public void shuffle() {
		for(Card c : discard) {
			deck.add(c);
		}
		discard.clear();
		for(int i = 0; i < shoeSize; i++) {
			deckSwap(i, randInt(i, shoeSize - 1));
		}
	}
	public Card nextCard() {
		return deck.remove(0);
	}
	public void discard(Card c) {
		discard.add(c);
	}
	public boolean needsShuffle() {
		return discard.size() > 3 * shufCard;
	}
}
