package cm3113.lab04;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

public class Ex1 extends JFrame {

    JLabel clock1, clock2, clock3, clock5;
    JLabel clock4; // Line 1.4 a
    // ClockLabel clock4;
    JPanel panel;
    JTextArea times;

    public Ex1() {
        initComponents();
		
		// Exercise 1.2
		int interval = 1000;
		javax.swing.Timer timer = new javax.swing.Timer(interval,
		new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clock1.setText(getTime());
			}
		});
		timer.start();
		
		// Exercise 1.3
		Thread updating = new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e){}
					java.awt.EventQueue.invokeLater(
						new Runnable() {
						@Override
						public void run() {
							clock2.setText(getTime());
						}
					});
				}
			}
		};
		updating.setDaemon(true);
		updating.start();
		
		// Exercise 1.4
		java.util.Timer timer2 = new java.util.Timer(true);
		
		java.util.TimerTask task = new java.util.TimerTask() {
			@Override
			public void run() {
				java.awt.EventQueue.invokeLater(
					new Runnable() {
					@Override
					public void run() {
						clock3.setText(getTime());
					}
				});
			}
		};
        timer2.scheduleAtFixedRate(task, 0, 1000);
		
		// Exercise 1.5
        ClockLabel clock4;
		
		// Exercise 1.6
        javax.swing.SwingWorker<Void, String> worker = new javax.swing.SwingWorker<>() {
			
			int count = 0;

			@Override
			protected Void doInBackground() {
				for (count = 0; count <= 5; count++) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {}
					String time = getTime();
					publish("Tick " + count + " at " + time);
				}
				return null;
			}

			@Override
			protected void done() {
				times.append("Last time was " + getTime());
				times.append(" after " + count + " ticks");
			}

			@Override
			protected void process(List<String> chunks) {
				for (String str : chunks) {
					clock5.setText(str);
				}
			}			
		};
		
		worker.execute();
	}
	
    /* main method that runs GUI as a program */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ex1().setVisible(true);
            }
        });
    }

    /* helper method that sets time, formats it, and returns as String */
    public String getTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ISO_DATE) + " "
                + now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /* sets up all the GUI components */
    private void initComponents() {
        this.setTitle("cm3113 - lab4 - MainAppEx1");
        this.setSize(600, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        panel = new JPanel(new GridLayout(5, 2));
        this.add(panel, BorderLayout.CENTER);
        panel.add(new JLabel("Clock 1 - updated by swing.Timer"));
        clock1 = new JLabel();
        clock1.setForeground(Color.red);
        panel.add(clock1);
        panel.add(new JLabel("Clock 2 - updated from another Thread"));
        clock2 = new JLabel();
        clock2.setForeground(Color.blue);
        panel.add(clock2);
        panel.add(new JLabel("Clock 3 - updated by util.Timer"));
        clock3 = new JLabel();
        clock3.setForeground(Color.magenta);
        panel.add(clock3);

        panel.add(new JLabel("Clock 4 - bespoke JLabel updates itself"));
        clock4 = new ClockLabel();
        clock4.setForeground(Color.GRAY);
        // clock4 = new ClockLabel(); clock4.setForeground(Color.GRAY);
        panel.add(clock4);

        clock5 = new JLabel();
        panel.add(clock5);

        times = new JTextArea();
        panel.add(times);
    }
	
    class ClockTimer extends Thread {
			
		private ClockLabel target;
		private long period;

		public ClockTimer(ClockLabel t, long p) {
			this.target = t;
			this.period = p;
			this.setPriority(Thread.MAX_PRIORITY);
			this.setDaemon(true);
		}

		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(period);
				} catch (InterruptedException e) {}
				target.tick();
			}
		}
	}
	
	class ClockLabel extends JLabel {
			
		private ClockTimer theClock;

		public ClockLabel() {
			theClock = new ClockTimer(this, 1000L);
			theClock.start();
		}

		public void tick() {
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.drawString(getTime(), 0, 25);
		}
	}
}
