import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
	//list to store the cards in (the deck)
    private List<Card> deck;

    //constructor to initialize and shuffle the deck
    public CardDeck() {
        initializeDeck();
        shuffleDeck();
    }

    //beginning the deck with one of each card combination (52 cards)
    private void initializeDeck() {
        deck = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        //creating a card for each suit and rank and adding it to the deck
        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(rank, suit);
                deck.add(card);
            }
        }
    }

    //shuffling the deck using Collections.shuffle()
    void shuffleDeck() {
        Collections.shuffle(deck);
    }

    //method to draw a card
    public Card drawCard() {
    	//removing a card from the top of the deck (position 0 of the list), returning the card
        return deck.remove(0);
    }
}
