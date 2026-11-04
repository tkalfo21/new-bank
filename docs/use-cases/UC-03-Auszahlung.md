# UC-03 – Auszahlung tätigen

## Name
Auszahlung tätigen

## Kurzbeschreibung
Eine Kundin oder ein Kunde hebt Geld von einem bestehenden Konto ab. Die Anwendung prüft die Regeln des jeweiligen Kontotyps.

## Akteure
- Kundin / Kunde
- Bankmitarbeiter (fachliche Rolle)

## Auslöser
Die Kundin oder der Kunde verlangt eine Auszahlung.

## Ergebnisse
Der Kontostand wird um den ausgezahlten Betrag vermindert und eine negative Buchung gespeichert.

## Vorbedingung
Das Konto existiert. Der gewünschte Betrag ist positiv.

## Nachbedingung
Bei Erfolg ist die Auszahlung als negative Buchung gespeichert. Bei Ablehnung bleiben Saldo und Buchungsliste unverändert.

## Eingehende Daten
- Kontonummer
- Auszahlungsbetrag in Millirappen
- Datum beziehungsweise aktueller Zeitpunkt
- Optionaler Buchungstext

## Essenzieller Ablauf
1. Die Kontonummer wird erfasst.
2. Die Kundin oder der Kunde nennt den Auszahlungsbetrag.
3. Die Anwendung prüft Betrag, Datum und kontospezifische Regeln.
4. Bei ausreichendem Guthaben oder erlaubtem Kredit wird die Auszahlung gebucht.
5. Der neue Kontostand wird angezeigt.

## Alternativ- und Fehlerabläufe
- A1: Sparkonto: Ein Überzug wird abgewiesen.
- A2: Lohnkonto: Die negative Kreditlimite darf nicht unterschritten werden.
- A3: Jugendsparkonto: Die Summe der Auszahlungen im UTC-Kalendermonat darf CHF 200 nicht überschreiten.
- A4: Betrag null/negativ, vergangenes Datum oder unbekannte Kontonummer: keine Buchung.

## Offene Punkte
Eine physische Auszahlung von Bargeld wird von der Java-Anwendung nicht durchgeführt.

## Änderungshistorie
- 16.09.2026: Erstellt
- 04.11.2026: Kontoregeln und Fehlerabläufe ergänzt

## Sonstiges / Anmerkungen
Tests: `BankUseCaseTests.auszahlung()`, `AccountBoundaryTests`.
