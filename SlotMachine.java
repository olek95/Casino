package casino;

import java.util.Random;
/**
 * Obiekt klasy <code>SlotMachine</code> będzie reprezentował grę w jednorękiego bandytę.
 * Gra ta polega na wylosowaniu trzech jednakowych elementów. Jeśli wszystkie 3 będą
 * takie same - gracz wygrywa, w przeciwnym wypadku wygrywa kasyno. 
 * @author AleksanderSklorz
 */
public class SlotMachine {
    private static SlotMachine game;
    private static int[] score;
    private SlotMachine(){
    }
    public static synchronized SlotMachine createSlotMachine(){
        if(game == null)
            game = new SlotMachine();
        return game;
    }
    /**
     * Uruchamia grę. Losuje trzy wartości z zakresu od 0 do 8.
     * @return tablica z wylosowanymi wartościami. 
     */
    public int[] play(){
        Random rand = new Random();
        score = new int[3];
        for(int i = 0; i < 3; i++)
            score[i] = rand.nextInt(9);
        return score;
    }
    /**
     * Sprawdza czy gracz wygrał grę. Wygrana będzie wtedy, gdy wszystkie 3 wartości są takie same.
     * @return czy gracz wygrał grę. 
     */
    public boolean isWin(){
        // są 3 wartości, więc wystarczy porównać pierwszą z drugą i drugą z trzecią, nie trzeba pierwszej z trzecią
        return !(score[0] != score[1] || score[1] != score[2]);
    }
}
