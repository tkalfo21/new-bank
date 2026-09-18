package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private final String id;
    private final List<Booking> bookings;

    public Account(String id) {
        this.id = id;
        this.bookings = new ArrayList<Booking>();
    }

    public String getId() {
        return id;
    }

    public long getBalance() {
        return bookings.stream()
                .mapToLong(Booking::amount)
                .sum();
    }

    public List<Booking> getBookings() {
        return List.copyOf(bookings);
    }

    public boolean isInvalidTransaction(Scheduled date) {
        return date.isPastDue();
    }

    public long deposit(Scheduled date, long amount)
            throws InvalidAmountException, InvalidDateException {
        return deposit(date, amount, "Einzahlung");
    }

    public long deposit(Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {

        if (amount <= 0) {
            throw new InvalidAmountException("amount must be greater than 0");
        }

        if (isInvalidTransaction(date)) {
            throw new InvalidDateException("date mustn't be in the past");
        }

        bookings.add(new Booking(date.toUnix(), amount, text));

        return getBalance();
    }

    public long withdraw(Scheduled date, long amount)
            throws InvalidAmountException, InvalidDateException {
        return withdraw(date, amount, "Auszahlung");
    }

    public long withdraw(Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {

        if (amount <= 0) {
            throw new InvalidAmountException("amount must be greater than 0");
        }

        if (isInvalidTransaction(date)) {
            throw new InvalidDateException("date mustn't be in the past");
        }

        bookings.add(new Booking(date.toUnix(), -amount, text));

        return getBalance();
    }

    public String printStatement() {
        StringBuilder statement = new StringBuilder();

        statement.append("Konto: ")
                .append(id)
                .append("\n");

        for (Booking booking : bookings) {
            statement.append(Instant.ofEpochMilli(booking.date()))
                    .append(" | ")
                    .append(formatAmount(booking.amount()))
                    .append(" | ")
                    .append(booking.text())
                    .append("\n");
        }

        statement.append("Saldo: ")
                .append(formatAmount(getBalance()));

        return statement.toString();
    }

    private String formatAmount(long amount) {
        return String.format("CHF %.5f", amount / 100000.0);
    }
}
