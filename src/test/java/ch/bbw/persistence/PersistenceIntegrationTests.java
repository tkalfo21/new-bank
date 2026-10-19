package ch.bbw.persistence;

import ch.bbw.Bank;
import ch.bbw.Scheduled;
import ch.bbw.web.BankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersistenceIntegrationTests {
    @Autowired private StoredAccountRepository repository;
    @Autowired private BankService service;

    @Test void accountsAndBookingsSurviveDomainReconstruction() throws Exception {
        repository.deleteAll();
        Bank.getInstance().resetForTests();
        try {
            String id = service.salary(-500);
            service.deposit(id, Scheduled.now(), 1000, "Lohn");
            service.withdraw(id, Scheduled.now(), 1200, "Rechnung");
            StoredAccount stored = repository.findById(id).orElseThrow();
            assertEquals(-500, stored.getCreditLimit());
            assertEquals(2, stored.toBookings().size());

            Bank.getInstance().resetForTests();
            BankService restored = new BankService(repository);
            assertEquals(-200, restored.balance(id));
            assertEquals(2, restored.bookings(id).size());
            assertTrue(restored.salary(-500).startsWith("P-"));
        } finally {
            repository.deleteAll();
            Bank.getInstance().resetForTests();
        }
    }
}
