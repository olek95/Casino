package casino;

import java.util.Random;

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
    public int[] play(){
        Random rand = new Random();
        score = new int[3];
        boolean win = true;
        for(int i = 0; i < 3; i++)
            score[i] = rand.nextInt(9);
        return score;
    }
    public boolean isWin(){
        for(int i = 0; i < 3; i++)
            if(i != 0)
                if(score[i] != score[i - 1]) return false;
        return true;
    }
}
