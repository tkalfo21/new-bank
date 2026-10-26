package ch.bbw;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

class SingletonTests {
    @Test void sameInstance() {
        assertSame(Bank.getInstance(), Bank.getInstance());
    }
    @Test void noPublicNoArgConstructor() {
        assertThrows(NoSuchMethodException.class, () -> Bank.class.getConstructor());
    }
    @Test void supportsFactoryInjectionForPackageTests() {
        Bank isolated = new Bank(new AccountFactory(9000));
        assertEquals("S-9000", isolated.createSavingsAccount());
    }
    @Test void singletonHasCleanStateAfterReset() throws Exception {
        Bank bank = Bank.getInstance();
        try {
            bank.createSavingsAccount();
        } finally {
            bank.resetForTests(); // Test-Behelf wegen global veränderbarem Zustand
        }
        assertEquals(0, bank.getBalance());
    }
}
