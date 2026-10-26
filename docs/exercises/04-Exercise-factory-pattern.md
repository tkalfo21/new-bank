# Übung 04 – Factory-Muster

**Sprint 4.** Ziel: Die `AccountFactory` entscheidet zentral, welcher konkrete Kontotyp erzeugt wird, und vergibt eindeutige Kontonummern. `Bank` kennt nur den abstrakten Kontotyp.

## Ausgangslage
Die Bank bietet drei Produkte: Sparkonto (`S-`), Jugendsparkonto (`Y-`) und Lohnkonto (`P-`). Die Factory muss tatsächlich die passenden Unterklassen erzeugen und beim Lohnkonto die Kreditlimite weiterreichen.

## Aufgaben

### 1. Tests zuerst
Prüfe mit `AccountFactoryTests`:
- Alle Erzeugungsmethoden liefern die richtigen konkreten Kontotypen.
- Die Kontonummern beginnen mit dem passenden Präfix.
- Bei mindestens 1000 Erzeugungen sind alle IDs eindeutig.
- `createSalaryAccount(-50_000)` übernimmt die Limite.
- `createSalaryAccount(50_000)` wird abgewiesen.
- `BankTests.testCreate()` erzeugt zwei unterschiedliche, auffindbare Sparkonten.

### 2. Richtige Untertypen erzeugen
Verwende in der Factory `SavingsAccount`, `PromoYouthSavingsAccount` und `SalaryAccount` mit ihren passenden Konstruktoren. `Bank` soll dafür nicht geändert werden müssen.

### 3. `AccountType` einführen
Definiere die Kontotypen und Präfixe gemeinsam:
```java
public enum AccountType {
    SAVINGS("S"), PROMO_YOUTH("Y"), SALARY("P");
}
```

### 4. Gemeinsame Erzeugungsmethode
Ergänze `create(AccountType type, long creditLimit)` mit einer `switch`-Auswahl. Entscheide, ob die Bank besser die drei benannten Methoden oder diese allgemeine Methode verwenden soll. Beachte, dass die Kreditlimite nur für Lohnkonten sinnvoll ist.

Dieses Vorgehen ist eine **Simple Factory**. Das klassische GoF-Factory-Method-Muster würde unterschiedliche Creator-Unterklassen einsetzen.

### 5. Eindeutige IDs
Ein normaler hochzählender `long` ist bei parallelen Aufrufen nicht sicher. Verwende beispielsweise `AtomicLong` oder dokumentiere ausdrücklich eine Ein-Thread-Annahme. Entscheide, ob die ID-Folge über alle Kontotypen hinweg oder getrennt läuft.

### 6. Konkrete Kontoerzeugung zentralisieren
`new SavingsAccount(...)`, `new SalaryAccount(...)` und `new PromoYouthSavingsAccount(...)` gehören im Anwendungscode ausschliesslich in die Factory. Tests dürfen konkrete Klassen direkt erzeugen.

## Diskussionsfragen
- Welchen Vorteil bringt die Factory gegenüber drei Konstruktoraufrufen direkt in `Bank`?
- Warum kann die Factory den abstrakten Typ `Account` zurückgeben?
- Wo sollte die Erzeugung eindeutiger IDs bei einer persistenten Anwendung stattfinden?
- Ergänzt eine Spring-`@Service`-Bean die Factory oder ersetzt sie diese?

## Nicht Bestandteil
Singleton, HTTP und dauerhafte Datenhaltung.

## Abnahmekriterien
- [ ] Alle Erzeugungsmethoden liefern den richtigen Kontotyp.
- [ ] Die Kreditlimite wird korrekt übergeben.
- [ ] `AccountType` verwaltet Typ und Präfix.
- [ ] IDs werden nebenläufigkeitssicher vergeben oder die Einschränkung ist dokumentiert.
- [ ] `Bank` erzeugt keine konkreten Konten.
- [ ] Tests bestehen.

## Freiwillige Zusatzaufgaben
Ein vierter Kontotyp; konfigurierbarer Startwert des Zählers; eine Registry aus Erzeugungsfunktionen.
