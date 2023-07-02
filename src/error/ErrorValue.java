package error;

public class ErrorValue extends Exception {
    public ErrorValue(String name, int maxValue) {
        super("Wartosc pola " + name + " powinna nie przekraczac " + maxValue + " z powodu wymiarów planszy");
    }
}
