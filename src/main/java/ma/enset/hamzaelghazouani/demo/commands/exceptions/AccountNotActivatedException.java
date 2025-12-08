package ma.enset.hamzaelghazouani.demo.commands.exceptions;

public class AccountNotActivatedException extends RuntimeException {
    
    public AccountNotActivatedException(String message) {
        super(message);
    }
    
    public AccountNotActivatedException(String accountId, String currentStatus) {
        super(String.format("Account %s is not activated. Current status: %s", accountId, currentStatus));
    }
}
