import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Table {
    /**
     * Total number of player. Use this variable whenever possible
     */
    private static final int NUM_OF_PLAYERS = 4;
    /**
     * Total number of cards used in this game. Use this variable whenever possible
     */
    private static final int TOTAL_NUMBER_OF_CARD = 104;
    /**
     * The four stacks of cards on the table.
     */
    private Card[][] stacks = new Card[4][6];
    /**
     * This number of cards of each stack on the table. For example, if the variable
     * stacks stores
     * -------------------------
     * | 0 | 10 13 14 -- -- -- |
     * | 1 | 12 45 -- -- -- -- |
     * | 2 | 51 55 67 77 88 90 |
     * | 3 | 42 -- -- -- -- -- |
     * -------------------------
     * 
     * stacksCount should be {3, 2, 6, 1}.
     * 
     * You are responsible to maintain the data consistency.
     */
    private int[] stacksCount = new int[4];
    /**
     * The array of players
     */
    private Player[] players = new Player[NUM_OF_PLAYERS];

    /**
     * Default constructor
     * 
     * In the constructor, you should perform the following tasks:
     * 
     * 1. Initialize cards for play. You should construct enough number of cards
     * to play. These cards should be unique (i.e., no two cards share the same
     * number). The value of card must be between 1 to 104. The number of bullHead
     * printed on each card can be referred to the rule.
     * 
     * 2. Initialize four player. The first player should be a human player, call
     * "Kevin". The other player should be a computer player. These computer player
     * should have the name "Computer #1", "Computer #2", "Computer #3".
     * 
     * 3. Deal randomly 10 cards to each player. A card can only be dealt to one
     * player. That is, no two players can have the same card.
     * 
     * 4. Deal a card on each stack. The card dealt on the stack should not be dealt
     * to any player. Card dealt on each stack should also be unique (no two stack
     * have the same card).
     * 
     */
    public Table() {
        //Initialize cards for play.
        //Generate a randomised int array of 104 elements
        int[] number= new int[TOTAL_NUMBER_OF_CARD];

        int count = 0;

        while (count < number.length) {
            int candidate = ThreadLocalRandom.current().nextInt(1, 105);

            // Check whether candidate number is in our array
            boolean exists = false;
            for (int i = 0; i < count; i++) {
                if (number[i] == candidate) {
                    exists = true;
                    break;
                }
            }

            // If not found, add it to the array
            if (!exists) {
                number[count++] = candidate;
            }
        }

        //The use the int array to create new and random cards.
        Card[] deck = new Card[TOTAL_NUMBER_OF_CARD];
        for (int i = 0; i < TOTAL_NUMBER_OF_CARD; i++)
            deck[i] = new Card(number[i]);

        //Initialize 4 players
        //players[0] is kevin while the rest are computer
        players[0] = new Player("Kevin");
        for (int i = 1; i < 4; i++)
            players[i] = new Player();


        //Dealing 10 cards randomly to players
        for (int i = 0; i < 10; i++){
           players[0].dealCard(deck[i]);
           players[1].dealCard(deck[i+10]);
           players[2].dealCard(deck[i+20]);
           players[3].dealCard(deck[i+30]);
        }

        //Deal a card on each stack
        for (int i = 0; i < 4; i++) {
            int n = 0;
            stacks[i][0] = deck[i + 50];
            for (int j = 0; j < 6; j++){
                if (stacks[i][j] != null)
                    n ++;
            }
            stacksCount[i] = n;
        }
    }


    /**
     * This method is to find the correct stack that a card should be added to
     * according to the rule. It should return the stack among which top-card of
     * that stack is the largest of those smaller than the card to be placed. (If
     * the rule sounds complicate to you, please refer to the game video.)
     * 
     * In case the card to be place is smaller than the top cards of all stacks,
     * return -1.
     * 
     * @param card - the card to be placed
     * @return the index of stack (0,1,2,3) that the card should be place or -1 if
     *         the card is smaller than all top cards
     */
    public int findStackToAdd(Card card) {
        int cardNum = card.getNumber();

        //Each stack's top card's number
        int[] stackNumAry = new int[4];

        //To see the differences between the card and each stack's top card's number
        int[] diff = new int[4];

        for (int i = 0; i < 4; i++)
            stackNumAry[i] = getStackNum(i);

        for (int i = 0; i < 4; i++)
            diff[i] = cardNum - stackNumAry[i];


        int min = 200;
        int index = 10;
        //Find the smallest the min number and index
        for (int i = 0; i < 4; i++){
            if (diff[i] < 104 && diff[i]>0 && diff[i]<min){
                min = diff[i];
                index = i;
            }
        }

        //Return the index (0 to 3) the player should play
        if (min > 0 && min!=200)
            return index;
        //else return -1
        else return -1;

    }

    /**
     * This method is to return each stack's top card's number.
     *
     * @param n - the index number of the stack
     * @return the highest number of the stack
     */
    private int getStackNum(int n){
        int stackNum = 0;
        for (int i = 0; i < 6; i++){
            if (stacks[n][i] != null)
                stackNum = stacks[n][i].getNumber();
        }
        return stackNum;
    }


    /**
     * To print the stacks on the table. Please refer to the demo program for the
     * format. Within each stack, the card should be printed in ascending order,
     * left to right. However, there is no requirement on the order of stack to
     * print.
     */
    public void print() {
        System.out.println("----------Table----------");
        for (int i = 0; i < 4; i++){
            System.out.print("Stack " + i + ":");
            for (int j = 0; j < 6; j++){
                if (stacks[i][j] == null)
                    System.out.print("");
                else
                    System.out.print(stacks[i][j]);
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    /**
     * This method is the main logic of the game. You should create a loop for 10
     * times (running 10 rounds). In each round all players will need to play a
     * card. These cards will be placed to the stacks from small to large according
     * to the rule of the game.
     * 
     * In case a player plays a card smaller than all top cards, he will be
     * selecting one of the stack of cards and take them to his/her own score pile.
     * If the player is a human player, he will be promoted for selection. If the
     * player is a computer player, the computer player will select the "cheapest"
     * stack, i.e. the stack that has fewest bull heads. If there are more than
     * one stack having fewest bull heads, selecting any one of them.
     */
    public void runApp() {
        new Table();
        //Keep track of what the players are playing
        //index order: 0 = Kevin, 1 = Computer1, 2 = Computer2, 3 = Computer3
        Card[] placedCards = new Card[4];

        for (int turn = 0; turn < 10; turn++) {
            // print Table
            print();
            Scanner in = new Scanner(System.in);
            int input;
            Card kevinToPlay = null;

            //Players play the cards
            do{
                kevinToPlay = players[0].playCard();
            }  while (kevinToPlay == null);

            placedCards[0] = kevinToPlay;
            for (int i = 1; i < 4; i++)
                placedCards[i] = players[i].playCardRandomly();

            for (int i = 0; i < 4; i++)
                System.out.println(players[i].getName()+ ":" + placedCards[i]);

            //Check for the smallest card from the placedCards & repeat for the remaining cards after adding
            int smallestCardNumber = 0;
            for (int checkNum = 0; checkNum < 4; checkNum++){
                int min = 200;
                int index = -1;

                //Update the min number to the smallest card
                for(int i = 0; i < 4; i++) {
                    if (placedCards[i].getNumber() < min && placedCards[i].getNumber() > smallestCardNumber) {
                        min = placedCards[i].getNumber();
                        index = i;
                    }
                }

                Card smallestCard = placedCards[index];
                System.out.println("Place the card " + smallestCard + " for " + players[index].getName());
                int stackNumtoAdd = findStackToAdd(smallestCard);

                //stackNum is -1 when the player's card is smaller than each stack's top card
                if (stackNumtoAdd == -1){
                    //for human player(Kevin)
                    if (index == 0){
                        do{
                            System.out.println("Pick a stack to collect the cards ");
                            stackNumtoAdd = in.nextInt();
                        } while (stackNumtoAdd < 0 || stackNumtoAdd > 3);
                    }
                    //for computer
                    else{
                        //find the smallest number of bullheads for each stack
                        int numBullHeads = getStackBullHeads(0);
                        stackNumtoAdd = 0;

                        for (int i = 1; i < 4; i++){
                            if (getStackBullHeads(i) < numBullHeads){
                                numBullHeads = getStackBullHeads(i);
                                stackNumtoAdd = i;
                            }
                        }
                    }
                    //Prints Player takes the card:...
                    System.out.print(players[index].getName() + " takes the card:");
                    for (int i = 0; i <stacksCount[stackNumtoAdd]; i++){
                        System.out.print(stacks[stackNumtoAdd][i]);
                    }
                    System.out.println();
                    //Move the stack to players' pile
                    players[index].moveToPile(stacks[stackNumtoAdd], stacksCount[stackNumtoAdd]);
                    //After moving the cards to pile, the stack is empty
                    stacksCount[stackNumtoAdd] = 0;
                }

                //Move the stack to players' pile if number of cards in stack is 5
                if (stacksCount[stackNumtoAdd] == 5){
                    //Prints Player takes the card:...
                    System.out.print(players[index].getName() + " takes the card:");
                    for (int i = 0; i <stacksCount[stackNumtoAdd]; i++){
                        System.out.print(stacks[stackNumtoAdd][i]);
                    }
                    System.out.println();
                    //Move the stack to players' pile
                    players[index].moveToPile(stacks[stackNumtoAdd], stacksCount[stackNumtoAdd]);
                    //After moving the cards to pile, the stack is empty
                    stacksCount[stackNumtoAdd] = 0;
                }

                //Add the card to the chosen stack (automatic)
                addToStack(stacks, stackNumtoAdd,placedCards, index);
                //Update the smallestCardNumber, use the updated version to get the next smallest card number
                //Repeat until index reaches 4 (get the largest card)
                smallestCardNumber =smallestCard.getNumber();
            }
        }

        //Last print each player's pile
        for (Player p : players) {
            System.out.println(p.getName() + " has a score of " + p.getScore());
            p.printPile();
        }
    }


    private void addToStack(Card[][] stacks, int stackNum, Card[] placedCards, int cardIndex){
        stacks[stackNum][stacksCount[stackNum]] = placedCards[cardIndex];
        stacksCount[stackNum]++;
    }

    private int getStackBullHeads(int stackNumber) {
        int total = 0;

        for(int i = 0; i < this.stacksCount[stackNumber]; i++) {
            total += this.stacks[stackNumber][i].getBullHead();
        }
        return total;
    }


    /**
     * Programme main. You should not change this.
     * 
     * @param args - no use.
     */
    public static void main(String[] args) {
        new Table().runApp();
    }

}
