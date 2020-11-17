// File: MainAppEx2SwingTimer.java
package cm3113.lab04;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainAppEx2Example extends JFrame {

    /* references to GUI components that will be event sources here */
    private JButton button1, button2, button3;
    private JMenuItem menuItemA, menuItemB;
    private JTextField textfield1;
    private JTextArea textarea1;
    private javax.swing.Timer timer;

    // define delay between actionEvent's automatically generated by the timer
    private static final int TIMER_DELAY = 1000;

    /* **** declare references to application specific objects here **** */
    
    /* Constructor for JFrame */
    public MainAppEx2Example() {
        initComponents(); // initialise GUI components
        setupTimer();
    }

    /* JFrame runs as an application, running as per good practice on EDT */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainAppEx2Example().setVisible(true);
            }
        });
    }

    public void setupTimer(){
    /* construct a swing timer to generate actionEvents every 1000 milliseconds
     * Because the timer is not a visible GUI component we don't need to add 
     * the timer to the visible contents of the frame. But we do need to 
     * ensure that timer events are handled */
        timer = new javax.swing.Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do whatever timer requires
                textarea1.append("The timer has generated an event\n");
            }
        });
        // the timer object is a thread so we need to start it!
        timer.start();
    }
    
    public void initComponents() {

        this.setTitle("cm3113 lab04 Ex2 SwingTimer"); // set title of frame  
        this.setSize(400, 400);    // set initial size of frame (x,y) in pixels

        // set up menu bar for this frame
        JMenuBar menubar = new JMenuBar();
        JMenu menu1 = new JMenu("Menu1");
        menuItemA = new JMenuItem("Item A");
        menuItemB = new JMenuItem("Item B");
        menu1.add(menuItemA);
        menu1.add(menuItemB);
        menubar.add(menu1);

        this.setJMenuBar(menubar);     // add menu bar to this frame
        // register ActionListener for each menu item
        menuItemA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* **** do whatever item1a requires **** */
                textarea1.append("You selected item1a\n");
            }
        });
        menuItemB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* **** do whatever item1b requires **** */
                textarea1.append("You selected item1b\n");
            }
        });

        // create a Panel to group together button1, button2, button3
        JPanel panel = new JPanel(new GridLayout(3, 1));
        button1 = new JButton("B1");
        panel.add(button1);
        button2 = new JButton("B2");
        panel.add(button2);
        button3 = new JButton("B3");
        panel.add(button3);
        this.add(panel, BorderLayout.EAST);  // add panel to east side of frame

        // register listeners for action events generated by each button
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* **** do whatever button 1 requires **** */
                textarea1.append("You hit button 1\n");
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* **** do whatever button 2 requires **** */
                textarea1.append("You hit button 2\n");
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* **** do whatever button 3 requires **** */
                textarea1.append("You hit button 3\n");
            }
        });

        // create a Text Field and locate it at the south side of the frame
        textfield1 = new JTextField("", 20);
        /* (initial text, column width) */
        this.add(textfield1, BorderLayout.SOUTH);
        // make sure user can edit this field i.e. this field is for input
        textfield1.setEditable(true);
        // register frame as listener for action events generated by this field
        textfield1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // user has finished editing textfield1 get text and use it
                String text = textfield1.getText();
                textarea1.append("You typed " + text + "\n");
            }
        });

        // create a Text Field and locate it at the south side of the frame
        textarea1 = new JTextArea("", 5, 20); /*(initial text,rows,cols) */
        // make sure user cannot edit this field i.e. if for output only
        textarea1.setEditable(false);
        /* provide automatic scrolling capability for the text area by adding
         * it into a JScrollPane add to the contents of this frame */
        this.add(new JScrollPane(textarea1), BorderLayout.CENTER);
        
        // construct any application specific objects here... 
    }
}
