/**
 * @author Marek Tomaszewicz
 * Initializes a game of BlackJack
 */
public class Main {
	public static void main(String[] args) {
		Logic game = Logic.newGame();
		while(true) {
			System.out.println("You have $" + game.money());
			game.newHand();
			game.anotherHand();
		}
	}
}
