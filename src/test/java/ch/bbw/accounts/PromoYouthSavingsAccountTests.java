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

    @Test void jugendkontoStartetOhneGuthaben() {
        PromoYouthSavingsAccount konto = new PromoYouthSavingsAccount("Y-2000");
        assertEquals(0, konto.getBalance());
        assertTrue(konto.getBookings().isEmpty());
    }

    @Test void jugendkontoVerhindertUeberzug() throws Exception {
        PromoYouthSavingsAccount konto = new PromoYouthSavingsAccount("Y-2001");
        konto.deposit(Scheduled.now(), 1000);
        assertThrows(InvalidAmountException.class,
                () -> konto.withdraw(Scheduled.now(), 1001));
        assertEquals(1000, konto.getBalance());
    }

    @Test void jugendkontoAkzeptiertExaktDieMonatslimite() throws Exception {
        PromoYouthSavingsAccount konto = new PromoYouthSavingsAccount("Y-2002");
        konto.deposit(Scheduled.now(), 30_000_000L);
        assertEquals(10_000_000L, konto.withdraw(Scheduled.now(), 20_000_000L));
    }
}
