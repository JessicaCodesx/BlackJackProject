 public class Card {
    // setting the variables of card value and card suit
    private String cardValue;
    private String cardSuit;

    // setting constructor for a card
    public Card(String cardValue, String cardSuit) {
        this.cardValue = cardValue;
        this.cardSuit = cardSuit;
    }

   
    // getter methods for card value and card suit
    public String getCardValue() {
        return cardValue;
    }
    public String getCardSuit() {
        return cardSuit;
    }

    // method to calculate the value of a card
    public int calcValue() {   
        switch (cardValue) {
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "10":
            case "Jack":
            case "Queen":
            case "King":
                return 10;
            case "Ace":
                return 11;
        }
          return 0;
    }

    // method to get the numeric value of the card
    public int getValue() {
        return calcValue(); // default case
    }

    // method to get the rank of the card
    public String getRank() {
        return cardValue;
    }

    // method to show the card as a string by combining value and suit
    @Override
    public String toString() {
        return " " + cardValue + " of " + cardSuit + " ";
    }
}

