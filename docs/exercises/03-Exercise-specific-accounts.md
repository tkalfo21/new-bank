# Übung 03 – Spezifische Kontotypen und Vererbung

**Sprint 3.** Ziel: Für verschiedene Bankprodukte eigene Kontounterklassen erstellen und ihre Regeln über Polymorphie anwenden. `Bank` soll ausschliesslich den abstrakten Typ `Account` verwenden.

## Ausgangslage
`Account` enthält allgemeine Funktionen und Validierungen. `SavingsAccount` verhindert Überzüge. Zwei weitere Produkte werden benötigt: `SalaryAccount` und `PromoYouthSavingsAccount`.

## Testgetriebenes Vorgehen
Schreibe Grenzwerttests zuerst. Ein zunächst nicht kompilierender Test ist erlaubt, wenn die geprüfte Klasse noch fehlt. Ergänze anschliessend schrittweise die Implementierung.

## Aufgaben

### 1. Kontotypen testen
Ergänze `SalaryAccountTests` und `SavingsAccountTests` sowie eine neue Datei `PromoYouthSavingsAccountTests`.

Erforderliche Grenzfälle:
- Sparkonto: genau das Guthaben auszahlen ist erlaubt; ein Millirappen mehr nicht.
- Lohnkonto: genau bis zur Kreditlimite auszahlen ist erlaubt; ein Millirappen darüber nicht.
- Lohnkonto mit Kreditlimite 0 verhält sich beim Überzug wie ein Sparkonto.
- Verbotene Auszahlungen erzeugen keine Buchung.
- Jugendkonto: eigene Sonderregel an ihrem Grenzwert prüfen.
- Polymorphie: drei Kontotypen als `List<Account>` behandeln und ihre Saldos summieren.

### 2. Lohnkonto
Implementiere `SalaryAccount extends Account` mit `SalaryAccount(String id, long creditLimit)`.

Die Kreditlimite ist **negativ oder null** und beschreibt den kleinsten erlaubten Saldo. Eine positive Limite wird mit `IllegalArgumentException` abgewiesen. Eine Auszahlung ist nur erlaubt, wenn der neue Saldo mindestens so hoch wie `creditLimit` bleibt. Ergänze einen Getter.

### 3. Jugendkonto
Entscheide, ob `PromoYouthSavingsAccount` direkt von `Account` oder von `SavingsAccount` erbt. Begründe die Wahl in einem Kommentar. Ergänze mindestens eine eigene Regel, zum Beispiel:
- Höchstens CHF 200 (20 Millionen Millirappen) pro Kalendermonat auszahlen.
- Einen zusätzlichen Bonuszins.
- Einen Mindestkontostand.

### 4. Gemeinsame abstrakte Methode
Definiere in `Account` eine abstrakte Methode, die alle Untertypen implementieren, beispielsweise `getAccountType()` oder `applyInterest(...)`.

### 5. Polymorphe Bank
`Bank` darf keine Typabfragen mit `instanceof` oder Umwandlungen in konkrete Konto-Unterklassen benötigen. Prüfe die Gesamtsumme und die fünf höchsten beziehungsweise niedrigsten Saldos auch bei einem überzogenen Lohnkonto.

## Diskussionsfragen
- Schränkt das Überzugsverbot das Liskovsche Substitutionsprinzip ein?
- Warum sind private Felder besser geschützt als `protected`-Felder?
- Wann ist Komposition mit Strategien sinnvoller als Vererbung?
- Was bringt eine abstrakte Basisklasse gegenüber einem Interface?

## Nicht Bestandteil
Factory, Singleton, HTTP, DTOs und Persistenz.

## Abnahmekriterien
- [ ] Beide zusätzlichen Kontounterklassen existieren.
- [ ] Kreditlimite wird eingehalten und geprüft.
- [ ] Jugendkonto hat eine dokumentierte eigene Regel.
- [ ] Mindestens eine abstrakte Operation in `Account`.
- [ ] `Bank` bleibt unabhängig von konkreten Kontotypen.
- [ ] Alle Grenzwerttests bestehen.

## Freiwillige Zusatzaufgaben
`sealed`-Hierarchie; monatliche Kontogebühr; hilfreiche `toString()`-Ausgaben.
