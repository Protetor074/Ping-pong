/**
 * AUTOR:
 * Kamil Gondek
 *
 * SPOSÓB KOMPILACJI:
 * set path="C:\Program Files\Java\jdk-17.0.2\bin\";%path%
 * javac -encoding UTF8 -d tojar/classes @compile.txt
 *
 * SPOSÓB BUDOWANIA:
 * jar --create --file Lab05_pop.jar --main-class main.MainSimulation --module-version 1.0 -C tojar\classes module-info.class -C tojar classes -C tojar src
 *
 * SPOSÓB URUCHOMIENIA:
 * java -p lab05_pop.jar -m main.AainSimulation
 *
 * */


package main;

public class MainSimulation {
    public static void main(String[] args) {
        new frame.StartWindow("Parametry wejściowe");
        //new frame.Frame("Symulacja",9,8);
    }
}
