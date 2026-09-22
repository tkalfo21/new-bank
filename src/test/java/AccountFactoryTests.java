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

    @Test
    @DisplayName("Die neue create Methode soll genauso funktionieren, wie die Alte - Promo Youth Savings")
    void createYouthAccount(){
        AccountFactory factory = new AccountFactory();

        Account youth = factory.create("PromoYouthSavingsAccount", 0);
        Account youth2 = factory.createPromoYouthSavingsAccount();
        assertEquals(youth.getAccountType(), youth2.getAccountType());
    }

    @Test
    @DisplayName("Die neue create Methode soll genauso funktionieren, wie die Alte - Savings")
    void createSavingsAccount(){
        AccountFactory factory = new AccountFactory();

        Account acc = factory.create("SavingsAccount", 0);
        Account acc2 = factory.createSavingsAccount();
        assertEquals(acc.getAccountType(), acc2.getAccountType());
    }

    @Test
    @DisplayName("FEHLERMELDUNG SOLL ENTSTEHEN - Verschiedene Typen vergleichen")
    void createDifferentTypes(){
        AccountFactory factory = new AccountFactory();

        Account acc = factory.create("SavingsAccount", 0);
        Account acc2 = factory.createPromoYouthSavingsAccount();
        assertEquals(acc.getAccountType(), acc2.getAccountType());
    }
}
