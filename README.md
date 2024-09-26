# Blackjack Multiplayer Game

## Overview
This project implements a multiplayer Blackjack game where 1 to 4 players can play against the dealer. The game follows standard Blackjack rules, such as busting when over 21 and winning with a hand closer to 21 than the dealer's. 

### Gameplay Features:
- Players can take additional cards or hold to keep their score.
- Dealer plays according to Blackjack rules (hit below 16, stand on 17+).
- Detects busts, Blackjack, and ties (push).
  
### Advanced Features:
- Multi-round gameplay: The game can run for a user-defined number of rounds.
- Player balances: Players can bet and are eliminated when their balance reaches zero.
- Card deck management: Cards are randomly dealt and tracked.

### How to Play:
1. Input the number of players (1 to 4) and the game starts by dealing cards.
2. Each player takes turns to either take another card or hold.
3. The dealer reveals its hand after players have finished.
4. Dealer hits if under 16 and stands if 17 or above.
5. The round ends with comparing the dealer's hand to each player's.

### Requirements:
- Java SE 8+

### Installation:
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/blackjack-game.git

2. Compile and run the program:
  ```bash
    javac BlackjackGame.java
    java BlackjackGame

This project is open-source and available under the MIT License.
