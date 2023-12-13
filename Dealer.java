public class Dealer extends Player {

	//constructor to initialize dealer object and name
    public Dealer() {
        super("Dealer", 5000);
    }
    //method for dealer's turn
    public void play(CardDeck deck) {
    	//dealer to continue drawing cards when hand value is less than 17
        while (shouldDraw()) {
        	//adding a card to dealer's hand if dealer's hand value is <17
            addCard(deck.drawCard());
        }
    }

    //method to decide if dealer should continue to draw a card
    public boolean shouldDraw() {
    	//the dealer will draw if dealer's hand value is <17
        return calculateHandValue() <17;
    }
    
    //method to override method from player class for overriding example
    @Override
    public void displayPlayerInfo() {
    	System.out.println("Dealer: ");
    	System.out.println("Wins: " + getWins());
    	System.out.println("Losses: " + getLosses());	
    }
}
