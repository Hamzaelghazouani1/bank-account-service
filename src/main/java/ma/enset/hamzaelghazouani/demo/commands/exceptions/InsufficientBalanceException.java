package ma.enset.hamzaelghazouani.demo.commands.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(double requested, double available) {
        super(String.format("Insufficient balance: requested %.2f but only %.2f available", requested, available));
    }
}
