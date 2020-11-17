package lab7startingpoint.AlgorithmsDemo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author dpl0795
 */
public class App extends javax.swing.JFrame {

    String s = "";

    public enum SolutionType {
        MONITORV1, MONITORV2, MONITORV3, MONITORV4
    };

    static long timeProducerToProduceData_ms = 60;
    static long timeProducerToWriteData_ms = 100;
    long updateTime_ms = 1;
    static long timeConsumerToReadData_ms = 100;
    static long timeConsumerToConsumeData_ms = 100;
    SolutionType solution = SolutionType.MONITORV1;

    long numberProduced = 0, numberConsumed = 0;
    PCmonitor monitor;
    int SIZE = 40, NP = 1, NC = 1;
    JLabel[] b = new JLabel[SIZE];
    Data[] data = new Data[SIZE];
    Producer[] producers;
    Consumer[] consumers;
    ExecutorService executor;
    Boolean going;
    TimerTask tt;
    java.util.Timer timer1;
    javax.swing.Timer timer2;

    /**
     * Creates new form OutPutFrame
     */
    public App() {
        initComponents();

        jSpinProdProd.setValue((int) timeProducerToProduceData_ms);
        jSpinProdWrit.setValue((int) timeProducerToWriteData_ms);
        jSpinConsumerRead.setValue((int) timeConsumerToReadData_ms);
        jSpinConsumerCons.setValue((int) timeConsumerToConsumeData_ms);
        jSpinnerBuffer.setValue((int) SIZE);
        jSpinnerNP.setValue((int) NP);
        jSpinnerNC.setValue((int) NC);
        going = new Boolean(false);
        executor = Executors.newFixedThreadPool(8);
        reset();

    }
    
    public void updateProducerData(){
        if(Producer.totalItemsProduced==0)return;
        long nsTOmsPerItem = Producer.totalItemsProduced*1000000*NP;
        long averageTimePerProduct_ms = Producer.totalTimeInProducers_ns/nsTOmsPerItem;
        long averageTimeWaiting_ms = Producer.totalTimeWaiting_ns/nsTOmsPerItem;
        long averageTimeInMonitor_ms = Producer.totalTimeInMonitor_ns/nsTOmsPerItem;
        
        this.textProdData.setText(Producer.totalItemsProduced+"");
        this.textProdAvgms.setText(averageTimePerProduct_ms+"");
        this.textProdWaitms.setText(averageTimeWaiting_ms+"");
        this.textProdInMinms.setText(averageTimeInMonitor_ms+"");
    }
    
    public void updateConsumerData(){
        if(Consumer.totalItemsConsumed==0)return;
        long nsTOmsPerItem = Consumer.totalItemsConsumed*1000000*NC;
        long averageTimePerConsume_ms = Consumer.totalTimeInConsumers_ns/nsTOmsPerItem;
        long averageTimeWaiting_ms = Consumer.totalTimeWaiting_ns/nsTOmsPerItem;
        long averageTimeMonitor_ms = Consumer.totalTimeInMonitor_ns/nsTOmsPerItem;
        
        this.textConsData.setText(Consumer.totalItemsConsumed+"");
        this.textConsAvgms.setText(averageTimePerConsume_ms+"");
        this.textConsWaitms.setText(averageTimeWaiting_ms+"");
        this.textConsInMinms.setText(averageTimeMonitor_ms+"");
    }
    
    public void stopThreads(){
        for (int i = 0; i < NP; i++) {
            producers[i].cease();
        }
        for (int i = 0; i < NC; i++) {
            consumers[i].cease();
        }
        //executor.shutdownNow();
    }

