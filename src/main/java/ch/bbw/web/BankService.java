package ch.bbw.web;

import ch.bbw.Bank;
import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.accounts.Account;
import ch.bbw.accounts.SalaryAccount;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import ch.bbw.persistence.StoredAccount;
import ch.bbw.persistence.StoredAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Transaktionale Service-Schicht; stellt gespeicherte Konten beim Start wieder her. */
@Service
public class BankService {
    private final Bank bank;
    private final StoredAccountRepository repository;

    @Autowired
    public BankService(StoredAccountRepository repository) {
        this.bank = Bank.getInstance();
        this.repository = repository;
        repository.findAll().forEach(record -> bank.restoreAccount(
                record.getId(), record.getCreditLimit(), record.toBookings()));
    }

    /** Abhängigkeitsinjektion für unabhängige Tests ohne Datenbank. */
    public BankService(Bank bank) {
        this.bank = bank;
        this.repository = null;
    }

    @Transactional
    public synchronized String savings() {
        String id = bank.createSavingsAccount();
        save(id);
        return id;
    }
    @Transactional
    public synchronized String youth() {
        String id = bank.createPromoYouthSavingsAccount();
        save(id);
        return id;
    }
    @Transactional
    public synchronized String salary(long creditLimit) {
        String id = bank.createSalaryAccount(creditLimit);
        save(id);
        return id;
    }
    public long balance(String id) throws InvalidAmountException { return bank.getBalance(id); }
    public long totalBalance() { return bank.getBalance(); }
    public Account account(String id) throws InvalidAmountException { return bank.getAccount(id); }
    public List<Booking> bookings(String id) throws InvalidAmountException { return account(id).getBookings(); }

    @Transactional
    public synchronized long deposit(String id, Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {
        bank.deposit(id, date, amount, text);
        save(id);
        return bank.getBalance(id);
    }

    @Transactional
    public synchronized long withdraw(String id, Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {
        long result = bank.withdraw(id, date, amount, text);
        save(id);
        return result;
    }

    public List<Account> highest() { return bank.top5HighestBalances(); }
    public List<Account> lowest() { return bank.top5LowestBalances(); }

    private void save(String id) {
        if (repository == null) return;
        try {
            Account account = bank.getAccount(id);
            long limit = account instanceof SalaryAccount salary ? salary.getCreditLimit() : 0;
            StoredAccount stored = repository.findById(id).orElseGet(() -> new StoredAccount(id));
            stored.replaceWith(account, limit);
            repository.saveAndFlush(stored);
        } catch (InvalidAmountException e) {
            throw new IllegalStateException(e);
        }
    }
}
