package casino;

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
    public void setCash(int cash){
        this.cash = cash;
    }
    public int getCash(){
        return cash;
    }
    public void changeCash(int cash, boolean winOfPlayer){
        if(winOfPlayer) this.cash += cash;
        else this.cash -= cash;
    }
}
