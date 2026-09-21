package ch.bbw.accounts;


import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

/**
 * Sparkonto.
 *
 * @author Luigi Cavuoti, lro@gmx.ch
 * @version 1.1
 */
public class PromoYouthSavingsAccount extends Account
{
	/**
	 * Initialisiert ein Sparkonto
	 *
	 * @param id die Kontonummer
	 */
	public PromoYouthSavingsAccount(String id)
	{
		super(id);
	}

    /**
     * Zahlt den Betrag ein
     * 
     *  @param date
     *  @param amount
     *  @param text
     * 
     *  @return new balance
     */


    @Override
    public long deposit(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        long bonus = amount / 100;
        amount = amount + bonus;

        return super.deposit(date, amount, text);
    }
	/**
	 * Hebt den gegebenen Betrag vom Konto ab.
	 *
	 * @param date das Transaktionsdatum
	 * @param amount der abzuhebende Betrag
	 *
	 * @return new balance
	 */
	@Override
	public long withdraw(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
		if (getBalance() < amount){
			throw new InvalidAmountException("Amount is greater than balance");}
        if (amount > 200){
            throw new InvalidAmountException("Amount too large for a Youth Savings Account. You may not withdraw over 200.");
        }
        if (amount < 100){
            throw new InvalidAmountException("Amount too small for a Youth Savings Account. You may not withdraw less than 100.");
        }

        return super.withdraw(date, amount);
}
}