# UC-02 – Einzahlung tätigen

## Name
Einzahlung tätigen

## Kurzbeschreibung
Ein positiver Geldbetrag wird auf ein bestehendes Konto eingezahlt und als Buchung gespeichert.

## Akteure
- Kundin / Kunde
- Bankmitarbeiter (fachliche Rolle)

## Auslöser
Die Kundin oder der Kunde möchte Geld einzahlen.

## Ergebnisse
Der Kontostand steigt um den eingezahlten Betrag.

## Vorbedingung
Das angegebene Konto existiert.

## Nachbedingung
Eine neue positive Buchung mit Datum, Betrag und Text ist vorhanden. Der Saldo entspricht der Summe aller Buchungen.

## Eingehende Daten
- Kontonummer
- Einzahlungsbetrag in Millirappen (grösser als null)
- Datum beziehungsweise aktueller Zeitpunkt
- Buchungstext

## Essenzieller Ablauf
1. Die Kontonummer wird angegeben.
2. Der Einzahlungsbetrag und gegebenenfalls ein Buchungstext werden erfasst.
3. Die Anwendung prüft Konto, Datum und Betrag.
4. Die Einzahlung wird als positive Buchung gespeichert.
5. Der neue Kontostand wird ausgegeben.

## Alternativ- und Fehlerabläufe
- A1: Der Betrag ist null oder negativ. Die Einzahlung wird abgelehnt.
- A2: Das Konto existiert nicht. Die Anwendung meldet einen Fehler.
- A3: Das Buchungsdatum liegt in der Vergangenheit. Es erfolgt keine Buchung.
- Bei jedem Fehler bleiben Saldo und Buchungsliste unverändert.

## Offene Punkte
Eine reale Bargeldübergabe oder Zahlungsabwicklung ist nicht Teil der Java-Anwendung.

## Änderungshistorie
- 14.09.2026: Erstellt
- 04.11.2026: Grenz- und Fehlerfälle ergänzt

## Sonstiges / Anmerkungen
Tests: `BankUseCaseTests.einzahlung()`, `BankUseCaseTests.fehlerhafteEinzahlung()`.
