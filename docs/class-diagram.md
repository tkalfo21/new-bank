# Klassendiagramm – Bank

## Beschreibung

Das Klassendiagramm wurde aus den Use Cases des Bank-Projekts abgeleitet.

Die wichtigsten Klassen sind:

### Kunde
Ein Kunde kann ein Konto eröffnen und einen Kontoauszug anfordern.

Attribute:
- name: String
- kundenNr: int

Methoden:
- kontoEroeffnen()
- kontoauszugAnfordern()

### Konto
Ein Konto besitzt eine Kontonummer und einen Saldo.

Attribute:
- kontoNr: int
- saldo: double

Methoden:
- einzahlen(betrag: double)
- auszahlen(betrag: double)
- kontoauszugErstellen()

### Bankmitarbeiter
Der Bankmitarbeiter bearbeitet die Vorgänge des Kunden am Bankschalter.

Attribute:
- name: String
- mitarbeiterNr: int

Methoden:
- kontoEroeffnen()
- einzahlungBuchen()
- auszahlungBuchen()

### Transaktion
Eine Transaktion beschreibt eine Einzahlung oder Auszahlung auf einem Konto.

Attribute:
- betrag: double
- datum: Date
- typ: String

Methoden:
- ausfuehren()

## Beziehungen

- Ein Kunde besitzt ein oder mehrere Konten.
- Ein Bankmitarbeiter bearbeitet Konten.
- Ein Konto besitzt mehrere Transaktionen.

## Klassendiagramm

Das Klassendiagramm wurde mit draw.io erstellt.
