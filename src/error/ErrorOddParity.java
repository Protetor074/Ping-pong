package error;

public class ErrorOddParity extends Exception {
    public ErrorOddParity() {
        super("Wartość parametru liczby kolumn musi być nieparzysta!");
    }
}
