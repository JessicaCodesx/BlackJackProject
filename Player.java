import java.util.ArrayList;
import java.util.List;

public class Player {
	//variables for player name and player hand
    private String playerName;
    private List<Card> hand;
    private int balance;
    private int bet;
    private int wins;
    private int losses;

    //constructor to initialize player object
    public Player(String playerName, int initialBalance) {
        this.playerName = playerName;
        this.hand = new ArrayList<>();
        this.balance = initialBalance;
        this.bet = 0;
    }
    
    //method to get wins
    public int getWins() {
    	return wins;
    }

    //method to get losses
    public int getLosses() {
    	return losses;
    }
    
    //method to update wins
    public void addWins() {
    	wins++;
    }
    
    //method to add losses
    public void addLosses() {
    	losses++;
    }
    
    //method to add a card to player's hand in case of hitting
    public void addCard(Card card) {
        hand.add(card);
    }

    //method to show the player's cards
    public List<Card> getHand() {
        return hand;
    }

    //method to retrieve player name
    public String getPlayerName() {
        return playerName;
    }

    //method to remove cards from player hand
    public void clearHand() {
        hand.clear();
    }
    
    //method to place bet 
    public void bet(int betAmount) {
    	this.bet = betAmount;
    	this.balance -= betAmount;
    }
    //method to get the player's bet
    public int getBet() {
    	return bet;
    }
    
    //method to retrieve player's balance
    public int getBalance() {
        return balance;
    }
    //method to set player's balance
    public void setBalance(int balance) {
        this.balance = balance;
    }
    //method to calculate the value of the dealer's hand
    public int calculateHandValue() {
    	//variables for storing hand value and number of aces
        int value = 0;
        int numAces = 0;
        //iterating through cards in hand
        for (Card card : getHand()) {
        	//add value of current card to hand value
            value += card.calcValue();
            //check if card is an ace
            if (card.getCardValue().equals("ace")) {
            	//if it is, add to number of aces
                numAces++;
            }
        }
        // check if adding 11 for an ace is beneficial
        while (numAces > 0 && value + 11 <= 21) {
        	//if there's an ace and it doesn't bust the hand, add 10 to hand total
            value += 10; 
            //decrement number of aces now that one has been calculated (in case of multiples)
            numAces--;
        }
        //return hand value
        return value;
    }
    //method to handle player betting
    public boolean placeBet(int betAmount) {
        if (betAmount > 10 && betAmount <= balance) {
        	bet = betAmount;
            balance -= betAmount;
            return true; 
            } else {
            return false; 
        }
    }

    //method to update winnings
    public void updateWinnings(int winnings) {
        balance += winnings;
    }
    
    //method to display player information for method overriding example
    public void displayPlayerInfo() {
    	System.out.println("Player: " + playerName);
    	System.out.println("Wins: " + wins);
    	System.out.println("Losses: " + losses);
    }
}
