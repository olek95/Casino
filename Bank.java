package casino;

/**
 * Obiekt klasy <code>Bank</code> reprezentuje bank kasyna, który zarządza jego
 * stanem pieniędzy oraz stawkami w grach.
 * @author AleksanderSklorz
 */
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
    /**
     * Zmienia stan pieniędzy kasyna w zależności od tego czy gracz wygrał czy 
     * przegrał w grze. 
     * @param cash dodana lub odjęta liczba pieniędzy.
     * @param winOfPlayer określa czy gracz wygrał czy przegrał w grze. 
     */
    public void changeStateOfCasinoMoney(int cash, boolean winOfPlayer){
        if(winOfPlayer) stateOfCasinoMoney -= cash;
        else stateOfCasinoMoney += cash;
    }
    /**
     * Ustawia stan pieniędzy kasyna. 
     * @param cash liczba pieniędzy jaką kasyno posiada na koncie.
     */
    public void setStateOfCasinoMoney(int cash){
        stateOfCasinoMoney = cash;
    }
    /**
     * Zwraca stan pieniędzy kasyna.
     * @return liczba pieniędzy kasyna na koncie.
     */
    public int getStateOfCasinoMoney(){
        return stateOfCasinoMoney;
    }
    /**
     * Ustawia stawkę gry. 
     * @param rate stawka gry.
     */
    public void setRate(int rate){
        this.rate = rate;
    }
    /**
     * Zwraca stawkę gry.
     * @return stawka gry.
     */
    public int getRate(){
        return rate;
    }
}
