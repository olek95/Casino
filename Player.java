package casino;
/**
 * Obiekt klasy <code>Player</code> będzie reprezentował gracza grającego w
 * kasynie w gry Jednoręki bandyta i Blackjack. 
 * @author AleksanderSklorz
 */
public class Player {
    private static Player p;
    private static int cash;
    private Player(){
    }
    public static synchronized Player createPlayer(){
        if(p == null)
            p = new Player(); 
        return p;
    }
    /**
     * Ustawia graczowi liczbę posiadanych przez niego pieniędzy.
     * @param cash liczba pieniędzy jaką posiada.
     */
    public void setCash(int cash){
        this.cash = cash;
    }
    /**
     * Zwraca liczbę posiadanych przez gracza pieniędzy.
     * @return liczba posiadanych przez gracza pieniędzy.
     */
    public int getCash(){
        return cash;
    }
    /**
     * Dodaje lub odejmuje graczowi pieniądze, w zalezności od tego czy wygrał czy przegrał grę.
     * @param cash liczba dodanych lub odjętych pieniędzy.
     * @param winOfPlayer określa czy gracz wygrał czy przegrał w grze.
     */
    public void changeCash(int cash, boolean winOfPlayer){
        if(winOfPlayer) this.cash += cash;
        else this.cash -= cash;
    }
}
