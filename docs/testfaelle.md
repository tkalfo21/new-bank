# Testkonzept – Bankprojekt (Modul 320)

## Bezug zur Aufgabenstellung

Die Lehrperson verlangt für jeden Anwendungsfall zunächst Tests, danach die Implementierung sowie die Dokumentation in GitHub. Die aktuelle Testabdeckung wird deshalb anhand von **Normalfällen**, **Grenzwerten** und **Fehlerfällen** begründet.

Die folgende Übersicht dokumentiert die vorhandenen Tests. Sie ist **kein rückwirkender Beleg**, dass jeder Test zeitlich vor der Implementierung geschrieben wurde.

| Nr. | Anwendungsfall | Geprüftes Verhalten | Testdatei |
|---|---|---|---|
| T01 | UC-01 Konto eröffnen | Verschiedene Kontotypen und eindeutige Nummern | `BankUseCaseTests` |
| T02 | UC-01 Konto eröffnen | Korrekte Kontotypen aus der Factory | `FactoryContractTests` |
| T03 | UC-02 Einzahlung | Zwei Buchungen und richtig berechneter Saldo | `BankUseCaseTests` |
| T04 | UC-02 Einzahlung | Null, negativer Betrag, vergangenes Datum: keine Änderung | `BankUseCaseTests` |
| T05 | UC-03 Auszahlung | Auszahlung exakt bis zum Guthaben | `BankUseCaseTests` |
| T06 | UC-03 Auszahlung | Überzug wird abgewiesen, keine Zusatzbuchung | `AccountBoundaryTests` |
| T07 | UC-03 Auszahlung | Negative Kreditlimite des Lohnkontos | `AccountBoundaryTests` |
| T08 | UC-03 Auszahlung | CHF-200-Monatslimite des Jugendkontos | `AccountBoundaryTests` |
| T09 | UC-03 Auszahlung | Limite gilt im nächsten Monat erneut | `AccountBoundaryTests` |
| T10 | UC-04 Kontoauszug | Kontonummer, Texte, Beträge und Saldo | `BankUseCaseTests` |
| T11 | UC-04 Kontoauszug | Auszug bei leerem Konto | `AccountBoundaryTests` |
| T12 | Allgemein | Unbekannte Konto-ID löst Fehler aus | `BankUseCaseTests` |
| T13 | Allgemein | Buchungsdaten sind von aussen unveränderlich | `AccountBoundaryTests` |
| T14 | Allgemein | Buchungstext darf nicht leer sein | `AccountBoundaryTests` |
| T15 | Erweiterung | Top-5 korrekt, auch bei wenigen Konten | `BankUseCaseTests` |
| T16 | REST | HTTP-Status und Buchungshistorie | `BankHttpIntegrationTests` |
| T17 | REST | Eingabevalidierung | `BankValidationIntegrationTests` |
| T18 | Datenbank | Rekonstruktion gespeicherter Konten und Buchungen | `PersistenceIntegrationTests` |
| T19 | REST: UC-01 | Kontoeröffnung und Anfangssaldo über HTTP | `BankApiUseCaseTests` |
| T20 | REST: UC-02 | Einzahlung und Buchungsdetails über HTTP | `BankApiUseCaseTests` |
| T21 | REST: UC-03 | Kreditlimite und abgewiesene Auszahlung | `BankApiUseCaseTests` |
| T22 | REST: UC-03 | Jugendsparlimite über HTTP | `BankApiUseCaseTests` |
| T23 | REST: UC-04 | Unbekanntes Konto gibt HTTP 404 | `BankApiUseCaseTests` |
| T24 | REST | Betrag null und fehlerhaftes Datum: HTTP 400 | `BankApiUseCaseTests` |
| T25 | Kontoauszug | Buchungen eines Monats ohne Folgemonat | `AccountTests.testMonthlyPrint()` |

## Testdurchführung

Unter Windows in Git Bash: `./mvnw test`

In PowerShell: `.\mvnw.cmd test`

Die GitHub-Actions-Workflowdatei `.github/workflows/tests.yml` führt Tests automatisiert aus.

## Grenzen

- Das ursprüngliche Szenario beschreibt einen Kunden und einen Bankmitarbeiter am Schalter. Im Java-Code sind keine eigenen Kunden- oder Mitarbeiterklassen umgesetzt.
- Der Monatsauszug wird über `printStatement(year, month)` und `AccountTests.testMonthlyPrint()` geprüft.
- Die Testabdeckung beweist nicht automatisch eine Note 6: Massgeblich ist die genaue Bewertungsrubrik der Lehrperson.
