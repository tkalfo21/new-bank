import ch.bbw.AccountFactory;
import ch.bbw.accounts.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountFactoryTests {
    @Test
    @DisplayName("Account IDs inkrementieren")
    void accountIdsIncrement() {
        AccountFactory factory = new AccountFactory();

        Account first = factory.createSavingsAccount();
        Account second = factory.createSavingsAccount();

        assertEquals("S-1000", first.getId());
        assertEquals("S-1001", second.getId());
    }
}
