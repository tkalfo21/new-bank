# UC-01 – Konto eröffnen

## Name
Konto eröffnen

## Kurzbeschreibung
Eine Kundin oder ein Kunde möchte ein Bankkonto eröffnen. Die Anwendung legt ein Sparkonto, ein Jugendsparkonto oder ein Lohnkonto an und vergibt eine eindeutige Kontonummer.

## Akteure
- Kundin / Kunde
- Bankmitarbeiter (fachlicher Ablauf; im Java-Code nicht als eigene Klasse implementiert)

## Auslöser
Die Kundin oder der Kunde beantragt ein Konto.

## Ergebnisse
Ein neues Konto mit eindeutiger Nummer und Anfangssaldo null wird angelegt.

## Vorbedingung
Die Kontoart ist festgelegt. Bei einem Lohnkonto ist eine gültige Kreditlimite bekannt.

## Nachbedingung
Das Konto kann anhand seiner Nummer abgerufen werden.

## Eingehende Daten
- Kontotyp (Sparkonto, Jugendsparkonto, Lohnkonto)
- Optional beim Lohnkonto: Kreditlimite (null oder negativ)

**Abgrenzung:** Name und Adresse der Kundin oder des Kunden werden in der aktuellen Java-Anwendung nicht gespeichert.

## Essenzieller Ablauf
1. Die Kundin oder der Kunde nennt den gewünschten Kontotyp.
2. Der Bankmitarbeiter veranlasst die Kontoeröffnung.
3. Die Anwendung ruft die passende Methode der `AccountFactory` auf.
4. Die Factory erzeugt das Konto und eine eindeutige Kontonummer.
5. Die Bank registriert das Konto.
6. Die Anwendung gibt die Kontonummer zurück.

## Alternativ- und Fehlerabläufe
- A1: Ein Lohnkonto wird mit einer positiven Kreditlimite angefordert. Die Anwendung lehnt die Eingabe ab.
- A2: Bei einer ungültigen Kontoart wird kein Konto angelegt.

## Offene Punkte
Kundenidentität, Legitimation und Vertragsunterzeichnung gehören zum realen Bankschalter, werden aber nicht umgesetzt.

## Änderungshistorie
- 14.09.2026: Erstellt
- 04.11.2026: An implementierte Kontotypen und Testfälle angepasst

## Sonstiges / Anmerkungen
Tests: `BankUseCaseTests.kontenEroeffnen()`, `FactoryContractTests`.
