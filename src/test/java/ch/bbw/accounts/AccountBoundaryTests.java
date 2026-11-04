package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Grenzwerttests: Fehler dürfen keine ungewollten Buchungen erzeugen. */
class AccountBoundaryTests {
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
