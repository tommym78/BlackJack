/**
 * @author Marek Tomaszewicz
 * This class simulates the BlackJack table
 */
import java.util.ArrayList;
import java.util.Scanner;

public class Logic {
	private int money;
	private Shoe boot;
	public static Scanner s = new Scanner(System.in);
	public Logic(int money) {
		if(money < 1) {
			this.money = 1;
			System.out.println("Because you tried to be funny, you now only start with 1 dollar.");
		}
		else this.money = money;
		boot = new Shoe();
	}
	public static Logic newGame() {
		int n;
		System.out.println("Enter Starting Cash:");
		n = s.nextInt();
		return new Logic(n);
	}
	public void anotherHand() {
		int n;
		System.out.println("Do you want to play another hand? Type 1 if Yes, anything else to exit");
		n = s.nextInt();
		if(n == 1) this.newHand();
		else {
			System.out.println("You ended with $" + this.money);
			System.exit(0);
		}
	}
	public int money() {
		return money;
	}
	private static boolean hasBlackJack(ArrayList<Card> c) {
		boolean ace = false;
		int sum = 0;
		for(int i = 0; i < 2; i++) {
			if(c.get(i).isAce()) ace = true;
			sum += c.get(i).getValue();
		}
		return ace & sum == 11;
	}
	private static boolean canSplit(ArrayList<Card> c) {
		return c.size() == 2 & c.get(0).getValue() == c.get(1).getValue();
	}
	private static int getValue(ArrayList<Card> c) {
		ArrayList<Integer> ns = new ArrayList<Integer>();
		int val;
		ns.add(0);
		for(int i = 0; i < c.size(); i++) {
			if(c.get(i).isAce()) {
				for(int j = 0; j < ns.size(); j += 2) {
					ns.add(j, ns.remove(j) + 1);
					ns.add(j + 1, ns.get(j) + 10);
				}
			}
			else {
				val = c.get(i).getValue();
				for(int j = 0; j < ns.size(); j++) {
					ns.add(j, ns.remove(j) + val);
				}
			}
		}
		int n = ns.get(0);
		for(int i = 0; i < ns.size(); i++) {
			if(ns.get(i) > 21) {
				ns.remove(i);
				i--;
			} 
			else {
				if(ns.get(i) > n) n = ns.get(i);
			}
		}
		if(ns.size() == 0) return 200;
		return n;
	}
	private int getAction(ArrayList<Card> c) {
		System.out.println("Enter 1 for Hit, 2 for Stand, 3 for Double-Down, 4 for Split");
		int m = s.nextInt();
		if(m < 1 || m > 4) {
			System.out.println("Invalid Input");
			return getAction(c);
		}
		if(m == 4) {
			if(canSplit(c)) return 3;
			else {
				System.out.println("You Cannot Split These Cards");
				return getAction(c);
			}
		}
		if(m == 3) {
			if(c.size() == 2) return 2;
			else {
				System.out.println("You Cannot Double-Down");
				return getAction(c);
			}
		}
		return m - 1;
	}
	public int newHand() {
		int bet, action, tempVal, j, totalBet, z, a;
		ArrayList<Boolean> pHandActions;
		boolean tHasBJ, pHasBJ, dAction;
		ArrayList<Card> tHand;
		ArrayList<ArrayList<Card>> pHand;
		ArrayList<Integer> bets;
		boot.shuffle();
		while(!boot.needsShuffle()) {
			tHand = new ArrayList<Card>();
			bets = new ArrayList<Integer>();
			dAction = true;
			pHandActions = new ArrayList<Boolean>();
			pHand = new ArrayList<ArrayList<Card>>();
			pHandActions.add(true);
			tHasBJ = false; 
			pHasBJ = false;
			pHand.clear();
			pHand.add(new ArrayList<Card>());
			tHand.clear();
			do {
				System.out.println("Enter a bet amount:");
				bet = s.nextInt();
			} while(bet > money || bet < 1);
			totalBet = bet;
			pHand.get(0).add(boot.nextCard());
			tHand.add(boot.nextCard());
			pHand.get(0).add(boot.nextCard());
			tHand.add(boot.nextCard());
			tHasBJ = hasBlackJack(tHand);
			pHasBJ = hasBlackJack(pHand.get(0));
			System.out.println("Dealer shows a " + tHand.get(0));
			if(!tHasBJ & !pHasBJ) {
				for(int i = 0; i < pHand.size(); i++) {
					System.out.print("You have a ");
					
					for(j = 0; j < pHand.get(i).size() - 1; j++) {
						System.out.print(pHand.get(i).get(j) + " and a ");
					}
					System.out.println(pHand.get(i).get(j));
					while(pHandActions.get(i)) {
						action = getAction(pHand.get(i));
						if(totalBet + bet > money & action > 1) {
							System.out.println("You do not have enough money to perform this action");
						}
						else {
							if(action == 2 || action == 1) {
								if(action == 2) {
									pHand.get(i).add(boot.nextCard());
									System.out.println("You Double-Down");
									totalBet += bet;
								}
								if(action == 1) {
									System.out.println("You Stand");
								}
								System.out.print("You have a ");
								for(j = 0; j < pHand.get(i).size() - 1; j++) {
									System.out.print(pHand.get(i).get(j) + " and a ");
								}
								System.out.println(pHand.get(i).get(j));
								pHandActions.add(i, false);
							}
							else if(action == 3) {
								pHand.add(i + 1, new ArrayList<Card>());
								pHand.get(i + 1).add(pHand.get(i).remove(1));
								pHand.get(i).add(boot.nextCard());
								pHand.get(i + 1).add(boot.nextCard());
								totalBet += bet;
								System.out.println("You Split");
								System.out.print("You have a ");
								for(j = 0; j < pHand.get(i).size() - 1; j++) {
									System.out.print(pHand.get(i).get(j) + " and a ");
								}
								System.out.println(pHand.get(i).get(j));
							}
							else {
								pHand.get(i).add(boot.nextCard());
								System.out.println("You Hit");
								System.out.print("You have a ");
								for(j = 0; j < pHand.get(i).size() - 1; j++) {
									System.out.print(pHand.get(i).get(j) + " and a ");
								}
								System.out.println(pHand.get(i).get(j));
							}
							tempVal = getValue(pHand.get(i));
							if(!(action == 2 || action == 1) & tempVal > 20) pHandActions.add(i, false);
							if(action == 2) bets.add(2 * bet);
							else bets.add(bet);
						}
					}
				}
				tempVal = getValue(tHand);
				while(dAction) {
					System.out.print("Dealer has a ");
					for(j = 0; j < tHand.size() - 1; j++) {
						System.out.print(tHand.get(j) + " and a ");
					}
					System.out.println(tHand.get(j));
					if(tempVal > 16) {
						System.out.println("Dealer Stands");
						dAction = false;
					}
					else {
						System.out.println("Dealer Hits");
						tHand.add(boot.nextCard());
						tempVal = getValue(tHand);
					}
				}
			}
			else {
				bets.add(bet);
				System.out.print("You have a ");
				for(j = 0; j < pHand.get(0).size() - 1; j++) {
					System.out.print(pHand.get(0).get(j) + " and a ");
				}
				System.out.println(pHand.get(0).get(j));
				System.out.print("Dealer has a ");
				for(j = 0; j < tHand.size() - 1; j++) {
					System.out.print(tHand.get(j) + " and a ");
				}
				System.out.println(tHand.get(j));
			}
			for(int i = 0; i < pHand.size(); i++) {
				z = i + 1;
				if(getValue(pHand.get(i)) > 21) {
					System.out.println("You bust hand #" + z);
					money -= bets.get(i);
					System.out.println("Your total money is now $" + money);
				} 
				else if(getValue(pHand.get(i)) < getValue(tHand)) {
					if(pHand.get(i).size() == 2 & getValue(pHand.get(i)) == 21) {
						System.out.println("BlackJack!");
						money += 3 * bets.get(i) / 2;
						System.out.println("Your total money is now $" + money);
					}
					else if(getValue(tHand) > 21 & getValue(pHand.get(i)) <= 21) {
						System.out.println("You won hand #" + z);
						money += bets.get(i);
						System.out.println("Your total money is now $" + money);
					}
					else {
						System.out.println("You lose hand #" + z);
						money -= bets.get(i);
						System.out.println("Your total money is now $" + money);
					}
				} 
				else if(getValue(pHand.get(i)) > getValue(tHand)) {
					if(pHand.get(i).size() == 2 & getValue(pHand.get(i)) == 21) {
						System.out.println("BlackJack!");
						money += 3 * bets.get(i) / 2;
						System.out.println("Your total money is now $" + money);
					}
					else {
						System.out.println("You won hand #" + z);
						money += bets.get(i);
						System.out.println("Your total money is now $" + money);
					}
				}
				else if(getValue(pHand.get(i)) == getValue(tHand)) {
					System.out.println("You push hand #" + z);
					System.out.println("Your total money is now $" + money);
				}
				else {
					if(getValue(tHand) > 21) {
						System.out.println("You won hand #" + z);
						money += bets.get(i);
						System.out.println("Your total money is now $" + money);
					}
					else {
						System.out.println("You lose hand #" + z);
						money -= bets.get(i);
						System.out.println("Your total money is now $" + money);
					}
				}
			}
			for(Card c : tHand) {
				boot.discard(c);
			}
			for(ArrayList<Card> cs : pHand) {
				for(Card c : cs) {
					boot.discard(c);
				}
			}
			if(money < 1) {
				System.out.println("Your Broke!");
				System.exit(0);
			}
			System.out.println("Type 1 to keep playing, or type anyting else to cash out");
			a = s.nextInt();
			if(a != 1) return money;
		}
		System.out.println("The shoe needs to be shuffled");
		return money;
	}
}
