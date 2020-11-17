package cm3113.lab05;

/* File: GUIApplication.java 
 * Starting point to CM3113 Lab 5 Exercise 1 */
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class Ex1Main extends JFrame {

    CountingThreadSem thread1, thread2;
    Semaphore sem;
    long critDelay = 1L, restDelay = 1L;

    public Ex1Main(){
        initComponents();
    }
    
    public void initComponents(){
        this.setTitle("Exercise 1");
        this.setSize(400,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(5,2));
        
        Counter counter1 = new Counter();
        panel.add(new JLabel("  thread 1"));   panel.add(counter1);
        Counter counter2 = new Counter();
        panel.add(new JLabel("  thread 2"));   panel.add(counter2);
        SharedCounter sharedCounter = new SharedCounter();
        panel.add(new JLabel("  violations")); panel.add(sharedCounter);
        
        this.add(panel, BorderLayout.CENTER);
        
        JButton goButton = new JButton("Go"), stopButton = new JButton("Stop");
        panel.add(goButton);  panel.add(stopButton);
        
        JPanel critPanel = new JPanel(new GridLayout(1,2)); panel.add(critPanel);
        JPanel restPanel = new JPanel(new GridLayout(1,2)); panel.add(restPanel);
        
        JSpinner spinnerCritDelay = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
        JSpinner spinnerRestDelay = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
        critPanel.add(new JLabel("Critical delay ms")); critPanel.add(spinnerCritDelay);
        restPanel.add(new JLabel("Rest delay ms")); restPanel.add(spinnerRestDelay);

        sem = new Semaphore(1);

        goButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                setTitle("Exercise 1 - Crit = " + critDelay + "ms" + restDelay + "ms");
                sharedCounter.reset();
                if (thread1 == null || thread1.getState()==Thread.State.TERMINATED) {
                    counter1.setValue(0L);
                    thread1 = new CountingThreadSem("t1", counter1, sharedCounter, sem, critDelay, restDelay);
                    thread1.start();
                }
                if (thread2 == null || thread2.getState()==Thread.State.TERMINATED) {
                    counter2.setValue(0L);
                    thread2 = new CountingThreadSem("t2", counter2, sharedCounter, sem, critDelay, restDelay);
                    thread2.start();
                }
                goButton.setEnabled(false);
                stopButton.setEnabled(true);
                spinnerCritDelay.setEnabled(false);
                spinnerRestDelay.setEnabled(false);
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                thread1.terminate();
                thread2.terminate();
                goButton.setEnabled(true);
                stopButton.setEnabled(false);
                spinnerCritDelay.setEnabled(true);
                spinnerRestDelay.setEnabled(true);
            }
        });
        
        spinnerCritDelay.addChangeListener(new javax.swing.event.ChangeListener(){
            @Override public void stateChanged(ChangeEvent e) {
                critDelay = (Integer)spinnerCritDelay.getValue();
            }
        });
        spinnerRestDelay.addChangeListener(new javax.swing.event.ChangeListener(){
            @Override public void stateChanged(ChangeEvent e) {
                restDelay = (Integer)spinnerRestDelay.getValue();
            }
        });
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
                @Override public void run() {
                    new Ex1Main().setVisible(true);
                }
            }
        );        
    }
}
