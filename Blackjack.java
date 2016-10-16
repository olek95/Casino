package casino;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {
     private static Blackjack game;
    private static int[] playerCards, croupierCards;
    private static HashMap<Integer, Integer> availableCards;
    private Blackjack(){
    }
    public static synchronized Blackjack createBlackjack(){
        if(game == null)
            game = new Blackjack();
        return game;
    }
    public boolean play(){
        playerCards = new int[9];
        croupierCards = new int[9];
        availableCards = new HashMap();
        for(int i = 2; i <= 14; i++)
            availableCards.put(i, 4);
        Arrays.fill(playerCards, 0);
        Arrays.fill(croupierCards, 0);
        for(int i = 0; i < 2; i++){
            hit(playerCards);
            hit(croupierCards);
        }
        return false;
    }
    public void hit(int[] ownerCards){
        Random rand = new Random();
        int i = findZero(ownerCards);
        int drawn;
        if(i != ownerCards.length){
            do{
                drawn = rand.nextInt(13) + 2;
                if(drawn == 14)
                    if(ownerCards == playerCards) drawn = aceOneOrEleven();
                    else if(getPoints(croupierCards) + 11 > 21) drawn = 15;
            }while((drawn != 15 && availableCards.get(drawn) == 0) || (drawn == 15 && availableCards.get(drawn - 1) == 0));
            ownerCards[i] = drawn;
            if(drawn == 15) drawn--;
            availableCards.replace(drawn, availableCards.get(drawn) - 1);
        }
    }
    public void split(int i){
        int card = playerCards[i - 1];
        int index = 0;
        boolean changed = false;
        do{
            if(index != i - 1 && playerCards[index] == card){
                playerCards[i - 1] = 0;
                hit(playerCards);
                availableCards.replace(card, availableCards.get(card) + 1);
                changed = true;
            }
            index++;
        }while(index < playerCards.length && !changed);
    }
    public int aceOneOrEleven(){
        System.out.println("Pobrałeś asa. Czy ten as ma mieć wartość 1 czy 11?");
        int value = new Scanner(System.in).nextInt();
        if(value == 11) return 14;
        else return 15;
    }
    public int getPoints(int[] ownerCards){
        int i = 0, sum = 0;
        boolean end = false;
        while(i < ownerCards.length && !end){
            if(ownerCards[i] >= 2 && ownerCards[i] <= 10) sum += ownerCards[i];
            else if(ownerCards[i] >= 11 && ownerCards[i] <= 13) sum += 10; 
            else if(ownerCards[i] == 14) sum += 11; 
            else if(ownerCards[i] == 15) sum += 1; 
            else end = true;
            i++;
        }
        return sum; 
    }
    public boolean isWin(){
        int playerSum = getPoints(playerCards);
        return playerSum >= getPoints(croupierCards) && playerSum <= 21;
    }
    public int[] getPlayerCards(){
        return playerCards;
    }
    public int[] getCroupierCards(){
        return croupierCards;
    }
    private int findZero(int[] ownerCards){
        for(int i = 0; i < ownerCards.length; i++)
            if(ownerCards[i] == 0) return i;
        return ownerCards.length;
    }
    public HashMap<Integer, Integer> getA(){
        return availableCards;
    }
}
