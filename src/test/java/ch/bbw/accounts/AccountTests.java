package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTests {

    @Test
    @DisplayName(
            "New account has no bookings and balance is zero"
    )
    public void testInitialization() {

        Account account =
                new SavingsAccount("S-1000");

        assertEquals(0, account.getBalance());
        assertTrue(
                account.getBookings().isEmpty()
        );
    }

    @Test
    @DisplayName(
            "Deposit creates a positive booking"
    )
    public void testDeposit() throws Exception {

        Account account =
                new SavingsAccount("S-1000");

        account.deposit(
                Scheduled.now(),
                5000,
                "Lohn"
        );

        assertEquals(
                5000,
                account.getBalance()
        );

        assertEquals(
                1,
                account.getBookings().size()
        );

        Booking booking =
                account.getBookings().get(0);

        assertEquals(
                5000,
                booking.amount()
        );

        assertEquals(
                "Lohn",
                booking.text()
        );
    }

    @Test
    @DisplayName(
            "Withdraw creates a negative booking"
    )
    public void testWithdraw() throws Exception {

        Account account =
                new SavingsAccount("S-1000");

        account.deposit(
                Scheduled.now(),
                5000,
                "Einzahlung"
        );

        account.withdraw(
                Scheduled.now(),
                2000,
                "Einkauf"
        );

        assertEquals(
                3000,
                account.getBalance()
        );

        assertEquals(
                2,
                account.getBookings().size()
        );

        assertEquals(
                -2000,
                account.getBookings()
                        .get(1)
                        .amount()
        );

        assertEquals(
                "Einkauf",
                account.getBookings()
                        .get(1)
                        .text()
        );
    }

    @Test
    @DisplayName(
            "Balance equals sum of bookings"
    )
    public void testBalance() throws Exception {

        Account account =
                new SavingsAccount("S-1000");

        account.deposit(
                Scheduled.now(),
                5000,
                "Einzahlung 1"
        );

        account.deposit(
                Scheduled.now(),
                3000,
                "Einzahlung 2"
        );

        account.withdraw(
                Scheduled.now(),
                2000,
                "Auszahlung"
        );

        long sum =
                account.getBookings()
                        .stream()
                        .mapToLong(Booking::amount)
                        .sum();

        assertEquals(
                sum,
                account.getBalance()
        );

        assertEquals(
                6000,
                account.getBalance()
        );
    }

    @Test
    @DisplayName(
            "Booking list cannot be modified"
    )
    public void testBookingsAreReadOnly()
            throws Exception {

        Account account =
                new SavingsAccount("S-1000");

        account.deposit(
                Scheduled.now(),
                1000,
                "Einzahlung"
        );

        List<Booking> bookings =
                account.getBookings();

        assertThrows(
                UnsupportedOperationException.class,
                () -> bookings.add(
                        new Booking(
                                1000L,
                                500L,
                                "Test"
                        )
                )
        );
    }

    @Test
    @DisplayName(
            "Invalid amount creates no booking"
    )
    public void testInvalidAmount() {

        Account account =
                new SavingsAccount("S-1000");

        assertThrows(
                InvalidAmountException.class,
                () -> account.deposit(
                        Scheduled.now(),
                        0,
                        "Ungueltig"
                )
        );

        assertTrue(
                account.getBookings().isEmpty()
        );

        assertEquals(
                0,
                account.getBalance()
        );
    }

    @Test
    @DisplayName(
            "Past date creates no booking"
    )
    public void testInvalidDate() {

        Account account =
                new SavingsAccount("S-1000");

        Scheduled pastDate =
                Scheduled.now()
                        .minus(
                                1,
                                ChronoUnit.DAYS
                        );

        assertThrows(
                InvalidDateException.class,
                () -> account.deposit(
                        pastDate,
                        1000,
                        "Zu spaet"
                )
        );

        assertTrue(
                account.getBookings().isEmpty()
        );

        assertEquals(
                0,
                account.getBalance()
        );
    }

    @Test
    @DisplayName("Print account statement")
    public void testPrint() throws Exception {

        Account account =
                new SavingsAccount("S-1000");

        account.deposit(
                Scheduled.now(),
                5000,
                "Lohn"
        );

        account.withdraw(
                Scheduled.now(),
                2000,
                "Einkauf"
        );

        String statement =
                account.printStatement();

        assertTrue(
                statement.contains("S-1000")
        );

        assertTrue(
                statement.contains("Lohn")
        );

        assertTrue(
                statement.contains("Einkauf")
        );

        assertTrue(
                statement.contains(
                        "CHF 0.05000"
                )
        );

        assertTrue(
                statement.contains(
                        "CHF -0.02000"
                )
        );

        assertTrue(
                statement.contains(
                        "CHF 0.03000"
                )
        );
    }

    @Test
    @DisplayName(
            "Print monthly account statement"
    )
    public void testMonthlyPrint() {
        // Optionales Stretch Goal.
    }
}