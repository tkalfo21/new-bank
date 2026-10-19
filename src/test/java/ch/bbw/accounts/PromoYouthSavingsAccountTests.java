package ch.bbw.accounts;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PromoYouthSavingsAccountTests {
    @Test void monthlyCapAndUnchangedBookingHistory() throws Exception {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");
        account.deposit(Scheduled.now(), 40_000_000L);
        account.withdraw(Scheduled.now(), 15_000_000L);
        account.withdraw(Scheduled.now(), 5_000_000L, "Rest");
        assertEquals(20_000_000L, account.getBalance());
        int count = account.getBookings().size();
        assertThrows(InvalidAmountException.class,
                () -> account.withdraw(Scheduled.now(), 1, "Limite"));
        assertEquals(count, account.getBookings().size());
    }
    @Test void youthIsSavingsAccount() {
        assertInstanceOf(SavingsAccount.class, new PromoYouthSavingsAccount("Y-1000"));
    }
}
