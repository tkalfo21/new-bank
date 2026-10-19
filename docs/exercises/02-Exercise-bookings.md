# Übung 02 – Buchungen

**Sprint 2.** Ziel: `Booking` als eigenständigen Bestandteil des Bankmodells verwenden. Jede Buchung erhält einen Text, die Buchungshistorie wird lesbar und der Saldo wird daraus berechnet.

## Ausgangslage
`Account` verarbeitet Ein- und Auszahlungen. Auszahlungen werden als negative Beträge gespeichert. Ein separater Saldo wäre eine zweite Wahrheitsquelle und kann von der Buchungsliste abweichen. Zudem müssen die Zeitangaben einheitlich sein.

Lies vor Beginn `Booking.java`, `Account.java` und `Scheduled.java`.

## Vorgehen nach TDD
1. Schreibe zuerst einen Test für genau eine Regel.
2. Führe den Test aus und kontrolliere, dass er zunächst fehlschlägt.
3. Implementiere die kleinste passende Änderung.
4. Führe alle Tests erneut aus und überarbeite den Code bei Bedarf.

## Aufgaben

### 1. Tests
Ergänze `BookingTests.testInitialization()`, `BookingTests.testPrint()`, `AccountTests.testDeposit()`, `testWithdraw()`, `testPrint()` und `testMonthlyPrint()`.

Prüfe insbesondere:
- Neues Konto: keine Buchungen, Saldo 0.
- Einzahlung: genau eine positive Buchung mit richtigem Text.
- Auszahlung: negative Buchung.
- Saldo: Summe aller Buchungsbeträge.
- `getBookings()`: von aussen nicht veränderbar.
- Ungültiger Betrag oder vergangenes Datum: Ausnahme, keine zusätzliche Buchung, Saldo unverändert.

### 2. Buchungen erweitern
Verwende `Booking(long date, long amount, String text)`. Entferne bei einem Java-`record` unnötige selbstgeschriebene Getter. Entscheide, ob `null` und leere Texte abgewiesen oder durch Standardtexte ersetzt werden; dokumentiere die Wahl.

### 3. Buchungstexte durchreichen
`Account.deposit(...)`, `withdraw(...)` und die passenden Methoden in `Bank` akzeptieren einen Text. Bestehende Signaturen bleiben als Überladungen mit einem Standardtext erhalten.

### 4. Buchungshistorie bereitstellen
Ergänze `List<Booking> getBookings()`. Gib eine unveränderliche Ansicht beziehungsweise Kopie zurück, damit Aufrufer keine Buchungen direkt hinzufügen können.

### 5. Saldo aus Buchungen berechnen
Entferne das separate Feld `balance`. Beispiel:
```java
public long getBalance() {
    return bookings.stream().mapToLong(Booking::amount).sum();
}
```
Einzahlung und Auszahlung fügen zuerst eine Buchung hinzu und geben danach den neuen Saldo zurück. Prüfe den Überzugsschutz von `SavingsAccount`.

### 6. Zeitangaben vereinheitlichen
Bevorzugt wird ein Unix-Zeitstempel **in Millisekunden**. Alternativ wären Banktage seit dem 1.1.1970 denkbar. Implementierung und JavaDoc müssen dieselbe Bedeutung verwenden.

### 7. Kontoauszug ausgeben
Implementiere beispielsweise `printStatement()`: pro Buchung Datum, Betrag und Text sowie am Ende den Saldo. **Beträge sind Millirappen:** `123_456` entspricht CHF 1.23456.

## Diskussionsfragen
- Die Berechnung der Summe benötigt O(n). Wann wird das langsam? Wie könnten Zwischenspeicherung oder Snapshots helfen?
- Warum eignet sich ein unveränderliches `record` für Buchungen?
- Was schützt `final` bei einer Liste und was nicht?

## Nicht Bestandteil dieser Übung
Neue Kontotypen, Anpassungen an der Factory, HTTP und Datenbankpersistenz.

## Abnahmekriterien
- [ ] `Booking` enthält Datum, Betrag und Text.
- [ ] Kein separates Saldo-Feld; Saldo aus Buchungen.
- [ ] Unveränderliche Buchungshistorie.
- [ ] Einheitliche Datumssemantik.
- [ ] Lesbarer Kontoauszug.
- [ ] Die automatisierten Tests bestehen.

## Freiwillige Zusatzaufgaben
Monatsauszug `printStatement(int year, int month)`; Buchungstyp als Enum; Buchungsnummer zur Unterscheidung gleicher Buchungen.
