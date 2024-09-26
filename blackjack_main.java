/*
 * Sophia Herman
 * SWE 510
 * BlackJack Game
 * October 15, 2023
 */


import java.util.Random;
import java.util.Scanner;

public class blackjack_main {
  private static final String[] SUITS = {
    "Hearts",
    "Spades",
    "Clubs",
    "Diamonds",
  };
  private static final String[] VALUES = {
    "Two",
    "Three",
    "Four",
    "Five",
    "Six",
    "Seven",
    "Eight",
    "Nine",
    "Ten",
    "Jack",
    "Queen",
    "King",
    "Ace",
  };
  private static final int[] CARD_VALUES = {
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    10,
    10,
    10,
    10,
    11,
  };

  private static boolean[] deck = new boolean[52];
  private static Scanner scanner = new Scanner(System.in);
  private static Random random = new Random();
  private static final int INITIAL_MONEY = 100;

  public static void main(String[] args) {
    System.out.println("Welcome to Blackjack!");

    // Number of players
    System.out.println("Enter the number of players (1-4):");
    int numPlayers = scanner.nextInt();
    int[][] playerCards = new int[numPlayers][11];
    int[] playerMoney = new int[numPlayers];
    for (int i = 0; i < numPlayers; i++) {
      playerMoney[i] = INITIAL_MONEY;
    }
    int[] playerBets = new int[numPlayers];

    boolean playAgain;

    do {
      resetDeck();

      // Player's betting phase
    for (int i = 0; i < numPlayers; i++) {
        if (playerMoney[i] <= 0) {
            continue; // Skip players with no money
        }
        System.out.println(
            "Player " +
            (i + 1) +
            ", you have $" +
            playerMoney[i] +
            ". How much do you want to bet?"
        );
        int bet;
        while (true) {
            bet = scanner.nextInt();
            if (bet > 0 && bet <= playerMoney[i]) {
                break;
            }
            System.out.println(
                "Invalid bet. Please bet between $1 and $" + playerMoney[i] + "."
            );
        }
        playerBets[i] = bet;
    }

      // Deal initial cards
    for (int i = 0; i < numPlayers; i++) {
        if (playerMoney[i] > 0) { // Only deal cards if the player has money
            playerCards[i][0] = dealCard();
            playerCards[i][1] = dealCard();
        } else {
            playerCards[i][0] = 0; // Reset cards for players with no money
            playerCards[i][1] = 0;
        }
    }
    int[] dealerCards = { dealCard(), dealCard() };

      // Display initial cards
    for (int i = 0; i < numPlayers; i++) {
        if (playerMoney[i] > 0) { // Only display cards if the player has money
            System.out.println(
                "Player " +
                (i + 1) +
                "'s cards: " +
                cardString(playerCards[i][0]) +
                ", " +
                cardString(playerCards[i][1])
            );
        }
    }
    System.out.println(
        "Dealer's card: " + cardString(dealerCards[0]) + ", ???"
    );

      // Player's turn
      boolean allPlayersBusted = true;

      for (int i = 0; i < numPlayers; i++) {
        if (playerMoney[i] <= 0) {
          System.out.println(
            "Player " + (i + 1) + " is out of money and can't play."
          );
          continue;
        }
        System.out.println("Player " + (i + 1) + "'s turn:");
        int cardCount = 2;
        boolean playerBusted = false;
        while (true) {
          int handValue = calculateHandValue(playerCards[i]);
          if (handValue == 21) {
            System.out.println(
              "Blackjack! Player " + (i + 1) + " wins this round!"
            );
            playerMoney[i] += playerBets[i];
            break;
          } else if (handValue > 21) {
            System.out.println("Busted!");
            playerMoney[i] -= playerBets[i];
            playerBusted = true;
            break;
          }
          System.out.println("Do you want to (1)Hit or (2)Stand?");
          int choice = scanner.nextInt();
          if (choice == 1) {
            int newCard = dealCard();
            playerCards[i][cardCount] = newCard;
            cardCount++;
            System.out.println("You got: " + cardString(newCard));
          } else if (choice == 2) {
            break;
          } else {
            System.out.println("Invalid choice.");
          }
        }
        if (!playerBusted) {
          allPlayersBusted = false;
        }
      }
      // If all players are busted, reveal the dealer's cards. Dealer automatically wins
      if (allPlayersBusted) {
        System.out.println(
          "Dealer's second card: " + cardString(dealerCards[1])
        );
        System.out.println("All players busted! Dealer wins!");
        return; // Exit the game
      }

      // Dealer's turn
      System.out.println("Dealer's turn:");
      System.out.println("Dealer's second card: " + cardString(dealerCards[1])); // Display dealer's second card
      while (calculateHandValue(dealerCards) < 17) {
        int newCard = dealCard();
        boolean cardAdded = false;
        for (int i = 0; i < dealerCards.length; i++) {
          if (dealerCards[i] == 0) {
            dealerCards[i] = newCard;
            cardAdded = true;
            break;
          }
        }
        if (!cardAdded) {
          // Expand the dealer's cards array and add the new card
          int[] newDealerCards = new int[dealerCards.length + 1];
          System.arraycopy(
            dealerCards,
            0,
            newDealerCards,
            0,
            dealerCards.length
          );
          newDealerCards[dealerCards.length] = newCard;
          dealerCards = newDealerCards;
        }
        System.out.println("Dealer got: " + cardString(newCard));
        if (calculateHandValue(dealerCards) > 21) {
          System.out.println("Dealer busted!");
          break; // Break out of the dealer's turn loop
        }
      }

      // Display results
      int dealerValue = calculateHandValue(dealerCards);
      System.out.println("Dealer's hand value: " + dealerValue);
      for (int i = 0; i < numPlayers; i++) {
        if (playerMoney[i] > 0) { // Only display hand value if the player has money
            int playerValue = calculateHandValue(playerCards[i]);
            System.out.println("Player " + (i + 1) + "'s hand value: " + playerValue);
        if (playerValue > 21) {
          System.out.println("Player " + (i + 1) + " busted!");
          playerMoney[i] = Math.max(0, playerMoney[i] - playerBets[i]); // Ensure money doesn't go negative
        } else if (dealerValue > 21 || playerValue > dealerValue) {
          System.out.println("Player " + (i + 1) + " wins!");
          playerMoney[i] += playerBets[i];
        } else if (playerValue == dealerValue) {
          System.out.println(
            "It's a tie between Player " + (i + 1) + " and the dealer!"
          );
          // No change in player's money
        } else {
          System.out.println("Dealer wins against Player " + (i + 1) + "!");
          playerMoney[i] = Math.max(0, playerMoney[i] - playerBets[i]); // Ensure money doesn't go negative
        }
        System.out.println("Player " + (i + 1) + " now has $" + playerMoney[i]);
      }
    }
      // Prompt to continue or exit
      boolean allPlayersOutOfMoney = true;
      for (int money : playerMoney) {
        if (money > 0) {
          allPlayersOutOfMoney = false;
          break;
        }
      }
      if (allPlayersOutOfMoney) {
        System.out.println("All players are out of money. Game over!");
        return;
      }
      System.out.println("Do you want to play another round? (1)Yes (2)No");
      int choice = scanner.nextInt();
      playAgain = (choice == 1);
    } while (playAgain);

    System.out.println("Thank you for playing!");
  }

