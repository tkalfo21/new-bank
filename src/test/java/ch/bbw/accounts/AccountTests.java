package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("Monatsauszug zeigt nur Buchungen des gewählten Monats")
    public void testMonthlyPrint() {
        Account konto = new SavingsAccount("S-1000");
        java.time.YearMonth monat = java.time.YearMonth.now(java.time.ZoneOffset.UTC);
        long ersterTag = monat.atDay(1).atStartOfDay()
                .toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
        long naechsterMonat = monat.plusMonths(1).atDay(1).atStartOfDay()
                .toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
        konto.restoreBooking(new Booking(ersterTag, 3000, "Aktueller Monat"));
        konto.restoreBooking(new Booking(naechsterMonat, 7000, "Anderer Monat"));

        String auszug = konto.printStatement(monat.getYear(), monat.getMonthValue());
        assertTrue(auszug.contains("Aktueller Monat"));
        assertFalse(auszug.contains("Anderer Monat"));
        assertTrue(auszug.contains("Monatssaldo: CHF 0.03000"));
        assertEquals(10000, konto.getBalance());
    }

@Test @DisplayName("Buchung benötigt einen gültigen Text")
    void buchungstext() {
        assertThrows(IllegalArgumentException.class, () -> new Booking(0, 1, null));
        assertThrows(IllegalArgumentException.class, () -> new Booking(0, 1, " "));
        assertEquals("Lohn", new Booking(0, 1, "Lohn").text());
    }

    @Test @DisplayName("Buchungsliste kann nicht von aussen verändert werden")
    void nichtVeraenderbareListe() throws Exception {
        SavingsAccount konto = new SavingsAccount("S-2000");
        konto.deposit(Scheduled.now(), 500);
        assertThrows(UnsupportedOperationException.class,
            () -> konto.getBookings().add(new Booking(0, 1, "Manipulation")));
        assertEquals(500, konto.getBalance());
        assertEquals(1, konto.getBookings().size());
    }

    @Test @DisplayName("Abgelehnte Auszahlung verändert den Kontostand nicht")
    void verboteneAuszahlung() throws Exception {
        SavingsAccount konto = new SavingsAccount("S-2001");
        konto.deposit(Scheduled.now(), 1000);
        assertThrows(InvalidAmountException.class, () -> konto.withdraw(Scheduled.now(), 1001));
        assertThrows(InvalidAmountException.class, () -> konto.withdraw(Scheduled.now(), 0));
        assertThrows(InvalidDateException.class, () -> konto.withdraw(new Scheduled(-60000), 100));
        assertEquals(1000, konto.getBalance());
        assertEquals(1, konto.getBookings().size());
    }

    @Test @DisplayName("Lohnkonto darf exakt bis zur Kreditlimite ins Minus gehen")
    void lohnkontoGrenze() throws Exception {
        SalaryAccount konto = new SalaryAccount("P-2000", -500);
        assertEquals(-500, konto.withdraw(Scheduled.now(), 500));
        assertThrows(InvalidAmountException.class, () -> konto.withdraw(Scheduled.now(), 1));
        assertEquals(-500, konto.getBalance());
        assertEquals(1, konto.getBookings().size());
    }

    @Test @DisplayName("Kreditlimite null erlaubt keinen Überzug")
    void lohnkontoOhneKredit() throws Exception {
        SalaryAccount konto = new SalaryAccount("P-2001", 0);
        konto.deposit(Scheduled.now(), 500);
        assertEquals(0, konto.withdraw(Scheduled.now(), 500));
        assertThrows(InvalidAmountException.class, () -> konto.withdraw(Scheduled.now(), 1));
        assertThrows(IllegalArgumentException.class, () -> new SalaryAccount("P-2002", 1));
    }

    @Test @DisplayName("Jugendkonto: Genau CHF 200 erlaubt, darüber abgewiesen")
    void jugendkontoMonatsgrenze() throws Exception {
        PromoYouthSavingsAccount konto = new PromoYouthSavingsAccount("Y-2000");
        konto.deposit(Scheduled.now(), 50000000);
        konto.withdraw(Scheduled.now(), 10000000);
        konto.withdraw(Scheduled.now(), 10000000);
        assertThrows(InvalidAmountException.class, () -> konto.withdraw(Scheduled.now(), 1));
        assertEquals(30000000, konto.getBalance());
        assertEquals(3, konto.getBookings().size());
    }

    @Test @DisplayName("Jugendkonto: Im Folgemonat beginnt die Auszahlungslimite erneut")
    void neuerMonat() throws Exception {
        PromoYouthSavingsAccount konto = new PromoYouthSavingsAccount("Y-2001");
        konto.deposit(Scheduled.now(), 60000000);
        konto.withdraw(Scheduled.now(), 20000000);
        assertEquals(20000000,
            konto.withdraw(new Scheduled(40L * 24 * 60 * 60 * 1000), 20000000));
    }

    @Test @DisplayName("Leeres Konto zeigt einen Kontoauszug mit Saldo null")
    void leererAuszug() {
        SavingsAccount konto = new SavingsAccount("S-2002");
        assertTrue(konto.printStatement().contains("Saldo: CHF 0.00000"));
    }
}
