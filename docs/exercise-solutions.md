# Bank – Lösungen und Architekturentscheidungen

## Exercise 02 – Buchungen
Buchungen speichern Timestamp in Unix-Millisekunden, Betrag in Millirappen und Text.
Der Saldo ergibt sich ausschliesslich aus der Summe der Buchungen. Die Historie ist unveränderlich.

## Exercise 03 – Vererbung
`Account` verlangt mit `getAccountType()` eine echte abstrakte Operation.
`SavingsAccount` verhindert Überzüge. `SalaryAccount` erlaubt negative Salden bis zur
nichtpositiven Kreditlimite. `PromoYouthSavingsAccount` erbt das Überzugsverbot und
begrenzt Auszahlungen auf 200 CHF (20 Millionen Millirappen) je UTC-Kalendermonat.
Ein Untertyp, der bestimmte Auszahlungen ablehnt, schränkt die Basisklasse ein;
deshalb müssen Geschäftsregeln klar Teil des Account-Vertrags sein.
Private Felder schützen Invarianten. Komposition/Strategien würden bei vielen
variablen Regeln besser skalieren als immer neue Unterklassen.

## Exercise 04 – Factory
Ein `AccountType` verbindet Typ und Präfix; ein gemeinsamer `AtomicLong`-Zähler
vergibt IDs. Die `AccountFactory` ist eine *Simple Factory*, nicht das GoF
Factory-Method-Muster. Benannte Methoden passen besser als ein pauschaler
Parameter `creditLimit`, der für Sparkonten gar keinen Sinn hat.
Die Factory vermeidet konkrete Kontotypen in `Bank`.
In Produktion benötigt der ID-Zähler persistente, transaktionssichere Vergabe;
ein In-Memory-Zähler allein reicht nach Neustarts nicht.

## Exercise 05 – Singleton
* Eager: threadsicher, nicht lazy, keine Zugriffssynchronisation.
* Lazy synchronized: threadsicher und lazy, Synchronisationskosten bei jedem Zugriff.
* Holder: threadsicher durch Java-Klasseninitialisierung, lazy, keine Sperre pro Aufruf.
Gewählt: Holder. Ohne `volatile` kann Double-Checked Locking wegen Reordering
und fehlender Sichtbarkeit kaputte Zwischenzustände veröffentlichen.
Singleton ist nicht dasselbe wie eine Utility-Klasse; das Objekt besitzt Zustand.
Sein Zustand ist aber global, versteckt Abhängigkeiten und erschwert parallele Tests.
Das öffentliche `resetForTests()` ist nur ein Test-Workaround, ausdrücklich ein Code Smell.
Über einen Factory-Konstruktor mit Paketsichtbarkeit bleibt Dependency Injection
für Tests/Kompositionswurzel möglich. Pro JVM existiert eine Instanz;
mehrere Server-JVMs besitzen mehrere Instanzen.
Ein Spring-Singleton gehört dagegen zu *einem ApplicationContext*
und kann durch Constructor Injection einfach ausgetauscht werden.
Eine Enum-Singleton-Variante ist serien-/reflexionsrobuster, aber weniger flexibel.

## Backend (Spring Boot)
`BankController` ist der HTTP-Adapter, `BankService` die Service-Schicht.
DTOs statt Domain-Objekte werden als JSON ausgegeben.
Zeitstempel sind ISO-8601; ohne Datum gilt jetzt.
CORS erlaubt http://localhost:5173.
401/403 werden ohne Authentifizierung nicht verwendet; unbekanntes Konto
liefert 404, ungültige Beträge und Datumsangaben 400.
Der Backend-Stand nutzt In-Memory-Konten; die H2/JPA-Persistierung
und ein Frontend sind separate Ausbaustufen.


## Sprints 9–12 – Persistenz, Validierung, Tests, Architektur
Konten und Buchungen werden als JPA-Entities `StoredAccount` und `StoredBooking`
mit einer One-to-Many-Beziehung in H2 persistiert. Beim Booten rekonstruiert
`BankService` die Domänenkonten und Buchungshistorien. Die `AccountFactory`
setzt den ID-Zähler mindestens auf die höchste wiederhergestellte ID plus eins.
`@Transactional` schützt die JDBC-Transaktionen; ausserhalb der Datenbank
bleibt der Singleton ein Legacy-Domainmodell und kein Mehrinstanz-Produktionssystem.

Eingaben werden mittels `@Valid`, `@Positive` und `@Max(0)` geprüft.
Der Exception-Handler übersetzt ungültige Eingaben in HTTP 400 und fehlende Konten
in HTTP 404. Der Controller wird mit MockMvc auf HTTP-Ebene getestet.

Spring verwendet Constructor Injection und Singleton-Beans pro ApplicationContext.
Damit ist der Scope klar, Tests können einen eigenen Kontext aufbauen und Abhängigkeiten
lassen sich leichter ersetzen. Ein statisches Singleton wie `Bank.getInstance()`
hängt dagegen global an der JVM und erschwert Austausch und Tests.
Die `AccountFactory` erzeugt fachliche Konten, während Spring die Infrastruktur
(`BankService`, Repository, Controller) verwaltet: Beide Verantwortlichkeiten ergänzen sich.
