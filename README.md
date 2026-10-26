# Bankprojekt – Modul 320: Objektorientiertes Design

Dieses Projekt enthält die Umsetzung einer Bankanwendung mit Java 21 und Spring Boot. Die Übungen werden in aufeinander aufbauenden Sprints bearbeitet. Die ursprünglichen Anforderungen und Übungsaufgaben findest du unter `docs/exercises/` und `docs/scenarios/`.

## Funktionen
- Sparkonto ohne Überzug, Lohnkonto mit Kreditlimite und Jugendkonto mit monatlicher Auszahlungslimite
- Einzahlungen, Auszahlungen, Saldo und Buchungshistorie
- Factory zur Erzeugung der Konten mit eindeutigen Kontonummern
- Singleton für das Bank-Domänenmodell
- REST-API mit Service-Schicht, DTOs und Fehlerbehandlung
- Dauerhafte Speicherung von Konten und Buchungen mit Spring Data JPA und H2
- Automatisierte Tests über Maven und GitHub Actions

## Voraussetzungen und Start
Voraussetzung: **JDK 21**. Maven wird durch den mitgelieferten Maven-Wrapper bereitgestellt.

Unter Windows (PowerShell):
```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Unter Linux und macOS:
```bash
./mvnw test
./mvnw spring-boot:run
```

Die Anwendung ist unter `http://localhost:8080` erreichbar. Den Zustand des Servers überprüfst du über `http://localhost:8080/actuator/health`. Bei erfolgreichem Start liefert die Schnittstelle den Status `UP`.

## REST-Schnittstellen

| Methode | Pfad | Bedeutung |
|---|---|---|
| POST | `/accounts/savings` | Sparkonto eröffnen |
| POST | `/accounts/promo-youth` | Jugendsparkonto eröffnen |
| POST | `/accounts/salary` | Lohnkonto mit `creditLimit` eröffnen |
| GET | `/accounts/{id}` | Kontodaten abrufen |
| GET | `/accounts/{id}/balance` | Kontostand abrufen |
| POST | `/accounts/{id}/deposit` | Einzahlung buchen |
| POST | `/accounts/{id}/withdraw` | Auszahlung buchen |
| GET | `/accounts/{id}/bookings` | Buchungen abrufen |
| GET | `/bank/balance` | Gesamtsaldo der Bank |
| GET | `/bank/top5/highest` | Fünf höchste Kontostände |
| GET | `/bank/top5/lowest` | Fünf niedrigste Kontostände |

Beispiel für eine Einzahlung:
```json
{"amount":5000,"text":"Einzahlung"}
```

**Beträge werden in Millirappen gespeichert:** `100_000` entspricht CHF 1.00. Buchungen enthalten einen Unix-Zeitstempel in Millisekunden. Der optionale Datumswert über HTTP ist ein ISO-8601-Zeitstempel; ohne Angabe verwendet die Anwendung den aktuellen Zeitpunkt. Ungültige Eingaben liefern HTTP 400, unbekannte Konten HTTP 404. Für die lokale Frontend-Entwicklung ist CORS für `http://localhost:5173` zugelassen.

## Aufbau des Projekts
- `src/main/java/ch/bbw/accounts/` – Kontotypen und Geschäftsregeln
- `src/main/java/ch/bbw/persistence/` – Datenbank-Entitäten und Repository
- `src/main/java/ch/bbw/web/` – Controller, Service, DTOs und Fehlerbehandlung
- `src/test/java/` – automatisierte Tests
- `docs/exercises/` – Aufgaben der Sprints
- `docs/scenarios/` – ursprüngliche Szenarien als PDF
- `docs/use-cases/` – Anwendungsfälle
- `docs/exercise-solutions.md` – Lösungen und Architekturentscheidungen
- `TODO.md` – Bearbeitungsstand

## Reihenfolge der Sprints
1. Grundfunktionen: Konten eröffnen, ein- und auszahlen
2. Buchungen und abgeleiteter Kontostand
3. Kontounterklassen und Vererbung
4. Factory-Muster und Kontonummern
5. Singleton-Muster
6. Spring-Boot-Grundstruktur
7. REST-Schnittstelle für Konten
8. REST-Schnittstelle für Buchungen
9. Persistenz mit JPA
10. Validierung und Fehlerbehandlung
11. API- und Integrationstests
12. Architekturvergleich: klassische Entwurfsmuster und Spring

Die Aufgabe `10-Exercise-build-the-be.md` behandelt den Backend-Ausbau (historisch als «Exercise 01» bezeichnet).

## Vom Szenario zum Programm
1. Lies die jeweilige Szenario-PDF unter `docs/scenarios/`.
2. Erstelle daraus Anwendungsfälle und ein Klassendiagramm.
3. Vergleiche deine Lösung mit der Übungsbeschreibung.
4. Schreibe Tests, implementiere die Funktion und führe die Tests erneut aus.

| Szenario | Inhalt |
|---|---|
| `01_UML_Bank2.pdf` | Konto eröffnen, einzahlen, auszahlen, Kontoauszug |
| `03_UML_Bank3.pdf` | Buchungen mit Datum und Betrag |
| `04_UML_Bank4a.pdf` | Unterschiedliche Kontotypen |
| `04_UML_Bank4b.pdf` | Kreditlimite und Sonderregeln |
| `04_UML_Bank4c.pdf` | Automatisch erzeugte Kontonummern |
| `05_UML_Bank5.pdf` | Konten nach Saldo sortieren |
| `06_UML_Bank6.pdf` | Kontoerstellung über die Factory |
| `07_UML_Bank7.pdf` | Eine gemeinsame Bankinstanz |

## Technologien
Java 21, Spring Boot 4.1, Maven, Spring Web MVC, Spring Data JPA, H2, JUnit 5 und GitHub Actions.

Die H2-Datenbank wird lokal unter `data/` gespeichert; dieser Ordner ist nicht Teil des Git-Repositories. Die Anwendung ist ein Lernprojekt, keine produktionsreife Banksoftware. Persistenz, Authentisierung, Nebenläufigkeit und Transaktionssicherheit müssten für einen echten Bankbetrieb weiter abgesichert werden.