  private static int dealCard() {
    int card;
    do {
      card = random.nextInt(52);
    } while (deck[card]);
    deck[card] = true;
    return card;
  }

  private static String cardString(int card) {
    return VALUES[card % 13] + " of " + SUITS[card / 13];
  }

  private static int calculateHandValue(int[] hand) {
    int value = 0;
    int aces = 0;
    for (int card : hand) {
      if (card == 0) {
        continue;
      }
      value += CARD_VALUES[card % 13];
      if (card % 13 == 12) {
        aces++;
      }
    }
    while (value > 21 && aces > 0) {
      value -= 10;
      aces--;
    }
    return value;
  }

  private static void resetDeck() {
    for (int i = 0; i < deck.length; i++) {
      deck[i] = false;
    }
  }
}
//OUTPUT:
/*
Welcome to Blackjack!
Enter the number of players (1-4):
2
Player 1, you have $100. How much do you want to bet?
100
Player 2, you have $100. How much do you want to bet?
20
Player 1's cards: King of Clubs, King of Diamonds
Player 2's cards: Ace of Clubs, Ace of Spades
Dealer's card: Five of Diamonds, ???
Player 1's turn:
Do you want to (1)Hit or (2)Stand?  
1
You got: Six of Spades
Busted!
Player 2's turn:
Do you want to (1)Hit or (2)Stand?
2
Dealer's turn:
Dealer's second card: Ten of Spades
Dealer got: Seven of Diamonds
Dealer busted!
Dealer's hand value: 22
Player 2's hand value: 12
Player 2 wins!
Player 2 now has $120
Do you want to play another round? (1)Yes (2)No
1
Player 2, you have $120. How much do you want to bet?
20
Player 2's cards: Three of Hearts, Ace of Spades
Dealer's card: Six of Hearts, ???
Player 1 is out of money and can't play.
Player 2's turn:
Do you want to (1)Hit or (2)Stand?
2
Dealer's turn:
Dealer's second card: Ace of Diamonds
Dealer's hand value: 17
Player 2's hand value: 14
Dealer wins against Player 2!
Player 2 now has $100
 */
