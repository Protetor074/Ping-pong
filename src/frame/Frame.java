package frame;

import error.ErrorValue;
import threads.Player;
import threads.Selector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static frame.Results.*;

public class Frame extends JFrame {

    //Główny Panel
    private JPanel applicationPanel;

    //Wartości parametrów
    private JLabel[] parameterLabel;//Tablica nazw parametrow
    private JSpinner[] parameterSpiner;//Wartości parametrów

    //Przyciski
    private JButton startButton;//Start symulacji
    private JButton resetButton;//Reset Symulacji

    //Wartości stałe
    private final int yDistance = 15;//odstęp w osi y
    private final int xDistance = 15;//odstęp w osi x
    private final int widthWindow = 1920;
    private final int heightWindow = 1040;
    final int textHight = 20;//Wielkość pól tekstowych

    //Parametry planszy
    public static int playrNumber = 10;
    public static int ballNumber = 3;
    public static int tabelColumns = 17;
    public static int tabelRows = 16;

    //Parametry opuźnień
    public static int timeSelector = 100;
    public static int timeBall = 100;
    public static int timePlayer = 100;

    //Zabezpieczenia
    public static boolean stop = true;


    public Frame(String name, int tabelColumns, int tabelRows) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.tabelColumns = tabelColumns;
        this.tabelRows = tabelRows;
        setVisible(true);
        setLayout(null);
        createGui();
        setActionButton();
    }

    public void createGui() {

        final int border = 10;

        setSize(widthWindow, heightWindow);
        applicationPanel = new JPanel();
        setContentPane(applicationPanel);
        setLayout(null);
        applicationPanel.setLayout(null);
        setResizable(false);

        //tabela sumulacji
        Table table = new Table(tabelRows, tabelColumns);
        applicationPanel.add(table);
        table.setBounds(2 * xDistance + widthWindow / 4, yDistance / 2, widthWindow - 4 * yDistance - widthWindow / 4, heightWindow - 4 * xDistance);
        table.setBorder(BorderFactory.createTitledBorder("Symulator"));

        //plansza parametrow
        JPanel parameters = new JPanel();
        applicationPanel.add(parameters);
        parameters.setBounds(xDistance, yDistance, widthWindow / 4 - yDistance, heightWindow / 2 - 3 * xDistance);
        parameters.setBorder(BorderFactory.createTitledBorder("Parametry"));

        Results results = new Results();
        applicationPanel.add(results);
        results.setBounds(xDistance, heightWindow / 2 - yDistance, widthWindow / 4 - yDistance, heightWindow / 2 - 3 * xDistance);
        results.setBorder(BorderFactory.createTitledBorder("Wyniki"));


        //ustawienia etykiet pól wartości
        parameterLabel = new JLabel[]{
                new JLabel("Liczba piłek:"),
                new JLabel("Liczba obrońców:"),
                new JLabel("Czas opuźnienia wątku piłki:"),
                new JLabel("Czas opóźninia wątku selektora:"),
                new JLabel("Czas opuźnienia wątku graccza:")
        };

        //ustawienia pól wartości
        SpinnerNumberModel playerTime = new SpinnerNumberModel(300, 100, 10000, 20);//Wartość czasu
        SpinnerNumberModel ballTime = new SpinnerNumberModel(500, 100, 10000, 20);//Wartość czasu
        SpinnerNumberModel selectorTime = new SpinnerNumberModel(100, 100, 10000, 20);//Wartość czasu
        SpinnerNumberModel ball = new SpinnerNumberModel(5, 1, 16, 1);//Licba piłek
        SpinnerNumberModel player = new SpinnerNumberModel(3, 1, 16, 1);//Liczba graczy
        parameterSpiner = new JSpinner[]{
                new JSpinner(ball),
                new JSpinner(player),
                new JSpinner(ballTime),
                new JSpinner(selectorTime),
                new JSpinner(playerTime)
        };

        for (int i = 0; i < parameterSpiner.length; i++) {
            parameterLabel[i].setBounds(border, 3 * border + i * (3 * border + textHight), parameters.getWidth() / 2, parameters.getHeight() / 16);
            parameterSpiner[i].setBounds(border + parameters.getWidth() / 2, 3 * border + i * (3 * border + textHight), parameters.getWidth() / 8, parameters.getHeight() / 16);
            parameters.add(parameterLabel[i]);
            parameters.add(parameterSpiner[i]);
        }
        parameters.setLayout(null);

        startButton = new JButton("Start");
        startButton.setBounds(parameters.getWidth() / 2 - parameters.getWidth() / 4, border * 30, parameters.getWidth() / 2, parameters.getWidth() / 8);
        parameters.add(startButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(parameters.getWidth() / 2 - parameters.getWidth() / 6, border * 38, parameters.getWidth() / 3, parameters.getWidth() / 12);
        resetButton.setEnabled(false);
        parameters.add(resetButton);

    }

    private void setActionButton() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSimulation();
            }
        });
    }

    public void startSimulation() {
        if (checkData()) {
            stop = true;
            startButton.setEnabled(false);
            resetButton.setEnabled(true);
            readData();
            createEndRowSum();
            refreshValueEndRow();

            Player.activePlayer(tabelRows, tabelColumns);
            Selector.generateBall(tabelRows, tabelColumns);
        }
    }

    private boolean checkData() {
        boolean passCheck = true;
        try {
            if ((int) parameterSpiner[0].getValue() > tabelRows) {
                passCheck = false;
                throw new ErrorValue("Liczba piłek", tabelRows);
            }
            if ((int) parameterSpiner[1].getValue() > tabelRows) {
                passCheck = false;
                throw new ErrorValue("Liczba obrońców", tabelRows);
            }
        } catch (ErrorValue e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return passCheck;
    }

    private void readData() {
        ballNumber = (int) parameterSpiner[0].getValue();
        playrNumber = (int) parameterSpiner[1].getValue();
        timeBall = (int) parameterSpiner[2].getValue();
        timeSelector = (int) parameterSpiner[3].getValue();
        timePlayer = (int) parameterSpiner[4].getValue();

    }

    private void resetSimulation() {

        resetButton.setEnabled(false);
        stop = false;
        resetEndRowValue();
        //Wywoływanie wątków
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        reset();
        startButton.setEnabled(true);
    }

    private void reset() {

        for (int i = 0; i < tabelRows; i++) {
            for (int j = 0; j < tabelColumns; j++) {
                Table.panelSimulation[i][j].removeAll();
                int notPaint = 0;
                if (j == 0 || j == tabelColumns - 1) {
                    Table.panelSimulation[i][j].setBackground(Color.blue);
                    notPaint = 1;
                }
                if (j == tabelColumns - 2) {
                    Table.panelSimulation[i][j].setBackground(Color.green);
                    notPaint = 1;
                }
                if (j == 1) {
                    Table.panelSimulation[i][j].setBackground(Color.yellow);
                    notPaint = 1;
                }
                if (j == tabelColumns / 2) {
                    Table.panelSimulation[i][j].setBackground(Color.CYAN);
                    notPaint = 1;
                }
                if (notPaint == 0) {
                    Table.panelSimulation[i][j].setBackground(Color.red);
                }
            }
        }
        yellowPoint = 0;
        greenPoint = 0;
        refreshValue();
    }
}
