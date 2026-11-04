package ch.bbw;

import ch.bbw.accounts.Account;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Fachliche Tests der Anwendungsfälle UC-01 bis UC-04. */
class BankUseCaseTests {
    private Bank bank;

    @BeforeEach void vorbereiten() {
        bank = new Bank(new AccountFactory(2000));
    }

    @Test @DisplayName("UC-01: Drei Kontotypen mit eindeutigen Kontonummern eröffnen")
    void kontenEroeffnen() throws Exception {
        String spar = bank.createSavingsAccount();
        String jugend = bank.createPromoYouthSavingsAccount();
        String lohn = bank.createSalaryAccount(-500);
        assertAll(
            () -> assertTrue(spar.startsWith("S-")),
            () -> assertTrue(jugend.startsWith("Y-")),
            () -> assertTrue(lohn.startsWith("P-")),
            () -> assertNotEquals(spar, jugend),
            () -> assertNotEquals(spar, lohn),
            () -> assertNotEquals(jugend, lohn),
            () -> assertEquals(0, bank.getBalance())
        );
    }

    @Test @DisplayName("UC-02: Zwei Einzahlungen mit Text ergeben zwei positive Buchungen")
    void einzahlung() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 10000, "Lohn");
        bank.deposit(id, Scheduled.now(), 2500, "Geschenk");
        Account konto = bank.getAccount(id);
        assertEquals(12500, konto.getBalance());
        assertEquals(2, konto.getBookings().size());
        assertEquals("Lohn", konto.getBookings().get(0).text());
        assertEquals(2500, konto.getBookings().get(1).amount());
    }

    @Test @DisplayName("UC-02: Ungültige Einzahlung verändert weder Buchungen noch Saldo")
    void fehlerhafteEinzahlung() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 1000);
        assertThrows(InvalidAmountException.class, () -> bank.deposit(id, Scheduled.now(), 0));
        assertThrows(InvalidAmountException.class, () -> bank.deposit(id, Scheduled.now(), -1));
        assertThrows(InvalidDateException.class, () -> bank.deposit(id, new Scheduled(-60000), 100));
        assertEquals(1000, bank.getBalance(id));
        assertEquals(1, bank.getAccount(id).getBookings().size());
    }

    @Test @DisplayName("UC-03: Genau das Guthaben abheben ist erlaubt; ein Überzug nicht")
    void auszahlung() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 5000);
        assertEquals(0, bank.withdraw(id, Scheduled.now(), 5000));
        assertThrows(InvalidAmountException.class, () -> bank.withdraw(id, Scheduled.now(), 1));
        assertEquals(0, bank.getBalance(id));
        assertEquals(2, bank.getAccount(id).getBookings().size());
    }

    @Test @DisplayName("UC-04: Kontoauszug zeigt Nummer, Buchungstexte und korrekten Saldo")
    void kontoauszug() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 10000, "Lohn");
        bank.withdraw(id, Scheduled.now(), 3000, "Einkauf");
        String auszug = bank.getAccount(id).printStatement();
        assertAll(
            () -> assertTrue(auszug.contains(id)),
            () -> assertTrue(auszug.contains("Lohn")),
            () -> assertTrue(auszug.contains("Einkauf")),
            () -> assertTrue(auszug.contains("CHF 0.10000")),
            () -> assertTrue(auszug.contains("CHF -0.03000")),
            () -> assertTrue(auszug.contains("Saldo: CHF 0.07000"))
        );
    }

    @Test @DisplayName("Ungültige Kontonummer wird bei Lesen, Einzahlen und Auszahlen abgewiesen")
    void unbekanntesKonto() {
        assertThrows(InvalidAmountException.class, () -> bank.getAccount("S-unbekannt"));
        assertThrows(InvalidAmountException.class, () -> bank.deposit("S-unbekannt", Scheduled.now(), 100));
        assertThrows(InvalidAmountException.class, () -> bank.withdraw("S-unbekannt", Scheduled.now(), 100));
    }

    @Test @DisplayName("Die fünf höchsten und fünf niedrigsten Salden sind korrekt sortiert")
    void topFuenf() throws Exception {
        for (int n = 1; n <= 6; n++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, Scheduled.now(), n * 1000L);
        }
        List<Account> hoch = bank.top5HighestBalances();
        List<Account> tief = bank.top5LowestBalances();
        assertEquals(5, hoch.size());
        assertEquals(5, tief.size());
        assertEquals(6000, hoch.get(0).getBalance());
        assertEquals(2000, hoch.get(4).getBalance());
        assertEquals(1000, tief.get(0).getBalance());
        assertEquals(5000, tief.get(4).getBalance());
    }

    @Test @DisplayName("Bei weniger als fünf Konten werden nur vorhandene Konten angezeigt")
    void kleineRangliste() {
        bank.createSavingsAccount();
        bank.createSavingsAccount();
        assertEquals(2, bank.top5HighestBalances().size());
        assertEquals(2, bank.top5LowestBalances().size());
    }
}
