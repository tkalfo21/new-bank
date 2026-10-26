# Hilfe zum Bankprojekt

## Anwendung starten
Die Anwendung benötigt Java 21.

Unter Windows:
```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Unter Linux/macOS:
```bash
./mvnw test
./mvnw spring-boot:run
```

Anschliessend kannst du `http://localhost:8080/actuator/health` im Browser öffnen.

## Häufige Fragen
**Wo sind die Aufgaben?** In `docs/exercises/`.

**Wo sind die Lösungen und Erläuterungen?** In `docs/exercise-solutions.md` und `TODO.md`.

**Wo wird gespeichert?** Die lokale H2-Dateidatenbank liegt im Ordner `data/`.

**Wie starte ich die Tests?** Mit `./mvnw test` beziehungsweise `.\mvnw.cmd test` auf Windows.

## Weiterführende Dokumentation
- [Offizielle Maven-Dokumentation](https://maven.apache.org/guides/index.html)
- [Spring Boot – Dokumentation](https://docs.spring.io/spring-boot/)
- [Spring Data JPA – Dokumentation](https://docs.spring.io/spring-data/jpa/reference/)
- [Spring – REST-Dienste entwickeln](https://spring.io/guides/gs/rest-service/)
