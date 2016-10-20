package casino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Obiekt klasy <code>Blackjack</code> będzie reprezentował grę Blackjack. 
 * Gracz wygrywa w tej grze jeśli ma więcej lub tyle samo punktów co krupiej oraz
 * mniej lub równo 21 punktów albo jeśli krupier ma więcej niż 21 punktów a gracz mniej lub równo 21.
 * @author AleksanderSklorz
 */
public class Blackjack {
    private static Blackjack game;
    private static int[] playerCards, croupierCards;
    private static HashMap<Integer, Integer> availableCards;
    private static ArrayList<Integer> deck;
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
        Random ran = new Random();
        deck = new ArrayList();
        playerCards = new int[9]; // maksymalnie gracz może mieć 9 karty w talii
        croupierCards = new int[9];
        availableCards = new HashMap();
        for(int i = 2; i <= 14; i++)
            availableCards.put(i, 4); // każda wartość występuje po 4 razy
        for(int i = 0; i < 52; i++){ // talia ma 52 karty
            boolean added = false;
            do{
                int cardType = ran.nextInt(13) + 2;
                if(availableCards.get(cardType) != 0){
                    deck.add(cardType);
                    availableCards.replace(cardType, availableCards.get(cardType) - 1);
                    added = true;
                }
            }while(!added);
        }
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
        int i = findZero(ownerCards);
        int drawn;
        if(i != ownerCards.length){
            drawn = deck.remove(0); // pobiera pierwszą kartę z góry 
            if(drawn == 14)
                if(ownerCards == playerCards) drawn = aceOneOrEleven();
                else if(getPoints(croupierCards) + 11 > 21) drawn = 15;
            // założyłem że jeśli suma punktów krupiera + 11 jest mniejsza niż 11 to krupier może przyjąć wartość Asa jako 11 (bo 15 oznacza 11)
            ownerCards[i] = drawn;
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
            // sprawdza czy rzeczywiście istnieje duplikat
            if(index != i - 1 && (playerCards[index] == card || playerCards[index] == 15 && card == 14 
                    || playerCards[index] == 14 && card == 15)){ // zarówno 14 i 15 symbolizują Asa więc jest to ta sama karta
                playerCards[i - 1] = 0;
                hit(playerCards);
                if(card == 15) card--; // ogólny As ma symbol 14
                deck.add(card); // oddaje kartę na koniec talii 
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
     * Steruje decyzjami krupiera. Pobiera kartę z talii, jeśli suma punktów krupiera
     * będzie mniejsza lub równa niż 11 albo 12 albo 13 (granica dobrana losowo).
     * Ponadto decyduje o zamianie duplikatów.
     * Zdarzeia te są generowane losowo, także może zdarzyć się iż mimo np. wystąpienia 
     * duplikatu - nie zostanie on zastąpiony. 
     */
    public void doCroupierTurn(){
        Random ran = new Random(); 
        boolean doAction = ran.nextBoolean();
        /* zmienna dodana po to, aby nie zawsze dla 11 albo 12 albo 13 była pobrana karta
        lub aby nie zawsze dla duplikatów były robione zamiany*/
        if(doAction){ 
            int i = getDuplicateIndex(croupierCards);
            if(i != -1){
                int card = croupierCards[i];
                croupierCards[i] = 0;
                if(card == 15) card--;
                hit(croupierCards);
                deck.add(card);
            }else{
                /* nie trzeba sprawdzać czy nie ma więcej kart niż 9, poniewaz 
                nawet kombinacja dziesięciu najmniejszych wartości zawsze będzie mniejsza niż 
                nawiększa możliwa suma wartości czyli 13*/
                if(getPoints(croupierCards) <= ran.nextInt(3) + 11){
                    hit(croupierCards);
                }
            }
        }
    }
    /**
     * Sprawdza czy gracz wygrał w grę. Wygra wtedy, gdy suma punktów gracza
     * jest większa lub równa sumie punktów krupiera oraz mniejsza lub równa 21
     * albo gdy suma punktów krupiera jest większa niż 21 a suma punktów gracza mniejsza
     * lub równa 21.
     * @return czy gracz wygrał w grę.
     */
    public boolean isWin(){
        int playerSum = getPoints(playerCards);
        int croupierSum = getPoints(croupierCards);
        return croupierSum <= 21 && playerSum >= getPoints(croupierCards) && playerSum <= 21 || croupierSum > 21 && playerSum <= 21;
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
    private int getDuplicateIndex(int[] ownerCards){
        int zeroIndex = findZero(croupierCards);
        for(int i = 0; i < zeroIndex - 1; i++){
            for(int k = i + 1; k < ownerCards.length; k++)
                if(ownerCards[i] == ownerCards[k] || ownerCards[i] == 14 && ownerCards[k] == 15 
                        || ownerCards[i] == 15 && ownerCards[k] == 14)
                    return i;
        }
        return -1;
    }
}

