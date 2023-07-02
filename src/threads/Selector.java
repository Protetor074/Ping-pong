package threads;


import java.util.Random;

import static frame.Frame.*;

public class Selector {

    public static int ballLevel;
    public static int ballPosibleLocation[];

    static Random generator = new Random();

    public static void generateBall(int rowNumbers, int columnNumber) {
        ballLevel = 0;
        generatePosiblePositions(rowNumbers);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                createBall(rowNumbers, columnNumber);
            }
        });
        t.start();
    }

    private static synchronized void createBall(int rowNumbers, int columnNumber) {
        while (stop) {
            if (ballLevel < ballNumber) {
                int x = generator.nextInt() % ballPosibleLocation.length;
                if (x < 0) x = x * -1;
                String ballName = "B" + (ballPosibleLocation[x] + 1);
                System.out.println("Stworzono piłkę " + ballName + " na pozycji " + (ballPosibleLocation[x] + 1));
                try {
                    Thread.sleep(timeBall);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Ball.startSIBall(ballPosibleLocation[x], (columnNumber - 1) / 2, generator.nextBoolean(), ballName);
                editBalLLevel(1);
                removeXLocation(x);
            }
            try {
                Thread.sleep(timeSelector);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static synchronized void editBalLLevel(int addLevel) {
        ballLevel = ballLevel + addLevel;
        int ballNumber = ballLevel;
    }

    private static void generatePosiblePositions(int rowNumbers) {
        ballPosibleLocation = new int[rowNumbers];
        for (int i = 0; i < rowNumbers; i++) {
            ballPosibleLocation[i] = i;
        }
    }

    private static synchronized void removeXLocation(int x) {
        int tab[] = new int[ballPosibleLocation.length - 1];
        int j = 0;
        for (int i = 0; i < ballPosibleLocation.length; i++) {
            if (i != x) {
                tab[i - j] = ballPosibleLocation[i];
            } else {
                j = 1;
            }
        }
        ballPosibleLocation = tab;
    }

    public static void addXLocation(int x) {
        int tab[] = new int[ballPosibleLocation.length + 1];
        tab[0] = x;
        for (int i = 1; i <= ballPosibleLocation.length; i++) {
            tab[i] = ballPosibleLocation[i - 1];
        }
        ballPosibleLocation = tab;
    }


}
