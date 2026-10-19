package ch.bbw.accounts;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

/** Sparkonto ohne Überzug. */
public class SavingsAccount extends Account {
    public SavingsAccount(String id) { super(id); }
    @Override public String getAccountType() { return "SavingsAccount"; }
    @Override public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        validate(date, amount);
        if (getBalance() < amount) throw new InvalidAmountException("Auszahlung überschreitet den Kontostand");
        return super.withdraw(date, amount, text);
    }
}