    private void reset() {

        SIZE = (int) jSpinnerBuffer.getValue();
        b = new JLabel[SIZE];
        data = new Data[SIZE];
        
        if (executor != null) {
            executor.shutdownNow();
        }
        executor = Executors.newFixedThreadPool(8);
        Producer.reset();
        Consumer.reset();
        Data.reset();
        updateProducerData();
        updateConsumerData();

        NP = (int) jSpinnerNP.getValue();
        NC = (int) jSpinnerNC.getValue();
        timeProducerToProduceData_ms = (int) jSpinProdProd.getValue();
        timeProducerToWriteData_ms = (int) jSpinProdWrit.getValue();
        timeConsumerToReadData_ms = (int) jSpinConsumerRead.getValue();
        timeConsumerToConsumeData_ms = (int) jSpinConsumerCons.getValue();

        jPanelBuffer.removeAll();
        jPanelBuffer.repaint();
        if(SIZE<=40)jPanelBuffer.setLayout(new GridLayout(1, SIZE));
        else jPanelBuffer.setLayout(new GridLayout(SIZE/40, 40));
        for (int i = 0; i < SIZE; i++) {
            b[i] = new JLabel(".");
            data[i] = new Data(".", Color.gray);
            b[i].setOpaque(true);
            b[i].setBackground(Color.gray);
            b[i].setBorder(new LineBorder(Color.BLACK));
            jPanelBuffer.add(b[i]);
        }

        switch (solution) {
            case MONITORV1: { monitor = new PCmonitorV1(SIZE, data); break;}
            //case MONITORV2: { monitor = new PCmonitorV2(SIZE, data); break;}
            //case MONITORV3: { monitor = new PCmonitorV3(SIZE, data); break;}
            //case MONITORV4: { monitor = new PCmonitorV4(SIZE, data); break;}
            default: monitor = new PCmonitorV1(SIZE, data);
        }
        producers = new Producer[NP];
        consumers = new Consumer[NC];

        //going = new Boolean(true);
        for (int i = 0; i < NP; i++) {
            producers[i] = new Producer(monitor, i + "", going);
            executor.submit(producers[i]);
            System.out.println("P" + i + " started");
        }
        for (int i = 0; i < NC; i++) {
            consumers[i] = new Consumer(monitor, i + "", going);
            executor.submit(consumers[i]);
            System.out.println("C" + i + " started");
        }
        
        
        /* use a javax.swing.Timer to set a task on EDT that updates the GUI */
        timer2 = new javax.swing.Timer(0,
            new ActionListener() {
                @Override public void actionPerformed(ActionEvent e){
                for (int i = 0; i < SIZE; i++) {
                    b[i].setText(data[i].value);
                    b[i].setBackground(data[i].color);
                    updateProducerData();
                    updateConsumerData();
                }
            }
            });
        timer2.setDelay((int)updateTime_ms);
        timer2.start();       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanelBuffer = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        textProdData = new javax.swing.JTextField();
        textProdAvgms = new javax.swing.JTextField();
        textProdWaitms = new javax.swing.JTextField();
        textProdInMinms = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        textConsData = new javax.swing.JTextField();
        textConsAvgms = new javax.swing.JTextField();
        textConsWaitms = new javax.swing.JTextField();
        textConsInMinms = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSpinnerBuffer = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jSpinProdProd = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jSpinProdWrit = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinConsumerRead = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinConsumerCons = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerNP = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerNC = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Producer-Consumer Problem Green = Unread data, Blue = data has been read");

        jPanelBuffer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelBuffer.setLayout(new java.awt.GridLayout(3, 1));

        jPanel7.setLayout(new java.awt.GridLayout(3, 1));

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new java.awt.GridLayout(1, 9));
        jPanel4.add(jLabel10);

        jLabel11.setText("Data Processed");
        jPanel4.add(jLabel11);

        jLabel12.setText("Average Time (ms) Running");
        jPanel4.add(jLabel12);

        jLabel13.setText("Average Time (ms) Waiting");
        jPanel4.add(jLabel13);

        jLabel14.setText("Average Time (ms) in Monitor");
        jPanel4.add(jLabel14);

        jPanel7.add(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new java.awt.GridLayout(1, 9));

        jLabel15.setText("Producer Data:");
        jPanel5.add(jLabel15);
        jPanel5.add(textProdData);
        jPanel5.add(textProdAvgms);
        jPanel5.add(textProdWaitms);
        jPanel5.add(textProdInMinms);

