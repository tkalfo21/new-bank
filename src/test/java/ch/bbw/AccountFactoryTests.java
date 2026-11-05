package ch.bbw;

import ch.bbw.AccountFactory;
import ch.bbw.accounts.*;
import ch.bbw.accounts.Account;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        assertNotEquals(acc.getAccountType(), acc2.getAccountType());
    }

    @Test
    @DisplayName("Unbekannter Kontotyp wird abgewiesen")
    void unbekannterTyp() {
        AccountFactory factory = new AccountFactory();
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("NichtVorhanden", 0));
    }

@Test void typesAndPrefixesAndLimit() {
        AccountFactory factory = new AccountFactory();
        Account savings = factory.createSavingsAccount();
        Account youth = factory.createPromoYouthSavingsAccount();
        Account salary = factory.createSalaryAccount(-50_000);
        assertInstanceOf(SavingsAccount.class, savings);
        assertInstanceOf(PromoYouthSavingsAccount.class, youth);
        assertInstanceOf(SalaryAccount.class, salary);
        assertTrue(savings.getId().startsWith("S-"));
        assertTrue(youth.getId().startsWith("Y-"));
        assertTrue(salary.getId().startsWith("P-"));
        assertEquals(-50_000, ((SalaryAccount) salary).getCreditLimit());
        assertThrows(IllegalArgumentException.class, () -> factory.createSalaryAccount(1));
    }
    @Test void uniqueIdsAcrossThousandCreations() {
        AccountFactory factory = new AccountFactory();
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            Account account = switch (i % 3) {
                case 0 -> factory.createSavingsAccount();
                case 1 -> factory.createPromoYouthSavingsAccount();
                default -> factory.createSalaryAccount(-500);
            };
            assertTrue(ids.add(account.getId()));
        }
    }

    @Test void startwertDerKontonummer() {
        AccountFactory factory = new AccountFactory(7000);
        assertEquals("S-7000", factory.createSavingsAccount().getId());
        assertEquals("Y-7001", factory.createPromoYouthSavingsAccount().getId());
    }

    @Test void unbekannterKontotyp() {
        AccountFactory factory = new AccountFactory();
        assertThrows(IllegalArgumentException.class, () -> factory.create("Unbekannt", 0));
    }

    @Test void kreditlimiteNurBeiLohnkonto() {
        AccountFactory factory = new AccountFactory();
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(AccountType.SAVINGS, -100));
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(AccountType.PROMO_YOUTH, -100));
    }
}
