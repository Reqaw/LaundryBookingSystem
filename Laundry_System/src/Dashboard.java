/**Developed by HANNAH,THIRUCHELVAN,QHAREEF AND MUHAIMIN    */

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javazoom.jl.player.Player;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dashboard extends javax.swing.JFrame implements Runnable{
    int Total;
    String sound,title;
    FileInputStream fileInputStream;
    BufferedInputStream bufferedInputStream;
    Player player;
    long all;
    String hours, hh, mm,hourAlarm, minuteAlarm;
    private JLabel[] machines;
    private int machineno;
    private String type;
    
    public Dashboard() {
        initComponents();
        setResizable(false);
        LaundryMainBackPanel.setVisible(false);
        MenuMainBackPanel.setVisible(true);
        AlarmMainBackPanel.setVisible(false);
        ReservationMainBackPanel.setVisible(false);
        FeedbackMainBackPanel.setVisible(false);
        MenuBackPanel.setBackground(new Color(128, 0, 0));
        Menutxt.setForeground(Color.white);
        TotalTF.setEditable(false);
        PriceTF.setEditable(false);
        RecieptPrint.setEditable(false);
        ReservePriceTF.setEditable(false);
        customerTF.setEditable(false);
        Thread t = new Thread(this);
        t.start();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
        Date date = c.getTime();
        hh = simpleDateFormat1.format(date);
        mm = simpleDateFormat2.format(date);
        jComboBox1.setSelectedItem(hh);
        jComboBox2.setSelectedItem(mm);
        DisplayMachineLabels();
        this.setLocationRelativeTo(null);
        String user = LOGIN.username;
        customerTF.setText(user);
    }
    
    public void reset(){
        typechoice.setSelectedItem("Choose");
        PriceTF.setText("");
        WeightTF.setText("");
        TotalTF.setText("");
        PaymentTF.setText("");
    }
    
    public void ChooseRingtone(){
        JFileChooser JFC = new JFileChooser();
        int SoundI = JFC.showOpenDialog(null);
        if (SoundI == JFC.APPROVE_OPTION){
            File alarmMusic = JFC.getSelectedFile();
            sound = alarmMusic.getAbsolutePath();
            title = JFC.getSelectedFile().getName();
            System.out.println(title);
        } else if (SoundI == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog (null, "Set a ringtone");
        }
    }
    
    public void StartAlarm(){
        try{
            fileInputStream = new FileInputStream(sound);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            player = new Player(bufferedInputStream);
            all = fileInputStream.available();
            new Thread(){
                public void run(){
                    try {
                        player.play();
                    }catch (Exception e){
                        
                    }
                }
            }.start();
        }catch (Exception e){
            
        }
    }
    
    public void StopAlarm(){
        if (player !=null){
        player.close();
        }
    }
    
    public void AlarmTime(final int hour, final int minute){
        Thread t = new Thread(){
          public void run (){
              int time = 0;
              while (time == 0){
                  Calendar c = Calendar.getInstance();
                  int h = c.get(Calendar.HOUR);
                  int m = c.get(Calendar.MINUTE);
                  if (hour == h && minute == m) {
                      StartAlarm();
                      JOptionPane.showMessageDialog(null,"ALARM");
                      JOptionPane.showConfirmDialog(null,"STOP ALARM","",JOptionPane.CLOSED_OPTION);
                          StopAlarm();
                          break;
                 }
              }
          }  
        }; t.start();
    }
    
    private void DisplayMachineLabels() {
        machines = new JLabel[] {
            WM1, WM2, WM3, WM4, WM5, WM6, WM7, WM8, WM9, WM10, WM11, WM12
        };

        for (int i = 0; i < machines.length; i++) {
            final int machineNumber = i + 1;
            machines[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    lblclear();
                    machineno = machineNumber;
                    machines[machineno - 1].setForeground(Color.red);
                    JOptionPane.showMessageDialog(null, "You have selected machine number : " + machineno);
                }
            });
        }
    }
    
    private void Bill(){
        String customer = customerTF.getText();
        int machine1 = machineno;
        String price = ReservePriceTF.getText();
        
        SimpleDateFormat date_form = new SimpleDateFormat("yyyy-MM-dd");
        String date = date_form.format(jCalendar1.getDate());
        
        RecieptPrint.setText("");
        RecieptPrint.setText(RecieptPrint.getText() + "******************************\n");
        RecieptPrint.setText(RecieptPrint.getText() + "**********Reciept************\n");
        RecieptPrint.setText(RecieptPrint.getText() + "******************************\n");
        RecieptPrint.setText(RecieptPrint.getText() + "Customer Name : " + customer + "\n");
        RecieptPrint.setText(RecieptPrint.getText() + "Machine : " + machine1 + "\n");
        RecieptPrint.setText(RecieptPrint.getText() + "Type : " + type + "\n");
        RecieptPrint.setText(RecieptPrint.getText() + "Price : " + price + "\n");
        RecieptPrint.setText(RecieptPrint.getText() + "Date : " + date + "\n");
        RecieptPrint.setText(RecieptPrint.getText() + "\n");
        RecieptPrint.setText(RecieptPrint.getText() + "******************************\n");
        RecieptPrint.setText(RecieptPrint.getText() + "** Thank you for using our system **\n");
    }
    
    public void lblclear(){
        WM1.setForeground(Color.white);
        WM2.setForeground(Color.white);
        WM3.setForeground(Color.white);
        WM4.setForeground(Color.white);
        WM5.setForeground(Color.white);
        WM6.setForeground(Color.white);
        WM7.setForeground(Color.white);
        WM8.setForeground(Color.white);
        WM9.setForeground(Color.white);
        WM10.setForeground(Color.white);
        WM11.setForeground(Color.white);
        WM12.setForeground(Color.white);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LeftBackPanel = new javax.swing.JPanel();
        MenuBackPanel = new javax.swing.JPanel();
        Menutxt = new javax.swing.JLabel();
        LaundryBackPanel = new javax.swing.JPanel();
        Laundrytxt = new javax.swing.JLabel();
        AlarmBackPanel = new javax.swing.JPanel();
        Alarmtxt = new javax.swing.JLabel();
        ReservationBackPanel = new javax.swing.JPanel();
        Reservationtxt = new javax.swing.JLabel();
        FeedbackBackPanel = new javax.swing.JPanel();
        Feedbacktxt = new javax.swing.JLabel();
        LogoutPanel = new javax.swing.JPanel();
        Logouttxt = new javax.swing.JLabel();
        WelcomeBP = new javax.swing.JPanel();
        Welcometxt1 = new javax.swing.JLabel();
        Welcometxt2 = new javax.swing.JLabel();
        MenuMainBackPanel = new javax.swing.JPanel();
        SloganBackPanel = new javax.swing.JPanel();
        Slogantxt = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        WorkinghrsBackPanel = new javax.swing.JPanel();
        Workindaytxt = new javax.swing.JLabel();
        Workinghrstxt = new javax.swing.JLabel();
        timeworktxt = new javax.swing.JLabel();
        PricingBackpanel = new javax.swing.JPanel();
        typepricetxt = new javax.swing.JLabel();
        Pricingtxt = new javax.swing.JLabel();
        Pricestxt = new javax.swing.JLabel();
        Emergencybackpanel = new javax.swing.JPanel();
        emergencycontacttxt = new javax.swing.JLabel();
        Contact2txt = new javax.swing.JLabel();
        contact1txt = new javax.swing.JLabel();
        emergencymsgtxt = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        LaundryMainBackPanel = new javax.swing.JPanel();
        Laundrysystemtxt = new javax.swing.JLabel();
        PricingBackpanel1 = new javax.swing.JPanel();
        typepricetxt1 = new javax.swing.JLabel();
        Pricingtxt1 = new javax.swing.JLabel();
        Pricestxt1 = new javax.swing.JLabel();
        Paymenttxt = new javax.swing.JLabel();
        Typetxt = new javax.swing.JLabel();
        Pricetxt = new javax.swing.JLabel();
        Totalpricetxt = new javax.swing.JLabel();
        Weighttxt = new javax.swing.JLabel();
        typechoice = new javax.swing.JComboBox<>();
        PaymentTF = new javax.swing.JTextField();
        PriceTF = new javax.swing.JTextField();
        TotalTF = new javax.swing.JTextField();
        WeightTF = new javax.swing.JTextField();
        Cancelbtn = new javax.swing.JButton();
        Paybtn = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        AlarmMainBackPanel = new javax.swing.JPanel();
        AlarmClocktxt = new javax.swing.JLabel();
        Timertxt = new javax.swing.JLabel();
        timertext = new javax.swing.JLabel();
        hourtxt = new javax.swing.JLabel();
        minutetxt = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        Ringbtn = new javax.swing.JButton();
        SeparateTimetxt = new javax.swing.JLabel();
        SeparateTimetxt2 = new javax.swing.JLabel();
        Setalrmbtn = new javax.swing.JButton();
        CurrentRingtonetxt = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        ReservationMainBackPanel = new javax.swing.JPanel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        LaundryReservationtxt = new javax.swing.JLabel();
        pricereservetxt = new javax.swing.JLabel();
        Washingmachinetxt = new javax.swing.JLabel();
        Customertxt = new javax.swing.JLabel();
        customerTF = new javax.swing.JTextField();
        ReservePriceTF = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        RecieptPrint = new javax.swing.JTextArea();
        BackpanelWMS = new javax.swing.JPanel();
        WM12 = new javax.swing.JLabel();
        WM1 = new javax.swing.JLabel();
        WM2 = new javax.swing.JLabel();
        WM3 = new javax.swing.JLabel();
        WM4 = new javax.swing.JLabel();
        WM5 = new javax.swing.JLabel();
        WM6 = new javax.swing.JLabel();
        WM7 = new javax.swing.JLabel();
        WM8 = new javax.swing.JLabel();
        WM9 = new javax.swing.JLabel();
        WM10 = new javax.swing.JLabel();
        WM11 = new javax.swing.JLabel();
        bookbtn = new javax.swing.JButton();
        cancelbookbtn = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        FeedbackMainBackPanel = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        Feedbackmaintxt = new javax.swing.JLabel();
        feebacktxt3 = new javax.swing.JLabel();
        feedbacktxt1 = new javax.swing.JLabel();
        feedbacktxt2 = new javax.swing.JLabel();
        qrcode = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard");
        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(986, 520));
        getContentPane().setLayout(null);

        LeftBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        LeftBackPanel.setLayout(null);

        MenuBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        MenuBackPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuBackPanelMouseClicked(evt);
            }
        });
        MenuBackPanel.setLayout(null);

        Menutxt.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Menutxt.setText("Menu");
        MenuBackPanel.add(Menutxt);
        Menutxt.setBounds(70, 10, 60, 20);

        LeftBackPanel.add(MenuBackPanel);
        MenuBackPanel.setBounds(0, 80, 200, 50);

        LaundryBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        LaundryBackPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LaundryBackPanelMouseClicked(evt);
            }
        });
        LaundryBackPanel.setLayout(null);

        Laundrytxt.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Laundrytxt.setText("Laundry");
        LaundryBackPanel.add(Laundrytxt);
        Laundrytxt.setBounds(60, 10, 80, 24);

        LeftBackPanel.add(LaundryBackPanel);
        LaundryBackPanel.setBounds(0, 150, 200, 50);

        AlarmBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        AlarmBackPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AlarmBackPanelMouseClicked(evt);
            }
        });
        AlarmBackPanel.setLayout(null);

        Alarmtxt.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Alarmtxt.setText("Alarm");
        Alarmtxt.setToolTipText("");
        AlarmBackPanel.add(Alarmtxt);
        Alarmtxt.setBounds(70, 10, 60, 20);

        LeftBackPanel.add(AlarmBackPanel);
        AlarmBackPanel.setBounds(0, 220, 200, 50);

        ReservationBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        ReservationBackPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReservationBackPanelMouseClicked(evt);
            }
        });
        ReservationBackPanel.setLayout(null);

        Reservationtxt.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Reservationtxt.setText("Reservation");
        ReservationBackPanel.add(Reservationtxt);
        Reservationtxt.setBounds(40, 10, 120, 24);

        LeftBackPanel.add(ReservationBackPanel);
        ReservationBackPanel.setBounds(0, 290, 200, 50);

        FeedbackBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        FeedbackBackPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FeedbackBackPanelMouseClicked(evt);
            }
        });
        FeedbackBackPanel.setLayout(null);

        Feedbacktxt.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Feedbacktxt.setText("Feedback");
        FeedbackBackPanel.add(Feedbacktxt);
        Feedbacktxt.setBounds(50, 10, 100, 24);

        LeftBackPanel.add(FeedbackBackPanel);
        FeedbackBackPanel.setBounds(0, 370, 200, 50);

        LogoutPanel.setBackground(new java.awt.Color(255, 255, 255));
        LogoutPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LogoutPanelMouseClicked(evt);
            }
        });
        LogoutPanel.setLayout(null);

        Logouttxt.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Logouttxt.setText("Logout");
        LogoutPanel.add(Logouttxt);
        Logouttxt.setBounds(60, 10, 70, 24);

        LeftBackPanel.add(LogoutPanel);
        LogoutPanel.setBounds(0, 440, 200, 40);

        WelcomeBP.setBackground(new java.awt.Color(128, 0, 0));
        WelcomeBP.setForeground(new java.awt.Color(128, 0, 0));

        Welcometxt1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        Welcometxt1.setForeground(new java.awt.Color(255, 255, 255));
        Welcometxt1.setText("Welcome To Our");
        WelcomeBP.add(Welcometxt1);

        Welcometxt2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        Welcometxt2.setForeground(new java.awt.Color(255, 255, 255));
        Welcometxt2.setText("Smart Laundry System");
        WelcomeBP.add(Welcometxt2);

        LeftBackPanel.add(WelcomeBP);
        WelcomeBP.setBounds(0, 0, 200, 60);

        getContentPane().add(LeftBackPanel);
        LeftBackPanel.setBounds(0, 0, 200, 500);

        MenuMainBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        MenuMainBackPanel.setToolTipText("");
        MenuMainBackPanel.setLayout(null);

        SloganBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        SloganBackPanel.setLayout(null);

        Slogantxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Slogantxt.setText("Laundry Machines always busy?");
        SloganBackPanel.add(Slogantxt);
        Slogantxt.setBounds(230, 10, 290, 20);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Book your slot using our system for easier laundry access!");
        SloganBackPanel.add(jLabel1);
        jLabel1.setBounds(130, 40, 530, 20);

        MenuMainBackPanel.add(SloganBackPanel);
        SloganBackPanel.setBounds(0, 0, 780, 60);

        WorkinghrsBackPanel.setLayout(null);

        Workindaytxt.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        Workindaytxt.setText("Saturday - Thursday");
        WorkinghrsBackPanel.add(Workindaytxt);
        Workindaytxt.setBounds(90, 30, 110, 13);

        Workinghrstxt.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        Workinghrstxt.setText("Working Hours : ");
        WorkinghrsBackPanel.add(Workinghrstxt);
        Workinghrstxt.setBounds(10, 10, 220, 17);

        timeworktxt.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        timeworktxt.setText("8AM - 10PM");
        WorkinghrsBackPanel.add(timeworktxt);
        timeworktxt.setBounds(110, 50, 70, 13);

        MenuMainBackPanel.add(WorkinghrsBackPanel);
        WorkinghrsBackPanel.setBounds(440, 80, 280, 70);

        PricingBackpanel.setLayout(null);

        typepricetxt.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        typepricetxt.setText("Type -     |     Washing     |     Dryer     |     Both     |");
        PricingBackpanel.add(typepricetxt);
        typepricetxt.setBounds(10, 40, 260, 13);

        Pricingtxt.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        Pricingtxt.setText("Pricing -  |    RM 4.00       |   RM 7.00   |  RM 10.00 |");
        PricingBackpanel.add(Pricingtxt);
        Pricingtxt.setBounds(10, 70, 260, 13);

        Pricestxt.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        Pricestxt.setText("Prices : ");
        PricingBackpanel.add(Pricestxt);
        Pricestxt.setBounds(10, 10, 70, 17);

        MenuMainBackPanel.add(PricingBackpanel);
        PricingBackpanel.setBounds(440, 210, 280, 100);

        Emergencybackpanel.setLayout(null);

        emergencycontacttxt.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        emergencycontacttxt.setText("Emergency Contact :");
        Emergencybackpanel.add(emergencycontacttxt);
        emergencycontacttxt.setBounds(10, 10, 160, 17);

        Contact2txt.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        Contact2txt.setText("Thiru - 01482572063 ");
        Emergencybackpanel.add(Contact2txt);
        Contact2txt.setBounds(20, 70, 120, 14);

        contact1txt.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        contact1txt.setText("Hafiz - 01938553751");
        Emergencybackpanel.add(contact1txt);
        contact1txt.setBounds(20, 40, 110, 14);

        emergencymsgtxt.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        emergencymsgtxt.setText("Contact us for any concerns or enquiries regarding our laundry system");
        Emergencybackpanel.add(emergencymsgtxt);
        emergencymsgtxt.setBounds(180, 90, 510, 17);

        MenuMainBackPanel.add(Emergencybackpanel);
        Emergencybackpanel.setBounds(60, 360, 690, 110);

        jSeparator12.setForeground(new java.awt.Color(128, 0, 0));
        MenuMainBackPanel.add(jSeparator12);
        jSeparator12.setBounds(60, 60, 660, 2);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Laundryimg.png"))); // NOI18N
        MenuMainBackPanel.add(jLabel2);
        jLabel2.setBounds(80, 100, 250, 220);

        getContentPane().add(MenuMainBackPanel);
        MenuMainBackPanel.setBounds(210, 0, 780, 500);

        LaundryMainBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        LaundryMainBackPanel.setLayout(null);

        Laundrysystemtxt.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        Laundrysystemtxt.setText("Smart Laundry System");
        LaundryMainBackPanel.add(Laundrysystemtxt);
        Laundrysystemtxt.setBounds(260, 30, 260, 28);

        PricingBackpanel1.setLayout(null);

        typepricetxt1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        typepricetxt1.setText("Type -     |     Washing     |     Dryer     |     Both     |");
        PricingBackpanel1.add(typepricetxt1);
        typepricetxt1.setBounds(10, 40, 260, 13);

        Pricingtxt1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        Pricingtxt1.setText("Pricing -  |    RM 4.00       |   RM 7.00   |  RM 10.00 |");
        PricingBackpanel1.add(Pricingtxt1);
        Pricingtxt1.setBounds(10, 70, 270, 13);

        Pricestxt1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        Pricestxt1.setText("Prices : ");
        PricingBackpanel1.add(Pricestxt1);
        Pricestxt1.setBounds(10, 10, 70, 17);

        LaundryMainBackPanel.add(PricingBackpanel1);
        PricingBackpanel1.setBounds(480, 80, 280, 100);

        Paymenttxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Paymenttxt.setText("Payment :");
        LaundryMainBackPanel.add(Paymenttxt);
        Paymenttxt.setBounds(60, 340, 90, 22);

        Typetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Typetxt.setText("Type : ");
        LaundryMainBackPanel.add(Typetxt);
        Typetxt.setBounds(60, 110, 60, 22);

        Pricetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Pricetxt.setText("Price(RM) : ");
        LaundryMainBackPanel.add(Pricetxt);
        Pricetxt.setBounds(60, 160, 110, 22);

        Totalpricetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Totalpricetxt.setText("Total Price(RM) : ");
        LaundryMainBackPanel.add(Totalpricetxt);
        Totalpricetxt.setBounds(60, 280, 160, 22);

        Weighttxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Weighttxt.setText("Weight(KG) : ");
        LaundryMainBackPanel.add(Weighttxt);
        Weighttxt.setBounds(60, 220, 120, 22);

        typechoice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose", "Washing", "Dryer", "Wash & Dry" }));
        typechoice.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        typechoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typechoiceActionPerformed(evt);
            }
        });
        LaundryMainBackPanel.add(typechoice);
        typechoice.setBounds(280, 110, 110, 24);
        LaundryMainBackPanel.add(PaymentTF);
        PaymentTF.setBounds(280, 330, 140, 30);

        PriceTF.setBackground(new java.awt.Color(255, 255, 102));
        LaundryMainBackPanel.add(PriceTF);
        PriceTF.setBounds(280, 150, 140, 30);

        TotalTF.setBackground(new java.awt.Color(255, 255, 102));
        LaundryMainBackPanel.add(TotalTF);
        TotalTF.setBounds(280, 270, 140, 30);

        WeightTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                WeightTFKeyReleased(evt);
            }
        });
        LaundryMainBackPanel.add(WeightTF);
        WeightTF.setBounds(280, 210, 140, 30);

        Cancelbtn.setBackground(new java.awt.Color(128, 0, 0));
        Cancelbtn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Cancelbtn.setForeground(new java.awt.Color(255, 255, 255));
        Cancelbtn.setText("Cancel");
        Cancelbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelbtnMouseClicked(evt);
            }
        });
        LaundryMainBackPanel.add(Cancelbtn);
        Cancelbtn.setBounds(340, 410, 130, 50);

        Paybtn.setBackground(new java.awt.Color(128, 0, 0));
        Paybtn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Paybtn.setForeground(new java.awt.Color(255, 255, 255));
        Paybtn.setText("Pay");
        Paybtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PaybtnMouseClicked(evt);
            }
        });
        LaundryMainBackPanel.add(Paybtn);
        Paybtn.setBounds(80, 410, 130, 50);

        jSeparator11.setForeground(new java.awt.Color(128, 0, 0));
        LaundryMainBackPanel.add(jSeparator11);
        jSeparator11.setBounds(60, 60, 660, 2);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/LaundryIcon.png"))); // NOI18N
        LaundryMainBackPanel.add(jLabel3);
        jLabel3.setBounds(490, 200, 260, 200);

        getContentPane().add(LaundryMainBackPanel);
        LaundryMainBackPanel.setBounds(210, 0, 780, 490);

        AlarmMainBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        AlarmMainBackPanel.setLayout(null);

        AlarmClocktxt.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        AlarmClocktxt.setText("Alarm Clock");
        AlarmMainBackPanel.add(AlarmClocktxt);
        AlarmClocktxt.setBounds(320, 40, 140, 20);

        Timertxt.setBackground(new java.awt.Color(255, 255, 255));
        Timertxt.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        Timertxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Timertxt.setText("jLabel3");
        Timertxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 0, 0), 6));
        AlarmMainBackPanel.add(Timertxt);
        Timertxt.setBounds(110, 100, 550, 70);

        timertext.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        timertext.setText("Timer");
        AlarmMainBackPanel.add(timertext);
        timertext.setBounds(190, 230, 60, 24);

        hourtxt.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        hourtxt.setText("Hour");
        AlarmMainBackPanel.add(hourtxt);
        hourtxt.setBounds(420, 200, 50, 24);

        minutetxt.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        minutetxt.setText("Minute");
        AlarmMainBackPanel.add(minutetxt);
        minutetxt.setBounds(290, 200, 70, 24);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        AlarmMainBackPanel.add(jComboBox1);
        jComboBox1.setBounds(290, 230, 70, 24);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        AlarmMainBackPanel.add(jComboBox2);
        jComboBox2.setBounds(420, 230, 70, 24);

        Ringbtn.setBackground(new java.awt.Color(128, 0, 0));
        Ringbtn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Ringbtn.setForeground(new java.awt.Color(255, 255, 255));
        Ringbtn.setText("Ringtone");
        Ringbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RingbtnMouseClicked(evt);
            }
        });
        AlarmMainBackPanel.add(Ringbtn);
        Ringbtn.setBounds(180, 360, 140, 40);

        SeparateTimetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        SeparateTimetxt.setText(":");
        AlarmMainBackPanel.add(SeparateTimetxt);
        SeparateTimetxt.setBounds(390, 200, 10, 22);

        SeparateTimetxt2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        SeparateTimetxt2.setText(":");
        AlarmMainBackPanel.add(SeparateTimetxt2);
        SeparateTimetxt2.setBounds(390, 230, 10, 22);

        Setalrmbtn.setBackground(new java.awt.Color(128, 0, 0));
        Setalrmbtn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Setalrmbtn.setForeground(new java.awt.Color(255, 255, 255));
        Setalrmbtn.setText("Set Alarm");
        Setalrmbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SetalrmbtnMouseClicked(evt);
            }
        });
        AlarmMainBackPanel.add(Setalrmbtn);
        Setalrmbtn.setBounds(430, 360, 140, 40);

        CurrentRingtonetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        CurrentRingtonetxt.setText("Ringtone : None selected");
        AlarmMainBackPanel.add(CurrentRingtonetxt);
        CurrentRingtonetxt.setBounds(180, 290, 460, 30);

        jSeparator9.setForeground(new java.awt.Color(128, 0, 0));
        AlarmMainBackPanel.add(jSeparator9);
        jSeparator9.setBounds(60, 60, 660, 2);

        getContentPane().add(AlarmMainBackPanel);
        AlarmMainBackPanel.setBounds(210, 0, 780, 490);

        ReservationMainBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        ReservationMainBackPanel.setLayout(null);

        jCalendar1.setBackground(new java.awt.Color(255, 255, 255));
        jCalendar1.setForeground(new java.awt.Color(0, 0, 0));
        ReservationMainBackPanel.add(jCalendar1);
        jCalendar1.setBounds(350, 80, 390, 190);

        LaundryReservationtxt.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        LaundryReservationtxt.setText("Laundry Reservation");
        ReservationMainBackPanel.add(LaundryReservationtxt);
        LaundryReservationtxt.setBounds(250, 30, 240, 30);

        pricereservetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        pricereservetxt.setText("Price :");
        ReservationMainBackPanel.add(pricereservetxt);
        pricereservetxt.setBounds(20, 350, 60, 20);

        Washingmachinetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Washingmachinetxt.setText("Washing Machine :");
        ReservationMainBackPanel.add(Washingmachinetxt);
        Washingmachinetxt.setBounds(20, 190, 180, 20);

        Customertxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Customertxt.setText("Customer :");
        ReservationMainBackPanel.add(Customertxt);
        Customertxt.setBounds(20, 90, 100, 20);

        customerTF.setBackground(new java.awt.Color(255, 255, 102));
        customerTF.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ReservationMainBackPanel.add(customerTF);
        customerTF.setBounds(20, 120, 280, 20);

        ReservePriceTF.setBackground(new java.awt.Color(255, 255, 102));
        ReservePriceTF.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ReservationMainBackPanel.add(ReservePriceTF);
        ReservePriceTF.setBounds(20, 380, 280, 20);

        RecieptPrint.setBackground(new java.awt.Color(255, 255, 102));
        RecieptPrint.setColumns(20);
        RecieptPrint.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        RecieptPrint.setRows(5);
        jScrollPane1.setViewportView(RecieptPrint);

        ReservationMainBackPanel.add(jScrollPane1);
        jScrollPane1.setBounds(350, 290, 410, 180);

        BackpanelWMS.setBackground(new java.awt.Color(128, 0, 0));
        BackpanelWMS.setLayout(null);

        WM12.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM12.setForeground(new java.awt.Color(255, 255, 255));
        WM12.setText("12");
        BackpanelWMS.add(WM12);
        WM12.setBounds(180, 50, 30, 20);

        WM1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM1.setForeground(new java.awt.Color(255, 255, 255));
        WM1.setText("1");
        BackpanelWMS.add(WM1);
        WM1.setBounds(10, 10, 20, 20);

        WM2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM2.setForeground(new java.awt.Color(255, 255, 255));
        WM2.setText("2");
        BackpanelWMS.add(WM2);
        WM2.setBounds(40, 10, 20, 20);

        WM3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM3.setForeground(new java.awt.Color(255, 255, 255));
        WM3.setText("3");
        BackpanelWMS.add(WM3);
        WM3.setBounds(70, 10, 20, 20);

        WM4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM4.setForeground(new java.awt.Color(255, 255, 255));
        WM4.setText("4");
        BackpanelWMS.add(WM4);
        WM4.setBounds(100, 10, 20, 20);

        WM5.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM5.setForeground(new java.awt.Color(255, 255, 255));
        WM5.setText("5");
        BackpanelWMS.add(WM5);
        WM5.setBounds(140, 10, 20, 20);

        WM6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM6.setForeground(new java.awt.Color(255, 255, 255));
        WM6.setText("6");
        BackpanelWMS.add(WM6);
        WM6.setBounds(180, 10, 20, 20);

        WM7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM7.setForeground(new java.awt.Color(255, 255, 255));
        WM7.setText("7");
        BackpanelWMS.add(WM7);
        WM7.setBounds(10, 50, 20, 20);

        WM8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM8.setForeground(new java.awt.Color(255, 255, 255));
        WM8.setText("8");
        BackpanelWMS.add(WM8);
        WM8.setBounds(40, 50, 20, 20);

        WM9.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM9.setForeground(new java.awt.Color(255, 255, 255));
        WM9.setText("9");
        BackpanelWMS.add(WM9);
        WM9.setBounds(70, 50, 20, 20);

        WM10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM10.setForeground(new java.awt.Color(255, 255, 255));
        WM10.setText("10");
        BackpanelWMS.add(WM10);
        WM10.setBounds(100, 50, 30, 20);

        WM11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        WM11.setForeground(new java.awt.Color(255, 255, 255));
        WM11.setText("11");
        BackpanelWMS.add(WM11);
        WM11.setBounds(140, 50, 30, 20);

        ReservationMainBackPanel.add(BackpanelWMS);
        BackpanelWMS.setBounds(20, 220, 220, 80);

        bookbtn.setBackground(new java.awt.Color(128, 0, 0));
        bookbtn.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        bookbtn.setForeground(new java.awt.Color(255, 255, 255));
        bookbtn.setText("BOOK");
        bookbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookbtnMouseClicked(evt);
            }
        });
        ReservationMainBackPanel.add(bookbtn);
        bookbtn.setBounds(30, 430, 90, 40);

        cancelbookbtn.setBackground(new java.awt.Color(128, 0, 0));
        cancelbookbtn.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cancelbookbtn.setForeground(new java.awt.Color(255, 255, 255));
        cancelbookbtn.setText("CANCEL");
        cancelbookbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelbookbtnMouseClicked(evt);
            }
        });
        ReservationMainBackPanel.add(cancelbookbtn);
        cancelbookbtn.setBounds(190, 430, 100, 40);

        jSeparator10.setForeground(new java.awt.Color(128, 0, 0));
        ReservationMainBackPanel.add(jSeparator10);
        jSeparator10.setBounds(60, 60, 660, 2);

        getContentPane().add(ReservationMainBackPanel);
        ReservationMainBackPanel.setBounds(210, 0, 780, 490);

        FeedbackMainBackPanel.setBackground(new java.awt.Color(255, 255, 255));
        FeedbackMainBackPanel.setLayout(null);

        jSeparator5.setForeground(new java.awt.Color(128, 0, 0));
        FeedbackMainBackPanel.add(jSeparator5);
        jSeparator5.setBounds(60, 60, 660, 2);

        Feedbackmaintxt.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        Feedbackmaintxt.setText("Feedback");
        FeedbackMainBackPanel.add(Feedbackmaintxt);
        Feedbackmaintxt.setBounds(350, 30, 120, 28);

        feebacktxt3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        feebacktxt3.setText("Thank you for using our system!");
        FeedbackMainBackPanel.add(feebacktxt3);
        feebacktxt3.setBounds(260, 140, 260, 20);

        feedbacktxt1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        feedbacktxt1.setText("Welcome to our feedback section and in order for us to provide a better service to our \n");
        FeedbackMainBackPanel.add(feedbacktxt1);
        feedbacktxt1.setBounds(70, 80, 680, 20);

        feedbacktxt2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        feedbacktxt2.setText("customers, we require feedback as to how we may improve our current system.");
        FeedbackMainBackPanel.add(feedbacktxt2);
        feedbacktxt2.setBounds(100, 110, 620, 20);

        qrcode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/QR code.png"))); // NOI18N
        FeedbackMainBackPanel.add(qrcode);
        qrcode.setBounds(260, 190, 250, 250);

        getContentPane().add(FeedbackMainBackPanel);
        FeedbackMainBackPanel.setBounds(210, 0, 780, 490);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LaundryBackPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LaundryBackPanelMouseClicked
        LaundryMainBackPanel.setVisible(true);
        MenuMainBackPanel.setVisible(false);
        AlarmMainBackPanel.setVisible(false);
        ReservationMainBackPanel.setVisible(false);
        FeedbackMainBackPanel.setVisible(false);
        
        Laundrytxt.setForeground(Color.white);
        Menutxt.setForeground(Color.black);
        Alarmtxt.setForeground(Color.black);
        Reservationtxt.setForeground(Color.black);
        Feedbacktxt.setForeground(Color.black);
        
        LaundryBackPanel.setBackground(new Color(128, 0, 0));
        MenuBackPanel.setBackground (Color.white);
        AlarmBackPanel.setBackground (Color.white);
        ReservationBackPanel.setBackground (Color.white);
        FeedbackBackPanel.setBackground (Color.white);
    }//GEN-LAST:event_LaundryBackPanelMouseClicked

    private void MenuBackPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBackPanelMouseClicked
        LaundryMainBackPanel.setVisible(false);
        MenuMainBackPanel.setVisible(true);
        AlarmMainBackPanel.setVisible(false);
        ReservationMainBackPanel.setVisible(false);
        FeedbackMainBackPanel.setVisible(false);
        
        Laundrytxt.setForeground(Color.black);
        Menutxt.setForeground(Color.white);
        Alarmtxt.setForeground(Color.black);
        Reservationtxt.setForeground(Color.black);
        Feedbacktxt.setForeground(Color.black);
        
        MenuBackPanel.setBackground(new Color(128, 0, 0));
        LaundryBackPanel.setBackground (Color.white);
        AlarmBackPanel.setBackground (Color.white);
        ReservationBackPanel.setBackground (Color.white);
        FeedbackBackPanel.setBackground (Color.white);
    }//GEN-LAST:event_MenuBackPanelMouseClicked

    private void AlarmBackPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AlarmBackPanelMouseClicked
        LaundryMainBackPanel.setVisible(false);
        MenuMainBackPanel.setVisible(false);
        AlarmMainBackPanel.setVisible(true);
        ReservationMainBackPanel.setVisible(false);
        FeedbackMainBackPanel.setVisible(false);
        
        Laundrytxt.setForeground(Color.black);
        Menutxt.setForeground(Color.black);
        Alarmtxt.setForeground(Color.white);
        Reservationtxt.setForeground(Color.black);
        Feedbacktxt.setForeground(Color.black);
        
        AlarmBackPanel.setBackground(new Color(128, 0, 0));
        LaundryBackPanel.setBackground (Color.white);
        MenuBackPanel.setBackground (Color.white);
        ReservationBackPanel.setBackground (Color.white);
        FeedbackBackPanel.setBackground (Color.white);
    }//GEN-LAST:event_AlarmBackPanelMouseClicked

    private void ReservationBackPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReservationBackPanelMouseClicked
        LaundryMainBackPanel.setVisible(false);
        MenuMainBackPanel.setVisible(false);
        AlarmMainBackPanel.setVisible(false);
        ReservationMainBackPanel.setVisible(true);
        FeedbackMainBackPanel.setVisible(false);
        
        Laundrytxt.setForeground(Color.black);
        Menutxt.setForeground(Color.black);
        Alarmtxt.setForeground(Color.black);
        Reservationtxt.setForeground(Color.white);
        Feedbacktxt.setForeground(Color.black);
        
        ReservationBackPanel.setBackground(new Color(128, 0, 0));
        LaundryBackPanel.setBackground (Color.white);
        MenuBackPanel.setBackground (Color.white);
        AlarmBackPanel.setBackground (Color.white);
        FeedbackBackPanel.setBackground (Color.white);
    }//GEN-LAST:event_ReservationBackPanelMouseClicked

    private void FeedbackBackPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FeedbackBackPanelMouseClicked
        LaundryMainBackPanel.setVisible(false);
        MenuMainBackPanel.setVisible(false);
        AlarmMainBackPanel.setVisible(false);
        ReservationMainBackPanel.setVisible(false);
        FeedbackMainBackPanel.setVisible(true);
        
        Laundrytxt.setForeground(Color.black);
        Menutxt.setForeground(Color.black);
        Alarmtxt.setForeground(Color.black);
        Reservationtxt.setForeground(Color.black);
        Feedbacktxt.setForeground(Color.white);
        
        FeedbackBackPanel.setBackground(new Color(128, 0, 0));
        LaundryBackPanel.setBackground (Color.white);
        MenuBackPanel.setBackground (Color.white);
        ReservationBackPanel.setBackground (Color.white);
        AlarmBackPanel.setBackground (Color.white);
    }//GEN-LAST:event_FeedbackBackPanelMouseClicked

    private void typechoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typechoiceActionPerformed
        if (typechoice.getSelectedItem().equals("Choose")){
            PriceTF.setText("");
        } else if (typechoice.getSelectedItem().equals("Washing")){
            PriceTF.setText("4");
        } else if (typechoice.getSelectedItem().equals("Dryer")){
            PriceTF.setText("7");
        } else if (typechoice.getSelectedItem().equals("Wash & Dry")){
            PriceTF.setText("10");
        }
    }//GEN-LAST:event_typechoiceActionPerformed

    private void WeightTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WeightTFKeyReleased
        int price = Integer.parseInt(PriceTF.getText());
        int weight = Integer.parseInt(WeightTF.getText());
        Total = price*weight;
        TotalTF.setText(String.valueOf(Total));
        ReservePriceTF.setText(String.valueOf(Total));
    }//GEN-LAST:event_WeightTFKeyReleased

    private void PaybtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PaybtnMouseClicked
        if (PriceTF.getText().isEmpty() | WeightTF.getText().isEmpty()| TotalTF.getText().isEmpty()| PaymentTF.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Please complete all fields to do your laundry","Error", JOptionPane.ERROR_MESSAGE);
        } else {
           int Payment = Integer.parseInt(PaymentTF.getText());
           int balance = Payment - Total;
           type = (String) typechoice.getSelectedItem();
           if (balance < 0){
               JOptionPane.showMessageDialog(null,"Insufficient Payment, Order cancelled","Error", JOptionPane.ERROR_MESSAGE);
               reset();
           } else {
               JOptionPane.showMessageDialog(null,"Thank you!\n Remaining Balance : " + balance);
               reset();   
           }
        }
    }//GEN-LAST:event_PaybtnMouseClicked

    private void CancelbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelbtnMouseClicked
        reset();
    }//GEN-LAST:event_CancelbtnMouseClicked

    private void RingbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RingbtnMouseClicked
        ChooseRingtone();
        if(!sound.equals(null)){
            CurrentRingtonetxt.setText("Ringtone : " + title);
        }
    }//GEN-LAST:event_RingbtnMouseClicked

    private void SetalrmbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SetalrmbtnMouseClicked
        hourAlarm = jComboBox1.getSelectedItem().toString();
        minuteAlarm = jComboBox2.getSelectedItem().toString();
        if(CurrentRingtonetxt.getText() == ""){
            JOptionPane.showMessageDialog(null,"Set a ringtone");
        } else {
            String AlarmClock = hourAlarm + ":" + minuteAlarm;
            System.out.println(AlarmClock);
            AlarmTime(Integer.valueOf(hourAlarm), Integer.valueOf(minuteAlarm));
        }
    }//GEN-LAST:event_SetalrmbtnMouseClicked

    private void bookbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookbtnMouseClicked
        String customerName = customerTF.getText();
        String price = ReservePriceTF.getText();
        Date selectedDate = jCalendar1.getDate();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

        // Assuming machine number is stored in the variable machineno
        int machineNumber = machineno;

        if (customerName.isEmpty() | price.isEmpty() | machineno == 0) {
            JOptionPane.showMessageDialog(this, "Missing fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection con = null;
        PreparedStatement psCheck = null;
        PreparedStatement psInsert = null;
        ResultSet rs = null;
        
        try {
            // Establish a connection to the database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/laundrysystemdatabase", "root", "SQLreqaw2004");

            // Check if the machine number and date have already been booked
            String checkQuery = "SELECT * FROM reservation WHERE machineno = ? AND date = ?";
            psCheck = con.prepareStatement(checkQuery);
            psCheck.setInt(1, machineNumber);
            psCheck.setDate(2, sqlDate);

            rs = psCheck.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "This machine is currently booked", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Insert the booking information
                String insertQuery = "INSERT INTO reservation (Name, Date, Machineno, Price, Type) VALUES (?, ?, ?, ?, ?)";
                psInsert = con.prepareStatement(insertQuery);
                psInsert.setString(1, customerName);
                psInsert.setDate(2, sqlDate);
                psInsert.setInt(3, machineNumber);
                psInsert.setInt(4, Integer.parseInt(price));
                psInsert.setString(5,type);

                int result = psInsert.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Your booking is successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Bill();
                    lblclear();
                    ReservePriceTF.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (psCheck != null) psCheck.close();
                if (psInsert != null) psInsert.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_bookbtnMouseClicked

    private void LogoutPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoutPanelMouseClicked
        Logouttxt.setForeground(new Color(128, 0, 0));
        int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to logout",
               "Logout",JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            LOGIN loginpg = new LOGIN();
            loginpg.setVisible(true);
            dispose();
        }
        Logouttxt.setForeground(Color.black);
    }//GEN-LAST:event_LogoutPanelMouseClicked

    private void cancelbookbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelbookbtnMouseClicked
       lblclear();
        machineno = 0;
        RecieptPrint.setText("");
        ReservePriceTF.setText("");
    }//GEN-LAST:event_cancelbookbtnMouseClicked

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AlarmBackPanel;
    private javax.swing.JLabel AlarmClocktxt;
    private javax.swing.JPanel AlarmMainBackPanel;
    private javax.swing.JLabel Alarmtxt;
    private javax.swing.JPanel BackpanelWMS;
    private javax.swing.JButton Cancelbtn;
    private javax.swing.JLabel Contact2txt;
    private javax.swing.JLabel CurrentRingtonetxt;
    private javax.swing.JLabel Customertxt;
    private javax.swing.JPanel Emergencybackpanel;
    private javax.swing.JPanel FeedbackBackPanel;
    private javax.swing.JPanel FeedbackMainBackPanel;
    private javax.swing.JLabel Feedbackmaintxt;
    private javax.swing.JLabel Feedbacktxt;
    private javax.swing.JPanel LaundryBackPanel;
    private javax.swing.JPanel LaundryMainBackPanel;
    private javax.swing.JLabel LaundryReservationtxt;
    private javax.swing.JLabel Laundrysystemtxt;
    private javax.swing.JLabel Laundrytxt;
    private javax.swing.JPanel LeftBackPanel;
    private javax.swing.JPanel LogoutPanel;
    private javax.swing.JLabel Logouttxt;
    private javax.swing.JPanel MenuBackPanel;
    private javax.swing.JPanel MenuMainBackPanel;
    private javax.swing.JLabel Menutxt;
    private javax.swing.JButton Paybtn;
    private javax.swing.JTextField PaymentTF;
    private javax.swing.JLabel Paymenttxt;
    private javax.swing.JTextField PriceTF;
    private javax.swing.JLabel Pricestxt;
    private javax.swing.JLabel Pricestxt1;
    private javax.swing.JLabel Pricetxt;
    private javax.swing.JPanel PricingBackpanel;
    private javax.swing.JPanel PricingBackpanel1;
    private javax.swing.JLabel Pricingtxt;
    private javax.swing.JLabel Pricingtxt1;
    private javax.swing.JTextArea RecieptPrint;
    private javax.swing.JPanel ReservationBackPanel;
    private javax.swing.JPanel ReservationMainBackPanel;
    private javax.swing.JLabel Reservationtxt;
    private javax.swing.JTextField ReservePriceTF;
    private javax.swing.JButton Ringbtn;
    private javax.swing.JLabel SeparateTimetxt;
    private javax.swing.JLabel SeparateTimetxt2;
    private javax.swing.JButton Setalrmbtn;
    private javax.swing.JPanel SloganBackPanel;
    private javax.swing.JLabel Slogantxt;
    private javax.swing.JLabel Timertxt;
    private javax.swing.JTextField TotalTF;
    private javax.swing.JLabel Totalpricetxt;
    private javax.swing.JLabel Typetxt;
    private javax.swing.JLabel WM1;
    private javax.swing.JLabel WM10;
    private javax.swing.JLabel WM11;
    private javax.swing.JLabel WM12;
    private javax.swing.JLabel WM2;
    private javax.swing.JLabel WM3;
    private javax.swing.JLabel WM4;
    private javax.swing.JLabel WM5;
    private javax.swing.JLabel WM6;
    private javax.swing.JLabel WM7;
    private javax.swing.JLabel WM8;
    private javax.swing.JLabel WM9;
    private javax.swing.JLabel Washingmachinetxt;
    private javax.swing.JTextField WeightTF;
    private javax.swing.JLabel Weighttxt;
    private javax.swing.JPanel WelcomeBP;
    private javax.swing.JLabel Welcometxt1;
    private javax.swing.JLabel Welcometxt2;
    private javax.swing.JLabel Workindaytxt;
    private javax.swing.JPanel WorkinghrsBackPanel;
    private javax.swing.JLabel Workinghrstxt;
    private javax.swing.JButton bookbtn;
    private javax.swing.JButton cancelbookbtn;
    private javax.swing.JLabel contact1txt;
    private javax.swing.JTextField customerTF;
    private javax.swing.JLabel emergencycontacttxt;
    private javax.swing.JLabel emergencymsgtxt;
    private javax.swing.JLabel feebacktxt3;
    private javax.swing.JLabel feedbacktxt1;
    private javax.swing.JLabel feedbacktxt2;
    private javax.swing.JLabel hourtxt;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel minutetxt;
    private javax.swing.JLabel pricereservetxt;
    private javax.swing.JLabel qrcode;
    private javax.swing.JLabel timertext;
    private javax.swing.JLabel timeworktxt;
    private javax.swing.JComboBox<String> typechoice;
    private javax.swing.JLabel typepricetxt;
    private javax.swing.JLabel typepricetxt1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (true){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("hh:mm:ss");
        Date date = c.getTime();
        hours = simpleDateFormat.format(date);
        Timertxt.setText(hours);
    }
    }
}
