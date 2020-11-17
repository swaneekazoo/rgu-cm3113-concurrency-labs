package cm3113.lab05.solutions.ex3.ex2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Ex3Main extends JFrame {

    Semaphore sem;
    JButton startButton;
    JPanel dataPanel;
    ArrayList<JLabel> cells;
    ArrayList<Color> colors;
    javax.swing.Timer timer;
    int rows, cols, size;

    public Ex3Main() {
        initComponents();
    }
    
    public void startThreads(){
        startButton.setEnabled(false);
        System.out.println("r=" + rows + ", c=" + cols + ", n=" + size);
        Thread t1 = new Thread(new Filler(Color.BLUE, size/2, colors, sem));
        Thread t2 = new Thread(new Filler(Color.RED, size/2, colors, sem));
        t1.start();
        t2.start();
    }

    public void initComponents() {
        sem = new Semaphore(1);

        this.setTitle("Exercise 3");
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                startThreads();
            }
        }       
        );       
        this.add(startButton, BorderLayout.NORTH);
       
        rows = 50; cols = 50; size = rows*cols;
        dataPanel = new JPanel(new GridLayout(rows, cols));
        cells = new ArrayList();
        colors = new ArrayList();
        for (int i = 0; i < size; i++) {
            JLabel x = new JLabel("");
            x.setBackground(Color.LIGHT_GRAY);
            x.setBorder(new LineBorder(Color.BLACK));
            x.setOpaque(true);
            cells.add(x);
            dataPanel.add(x);
        }

        this.add(dataPanel, BorderLayout.CENTER);
               
        timer = new javax.swing.Timer(0,
            new ActionListener() {
                @Override public void actionPerformed(ActionEvent e){
                displayGrid();
                if(colors.size() == size) timer.stop();
            }
            });
        timer.setDelay(25);
        timer.start();
    }
    
    public void displayGrid(){
        for (int i = 0; i < colors.size(); i++) {
            cells.get(i).setBackground(colors.get(i));
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Ex3Main().setVisible(true);
            }
        }
        );
    }
    
    public void delayFor(long time){
        try {Thread.sleep(time);}
        catch (InterruptedException e) {System.out.println("?? in delayFor");}
    }
    
    /* ********************************************************* */

    class Filler implements Runnable {

        private Color color;
        private int number;
        private ArrayList<Color> panel;
        private Semaphore sem;

        public Filler(Color c, int num, ArrayList<Color> p, Semaphore sem) {
            this.color = c;
            this.number = num;
            this.panel = p;
            this.sem = sem;
        }

        @Override
        public void run() {
            try {
                sem.acquire();
                for (int i = 0; i < number; i++) {
                    panel.add(color);
                    delayFor(1L);
                }
            } catch (InterruptedException e) {
            } finally {
                sem.release();
            }
        }
    }
}
