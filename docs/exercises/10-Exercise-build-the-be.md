# Übung 01 – Das Bank-Backend entwickeln

**Erweiterung nach Sprint 5, im Plan Sprints 6–11.**

## Ziel
Das vorhandene Bank-Domänenmodell wird über eine Spring-Boot-REST-API bereitgestellt. Ein separates Frontend kann damit Konten eröffnen, Einzahlungen und Auszahlungen buchen sowie Kontostände und Buchungen lesen. Das fachliche Domänenmodell soll dabei nicht unnötig neu geschrieben werden.

## Voraussetzungen
Java 21, Maven, Spring Boot und die vorhandenen Klassen `Bank`, `Account`, `AccountFactory`, `Booking` sowie `Scheduled`.

## Aufgaben

### 1. Web-Server aktivieren
Füge den passenden Spring-Boot-Web-Starter hinzu und starte mit `./mvnw spring-boot:run`. Die Anwendung soll auf `localhost:8080` antworten.

### 2. REST-API entwerfen
| Methode | Pfad | Beschreibung |
|---|---|---|
| POST | `/accounts/savings` | Sparkonto eröffnen |
| POST | `/accounts/promo-youth` | Jugendsparkonto eröffnen |
| POST | `/accounts/salary` | Lohnkonto mit Kreditlimite eröffnen |
| GET | `/accounts/{id}` | Kontodaten |
| GET | `/accounts/{id}/balance` | Kontostand |
| POST | `/accounts/{id}/deposit` | Einzahlung |
| POST | `/accounts/{id}/withdraw` | Auszahlung |
| GET | `/bank/balance` | Gesamtsaldo |
| GET | `/bank/top5/highest` | Fünf höchste Saldos |
| GET | `/bank/top5/lowest` | Fünf niedrigste Saldos |

Der Zeitstempel soll in HTTP-Anfragen als ISO-8601-Zeitstempel angenommen werden. Fehlt das Datum, kann der Server den aktuellen Zeitpunkt verwenden.

### 3. Service-Schicht
Ein `@Service`, zum Beispiel `BankService`, kapselt den Zugriff auf das Bankmodell. Controller sollen nicht direkt `Bank` verwenden.

### 4. DTOs verwenden
Über HTTP sollen eigene Anfrage- und Antwortdatensätze übertragen werden, beispielsweise `AccountResponse`, `BalanceResponse`, `DepositRequest` und `WithdrawRequest`. Gib keine internen Domänenobjekte unmittelbar als API-Antworten aus.

### 5. Fehlerbehandlung
Mit `@RestControllerAdvice` werden erwartete Fehler in verständliche JSON-Antworten umgewandelt: etwa HTTP 404 bei unbekannter Kontonummer und HTTP 400 bei ungültigem Betrag oder Datum.

### 6. CORS konfigurieren
Erlaube für die lokale Entwicklung das Frontend unter `http://localhost:5173`.

### 7. Tests erstellen
Die Domänentests müssen weiterhin bestehen. Ergänze HTTP-Integrationstests für Kontoeröffnung, Einzahlung, Auszahlung und einen Fehlerfall, beispielsweise einen verbotenen Überzug.

## Zusätzliche Aufgabe: Fehler in der Kontoerstellung
Prüfe, ob `AccountFactory` die richtigen Kontounterklassen erzeugt und `creditLimit` nicht verliert. Behebe den Fehler oder dokumentiere deine bewusst vereinfachte Lösung.

## Nicht Bestandteil der ursprünglichen Backend-Übung
Dauerhafte Datenhaltung, Anmeldung/Berechtigungen und Frontend-Oberfläche. Dauerhafte Datenhaltung wird separat in Sprint 9 behandelt.

## Abnahmekriterien
- [ ] Anwendung startet und antwortet auf Port 8080.
- [ ] Alle vorgesehenen REST-Endpunkte funktionieren.
- [ ] Erwartete Fehler liefern sinnvolle JSON-Fehlerantworten.
- [ ] CORS für den lokalen Entwicklungsserver funktioniert.
- [ ] Kontoerstellung ist fachlich korrekt.
- [ ] Die Tests bestehen, auch auf HTTP-Ebene.

## Freiwillige Zusatzaufgaben
Buchungshistorie unter `/accounts/{id}/bookings`; OpenAPI/Swagger; Bean Validation für Anfragen.
