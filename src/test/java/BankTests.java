import ch.bbw.Bank;
import ch.bbw.Scheduled;
import ch.bbw.accounts.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankTests {

    @Test
    @DisplayName("Constructor Test, creates two SavingsAccounts")
    public void testCreate() throws Exception {
        Bank bank = new Bank();

        String id1 = bank.createSavingsAccount();
        String id2 = bank.createSavingsAccount();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);

        assertEquals(0, bank.getBalance(id1));
        assertEquals(0, bank.getBalance(id2));
    }

    @Test
    @DisplayName("Simple deposit on Bank-SavingsAccount should work")
    public void testDeposit() throws Exception {
        Bank bank = new Bank();

        String id = bank.createSavingsAccount();

        bank.deposit(
                id,
                Scheduled.now(),
                5000,
                "Einzahlung"
        );

        assertEquals(5000, bank.getBalance(id));
        assertEquals(1, bank.getAccount(id).getBookings().size());
        assertEquals(
                "Einzahlung",
                bank.getAccount(id).getBookings().get(0).text()
        );
    }

    @Test
    @DisplayName("Simple withdraw on Bank-SavingsAccount should work")
    public void testWithdraw() throws Exception {
        Bank bank = new Bank();

        String id = bank.createSavingsAccount();

        bank.deposit(
                id,
                Scheduled.now(),
                5000,
                "Einzahlung"
        );

        long balance = bank.withdraw(
                id,
                Scheduled.now(),
                2000,
                "Auszahlung"
        );

        assertEquals(3000, balance);
        assertEquals(3000, bank.getBalance(id));
    }

    @Test
    @DisplayName("Print account statement")
    public void testPrint() throws Exception {
        Bank bank = new Bank();

        String id = bank.createSavingsAccount();

        bank.deposit(
                id,
                Scheduled.now(),
                5000,
                "Lohn"
        );

        String statement =
                bank.getAccount(id).printStatement();

        assertTrue(statement.contains(id));
        assertTrue(statement.contains("Lohn"));
        assertTrue(statement.contains("CHF 0.05000"));
    }

    @Test
    @DisplayName("Monthly print is optional")
    public void testMonthlyPrint() throws Exception {
        Bank bank = new Bank();

        String id = bank.createSavingsAccount();

        bank.deposit(
                id,
                Scheduled.now(),
                1000,
                "Einzahlung"
        );

        assertEquals(1000, bank.getBalance(id));
    }

    @Test
    @DisplayName("Tests total balance of two accounts")
    public void testBalance() throws Exception {
        Bank bank = new Bank();

        String id1 = bank.createSavingsAccount();
        String id2 = bank.createSavingsAccount();

        bank.deposit(
                id1,
                Scheduled.now(),
                5000,
                "Einzahlung 1"
        );

        bank.deposit(
                id2,
                Scheduled.now(),
                3000,
                "Einzahlung 2"
        );

        assertEquals(8000, bank.getBalance());
    }


    @Test
    @DisplayName("assertSame soll bei Bank funktionieren.")
    public void checkAssertSame(){
        Bank bank1 = new Bank();
        Bank bank2 = new Bank();

        assertSame(bank1, bank2);
    }
    @Test
    @DisplayName("Tests the List of the top 5 balances")
    public void testTop5() throws Exception {
        Bank bank = new Bank();

        for (int i = 1; i <= 6; i++) {
            String id = bank.createSavingsAccount();

            bank.deposit(
                    id,
                    Scheduled.now(),
                    i * 1000L,
                    "Einzahlung"
            );
        }

        List<Account> accounts =
                bank.top5HighestBalances();

        assertEquals(5, accounts.size());

        assertEquals(
                6000,
                accounts.get(0).getBalance()
        );

        assertEquals(
                2000,
                accounts.get(4).getBalance()
        );
    }

    @Test
    @DisplayName("Tests the List of the bottom 5 balances")
    public void testBottom5() throws Exception {
        Bank bank = new Bank();

        for (int i = 1; i <= 6; i++) {
            String id = bank.createSavingsAccount();

            bank.deposit(
                    id,
                    Scheduled.now(),
                    i * 1000L,
                    "Einzahlung"
            );
        }

        List<Account> accounts =
                bank.top5LowestBalances();

        assertEquals(5, accounts.size());

        assertEquals(
                1000,
                accounts.get(0).getBalance()
        );

        assertEquals(
                5000,
                accounts.get(4).getBalance()
        );
    }
}