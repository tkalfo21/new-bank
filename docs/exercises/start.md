# Einstieg in die Bankübungen

Dieses Projekt dient zum Üben von objektorientiertem Design anhand einer Bankanwendung. Jeder Sprint ergänzt einen Teil des Systems.

**Vorgehen:** Lies vor einem Sprint das passende Szenario unter [`docs/scenarios/`](../scenarios/01_UML_Bank2.pdf). Leite selbst die Anwendungsfälle ab und zeichne bei Bedarf ein Klassendiagramm. Vergleiche deine Überlegungen danach mit der Aufgabenbeschreibung.

**Testgetriebene Entwicklung (TDD):** Schreibe zuerst einen Test für das erwartete Verhalten. Führe `./mvnw test` aus und überprüfe, dass der Test zunächst aus dem richtigen Grund fehlschlägt. Implementiere anschliessend nur die benötigte Funktion, bis der Test besteht. Wiederhole dies für weitere Anforderungen.

## Vorgesehene Reihenfolge

1. **Sprint 1 – Grundlagen:** `Bank` und `Account`; Konto eröffnen, einzahlen, auszahlen und Saldo abfragen.
2. **Sprint 2 – Buchungen:** `Booking` mit Datum, Betrag und Text; Saldo aus den Buchungen berechnen.
3. **Sprint 3 – Kontotypen:** Sparkonto, Lohnkonto, Jugendsparkonto; Regeln über Vererbung und Polymorphie.
4. **Sprint 4 – Factory:** Die Erzeugung der Konten und Kontonummern zentralisieren.
5. **Sprint 5 – Singleton:** Eine gemeinsame Bankinstanz und ihre Vor- und Nachteile betrachten.
6. **Sprint 6 – Spring Boot:** Backend-Struktur mit Domänenmodell, Service und Controller.
7. **Sprint 7 – REST für Konten:** Kontoeröffnung, Ein- und Auszahlungen und Saldos anbieten.
8. **Sprint 8 – REST für Buchungen:** Buchungshistorie über HTTP verfügbar machen.
9. **Sprint 9 – Persistenz:** Konten und Buchungen mit Spring Data JPA dauerhaft speichern.
10. **Sprint 10 – Validierung:** HTTP-Anfragen prüfen und verständliche Fehlerantworten liefern.
11. **Sprint 11 – Tests:** Service- und Controller-Tests sowie Integrationstests ergänzen.
12. **Sprint 12 – Architektur:** Factory, Singleton, Dependency Injection und Spring-Bean-Scopes vergleichen.

Der aktuelle Umsetzungsstand steht in [`TODO.md`](../../TODO.md). Die Designentscheidungen sind in [`docs/exercise-solutions.md`](../exercise-solutions.md) dokumentiert.
