package ch.bbw.accounts;


import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;


public class SalaryAccount extends Account
{

    private final long creditLimit;

    public SalaryAccount(String id, long creditLimit) {
        super(id);

        if (creditLimit > 0) {
            throw new IllegalArgumentException("Credit Limit muss <= 0 sein.");
        }

        this.creditLimit = creditLimit;
    }

    public long getCreditLimit(){
        return this.creditLimit;
    }

    @Override
    public long withdraw(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        if (getBalance() - amount < this.creditLimit)
            throw new InvalidAmountException("Amount is greater than balance");

        return super.withdraw(date, amount);
    }
}
