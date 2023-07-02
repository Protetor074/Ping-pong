package frame;

import javax.swing.*;
import java.awt.*;

import static frame.Frame.*;

public class Results extends JPanel {

    public static int yellowPoint = 0;
    public static int greenPoint = 0;

    private JLabel resultJLabel[];
    private static JTextField[] resultValue;
    private static int sumTab[] = new int[tabelRows * 2];
    private static JLabel endRowSumLabel[];

    private int xDistance = 10;
    private int yDistance = 10;
    private int elementWidth = 200;
    private int elementHight = 20;

    public Results() {
        setLayout(null);
        resultJLabel = new JLabel[]{
                new JLabel("Liczba zdobytych punktow Żułtych:"),
                new JLabel("Liczba zdobytych punktow Zielonych:"),
                new JLabel("Prowadzi drużyna:")
        };
        resultValue = new JTextField[]{
                new JTextField("0"),
                new JTextField("0"),
                new JTextField("Aktualnie panuje Remis")
        };

        for (int i = 0; i < resultJLabel.length; i++) {
            resultJLabel[i].setBounds(xDistance, 2 * yDistance + yDistance * 2 * i, elementWidth, elementHight);
            add(resultJLabel[i]);

        }
        for (int i = 0; i < resultJLabel.length - 1; i++) {
            resultValue[i].setBounds(3 * xDistance + elementWidth, 2 * yDistance + yDistance * 2 * i, elementWidth / 3, elementHight);
            add(resultValue[i]);
        }
        resultValue[2].setBounds(3 * xDistance + elementWidth, 2 * yDistance + yDistance * 2 * 2, elementWidth, elementHight);
        add(resultValue[2]);
    }

    public static void refreshValue() {
        resultValue[0].setText(yellowPoint + "");
        resultValue[1].setText(greenPoint + "");
        if (yellowPoint > greenPoint) {
            resultValue[2].setText("Żółtych");
        } else if (yellowPoint < greenPoint) {
            resultValue[2].setText("Zielonych");
        } else {
            resultValue[2].setText("Aktualnie panuje Remis");
        }
    }

    public static synchronized void endRowSum(int rowNumber, int said) {
        if (said == 0) {//lewa strona
            sumTab[rowNumber]++;
            endRowSumLabel[rowNumber].setText(sumTab[rowNumber] + "");
        } else {//prawa strona
            sumTab[rowNumber + tabelRows]++;
            endRowSumLabel[rowNumber + tabelRows].setText(sumTab[rowNumber + tabelRows] + "");
        }
    }

    public static void createEndRowSum() {
        int widthPanel = Table.panelSimulation[0][0].getHeight();
        int heightPanel = Table.panelSimulation[0][0].getWidth();
        endRowSumLabel = new JLabel[tabelRows * 2];

        int labelSize;
        if (widthPanel > heightPanel) {
            labelSize = heightPanel;
        } else {
            labelSize = widthPanel;
        }
        int scale = 6;
        double fountSize = labelSize / 10 * scale;


        for (int i = 0; i < tabelRows; i++) {
            endRowSumLabel[i] = new JLabel("0");
            Font f = endRowSumLabel[i].getFont();
            endRowSumLabel[i].setFont(new Font(f.getFontName(), f.getStyle(), (int) fountSize));
            endRowSumLabel[i].setForeground(Color.WHITE);
            endRowSumLabel[i].setSize(labelSize, labelSize);
            Table.panelSimulation[i][0].add(endRowSumLabel[i]);
        }
        for (int i = 0; i < tabelRows; i++) {
            endRowSumLabel[i + tabelRows] = new JLabel("0");
            Font f = endRowSumLabel[i].getFont();
            endRowSumLabel[i + tabelRows].setFont(new Font(f.getFontName(), f.getStyle(), (int) fountSize));
            endRowSumLabel[i + tabelRows].setForeground(Color.WHITE);
            endRowSumLabel[i + tabelRows].setSize(labelSize, labelSize);
            Table.panelSimulation[i][tabelColumns - 1].add(endRowSumLabel[i + tabelRows]);
        }
    }

    public static void refreshValueEndRow() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (stop) {
                    for (int i = 0; i < tabelRows * 2; i++) {
                        endRowSumLabel[i].setText(sumTab[i] + " ");
                    }
                }
            }
        });
        t.start();
    }

    public static void resetEndRowValue() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    for (int i = 0; i < endRowSumLabel.length; i++) {
                        sumTab[i] = 0;
                        endRowSumLabel[i].setText(sumTab[i] + " ");
                    }
                }
            }
        });
        t.start();
    }
}
