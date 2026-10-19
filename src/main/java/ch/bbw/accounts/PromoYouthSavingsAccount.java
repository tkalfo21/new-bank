package ch.bbw.accounts;
import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;

/**
 * Jugendsparkonto: erbt das Überzugsverbot des Sparkontos.
 * Eigene Regel: maximal CHF 200 (20_000_000 Millirappen)
 * Auszahlungen pro UTC-Kalendermonat.
 */
public class PromoYouthSavingsAccount extends SavingsAccount {
    public static final long MONTHLY_WITHDRAWAL_LIMIT = 20_000_000L;
    public PromoYouthSavingsAccount(String id) { super(id); }
    @Override public String getAccountType() { return "PromoYouthSavingsAccount"; }
    @Override public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        validate(date, amount);
        YearMonth month = YearMonth.from(date.toInstant().atZone(ZoneOffset.UTC));
        long withdrawn = getBookings().stream().filter(b -> b.amount() < 0)
                .filter(b -> YearMonth.from(Instant.ofEpochMilli(b.date()).atZone(ZoneOffset.UTC)).equals(month))
                .mapToLong(Booking::amount).map(Math::abs).sum();
        if (amount > MONTHLY_WITHDRAWAL_LIMIT - withdrawn)
            throw new InvalidAmountException("Monatliche Auszahlungslimite CHF 200 überschritten");
        return super.withdraw(date, amount, text);
    }
}
