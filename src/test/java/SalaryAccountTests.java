import ch.bbw.Scheduled;
import ch.bbw.accounts.SalaryAccount;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryAccountTests {
        @Test
        @DisplayName("CreditLimit muss Null oder Negativ sein.")
        void creditLimitMustBeZeroOrNegative() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new SalaryAccount("P-1000", 1)
            );
        }

        @Test
        @DisplayName("CreditLimit Null verhält sich wie SavingsAccount.")
        void creditLimitZeroBehavesLikeSavingsAccount() throws InvalidDateException, InvalidAmountException {
            SalaryAccount account = new SalaryAccount("P-1000", 0);

            account.deposit(Scheduled.now(), 1000);

            assertDoesNotThrow(() ->
                    account.withdraw(Scheduled.now(), 1000)
            );

            assertThrows(
                    InvalidAmountException.class,
                    () -> account.withdraw(Scheduled.now(), 1)
            );
        }

        @Test
        @DisplayName("Kann genau zur Limite auszahlen.")
        void canWithdrawExactlyToCreditLimit() throws InvalidDateException, InvalidAmountException {
            SalaryAccount account = new SalaryAccount("P-1000", -500);

            account.deposit(Scheduled.now(), 1000);

            // 1000 - 1500 = -500
            assertDoesNotThrow(() ->
                    account.withdraw(Scheduled.now(), 1500)
            );

            assertEquals(-500, account.getBalance());
        }

        @Test
        @DisplayName("Auszahlung unter die Limite unmöglich")
        void cannotWithdrawBelowCreditLimit() throws InvalidDateException, InvalidAmountException {
            SalaryAccount account = new SalaryAccount("P-1000", -500);

            account.deposit(Scheduled.now(), 1000);

            // 1000 - 1501 = -501 -> verboten
            assertThrows(
                    InvalidAmountException.class,
                    () -> account.withdraw(Scheduled.now(), 1501)
            );

            // Das Guthaben darf sich durch die abgelehnte Buchung nicht ändern.
            assertEquals(1000, account.getBalance());
        }

        @Test
        @DisplayName("Balance kann auch negativ werden.")
        void balanceCanBecomeNegative() throws InvalidDateException, InvalidAmountException {
            SalaryAccount account = new SalaryAccount("P-1000", -500);

            account.deposit(Scheduled.now(), 1000);

            account.withdraw(Scheduled.now(), 1200);

            assertEquals(-200, account.getBalance());
        }

        @Test
        @DisplayName("Getter funktioniert bei CreditLimit")
        void getCreditLimitReturnsCorrectLimit() {
            SalaryAccount account = new SalaryAccount("P-1000", -500);

            assertEquals(-500, account.getCreditLimit());
        }
    }
