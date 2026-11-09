# Frontend (Exercise 11)

React + Vite dient als Benutzeroberfläche für die REST-Schnittstelle aus dem Java-Spring-Boot-Projekt.

## Voraussetzungen
- Node.js LTS und npm
- Backend aus dem Projektverzeichnis gestartet (Port 8080)

## Start unter Windows / IntelliJ

Terminal 1 im Repository-Hauptordner:

```powershell
.\mvnw.cmd spring-boot:run
```

Terminal 2 im `frontend`-Ordner:

```powershell
cd frontend
npm install
npm run dev
```

Frontend im Browser: http://localhost:5173

`npm test` führt die Umrechnungs-/Validierungstests aus.
`npm run build` prüft, ob das Frontend gebaut werden kann.

## Aufbau
- `src/api/bankApi.js`: Einziger Ort für HTTP-`fetch`-Aufrufe, Fehler aus dem Backend werden als Meldungen geworfen.
- `src/App.jsx`: React-Oberfläche mit Kontoeröffnung, Saldo, Ein-/Auszahlung, Top 5 und Buchungen.
- `src/utils/money.js`: Umrechnung CHF ↔ Millirappen (CHF 1.00 = 100000).
- `.env`: `VITE_API_BASE_URL=http://localhost:8080` (in React-Komponenten keine hartcodierte Backend-Adresse).
- `src/styles.css`: Responsives Layout ohne CSS-Framework.

## Manuelle Prüfung
1. Backend und Frontend starten.
2. Ein Sparkonto, ein Jugendsparkonto und ein Lohnkonto erstellen; jeweils unterschiedliche ID prüfen.
3. Eine Einzahlung tätigen und Saldo/Buchungen ansehen.
4. Zu viel Geld abheben: Backend-Fehler muss im Frontend erscheinen.
5. Falsche Kontonummer eingeben und Saldo prüfen: HTTP-404-Fehler wird angezeigt.
6. Im Browser mit F12 → Network kontrollieren: Aufrufe gehen an Port 8080, ohne CORS-Fehler.

**Wichtig:** Dies ist eine Demo-Anwendung ohne Anmeldung. Keine echten Bankdaten eingeben.
