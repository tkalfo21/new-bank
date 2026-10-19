package ch.bbw;

import ch.bbw.accounts.Account;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class Bank {

    private final TreeMap<String, Account> accounts;
    private final AccountFactory accountFactory;
    /** Holder: lazy und durch JVM-Klasseninitialisierung threadsicher; kein Lock pro Zugriff.
     * Eager wäre threadsicher aber nicht lazy; synchronized lazy sperrt jeden Aufruf.
     * Double-checked locking ohne volatile wäre wegen Sichtbarkeits-/Reorderingproblemen falsch.
     * Pro Singleton: eine Bank, zentraler Zugang und ein gemeinsamer ID-Zähler.
     * Contra: versteckte globale Abhängigkeit, veränderbarer globaler Zustand, schwierige Tests,
     * Lebensdauer bis zum JVM-Ende; in mehreren Server-JVMs gibt es trotzdem mehrere Instanzen.
     */
    private static final class Holder {
        private static final Bank INSTANCE = new Bank(new AccountFactory());
    }

    private Bank() { this(new AccountFactory()); }

    /* Alternative für Dependency Injection und Tests, keine öffentliche No-Arg-Erzeugung. */
    Bank(AccountFactory factory) {
        accounts = new TreeMap<>();
        accountFactory = java.util.Objects.requireNonNull(factory);
    }

    /** Test-Workaround gegen globalen veränderbaren Zustand; kein gutes Produktionsdesign. */
    public synchronized void resetForTests() { accounts.clear(); }

    public static Bank getInstance(){
        return Holder.INSTANCE;
    }

    public String createSavingsAccount() {
        var account = accountFactory.createSavingsAccount();
        var id = account.getId();

        accounts.put(id, account);

        return id;
    }

    public String createPromoYouthSavingsAccount() {
        var account =
                accountFactory.createPromoYouthSavingsAccount();

        var id = account.getId();

        accounts.put(id, account);

        return id;
    }

    public String createSalaryAccount(long creditLimit) {
        var account =
                accountFactory.createSalaryAccount(creditLimit);

        var id = account.getId();

        accounts.put(id, account);

        return id;
    }

    public long getBalance() {
        return accounts.values()
                .stream()
                .mapToLong(Account::getBalance)
                .sum();
    }

    public long getBalance(String id)
            throws InvalidAmountException {
        return getAccount(id).getBalance();
    }

    public void deposit(
            String id,
            Scheduled date,
            long amount
    ) throws InvalidAmountException, InvalidDateException {

        getAccount(id).deposit(date, amount);
    }

    public void deposit(
            String id,
            Scheduled date,
            long amount,
            String text
    ) throws InvalidAmountException, InvalidDateException {

        getAccount(id).deposit(date, amount, text);
    }

    public long withdraw(
            String id,
            Scheduled date,
            long amount
    ) throws InvalidAmountException, InvalidDateException {

        return getAccount(id)
                .withdraw(date, amount);
    }

    public long withdraw(
            String id,
            Scheduled date,
            long amount,
            String text
    ) throws InvalidAmountException, InvalidDateException {

        return getAccount(id)
                .withdraw(date, amount, text);
    }

    public List<Account> top5HighestBalances() {
        return accounts.values()
                .stream()
                .sorted(
                        Comparator.comparingLong(
                                Account::getBalance
                        ).reversed()
                )
                .limit(5)
                .toList();
    }

    public List<Account> top5LowestBalances() {
        return accounts.values()
                .stream()
                .sorted(
                        Comparator.comparingLong(
                                Account::getBalance
                        )
                )
                .limit(5)
                .toList();
    }

    public Account getAccount(String id)
            throws InvalidAmountException {

        Optional<Account> account =
                Optional.ofNullable(accounts.get(id));

        if (account.isEmpty()) {
            throw new InvalidAmountException(
                    "Invalid bank id"
            );
        }

        return account.get();
    }
}