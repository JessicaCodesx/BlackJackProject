/* this is a game of Blackjack, where the goal is for the player to place bets
 * to win money by getting a total hand value closer to 21 than the
 * dealer's hand. 
 * CASE STUDY - CST8284 - JESSICA HAUGEN - 041118503 
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.util.ArrayList;
 import java.util.List;
 import java.io.BufferedWriter;
 import java.io.FileWriter;
 import java.io.IOException;
 
 public class BlackJack extends JFrame {
     private Player player;
     private CardDeck deck;                 //initializing deck for game
     private List<Player> players;          //creating list of players
     private Dealer dealer;                 //creating the dealer
 
     private JTextArea gameTextArea;        //Swing text area to display game
     private JButton standButton;           //button for player to stand 
     private JButton hitButton;             //button for player to hit
     private JButton playButton;            //button to replay game
     private JTextField nameField;          //field for player to enter name
     private boolean dealerCardHidden;      //boolean to keep dealer's second card hidden until after player's turn
     
     //constructor for the game 
     public BlackJack() {
         setTitle("Jessica's BlackJack");   //window title
         setSize(800, 500);                 //window size  
         gameTextArea = new JTextArea();    //setting Swing component for text area   
         add(new JScrollPane(gameTextArea), BorderLayout.CENTER);
         JPanel buttonPanel = new JPanel();
         nameField = new JTextField(25);
         hitButton = new JButton("Hit");
         standButton = new JButton("Stand");
         playButton = new JButton("Restart");
        
         buttonPanel.add(hitButton);        //adding the hit button to the panel
         buttonPanel.add(standButton);      //adding the stand button to the panel
         buttonPanel.add(playButton);       //adding the replay button to the panel
         add(buttonPanel, BorderLayout.SOUTH);  //putting them at the bottom of the window
 
         //action listener for hit button 
         hitButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 handleHit();
             }
         });
         //action listener for stand button
         standButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 handleStand();
             }
         });
         
         //action listener for restart button
         playButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 restartGame();
             }
         });
 
         //initializing the game components
         initializeGame();
     }
 
     private static final String FILE_PATH = "playerstats.txt"; //declare file path for file of player stats
     
     //starting the game 
     private void initializeGame() {
         //set player name at new game start
         String playerName = JOptionPane.showInputDialog("Please enter your name: ");
         
         player = new Player(playerName, 100);
         deck = new CardDeck();        
         players = new ArrayList<>();
         players.add(player);
         dealer = new Dealer();
         startRound();
     }
     //starting a round of the game
     private void startRound() {
         deck.shuffleDeck();
         
         int bet = getPlayerBet();
         players.get(0).placeBet(bet);
         
         dealInitialCards();
         //hiding dealer's second card 
         dealerCardHidden = true;
         updateGameTextArea();
         //allowing hit or stand buttons to be used
         hitButton.setEnabled(true);
         standButton.setEnabled(true);
     }
     //dealing cards to player and dealer
     private void dealInitialCards() {
         for (Player player : players) {
             player.addCard(deck.drawCard());
             player.addCard(deck.drawCard());
         }
         dealer.addCard(deck.drawCard());
         dealer.addCard(deck.drawCard());
     }
     //method for player to bet
     private int getPlayerBet() {
         while(true) {
             try {
                 String betInput = JOptionPane.showInputDialog("Enter your bet (Minimum bet is $5) . Your current balance is: $" + player.getBalance());
                 int bet = Integer.parseInt(betInput);
                 if (bet <= 0 || bet > player.getBalance()) {
                     JOptionPane.showMessageDialog(null, "Invalid.");
                 } else {
                     return bet;
                 }
             } catch (NumberFormatException e) {
                 JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
             }
         }
     }
     //method for hit button use
     private void handleHit() {
         players.get(0).addCard(deck.drawCard());
         updateGameTextArea();
         checkForBust();
     }
     //method for stand button use
     private void handleStand() {
         dealer.play(deck);
         dealerCardHidden = false;
         updateGameTextArea();
         determineWinner();
         endRound();
     }
     
     //method to restart game
     private void restartGame() {
         deck = new CardDeck();
         players.get(0).clearHand();
         dealer.clearHand();
         dealerCardHidden = true;
         startRound();
     }
     //checking if player has gone over 21, if so reveal dealer's cards and end round
     private void checkForBust() {
         int playerValue = calculateHandValue(players.get(0));
         if (playerValue > 21) {
             dealerCardHidden = false;
             updateGameTextArea();
             gameTextArea.append("Bust! You lost.");
             endRound();
         }
     }
     //deciding the winner of the round
     private void determineWinner() {
         int playerValue = calculateHandValue(players.get(0));
         int dealerValue = calculateHandValue(dealer);
         //if the player's total value is over 21, the player loses
         if (playerValue > 21) {
             gameTextArea.append("Bust! You lost.");
             player.addLosses();
         } else {
             //calculate winnings based on the player's bet
             int bet = player.getBet();
             int winnings = 0;
 
             //if the player gets 21, check for blackjack
             if (playerValue == 21 && players.get(0).getHand().size() == 2) {
                 winnings = (int) (bet * 2.5); // Blackjack pays 2.5 times the bet
                 gameTextArea.append("Blackjack! Congratulations.");
                 player.addWins();
             } else if (dealerValue > 21 || (dealerValue <= 21 && dealerValue < playerValue)) {
                 //player wins
                 winnings = bet * 2;
                 gameTextArea.append("You win! ");
                 player.addWins();
             } else if (dealerValue == playerValue) {
                 //it's a tie
                 winnings = bet;
                 gameTextArea.append("It's a tie!");
                 player.addWins();
             } else {
                 //dealer wins
                 gameTextArea.append("Dealer wins!");
                 player.addLosses();
             }
 
             //update player's winnings and display information
             player.updateWinnings(winnings);
             gameTextArea.append("You won $" + winnings + " Your new balance is: $" + player.getBalance() + "\n");
 
             //check if the player's balance is 0 or below
             if (player.getBalance() <= 0) {
                 gameTextArea.append("Game over! Your balance is $0.");
                 restartGame();
                 return; 
             }
             
             //write wins and losses to file
             writePlayerStatsToFile(player.getPlayerName(), player.getWins(), player.getLosses());
         }
 
         endRound();
     }
 
     //disabling buttons after round
     private void endRound() {
         hitButton.setEnabled(false);
         standButton.setEnabled(false);
     }
     //method to calculate the value of the hand
     public int calculateHandValue(Player player) {
         //variables to store total value and number of aces in a hand
         int value = 0;
         int numAces = 0;
         //iterating through each card in the player's hand
         for (Card card : player.getHand()) {
             //get value of current card using calcValue() method
             int cardValue = card.calcValue(); 
             //add the cards value to total hand value
             value += cardValue;
             //check if the card is an ace
             if (card.getCardValue().equals("ace")) {
                 //if it is an ace, increase the amount of aces
                 numAces++;
             }
         }
 
         //check if adding 11 for the ace works
         while (numAces > 0 && value + 11 <= 21) {
             //if there is an ace and it doesn't bust the hand, add 10 to hand total
             value += 10;
             //decrement amount of aces now that one has been calculated (in case of multiples)
             numAces--;
         }
         return value;
     }
 
     //updating the game in the text area
     private void updateGameTextArea() {
         gameTextArea.setText("");
         displayPlayerHand(players.get(0), "Your Hand");
         displayDealerHand(dealer, "Dealer's Hand", dealerCardHidden);
     }
 
     //showing the player cards and total card value
     private void displayPlayerHand(Player player, String title) {
         gameTextArea.append(title + ":\n");
         for (Card card : player.getHand()) {
             gameTextArea.append(card + "\n");
         }
         gameTextArea.append("Total Value: " + calculateHandValue(player) + "\n\n");
         gameTextArea.append("Balance: $" + player.getBalance() + "\n\n");
     }
     // showing the dealer cards and total card value
     private void displayDealerHand(Player player, String title, boolean hidden) {
         gameTextArea.append(title + ":\n");
 
         // display "hidden card"
         if (hidden) {
             gameTextArea.append("Hidden Card\n");
         } else {
             // display the revealed card
             gameTextArea.append(dealer.getHand().get(0) + "\n");
         }
 
         // display the remaining cards
         for (int i = 1; i < dealer.getHand().size(); i++) {
             gameTextArea.append(dealer.getHand().get(i) + "\n");
         }
 
         // display the total value only if the second card is revealed
         if (!hidden) {
             gameTextArea.append("Total Value: " + calculateHandValue(player) + "\n\n");
         }
     }
 
     //method to write wins and losses to a file
     private void writePlayerStatsToFile(String playerName, int wins, int losses) {
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
             //write player name and their wins and losses
             writer.write(playerName + "," + wins + "," + losses);
             writer.newLine();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
     //main method to invoke Swing game window
     public static void main(String[] args) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 new BlackJack().setVisible(true);
             }
         });
     }
 }
 