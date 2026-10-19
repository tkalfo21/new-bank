package ch.bbw.web;

import ch.bbw.Bank;
import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.accounts.Account;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.springframework.stereotype.Service;
import java.util.List;

/** Spring-Adapter: der Controller kennt ausschliesslich diesen Service. */
@Service
public class BankService {
    private final Bank bank;
    public BankService() { this(Bank.getInstance()); }
    public BankService(Bank bank) { this.bank = bank; }

    public String savings() { return bank.createSavingsAccount(); }
    public String youth() { return bank.createPromoYouthSavingsAccount(); }
    public String salary(long creditLimit) { return bank.createSalaryAccount(creditLimit); }
    public long balance(String id) throws InvalidAmountException { return bank.getBalance(id); }
    public long totalBalance() { return bank.getBalance(); }
    public Account account(String id) throws InvalidAmountException { return bank.getAccount(id); }
    public List<Booking> bookings(String id) throws InvalidAmountException { return account(id).getBookings(); }
    public long deposit(String id, Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {
        bank.deposit(id, date, amount, text);
        return bank.getBalance(id);
    }
    public long withdraw(String id, Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {
        return bank.withdraw(id, date, amount, text);
    }
    public List<Account> highest() { return bank.top5HighestBalances(); }
    public List<Account> lowest() { return bank.top5LowestBalances(); }
}
