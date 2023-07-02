package threads;

import frame.Results;
import frame.Table;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static frame.Frame.*;

public class Ball {

    public static void printBall(int column, int row, String ballName) {
        JPanel editPanel = Table.panelSimulation[row][column];
        editPanel.setName(ballName);
        editPanel.setBackground(Color.pink);
    }

    public static void removeBall(int column, int row) {
        JPanel editPanel = Table.panelSimulation[row][column];
        int painted = 0;
        if (column == 0 || column == tabelColumns - 1) {
            editPanel.setBackground(Color.blue);
            painted = 1;
        }
        if (column == tabelColumns - 2 && Table.panelSimulation[row][column].getBackground() != Color.black) {
            editPanel.setBackground(Color.green);
            painted = 1;
        }
        if (column == 1 && Table.panelSimulation[row][column].getBackground() != Color.black) {
            editPanel.setBackground(Color.yellow);
            painted = 1;
        }
        if (column == tabelColumns / 2) {
            editPanel.setBackground(Color.CYAN);
            painted = 1;
        }
        if (painted == 0 && Table.panelSimulation[row][column].getBackground() != Color.black) {
            editPanel.setBackground(Color.red);
        }
    }

    public static void startSIBall(int row, int column, boolean direction, String ballName) {
        ExecutorService executorService = Executors.newFixedThreadPool(ballNumber);
        executorService.submit(() -> {
            try {
                ballSi(row, column, direction, ballName);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void ballSi(int row, int column, boolean direction, String ballName) throws InterruptedException {
        int curentColumn = column;
        boolean curentDirection = direction;//true-prawo false-lewo
        Ball.printBall(column, row, ballName);
        Thread.sleep(timeSelector);
        while (stop) {
            if (curentColumn == 2 || curentColumn == tabelColumns - 3) {
                curentColumn = ballMoveProtectArea(row, curentColumn);//zwraca nową kolumne
                if (curentColumn == 3 || curentColumn == tabelColumns - 4) {
                    curentDirection = !curentDirection;
                }
            } else if (curentColumn == 0 || curentColumn == tabelColumns - 1) {//koniec pracy
                endTripBall(curentColumn, row);
                break;
            } else {//ruch po polach od 2-tabelColumns-3
                int move = -1;
                if (curentDirection) {
                    move = 1;
                }
                ballMoveNormal(row, curentColumn, curentColumn + move);
                curentColumn = curentColumn + move;
            }
            Thread.sleep(timeBall);
        }
    }

    private static synchronized void endTripBall(int curentColumn, int row) {
        Selector.ballLevel--;
        removeBall(curentColumn, row);
        Selector.addXLocation(row);
        if (curentColumn == 0) {
            Results.yellowPoint++;
            Results.endRowSum(row, 0);
        } else {
            Results.greenPoint++;
            Results.endRowSum(row, 1);
        }

        Results.refreshValue();
        System.out.println("Ball Remove");
    }

    private static void ballMoveNormal(int curentRow, int curentColumn, int newColumn) {
        String name = Table.panelSimulation[curentRow][curentColumn].getName();
        printBall(newColumn, curentRow, name);
        removeBall(curentColumn, curentRow);
    }

    private static int ballMoveProtectArea(int curentRow, int curentColumn) {
        int moveDirection = 1;
        int newColumn = -1;
        if (curentColumn == 2) {
            moveDirection = -1;
        }
        if (Player.moveInProtectArea(curentColumn, curentRow, curentColumn + moveDirection, curentRow, false)) {
            //piłka weszła na pole obronne
            newColumn = curentColumn + moveDirection;
            String name = Table.panelSimulation[curentRow][curentColumn].getName();
            printBall(newColumn, curentRow, name);
            removeBall(curentColumn, curentRow);
        } else {
            //obrut
            newColumn = curentColumn - moveDirection;
            String name = Table.panelSimulation[curentRow][curentColumn].getName();
            printBall(newColumn, curentRow, name);
            removeBall(curentColumn, curentRow);
        }
        return newColumn;
    }

}
