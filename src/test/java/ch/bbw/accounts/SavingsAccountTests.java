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
}