package threads;

import frame.Table;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


import static frame.Frame.*;
import static frame.Frame.playrNumber;
import static frame.Frame.tabelRows;

public class Player {


    private static void printPlayer(int column, int row, String name) {

        JPanel editPanel = Table.panelSimulation[row][column];
        editPanel.setName(name);
        editPanel.setBackground(Color.black);
    }

    private static void removePlayer(int column, int row) {
        JPanel editPanel = Table.panelSimulation[row][column];

        if (column == 1) {
            editPanel.setBackground(Color.yellow);
        } else {
            editPanel.setBackground(Color.green);
        }
    }

    private static void createPlayer(int rowNumber, int columnNumber) {
        Random generator = new Random();
        int column;
        String p;
        for (int i = 0; i < 2; i++) {
            int player = playrNumber;
            if (i == 0) {
                column = 1;
                p = "L";
            } else {
                column = columnNumber - 2;
                p = "P";
            }
            while (player != 0) {
                int x = generator.nextInt() % rowNumber;
                if (x < 0) x = x * -1;
                if (Table.panelSimulation[x][column].getBackground() != Color.black) {
                    printPlayer(column, x, p + player);
                    player--;
                }
            }
        }
    }

    public static void activePlayer(int tabelRows, int tabelColumns) {
        Player.createPlayer(tabelRows, tabelColumns);
        int playerLocation[][];
        playerLocation = foundPlayer();
        int playerPozition[][];
        playerPozition = foundPlayerPozytion();
        for (int i = 0; i < playrNumber; i++) {
            startSIPlayer(playerLocation[i][0], playerLocation[i][1], playerPozition[i][0], i, playerPozition);
            startSIPlayer(playerLocation[playrNumber + i][0], playerLocation[playrNumber + i][1], playerPozition[i][0], i, playerPozition);
        }

    }

    private static int[][] foundPlayer() {
        int playerLocation[][] = new int[playrNumber * 2][2];
        int x = 0;
        for (int j = 0; j < tabelRows; j++) {
            if (Table.panelSimulation[j][1].getBackground() == Color.black) {
                playerLocation[x][0] = 1;//column
                playerLocation[x][1] = j;//row
                x++;
            }
        }
        for (int j = 0; j < tabelRows; j++) {
            if (Table.panelSimulation[j][tabelColumns - 2].getBackground() == Color.black) {
                playerLocation[x][0] = tabelColumns - 2;//column
                playerLocation[x][1] = j;//row
                x++;
            }
        }
        return playerLocation;
    }

    private static int[][] foundPlayerPozytion() {
        int playerProtectArea[] = new int[playrNumber];
        int playerPozytionArea[][] = new int[playrNumber * 2][2];
        int extraPozition;
        int minimalPozition;
        int x = tabelRows;
        extraPozition = x % playrNumber;
        x = x - extraPozition;
        minimalPozition = x / playrNumber;

        System.out.println("Liczba dodatkowych pozycji " + extraPozition);
        System.out.println("Liczba podstawowyh pozycji " + minimalPozition);
        for (int i = 0; i < playrNumber; i++) {
            playerProtectArea[i] = minimalPozition;
        }
        for (int i = 0; i < extraPozition; i++) {
            playerProtectArea[i] += 1;
        }
        for (int i = 0; i < playrNumber; i++) {
            System.out.println(i + " " + playerProtectArea[i]);
        }
        int sum = 0;
        for (int i = 0; i < playrNumber; i++) {
            playerPozytionArea[i][0] = sum;
            playerPozytionArea[i][1] = sum + playerProtectArea[i] - 1;
            sum = sum + playerProtectArea[i];
            System.out.println(i + " " + playerPozytionArea[i][0] + " - " + playerPozytionArea[i][1]);
        }
        return playerPozytionArea;
    }

