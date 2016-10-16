package casino;

public class Bank {
    private static int stateOfCasinoMoney = 10000;
    private static int rate;
    private static Bank b;
    private Bank(){
    }
    public static synchronized Bank createBank(){
        if(b == null)
            b = new Bank();
        return b;
    }
    public void changeStateOfCasinoMoney(int cash, boolean winOfPlayer){
        if(winOfPlayer) stateOfCasinoMoney -= cash;
        else stateOfCasinoMoney += cash;
    }
    public void setStateOfCasinoMoney(int cash){
        stateOfCasinoMoney = cash;
    }
    public int getStateOfCasinoMoney(){
        return stateOfCasinoMoney;
    }
    public void setRate(int rate){
        this.rate = rate;
    }
    public int getRate(){
        return rate;
    }
}
