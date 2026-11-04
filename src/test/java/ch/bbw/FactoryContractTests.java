package ch.bbw;
import ch.bbw.accounts.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class FactoryContractTests {
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
