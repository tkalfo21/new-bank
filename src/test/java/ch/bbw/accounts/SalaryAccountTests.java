package ch.bbw.accounts;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SalaryAccountTests {
    @Test void allowsExactCreditLimitAndRejectsOneMore() throws Exception {
        SalaryAccount account = new SalaryAccount("P-1000", -500);
        account.deposit(Scheduled.now(), 1000);
        account.withdraw(Scheduled.now(), 1500, "Miete");
        assertEquals(-500, account.getBalance());
        int bookings = account.getBookings().size();
        assertThrows(InvalidAmountException.class, () -> account.withdraw(Scheduled.now(), 1, "Zu viel"));
        assertEquals(bookings, account.getBookings().size());
    }
    @Test void zeroLimitAndPositiveLimit() {
        SalaryAccount account = new SalaryAccount("P-1000", 0);
        assertThrows(InvalidAmountException.class, () -> account.withdraw(Scheduled.now(), 1));
        assertThrows(IllegalArgumentException.class, () -> new SalaryAccount("P-1000", 1));
    }

    @Test void kontoStartetMitSaldoNull() {
        SalaryAccount konto = new SalaryAccount("P-2000", -1000);
        assertEquals(0, konto.getBalance());
        assertTrue(konto.getBookings().isEmpty());
    }

    @Test void einzahlungReduziertNegativenSaldo() throws Exception {
        SalaryAccount konto = new SalaryAccount("P-2001", -1000);
        konto.withdraw(Scheduled.now(), 700);
        assertEquals(-500, konto.deposit(Scheduled.now(), 200));
    }

    @Test void negativerBetragWirdAbgelehnt() {
        SalaryAccount konto = new SalaryAccount("P-2002", -1000);
        assertThrows(InvalidAmountException.class,
                () -> konto.withdraw(Scheduled.now(), -10));
        assertTrue(konto.getBookings().isEmpty());
    }
}
