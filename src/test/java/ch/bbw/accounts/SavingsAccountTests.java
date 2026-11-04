package ch.bbw.accounts;

import ch.bbw.Scheduled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsAccountTests {

	@Test
	@DisplayName("Constructs a SavingsAccount and tests a simple deposit")
	public void test() throws Exception {

		SavingsAccount account = new SavingsAccount("S-1000");

		account.deposit(
				Scheduled.now(),
				5000,
				"Einzahlung"
		);

		assertEquals(5000, account.getBalance());
		assertEquals(1, account.getBookings().size());
		assertEquals(5000, account.getBookings().get(0).amount());
		assertEquals("Einzahlung", account.getBookings().get(0).text());
	}

    @Test @DisplayName("Neues Sparkonto hat Saldo null")
    void anfangssaldo() {
        SavingsAccount konto = new SavingsAccount("S-2000");
        assertEquals(0, konto.getBalance());
    }

    @Test @DisplayName("Auszahlung des gesamten Guthabens ist erlaubt")
    void vollständigeAuszahlung() throws Exception {
        SavingsAccount konto = new SavingsAccount("S-2001");
        konto.deposit(Scheduled.now(), 1500);
        assertEquals(0, konto.withdraw(Scheduled.now(), 1500));
    }

    @Test @DisplayName("Auszahlung über Saldo wird abgewiesen")
    void überzugVerboten() throws Exception {
        SavingsAccount konto = new SavingsAccount("S-2002");
        konto.deposit(Scheduled.now(), 1000);
        org.junit.jupiter.api.Assertions.assertThrows(
                ch.bbw.exceptions.InvalidAmountException.class,
                () -> konto.withdraw(Scheduled.now(), 1001));
        assertEquals(1000, konto.getBalance());
        assertEquals(1, konto.getBookings().size());
    }

    @Test @DisplayName("Negativer Einzahlungsbetrag wird abgewiesen")
    void negativeEinzahlung() {
        SavingsAccount konto = new SavingsAccount("S-2003");
        org.junit.jupiter.api.Assertions.assertThrows(
                ch.bbw.exceptions.InvalidAmountException.class,
                () -> konto.deposit(Scheduled.now(), -1));
        assertEquals(0, konto.getBalance());
    }
}