import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    /**
     * The cards held on a player hand
     */
    private Card[] hand;
    /**
     * The number of card held by the player. This variable should be maintained
     * to match array hand.
     */
    private int handCount;
    /**
     * A dynamic array that holds the score pile.
     */
    private Card[] pile;
    /**
     * The name of the player
     */
    private String name;
    /**
     * A static variable that tells how many player has been initialized
     */
    private static int count = 1;

    /**
     * Other constructor that specify the name of the player.
     * 
     * You need to initialize your data member properly.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Default constructor that set the name of the player as "Computer #1",
     * "Computer #2", "Computer #3"...
     * The number grows when there are more computer players being created.
     * 
     * You need to initialize your data member properly.
     */
    public Player() {
        this.name = "Computer #"+ count;
        //count++ to automatically update the computer name number by 1 everytime it is called
        count++;
    }

    /**
     * Getter of name
     * 
     * @return - the name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method is called when a player is required to take the card from a stack
     * to his score pile. The variable pile should be treated as a dynamic array so
     * that the array will auto-resize to hold enough number of cards. The length of
     * pile should properly record the total number of cards taken by a player.
     * 
     * Important: at the end of this method, you should also help removing all cards
     * from the parameter array "cards".
     * 
     * 
     * 
     * @param cards - an array of cards taken from a stack
     * @param count - number of cards taken from the stack
     */
    public void moveToPile(Card[] cards, int count) {
        //When pile is empty, create a new pile with the stack cards and update the original pile with the new pile
        if (this.pile == null){
            Card[] newPile = new Card[count];
            for (int i = 0; i < count; i++){
                newPile[i] = cards[i];
            }
            this.pile = newPile;
            for (int i = 0; i < cards.length; i++)
                cards[i] = null;
            return;
        }
        //When pile is not empty, create a new pile with length of current pile and stack's number of cards
        //Update new pile with original pile's cards and stack's cards
        //Update the original pile with new pile
        else {
            Card[] newPile = new Card[this.pile.length + count];
            for (int i = 0; i < this.pile.length; i++){
                newPile[i] = this.pile[i];
            }
            for (int i = 0; i < count; i++) {
                newPile[i + this.pile.length] = cards[i];
            }
            this.pile = newPile;
            for (int i = 0; i < cards.length; i++)
                cards[i] = null;
        }

    }

    /**
     * This method prompts a human player to play a card. It first print
     * all cards on his hand. Then the player will need to select a card
     * to play by entering the INDEX of the card.
     * 
     * @return - the card to play
     */
    public Card playCard() {
        Scanner in = new Scanner(System.in);
        int input;
        do{
            for (int i = 0; i < this.hand.length; i++)
                System.out.println(i + ": " + this.hand[i]);
            System.out.println(this.getName() + ", please select a card to play:");
            input = in.nextInt();
        }while (input < 0 || input >= this.handCount);
        Card toReturn = this.hand[input];
        afterplayCard(input);
        return toReturn;
    }

    /**
     * This method is used to update player's hand and handCount after player plays their card.
     *
     * @param num - the player's input to play the specific card
     */
    private void afterplayCard(int num){
        Card[] temp = new Card[this.handCount - 1];
        //For num(input) less than handCount-1
        if (num != this.handCount-1){
            for (int i = 0; i < num; i++){
                temp[i] = this.hand[i];
            }
            for (int i = num; i <temp.length; i++)
                temp[i] = this.hand[i+1];
            this.hand = temp;
            this.handCount--;
            return;
        }
        //For num(input) equals to handCount-1
        else{
            for (int i = 0; i <temp.length; i++)
                temp[i] = this.hand[i];
            this.hand = temp;
            this.handCount--;
        }
    }

    /**
     * This method let a computer player to play a card randomly. The computer
     * player will pick any available card to play in a random fashion.
     * 
     * @return - card to play
     */
    public Card playCardRandomly() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, this.handCount);
        Card toReturn = this.hand[randomNum];
        afterplayCard(randomNum);
        return toReturn;
    }

    /**
     * Deal a card to a player. This should add a card to the variable hand and
     * update the variable handCount. During this method, you do not need to resize
     * the array. You can assume that a player will be dealt with at most 10 cards.
     * That is, the method will only be called 10 times on a player.
     * 
     * After each call of this method, the hand should be sorted properly according
     * to the number of the card.
     * 
     * @param card - a card to be dealt
     */
    public void dealCard(Card card) {
        //If original hand is null, create a new hand with a Card in the new hand.
        //Update the original hand with new hand.
        if (hand == null){
            hand = new Card[1];
            hand[0] = card;
            handCount = 1;
            return;
        }

        Card[] handN = new Card[hand.length + 1];
        //index is to get the index of where the card should be placed in the player's hand (ascending order)
        int index = -1;

        //Get the index of the card number from the hand smaller than the card's number.
        for (int i = 0; i < hand.length; i++){
            if (card.getNumber() < hand[i].getNumber()) {
                index = i;
                break;
            }
        }

        //When index is between 0 and hand.length, update new hand with original hand cards up till the index
        //Then add the card with the index
        //Lastly update the remaining original hand cards to the new hand
        if (index > 0 && index < hand.length) {
            for (int i = 0; i < index; i++) {
                handN[i] = hand[i];
            }
            handN[index] = card;
            for (int i = index; i < hand.length; i++) {
                handN[i + 1] = hand[i];
            }
        }
        //When index is 0, card is in the 0 index of the new hand
        //Update the remaining index of new hand with all of the original hand cards with i+1
        else if (index == 0) {
            handN[0] = card;
            for (int i = 0; i < hand.length; i++)
                handN[i+1] = hand[i];
        }
        //This means that index = -1, meaning the card is in the last index of the new hand.
        else {
            for (int i = 0; i < hand.length; i++)
                handN[i] = hand[i];
            handN[handN.length - 1] = card;
        }
        //assign the new hand to the original hand variable
        hand = handN;
        handCount = hand.length;
    }

    /**
     * Get the score of the player by counting the total number of Bull Head in the
     * score pile.
     * 
     * @return - score, 0 or a positive integer
     */
    public int getScore() {
        if (this.pile != null){
            int sum = 0;
            for (int i = 0; i < this.pile.length; i++)
                sum += this.pile[i].getBullHead();
            return sum;
        }
        return 0;
    }

    /**
     * To print the score pile. This method has completed for you.
     * 
     * You don't need to modify it and you should not modify it.
     */
    public void printPile() {
        for (Card c : pile) {
            c.print();
        }
        System.out.println();
    }

    /**
     * This is a getter of hand's card. This method has been completed for you
     *
     * You don't need to modify it and you should not modify it.
     * 
     * @param index - the index of card to take
     * @return - the card from the hand or null
     */
    public Card getHandCard(int index) {
        if (index < 0 || index >= handCount)
            return null;
        return hand[index];
    }
}
