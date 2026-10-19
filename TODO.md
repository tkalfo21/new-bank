# Fortschritt – new-bank

Stand: 19.10.2026. Die Änderungen liegen im Branch `complete-bank-exercises`.

## Sprint 1 – Basis
- [x] Kontoerstellung, Ein- und Auszahlung, Saldo.

## Sprint 2 – Buchungen
- [x] `Booking(date, amount, text)` und unveränderliche Buchungshistorie.
- [x] Kontostand aus Buchungssumme.
- [x] Alte und neue Ein-/Auszahlungs-Signaturen.
- [x] Kontoauszug, Unix-Millisekunden und Tests.
- [ ] Optional: Monats-Kontoauszug als separate Funktion.

## Sprint 3 – Kontounterklassen
- [x] Sparkonto ohne Überzug.
- [x] Lohnkonto mit negativer Kreditlimite.
- [x] Jugendkonto mit monatlicher Auszahlungslimite von CHF 200.
- [x] Abstrakte Methode `getAccountType()`.
- [x] Vererbungs-, Grenzwert- und Polymorphietests.

## Sprint 4 – Factory
- [x] `AccountType` definiert Kontotypen und Präfixe.
- [x] `AccountFactory` erzeugt korrekte Unterklassen.
- [x] Thread-sicherer `AtomicLong`-ID-Zähler.
- [x] Eindeutigkeits-/Kreditlimiten-Tests.

## Sprint 5 – Singleton
- [x] Lazy Holder Singleton `Bank.getInstance()`.
- [x] Privater No-Arg-Konstruktor.
- [x] Factory-Injektion über Paketskonstruktor.
- [x] Test-Isolation mit ausdrücklich bezeichnetem Test-Workaround.
- [x] Variantenvergleich und Trade-offs dokumentiert.

## Backend – Exercise 01 (`docs/exercises/10-Exercise-build-the-be.md`)
- [x] Spring Boot und WebMVC.
- [x] REST-Endpunkte für Konten, Salden, Buchungen und Transaktionen.
- [x] Service-Schicht und DTOs.
- [x] Fehlerbehandlung und CORS.
- [x] HTTP-Integrationstest und automatischer GitHub-Actions-Test.
- [ ] Optional in dieser Übung: OpenAPI/Swagger und Bean Validation.

## Weitere Erweiterungen aus `docs/exercises/start.md`
- [ ] Dauerhafte Speicherung von Konten/Buchungen über JPA (Sprint 9).
- [ ] Erweiterte Bean Validation und persistenzbezogene Integrationstests (Sprints 10–11).
- [ ] Zusätzliches Architekturgespräch zu Spring-Bean-Scopes (Sprint 12; Grundlagen in `docs/exercise-solutions.md`).

### Testen

```bash
./mvnw test
./mvnw spring-boot:run
```

Auf Windows (PowerShell): `./mvnw.cmd test` bzw. `./mvnw.cmd spring-boot:run`.

Hinweis: In-Memory-Konten werden bei Neustart nicht gespeichert. Vor Produktivbetrieb
braucht die Bank unter anderem persistente Transaktionen, sichere Authentisierung und
transaktionssichere Konto-IDs.
