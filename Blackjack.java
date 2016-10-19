package casino;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Obiekt klasy <code>Blackjack</code> będzie reprezentował grę Blackjack. 
 * Gracz wygrywa w tej grze jeśli ma więcej lub tyle samo punktów co krupiej oraz
 * mniej lub równo 21 punktów.
 * @author AleksanderSklorz
 */
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
    /**
     * Uruchamia grę. Tasuje karty (jest 52 kart) oraz rozdaje po dwie początkowe 
     * karty graczowi i krupierowi.
     */
    public void play(){
        playerCards = new int[9]; // maksymalnie gracz może mieć 9 karty w talii
        croupierCards = new int[9];
        availableCards = new HashMap();
        for(int i = 2; i <= 14; i++)
            availableCards.put(i, 4); // każda wartość występuje po 4 razy
        Arrays.fill(playerCards, 0); // 0 oznacza iż w tym miejscu brak karty
        Arrays.fill(croupierCards, 0);
        for(int i = 0; i < 2; i++){
            hit(playerCards);
            hit(croupierCards);
        }
    }
    /**
     * Pobiera jedną kartę.
     * @param ownerCards tablica właściciela do której ma trafić wyciągnięta karta
     * @return czy udało się pobrać kartę
     */
    public boolean hit(int[] ownerCards){
        Random rand = new Random();
        int i = findZero(ownerCards);
        int drawn;
        if(i != ownerCards.length){
            do{
                drawn = rand.nextInt(13) + 2; // dodaję 2 bo karty numeruje się od 2
                if(drawn == 14)
                    if(ownerCards == playerCards) drawn = aceOneOrEleven();
                    else if(getPoints(croupierCards) + 11 > 21) drawn = 15;
            }while((drawn != 15 && availableCards.get(drawn) == 0) || (drawn == 15 && availableCards.get(drawn - 1) == 0));
            ownerCards[i] = drawn;
            if(drawn == 15) drawn--;
            availableCards.replace(drawn, availableCards.get(drawn) - 1);
            return true;
        }
        return false;
    }
    /**
     * Zamienia duplikat karty na inną wyciągniętą z talii.
     * @param i numer karty (liczony od 1) którą gracz chce wyciągnąć
     * @return czy udało się zamienić
     */
    public boolean split(int i){
        int card = playerCards[i - 1];
        int index = 0;
        do{
            if(index != i - 1 && playerCards[index] == card){ // sprawdza czy rzeczywiście istnieje duplikat
                playerCards[i - 1] = 0;
                hit(playerCards);
                availableCards.replace(card, availableCards.get(card) + 1);
                return true;
            }
            index++;
        }while(index < playerCards.length);
        return false; // jeśli przeszło po wszystkich kartach i nie znaleziono duplikatu - zwraca false
    }
    private int aceOneOrEleven(){
        int value;
        do{
            value = JOptionPane.showOptionDialog(null, "Pobrałeś asa. Czy ten as ma mieć wartośc 1 czy 11?", null, 
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"1", "11"}, null);
        }while(value == JOptionPane.CLOSED_OPTION);
        return value == 0 ? 14 : 15; // 1 będzie symbolizowana przez 14, a 11 przez 15.
    }
    /**
     * Zwraca sumę wszystkich punktów uzyskanych przez właściciela kart.
     * Karty od 2 do 10 mają wartości odpowiednio od 2 do 10; walet, dama i krój
     * mają wartość 10, natomiast As ma wartość 1 lub 11.
     * @param ownerCards tablica kart których wartości maja zostać zsumowane.
     * @return suma wartości kart.
     */
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
    /**
     * Sprawdza czy gracz wygrał w grę. Wygra wtedy, gdy suma punktów gracza
     * jest większa lub równa sumie punktów krupiera oraz mniejsza lub równa 21.
     * @return czy gracz wygrał w grę.
     */
    public boolean isWin(){
        int playerSum = getPoints(playerCards);
        return playerSum >= getPoints(croupierCards) && playerSum <= 21;
    }
    /**
     * Zwraca wszystkie karty gracza (wraz z miejscami równymi 0).
     * @return tablica z kartami gracza.
     */
    public int[] getPlayerCards(){
        return playerCards;
    }
    /**
     * Zwraca wszystkie karty krupiera (wraz z miejscami równymi 0).
     * @return tablica z kartami krupiera.
     */
    public int[] getCroupierCards(){
        return croupierCards;
    }
    private int findZero(int[] ownerCards){
        for(int i = 0; i < ownerCards.length; i++)
            if(ownerCards[i] == 0) return i;
        return ownerCards.length; // jeśli nie znajdzie 0, zwraca długość
    }
}

