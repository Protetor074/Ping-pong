package frame;

import javax.swing.*;
import java.awt.*;

public class Table extends JPanel {

    public static JPanel panelSimulation[][];


    public Table(int rowsNumber, int columnsNumber) {
        setLayout(new GridLayout(rowsNumber, columnsNumber));
        addPanel(rowsNumber, columnsNumber);
    }

    public void addPanel(int rowsNumber, int columnsNumber) {
        panelSimulation = new JPanel[rowsNumber][columnsNumber];
        for (int row = 0; row < rowsNumber; row++) {
            for (int column = 0; column < columnsNumber; column++) {
                JPanel panel = new JPanel();

                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                int notPaint = 0;
                if (column == 0 || column == columnsNumber - 1) {
                    panel.setBackground(Color.blue);
                    notPaint = 1;
                }
                if (column == columnsNumber - 2) {
                    panel.setBackground(Color.green);
                    notPaint = 1;
                }
                if (column == 1) {
                    panel.setBackground(Color.yellow);
                    notPaint = 1;
                }
                if (column == columnsNumber / 2) {
                    panel.setBackground(Color.CYAN);
                    notPaint = 1;
                }
                if (notPaint == 0) {
                    panel.setBackground(Color.red);
                }
                add(panel);
                panelSimulation[row][column] = panel;
            }
        }
    }

}
