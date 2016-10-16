package casino;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class CasinoFrame extends JFrame{
    private static CasinoFrame frame;
    private static JRadioButton slotMachineButton, blackjackButton;
    private static JButton startButton, resetButton, exitButton, point1, point2, point3; 
    private static JTextField rateField, playerCashField;
    private static JTextArea scoreArea;
    private static JPanel scorePanel;
    private static JLabel rateLabel;
    private static Bank b;
    private static Player p; 
    private static boolean start; 
    private CasinoFrame(){ 
        scorePanel = new JPanel();
        rateLabel = new JLabel();
        JPanel gamesPanel = new JPanel(); 
        ButtonGroup gamesButtonGroup = new ButtonGroup();
        slotMachineButton = new JRadioButton("Jednoręki bandyta");
        setLayout(new BorderLayout());
        slotMachineButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                rateLabel.setText("Podaj stawkę (1/5/10/15):");
                scorePanel.removeAll();
                point1 = new JButton("1");
                point1.setPreferredSize(new Dimension(180, 320));
                point2 = new JButton("2");
                point2.setPreferredSize(new Dimension(180, 320));
                point3 = new JButton("3");
                point3.setPreferredSize(new Dimension(180, 320));
                scorePanel.add(point1);
                scorePanel.add(point2);
                scorePanel.add(point3);
                add(scorePanel, BorderLayout.CENTER);
                validate();
            } 
        });
        slotMachineButton.doClick();
        blackjackButton = new JRadioButton("Blackjack");
        blackjackButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                rateLabel.setText("Podaj stawkę:");
                scorePanel.removeAll();
                scoreArea = new JTextArea(20, 50);
                scorePanel.add(scoreArea);
                add(scorePanel, BorderLayout.CENTER);
                validate();
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
                String rate = rateField.getText();
                String cash = playerCashField.getText(); 
                if(!start)
                    if(rate != null && !rate.equals("") && rate.matches("[0-9]+")){
                        if(cash != null && !cash.equals("") && cash.matches("[0-9]+")){
                            if(slotMachineButton.isSelected()) startSlotMachineGame(Integer.parseInt(cash), Integer.parseInt(rate));
                            else startBlackjackGame(Integer.parseInt(cash), Integer.parseInt(rate));
                        }else
                            JOptionPane.showMessageDialog(null, "Pole z Twoimi pieniędzmi nie może być puste oraz musi składać się z cyfr.");
                    }else{
                        JOptionPane.showMessageDialog(null, "Pole ze stawką nie może być puste oraz musi składać się z cyfr.");
                    }
                else if(slotMachineButton.isSelected()) startSlotMachineGame(0, 0);
                     else startBlackjackGame(0, 0);
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
    public void startSlotMachineGame(int cash, int rate){
        SlotMachine slotMachineGame = SlotMachine.createSlotMachine(); 
        if(!start){
            b.setRate(rate);
            p.setCash(cash);
            start = true;
        }
        int[] score = slotMachineGame.play(); 
        point1.setText(score[0] + "");
        point2.setText(score[1] + "");
        point3.setText(score[2] + "");
        boolean win = slotMachineGame.isWin();
        p.changeCash(b.getRate(), win);
        b.changeStateOfCasinoMoney(b.getRate(), win);
        if(win){
            JOptionPane.showMessageDialog(null, "WYGRAŁEŚ!!\nTwoje pieniądze: " + p.getCash() + " pieniądze kasyna: " + b.getStateOfCasinoMoney());
        }else{
            JOptionPane.showMessageDialog(null, "PRZEGRAŁEŚ!!\nTwoje pieniądze: " + p.getCash() + " pieniądze kasyna: " + b.getStateOfCasinoMoney());
        }
    }
    public void startBlackjackGame(int cash, int rate){
        Blackjack blackjackGame = Blackjack.createBlackjack();
        int[] playerCards, croupierCards;
        blackjackGame.play();
        char character;
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
            scoreArea.append("Co chcesz zrobić?\n");
            scoreArea.append("h - dobrać kartę, s - nie dobierać karty, r - rozdwoić karty, c - sprawdzić wynik\n");
            int actualLineCount;
            actualLineCount = scoreArea.getLineCount();
            String text = scoreArea.getText();
            System.out.println(getLastRow(scoreArea));
            do{
            }while(text.equals(scoreArea.getText()));
            character = Character.toLowerCase(getLastRow(scoreArea).charAt(0));
            switch(character){
                case 'h':
                    blackjackGame.hit(blackjackGame.getPlayerCards());
                    break;
                case 's':
                     break;
                case 'r':
                    scoreArea.append("Wybierz numer karty, której duplikatu chcesz się pozbyć:\n");
                    int i = Integer.parseInt(getLastRow(scoreArea));
                    blackjackGame.split(i);
                    break;
                case 'c':
                    scoreArea.append("Wynik:\n");
                    win = blackjackGame.isWin();
                    scoreArea.append("Gracz: " + blackjackGame.getPoints(playerCards) + " krupier: " + blackjackGame.getPoints(croupierCards) + "\n");
            }
        }while(character != 'c');
    }
    private String getLastRow(JTextArea textArea){
        int i = textArea.getLineCount() - 2;
        String text = textArea.getText();
        return text.split("\n")[i];
    }
    private boolean isEnteredText(JTextArea textArea, int actualLineCount){
        if(textArea.getLineCount() != actualLineCount) return true; 
        else return false;
    }
}
