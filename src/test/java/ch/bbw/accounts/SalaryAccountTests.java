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
}
