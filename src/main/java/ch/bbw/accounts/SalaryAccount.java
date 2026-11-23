package ch.bbw.accounts;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import java.util.Locale;

/** Lohnkonto mit einer Kreditlimite <= 0. */
public class SalaryAccount extends Account {
    private final long creditLimit;
    public SalaryAccount(String id, long creditLimit) {
        super(id);
        if (creditLimit > 0) throw new IllegalArgumentException("Kreditlimite muss <= 0 sein");
        this.creditLimit = creditLimit;
    }
    @Override public String getAccountType() { return "SalaryAccount"; }
    public long getCreditLimit() { return creditLimit; }
    @Override public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        validate(date, amount);
        if (getBalance() < creditLimit + amount)
            throw new InvalidAmountException(String.format(Locale.ROOT, "Kreditlimite CHF %.2f würde unterschritten", creditLimit / 100000.0));
        return super.withdraw(date, amount, text);
    }
}
