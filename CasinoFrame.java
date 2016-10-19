package casino;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Obiekt klasy <code>CasinoFrame</code> reprezentuje ramkę zarządzającą kasynem.
 * Umożliwia ona podanie pieniędzy posiadanych przez gracza, ustawienie stawki gry, 
 * uruchomienie jednorękiego bandyty lub blackjacka oraz resetowanie ustawień.
 * @author AleksanderSklorz
 */
public class CasinoFrame extends JFrame{
    private static CasinoFrame frame;
    private static JRadioButton slotMachineButton, blackjackButton;
    private static JButton startButton, resetButton, exitButton, point1, point2, point3; 
    private static JTextField rateField, playerCashField;
    private static JTextArea scoreArea;
    private static JPanel scoreSlotMachinePanel;
    private static JScrollPane scoreScrollPane;
    private static JLabel rateLabel;
    private static Bank b;
    private static Player p; 
    private static boolean start; 
    private CasinoFrame(){ 
        scoreSlotMachinePanel = new JPanel();
        scoreArea = new JTextArea(20, 50);
        scoreScrollPane = new JScrollPane(scoreArea);
        point1 = new JButton();
        point1.setPreferredSize(new Dimension(180, 320));
        point1.setEnabled(false);
        point1.setBackground(Color.CYAN);
        point2 = new JButton();
        point2.setEnabled(false);
        point2.setBackground(Color.CYAN);
        point2.setPreferredSize(new Dimension(180, 320));
        point3 = new JButton();
        point3.setEnabled(false);
        point3.setBackground(Color.CYAN);
        point3.setPreferredSize(new Dimension(180, 320));
        scoreSlotMachinePanel.setVisible(true);
        scoreScrollPane.setVisible(false);
        scoreSlotMachinePanel.add(point1);
        scoreSlotMachinePanel.add(point2);
        scoreSlotMachinePanel.add(point3);
        rateLabel = new JLabel();
        JPanel gamesPanel = new JPanel(); 
        ButtonGroup gamesButtonGroup = new ButtonGroup();
        slotMachineButton = new JRadioButton("Jednoręki bandyta");
        setLayout(new BorderLayout());
        slotMachineButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                rateLabel.setText("Podaj stawkę (1/5/10/15):");
                scoreSlotMachinePanel.setVisible(true);
                scoreScrollPane.setVisible(false);
                add(scoreSlotMachinePanel, BorderLayout.CENTER);
                validate();
                if(start) changeRate(); 
            } 
        });
        slotMachineButton.doClick();
        blackjackButton = new JRadioButton("Blackjack");
        blackjackButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                rateLabel.setText("Podaj stawkę:");
                scoreArea.setLineWrap(true);
                scoreArea.setText("ZASADY GRY:\nKarty o wartościach od 2 do 10 są odpowiednio"
                + "oznaczone liczbami od 2 do 10. Waltet, Dama i Król to kolejno liczby "
                + "11, 12, 13. Te trzy karty mają wartość 10. Natomiast AS może mieć wartośc "
                + "1 lub 11, gdzie 1 to oznaczenie 14, a 11 to oznaczenie 15.\n"
                + "Gracz wygrywa, gdy jego liczba punktów jest >= niż punkty krupiera oraz <= od 21.\n"
                + "Możliwe ruchy gracza: hit (pobranie karty), split (zamiana podwójnych kart), stand (oczekiwanie)"
                + "lub check (sprawdzenie wyniku).\n\n");
                scoreSlotMachinePanel.setVisible(false); 
                scoreScrollPane.setVisible(true);
                add(scoreScrollPane, BorderLayout.CENTER);
                validate();
                if(start) changeRate();
            } 
        });
        gamesButtonGroup.add(slotMachineButton);
        gamesButtonGroup.add(blackjackButton);
        gamesPanel.add(slotMachineButton);
        gamesPanel.add(blackjackButton);
        gamesPanel.add(new JLabel("Twoja kasa:"));
        playerCashField = new JTextField(5);
        gamesPanel.add(playerCashField);
        gamesPanel.add(rateLabel);
        rateField = new JTextField(4);
        gamesPanel.add(rateField);
        add(gamesPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(); 
        startButton = new JButton("START");
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String sRate = rateField.getText();
                String sCash = playerCashField.getText(); 
                if(sRate != null && sRate.matches("[0-9]+")){
                    if(sCash != null && sCash.matches("[0-9]+")){
                        int rate = Integer.parseInt(sRate);
                        int cash = Integer.parseInt(sCash);
                        if(cash < rate || b.getStateOfCasinoMoney() < rate){
                            JOptionPane.showMessageDialog(null, "Tobie lub kasynie brakuje pieniędzy.");
                        }else{
                            rateField.setEnabled(false);
                            playerCashField.setEnabled(false);
                            if(slotMachineButton.isSelected()){
                                if(checkForSlotMachineGame(rate))
                                    startSlotMachineGame(cash, rate);
                                else{
                                    rateField.setEnabled(true);
                                    playerCashField.setEnabled(true);
                                    JOptionPane.showMessageDialog(null, "Dla jednorękiego bandyty dozwolone wartości to 1/5/10/15");
                                }
                            }
                            else startBlackjackGame(cash, rate);
                        }
                    }else
                        JOptionPane.showMessageDialog(null, "Pole z Twoimi pieniędzmi nie może być puste oraz musi składać się z cyfr.");
                }else{
                    JOptionPane.showMessageDialog(null, "Pole ze stawką nie może być puste oraz musi składać się z cyfr.");
                }
            }
        });
        exitButton = new JButton("EXIT");
        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        resetButton = new JButton("RESET");
        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                start = false;
                b.setStateOfCasinoMoney(10000);
                rateField.setEnabled(true);
                playerCashField.setEnabled(true);
            }
        });
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(exitButton); 
        add(buttonPanel, BorderLayout.SOUTH);
        b = Bank.createBank();
        p = Player.createPlayer();
        pack();
    }
    public static synchronized CasinoFrame createCasinoFrame(){
        if(frame == null)
            frame = new CasinoFrame();
        return frame;
    }
    /**
     * Uruchamia grę Jednoręki bandyta. Ustawia wartości pieniędzy gracza oraz
     * stawkę gry. Wyświetla wylosowane wartości oraz informację o wygranej lub 
     * przegranej gracza. Wygrana gracza jest dodatkowo sygnalizowana dźwiękiem.
     * @param cash pieniądze posiadane przez gracza.
     * @param rate stawka gry.
     */
    public void startSlotMachineGame(int cash, int rate){
        SlotMachine slotMachineGame = SlotMachine.createSlotMachine(); 
        if(!start){ // uniemożliwia zmianę pieniędzy gracza i stawki w trakcie gry
            b.setRate(rate);
            p.setCash(cash);
            start = true;
        }
        int[] score = slotMachineGame.play(); 
        point1.setText("<html><b><font size = 7>" + score[0] + "</font></b></html>");
        point2.setText("<html><b><font size = 7>" + score[1] + "</font></b></html>");
        point3.setText("<html><b><font size = 7>" + score[2] + "</font></b></html>");
        boolean win = slotMachineGame.isWin();
        p.changeCash(b.getRate(), win);
        b.changeStateOfCasinoMoney(b.getRate(), win);
        // dzięki poniższemu wywołaniu pieniądze będą wyświetlane w walucie państwa w którym uruchomiono program
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        if(win){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "WYGRAŁEŚ!!\nTwoje pieniądze: " + currencyFormatter.format(p.getCash()) + " pieniądze kasyna: " + currencyFormatter.format(b.getStateOfCasinoMoney()));
        }else{
            JOptionPane.showMessageDialog(null, "PRZEGRAŁEŚ!!\nTwoje pieniądze: " + currencyFormatter.format(p.getCash()) + " pieniądze kasyna: " + currencyFormatter.format(b.getStateOfCasinoMoney()));
        }
        changeRate();
    }
    /**
     * Uruchamia grę Blackjack. Ustawia wartości pieniędzy gracza oraz stawkę gry.
     * Wyświetla pobrane karty gracza oraz karty krupiera, przy czym jedna karta
     * krupiera jest zasłonięta. Wyświetla też informację o wygranej lub przegranej 
     * gracza, wygrana jest sygnalizowana dodatkowo dźwiękiem. W trakcie gry 
     * wyświetla okienko dialogowe umożliwiające wykonywanie czynności w trakcie grzy przez gracza.
     * @param cash pieniądze posiadane przez gracza. 
     * @param rate stawka gry. 
     */
    public void startBlackjackGame(int cash, int rate){
        Blackjack blackjackGame = Blackjack.createBlackjack();
        if(!start){
            b.setRate(rate);
            p.setCash(cash);
            start = true;
        }
        int[] playerCards, croupierCards;
        blackjackGame.play();
        int option;
        boolean win = false;
        do{
            scoreArea.append("Twoje karty: ");
            playerCards = blackjackGame.getPlayerCards();
            for(int card : playerCards)
                if(card != 0) scoreArea.append(card + " ");
            scoreArea.append("\n");
            scoreArea.append("Suma Twoich punktów: " + blackjackGame.getPoints(playerCards) + "\n");
            scoreArea.append("Karty krupiera: ");
            croupierCards = blackjackGame.getCroupierCards(); 
            for(int i = 0; i < croupierCards.length; i++){
                if(croupierCards[i] != 0)
                    if(i == 0) scoreArea.append("? ");
                    else scoreArea.append(croupierCards[i] + " ");
            }
            scoreArea.append("\n");
            boolean done;
            String sNumber;
            do{
                done = true;
                sNumber = "";
                option  = JOptionPane.showOptionDialog(null, "Co chcesz zrobić?", null, 
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Hit", "Stand", "Split", "Check"}, null);
                switch(option){
                    case 0:
                        done = blackjackGame.hit(blackjackGame.getPlayerCards());
                        if(!done)
                            JOptionPane.showMessageDialog(null, "Nie możesz mieć więcej kart niż 9.");
                        break;
                    case 1:
                         break;
                    case 2:
                        do{
                            sNumber = JOptionPane.showInputDialog("Wybierz numer karty, której duplikatu chcesz się pozbyć:");
                            if(sNumber != null && sNumber.matches("[0-9]+")){
                                int i = Integer.parseInt(sNumber);
                                done = blackjackGame.split(i);
                            }
                        }while(sNumber != null && !sNumber.matches("[0-9]+"));
                        if(!done)
                            JOptionPane.showMessageDialog(null,"Ta liczba się nie powtarza. Wybierz inną czynność lub podaj inny numer liczby.");
                        break;
                    case 3:
                        scoreArea.append("Wynik:\n");
                        win = blackjackGame.isWin();
                        scoreArea.append("Gracz: " + blackjackGame.getPoints(playerCards) + " krupier: " + blackjackGame.getPoints(croupierCards) + "\n");
                }
            }while(!done || sNumber == null);
        }while(option != 3);
        b.changeStateOfCasinoMoney(b.getRate(), win);
        p.changeCash(b.getRate(), win);
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        if(win){
            Toolkit.getDefaultToolkit().beep();
            scoreArea.append("WYGRAŁEŚ!!\nTwoje pieniądze: " + currencyFormatter.format(p.getCash()) + " pieniądze kasyna: " + currencyFormatter.format(b.getStateOfCasinoMoney()) + "\n\n");
        }else{
            scoreArea.append("PRZEGRAŁEŚ!!\nTwoje pieniądze: " + currencyFormatter.format(p.getCash()) + " pieniądze kasyna: " + currencyFormatter.format(b.getStateOfCasinoMoney()) + "\n\n");
        }
        changeRate();
    }
    private void changeRate(){
        boolean notEnoughMoney = false;
        int option = JOptionPane.showConfirmDialog(null, "Czy chcesz zmienić stawkę?");
        if(option == JOptionPane.YES_OPTION){
            String sNewRate;
            int newRate = 0;
            do{
                sNewRate = JOptionPane.showInputDialog("Ile ma wynosić nowa wartość stawki? (jeśli Jednoręki bandyta to 1/5/10/15)");
                if(sNewRate != null && sNewRate.matches("[0-9]+")){
                    newRate = Integer.parseInt(sNewRate);
                    notEnoughMoney = newRate > p.getCash() || newRate > b.getStateOfCasinoMoney();
                    if(notEnoughMoney){
                        JOptionPane.showMessageDialog(null, "Gracz lub kasyno nie mają tyle pieniędzy.");
                    }else{
                        if(checkForSlotMachineGame(newRate)){
                            b.setRate(Integer.parseInt(sNewRate));
                        }
                    }
                }
            }while(sNewRate != null && (!sNewRate.matches("[0-9]+") || !checkForSlotMachineGame(newRate) || notEnoughMoney));
        }    
    }
    private boolean checkForSlotMachineGame(int cash){
        return slotMachineButton.isSelected() && (cash == 1 || cash == 5 || cash == 10 || cash == 15) || !slotMachineButton.isSelected();
    }
}
