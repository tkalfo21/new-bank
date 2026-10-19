package ch.bbw.accounts;
import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Gemeinsame Logik für alle Kontotypen. Beträge sind Millirappen. */
public abstract class Account {
    private final String id;
    private final List<Booking> bookings = new ArrayList<>();
    protected Account(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Konto-ID fehlt");
        this.id = id;
    }
    public String getId() { return id; }
    public abstract String getAccountType();
    public long getBalance() { return bookings.stream().mapToLong(Booking::amount).sum(); }
    public List<Booking> getBookings() { return List.copyOf(bookings); }
    public boolean isInvalidTransaction(Scheduled date) { return date == null || date.isPastDue(); }
    public long deposit(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return deposit(date, amount, "Einzahlung");
    }
    public long deposit(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        validate(date, amount);
        bookings.add(new Booking(date.toUnix(), amount, text));
        return getBalance();
    }
    public long withdraw(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return withdraw(date, amount, "Auszahlung");
    }
    public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        validate(date, amount);
        bookings.add(new Booking(date.toUnix(), -amount, text));
        return getBalance();
    }
    protected void validate(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        if (amount <= 0) throw new InvalidAmountException("Betrag muss positiv sein");
        if (isInvalidTransaction(date)) throw new InvalidDateException("Datum darf nicht in der Vergangenheit liegen");
    }
    public String printStatement() {
        StringBuilder statement = new StringBuilder("Konto: ").append(id).append("\n");
        for (Booking booking : bookings) {
            statement.append(Instant.ofEpochMilli(booking.date())).append(" | ")
                     .append(String.format(Locale.ROOT, "CHF %.5f", booking.amount()/100000.0))
                     .append(" | ").append(booking.text()).append("\n");
        }
        return statement.append("Saldo: ").append(String.format(Locale.ROOT, "CHF %.5f", getBalance()/100000.0)).toString();
    }
}
