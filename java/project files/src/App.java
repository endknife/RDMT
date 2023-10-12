import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class App extends JFrame {

    //Initialising colors and fonts
    //public Farbe col = new Farbe();
    public Tema col = new Tema();
    final Font font = new FontUIResource("arial", 3, 18);

    //Attributes
    private int colonne = 3;
    private int righe = 5;
    private int n_banchi = 15;
    private boolean[] banchiSelezionati = new boolean[n_banchi];
    private int contatoreBanchiSelezionati = 0;
    private char mode = ' ';
    private boolean[] boolArray = new boolean[n_banchi];


    //Frame components
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel centerPanel = new JPanel(new GridLayout(righe, colonne,5,5));
    private JPanel modePanel = new JPanel(new GridLayout(3,1));
    private JPanel modeNorthPanel = new JPanel();
    private JPanel modeCenterPanel = new JPanel(new GridLayout(1,4,10,10));
    private JPanel modeSouthPanel = new JPanel();
    private MyButton[] buttons = new MyButton[n_banchi];
    private final JButton upButton = new JButton(new ImageIcon(((new ImageIcon(getClass().getClassLoader().getResource("arrow-circle-up.png"))).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    private final JButton downButton = new JButton(new ImageIcon(((new ImageIcon(getClass().getClassLoader().getResource("arrow-circle-down.png"))).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    private final JButton stopButton = new JButton(new ImageIcon(((new ImageIcon(getClass().getClassLoader().getResource("cross-circle.png"))).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    private final JButton selectAllButton = new JButton(new ImageIcon(((new ImageIcon(getClass().getClassLoader().getResource("list-check.png"))).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    private JPanel westPanel = new JPanel();
    private JPanel eastPanel = new JPanel();
    private JPanel northPanel = new JPanel();

    //Initialising communication attributes
    Communication comm;


    /**
     * <h1>MAIN APP CONSTRUCTOR</h1>
     * The "app" constructor is the only constructor of the class App. When it's called the application starts.
     * @creators: Recla & Zago
     * @last_update: 10/10/23
     */
    App(){
        //Creation and initialization of the main frame
        frameSettings();

        //Needed to send the stop signal
        Arrays.fill(boolArray, true);

        //Initialising MenuBar and Keypad buttons
        initializeMenuBar();
        initialiseButtons();

        //Adding panels to the main frame and adding action listeners to the buttons
        panelsBuilder();

        //Sets the main frame visible
        this.setVisible(true);

        comm = new Communication();
        while (!comm.connection()){
            JOptionPane.showConfirmDialog(this,  "Arduino non connesso.\nControllare che il cavo USB sia inserito correttamente", "ERRORE!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }

    }




    //-----------METODI--------------

    /**
     * <h2>Initialisation of the Buttons</h2>
     * This method creates and initializes the <b>buttons</b> and adds the action listeners.
     */
    public void initialiseButtons(){
        for(int i = 0; i<n_banchi; i++){
            MyButton button = new MyButton(String.valueOf(i+2));
            buttons[i] = button;
            buttons[i].setBackground(col.buttonUnPressedBg);
            buttons[i].setFont(font);
            buttons[i].setFocusPainted(false);


            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int nrB = Integer.parseInt(e.getActionCommand())-2;

                    if(button.isPressed()){
                        button.setBackground(col.buttonUnPressedBg);
                        contatoreBanchiSelezionati--;
                        button.setPressed(false);
                    }else{
                        button.setBackground(col.buttonPressedBg);
                        contatoreBanchiSelezionati++;
                        button.setPressed(true);
                    }
                    System.out.println(button.isPressed());
                    System.out.println(contatoreBanchiSelezionati);
                    banchiSelezionati[nrB] = !banchiSelezionati[nrB];
                }
            });

            centerPanel.add(button);
        }

        upButton.setContentAreaFilled(false);
        upButton.setBorder(null);
        upButton.setFocusable(false);
        upButton.setToolTipText("Clicca per alzare");
        downButton.setContentAreaFilled(false);
        downButton.setBorder(null);
        downButton.setFocusable(false);
        downButton.setToolTipText("Clicca per abbassare");
        stopButton.setContentAreaFilled(false);
        stopButton.setBorder(null);
        stopButton.setFocusable(false);
        stopButton.setToolTipText("Clicca per fermare");
        selectAllButton.setContentAreaFilled(false);
        selectAllButton.setBorder(null);
        selectAllButton.setFocusable(false);
        selectAllButton.setToolTipText("Seleziona tutti");


        modeCenterPanel.add(upButton);
        modeCenterPanel.add(downButton);
        modeCenterPanel.add(stopButton);
        modeCenterPanel.add(selectAllButton);
    }


    /**
     * <h2>Frame settings</h2>
     * This method initialises the main settings of the <b>frame</b>. The method "setVisible()" has to be the last operation in the constructor.
     */
    public void frameSettings(){
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("data-transfer.png"));
        this.setIconImage(icon);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("TELECOMANDO");
        this.setSize(new Dimension(300, 450));
        mainPanel.setBackground(col.background);
        this.setContentPane(mainPanel);
        this.setResizable(false);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - this.getWidth();
        int y = (int) rect.getMaxY()-40 - this.getHeight();
        this.setLocation(x, y);

        //these lines hide the top window bar to avoid closing the program
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }



    /**
     * <h2>MenuBar initialization</h2>
     * This method creates the MenuBar and adds the action listener to the MenuItems. It also manages the file "info.txt".
     */
    public void initializeMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuItem1 = new JMenuItem("info");

        menu.add(menuItem1);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String path = "info.txt";
                StringBuilder msg = new StringBuilder();

                //Reads text file and if it exists a new frame is created, otherwise a confirmDialog pops up.
                try {
                    JFrame info = new JFrame("INFO");
                    JPanel mainInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    info.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    info.setLocationRelativeTo(null);
                    info.setContentPane(mainInfoPanel);

                    BufferedReader fileReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
                    Scanner scanner = new Scanner(fileReader);

                    JLabel label = new JLabel();

                    msg.append("<html><p>");

                    while(scanner.hasNextLine()){

                        msg.append(scanner.nextLine() + "<br>");

                    }
                    scanner.close();
                    msg.append("</p></html>");

                    label.setText(msg.toString());
                    mainInfoPanel.add(label);

                    info.pack();
                    info.setVisible(true);

                }catch (Exception event){
                    JOptionPane.showConfirmDialog(mainPanel, "file con path: " + path + " non trovato", "ERRORE!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }


            }
        });
    }


    /**
     * <h2>SouthPanel</h2>
     * This method creates a <b>south panel</b> and adds the necessary elements with action listeners.
     */
    public void panelsBuilder(){
        modePanel.add(modeNorthPanel);
        modePanel.add(modeCenterPanel);
        modePanel.add(modeSouthPanel);

        mainPanel.add(modePanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(eastPanel, BorderLayout.EAST);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        //-------------ACTION LISTENERS-------------

        //The action listeners dictate the functionalities of the buttons in the south panel.
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 'S';
                if(contatoreBanchiSelezionati == 0) {return;}
                comm.sendMessage(banchiSelezionati, mode);
            }
        });
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 'G';
                if(contatoreBanchiSelezionati == 0) {return;}
                comm.sendMessage(banchiSelezionati, mode);
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 'T';

                comm.sendMessage(boolArray, mode);
            }
        });


        selectAllButton.addActionListener(new ActionListener() {
            boolean selected = false;
            Color colore;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!selected){
                    contatoreBanchiSelezionati = n_banchi;
                    colore = col.buttonPressedBg;
                    Arrays.fill(banchiSelezionati, true);
                }else{
                    contatoreBanchiSelezionati = 0;
                    colore = col.buttonUnPressedBg;
                    Arrays.fill(banchiSelezionati, false);
                }
                for(int i = 0; i<buttons.length; i++){
                    buttons[i].setBackground(colore);
                    buttons[i].setPressed(!selected);
                }

                selected = !selected;
            }
        });
    }


}