        jPanel7.add(jPanel5);

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new java.awt.GridLayout(1, 9));

        jLabel16.setText("Consumer Data:");
        jPanel6.add(jLabel16);
        jPanel6.add(textConsData);
        jPanel6.add(textConsAvgms);
        jPanel6.add(textConsWaitms);
        jPanel6.add(textConsInMinms);

        jPanel7.add(jPanel6);

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.GridLayout(5, 1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Solution Type");
        jPanel2.add(jLabel1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Monitor using implicit mutex and notifyAll");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton2);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButton3.setText("Monitor using implicit mutex and notify");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton3);

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButton4.setText("Monitor using Two mutex Objects");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton4);

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButton5.setText("Monitor using Lock and Condition Objects");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton5);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new java.awt.GridLayout(8, 2));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Problem Settings");
        jPanel3.add(jLabel5);
        jPanel3.add(jLabel17);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("Buffer Size");
        jPanel3.add(jLabel9);

        jSpinnerBuffer.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jSpinnerBuffer.setModel(new javax.swing.SpinnerNumberModel(50, 1, 120, 10));
        jSpinnerBuffer.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerBufferStateChanged(evt);
            }
        });
        jPanel3.add(jSpinnerBuffer);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Producer Time to Produce (ms)");
        jPanel3.add(jLabel2);

        jSpinProdProd.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jSpinProdProd.setModel(new javax.swing.SpinnerNumberModel(100, 0, 1000, 10));
        jSpinProdProd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinProdProdStateChanged(evt);
            }
        });
        jPanel3.add(jSpinProdProd);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Producer Time to Write (ms)");
        jPanel3.add(jLabel7);

        jSpinProdWrit.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jSpinProdWrit.setModel(new javax.swing.SpinnerNumberModel(100, 0, 1000, 10));
        jSpinProdWrit.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinProdWritStateChanged(evt);
            }
        });
        jPanel3.add(jSpinProdWrit);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Consumer Time to Read (ms)");
        jPanel3.add(jLabel3);

        jSpinConsumerRead.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jSpinConsumerRead.setModel(new javax.swing.SpinnerNumberModel(100, 0, 1000, 10));
        jSpinConsumerRead.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinConsumerReadStateChanged(evt);
            }
        });
        jPanel3.add(jSpinConsumerRead);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Consumer Time to Consume (ms)");
        jPanel3.add(jLabel8);

        jSpinConsumerCons.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jSpinConsumerCons.setModel(new javax.swing.SpinnerNumberModel(100, 0, 1000, 10));
        jSpinConsumerCons.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinConsumerConsStateChanged(evt);
            }
        });
        jPanel3.add(jSpinConsumerCons);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Number of Producers");
        jPanel3.add(jLabel4);

        jSpinnerNP.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jSpinnerNP.setModel(new javax.swing.SpinnerNumberModel(1, 1, 16, 1));
        jSpinnerNP.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerNPStateChanged(evt);
            }
        });
        jPanel3.add(jSpinnerNP);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Number of Consumers");
        jPanel3.add(jLabel6);

        jSpinnerNC.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jSpinnerNC.setModel(new javax.swing.SpinnerNumberModel(1, 1, 16, 1));
        jSpinnerNC.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerNCStateChanged(evt);
            }
        });
        jPanel3.add(jSpinnerNC);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 204, 0));
        jButton1.setText("Restart");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanelBuffer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(147, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerNPStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerNPStateChanged
        reset();
    }//GEN-LAST:event_jSpinnerNPStateChanged

    private void jSpinnerNCStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerNCStateChanged
        reset();
    }//GEN-LAST:event_jSpinnerNCStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        reset();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSpinnerBufferStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerBufferStateChanged
        reset();
    }//GEN-LAST:event_jSpinnerBufferStateChanged

    private void jSpinProdProdStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinProdProdStateChanged
        reset();//timeProducerToProduceData = (int) jSpinProdProd.getValue();
    }//GEN-LAST:event_jSpinProdProdStateChanged

    private void jSpinProdWritStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinProdWritStateChanged
        reset();//timeProducerToWriteData = (int) jSpinProdWrit.getValue();
    }//GEN-LAST:event_jSpinProdWritStateChanged

    private void jSpinConsumerReadStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinConsumerReadStateChanged
        reset();//timeConsumerToReadData = (int) jSpinConsumerRead.getValue();
    }//GEN-LAST:event_jSpinConsumerReadStateChanged

    private void jSpinConsumerConsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinConsumerConsStateChanged
        reset();//timeConsumerToConsumeData = (int) jSpinConsumerCons.getValue();
    }//GEN-LAST:event_jSpinConsumerConsStateChanged

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        solution = SolutionType.MONITORV4;
        reset();//
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        solution = SolutionType.MONITORV1;
        reset();//
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        solution = SolutionType.MONITORV2;
        reset();//
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        solution = SolutionType.MONITORV3;
        reset();//
    }//GEN-LAST:event_jRadioButton4ActionPerformed

  
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }
    private int count;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelBuffer;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinConsumerCons;
    private javax.swing.JSpinner jSpinConsumerRead;
    private javax.swing.JSpinner jSpinProdProd;
    private javax.swing.JSpinner jSpinProdWrit;
    private javax.swing.JSpinner jSpinnerBuffer;
    private javax.swing.JSpinner jSpinnerNC;
    private javax.swing.JSpinner jSpinnerNP;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField textConsAvgms;
    private javax.swing.JTextField textConsData;
    private javax.swing.JTextField textConsInMinms;
    private javax.swing.JTextField textConsWaitms;
    private javax.swing.JTextField textProdAvgms;
    private javax.swing.JTextField textProdData;
    private javax.swing.JTextField textProdInMinms;
    private javax.swing.JTextField textProdWaitms;
    // End of variables declaration//GEN-END:variables


}