    private static void startSIPlayer(int columnPozition, int rowPozition, int endrow, int playerPozition, int[][] playerArea) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    moveToSetPozition(columnPozition, rowPozition, endrow, playerPozition, playerArea);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private static void moveToSetPozition(int columnPozition, int rowPozition, int endrow, int playerPozition, int[][] playerArea) throws InterruptedException {
        int curentPosition = rowPozition;
        //nie szukaj jeśli w zakresie
        if (rowPozition < playerArea[playerPozition][0] || rowPozition > playerArea[playerPozition][1]) {
            while (curentPosition != endrow && stop) {
                try {
                    Thread.sleep(timePlayer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                curentPosition = placedIntoPositions(columnPozition, curentPosition, endrow);
            }
        }
        while (stop) {
            playerMoving(playerArea, playerPozition, curentPosition, columnPozition);
        }
    }

    private static void playerMoving(int playerArea[][], int playerPozition, int curentPozition, int columnPozition) throws InterruptedException {
        while (stop) {
            int newPozition = detectBall(playerArea[playerPozition][0], playerArea[playerPozition][1], curentPozition, columnPozition);
            //idź do newPozition
            if (newPozition != curentPozition) {
                if (newPozition > curentPozition) {
                    moveInProtectArea(columnPozition, curentPozition, columnPozition, curentPozition + 1, true);
                    curentPozition++;
                } else {
                    moveInProtectArea(columnPozition, curentPozition, columnPozition, curentPozition - 1, true);
                    curentPozition--;
                }
            } else {
                System.out.println("Nie zmienia pozycji.");
            }
            Thread.sleep(timePlayer);
        }
    }

    private static int placedIntoPositions(int columnPozition, int rowPozition, int endrow) {
        System.out.println(columnPozition + " " + rowPozition + " to " + endrow);
        int newPosition = rowPozition;
        if (endrow < rowPozition) {//idź w dół
            if (Table.panelSimulation[rowPozition - 1][columnPozition].getBackground() != Color.black) {
                palyerMove(columnPozition, rowPozition, -1);
                newPosition--;
            }
        } else {//idź w górę
            if (Table.panelSimulation[rowPozition + 1][columnPozition].getBackground() != Color.black) {
                palyerMove(columnPozition, rowPozition, +1);
                newPosition++;
            }
        }
        return newPosition;
    }

    private static void palyerMove(int column, int row, int direction) {
        String name = Table.panelSimulation[row][column].getName();
        printPlayer(column, row + direction, name);
        removePlayer(column, row);
    }

    public static synchronized boolean moveInProtectArea(int startColumn, int startRow, int newColumn, int newRow, boolean moveType) {
        //moveType true - gracz false - piłka
        String name = Table.panelSimulation[startRow][startColumn].getName();
        boolean moveSucces = false;//false - ruch nie udany true - ruch udany

        if (Table.panelSimulation[newRow][newColumn].getBackground() != Color.black) {
            if (moveType) {
                printPlayer(newColumn, newRow, name);
                removePlayer(startColumn, startRow);
                moveSucces = true;
            } else {
                Ball.printBall(newColumn, newRow, name);
                Ball.removeBall(startColumn, startRow);
                moveSucces = true;
            }
        }
        return moveSucces;
    }

    private static synchronized int detectBall(int minRow, int maxRow, int curentRow, int curentColumn) {
        int detectBall[][] = new int[(maxRow - minRow + 1)][2];
        int x = 0;

        if (curentColumn == 1) {//szukanie po lewej stronie
            for (int i = 0; i <= maxRow - minRow; i++) {
                for (int j = 0; j < (tabelColumns - 5) / 2; j++) {
                    if (Table.panelSimulation[i + minRow][j + 2].getBackground() == Color.pink) {
                        detectBall[x][0] = i + minRow;//row
                        detectBall[x][1] = j + 2;//column
                        System.out.println("Znaleziono pilke na pozycji " + i + " " + j + " po lewj stronie.");
                        x++;
                    }
                }
            }
        } else {//szukanie po prawej stronie
            for (int i = 0; i <= maxRow - minRow; i++) {
                for (int j = 0; j < (tabelColumns - 5) / 2; j++) {
                    if (Table.panelSimulation[i + minRow][j + tabelColumns / 2 + 1].getBackground() == Color.pink) {
                        detectBall[x][0] = i + minRow;//row
                        detectBall[x][1] = j + 2;//column
                        System.out.println("Znaleziono pilke na pozycji " + i + " " + j + " po prawej stronie.");
                        x++;
                    }
                }
            }
        }
        if (x == 0) {
            System.out.println("Nie znaleziono piłki.");
        }
        return foundNearestBall(curentRow, detectBall, x);
    }

    public static int foundNearestBall(int curentRow, int ballPosition[][], int detectNumber) {
        int minDistance = 100;
        int newPosition = curentRow;//jeśli pozycja pozostanie -1 to nie wykonuje ruchu bo nie znaleziono piłki
        int distance;
        for (int i = 0; i < detectNumber; i++) {
            distance = curentRow - ballPosition[i][0];
            if (distance < 0) distance = -distance;
            if (minDistance > distance) {
                newPosition = ballPosition[i][0];
            }
        }
        return newPosition;
    }
}
