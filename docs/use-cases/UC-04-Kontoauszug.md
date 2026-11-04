# UC-04 – Kontoauszug erhalten

## Name
Kontoauszug erhalten

## Kurzbeschreibung
Die Kundin oder der Kunde möchte einen Überblick über die Buchungen und den aktuellen Kontostand.

## Akteure
- Kundin / Kunde
- Bankmitarbeiter (fachliche Rolle)

## Auslöser
Ein Kontoauszug wird angefordert.

## Ergebnisse
Ein lesbarer Auszug mit Kontonummer, Buchungsdatum, Buchungsbetrag, Buchungstext und Endsaldo wird erstellt.

## Vorbedingung
Das Konto existiert.

## Nachbedingung
Der Auszug kann angezeigt werden. Das Konto und seine Buchungen bleiben unverändert.

## Eingehende Daten
- Kontonummer

## Essenzieller Ablauf
1. Die Kundin oder der Kunde verlangt einen Kontoauszug.
2. Die Kontonummer wird erfasst.
3. Die Anwendung ruft die Buchungshistorie ab.
4. Die Methode `Account.printStatement()` formatiert alle Buchungen.
5. Die Anwendung gibt die Textdarstellung inklusive Saldo zurück.

## Alternativ- und Fehlerabläufe
- A1: Ein neues Konto besitzt noch keine Buchungen. Der Auszug zeigt den Saldo null.
- A2: Die Kontonummer ist unbekannt. Ein Fehler wird ausgegeben.
- A3: Die Buchungsliste kann von aussen nicht verändert werden.

## Offene Punkte
Ein physischer Ausdruck ist nicht implementiert. Ein nach Monat gefilterter Auszug ist derzeit noch eine freiwillige Erweiterung.

## Änderungshistorie
- 16.09.2026: Erstellt
- 04.11.2026: Ausgabe- und Fehlerfälle präzisiert

## Sonstiges / Anmerkungen
Tests: `BankUseCaseTests.kontoauszug()`, `AccountBoundaryTests.leererAuszug()`.
