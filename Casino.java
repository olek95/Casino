package casino;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Casino {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                JFrame frame = CasinoFrame.createCasinoFrame();
                frame.setTitle("Casino");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    }
}
