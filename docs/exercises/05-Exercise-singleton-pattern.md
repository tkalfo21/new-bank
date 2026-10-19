# Übung 05 – Singleton-Muster

**Sprint 5.** Ziel: `Bank` soll als Singleton verwendet werden. Anschliessend werden die Folgen dieser Entscheidung für Tests, Abhängigkeiten und Erweiterbarkeit untersucht.

## Aufgaben

### 1. Tests zuerst
- `Bank.getInstance()` liefert bei wiederholtem Aufruf dieselbe Instanz (`assertSame`).
- Der parameterlose Bank-Konstruktor ist nicht öffentlich (Reflexionstest).
- Tests bleiben voneinander unabhängig; Änderungen an einem Konto dürfen keinen nachfolgenden Test verfälschen.

### 2. Singleton implementieren
Der parameterlose Konstruktor wird `private`. Über `public static Bank getInstance()` erhält man die gemeinsame Instanz. Bestehende Aufrufe müssen angepasst werden.

### 3. Drei Varianten vergleichen
**Sofortige Initialisierung:**
```java
private static final Bank INSTANCE = new Bank();
public static Bank getInstance() { return INSTANCE; }
```
Threadsicher, aber nicht verzögert.

**Verzögerte synchronisierte Initialisierung:**
```java
private static Bank instance;
public static synchronized Bank getInstance() {
    if (instance == null) instance = new Bank();
    return instance;
}
```
Threadsicher und verzögert, jedoch mit Synchronisierung bei jedem Aufruf.

**Initialization-on-demand Holder:**
```java
private static class Holder {
    static final Bank INSTANCE = new Bank();
}
public static Bank getInstance() { return Holder.INSTANCE; }
```
Verzögert und durch die JVM-Klasseninitialisierung threadsicher; kein zusätzlicher Lock je Aufruf.

Erkläre, warum Double-Checked Locking ohne `volatile` wegen möglicher Speicher-Sichtbarkeitsprobleme falsch sein kann.

### 4. Testisolation
Ein globales Singleton besitzt veränderbaren Zustand. Führe die Tests erneut aus und behebe Abhängigkeiten von ihrer Reihenfolge. Eine spezielle `reset()`-Methode nur für Tests ist ein **Notbehelf und ein Hinweis auf ein Designproblem**. Bei parallelen Tests können zusätzliche Konflikte auftreten.

### 5. Vor- und Nachteile dokumentieren
**Vorteile:** Ein gemeinsamer Zustand, ein zentraler Zugriffspunkt und keine getrennten ID-Zähler pro Bankinstanz.

**Nachteile:** Versteckte Abhängigkeiten, global veränderbarer Zustand, schwierigere isolierte Tests und Lebensdauer bis zum Programmende. Zudem besitzt jede JVM ihre *eigene* Singleton-Instanz.

### 6. Gegenentwurf mit Dependency Injection
Behalte für die Übung das Singleton bei, ermögliche jedoch eine Factory-Injektion über einen separaten Konstruktor. Verwende in der eigentlichen Anwendung nach Möglichkeit übergebene Abhängigkeiten statt überall `Bank.getInstance()` aufzurufen.

## Diskussionsfragen
- Singleton oder statische Hilfsklasse: Was ist der Unterschied?
- Welche Vorteile hätte ein Enum-Singleton?
- Was passiert bei mehreren Serverinstanzen?
- Wie unterscheiden sich Spring-Singleton-Beans von einem statischen Java-Singleton?

## Abnahmekriterien
- [ ] Privater parameterloser Konstruktor und `getInstance()`.
- [ ] Threadsichere Implementierung mit Begründung.
- [ ] Drei Varianten schriftlich verglichen.
- [ ] Testisolation ist sichergestellt und der Notbehelf dokumentiert.
- [ ] Factory wird injiziert statt fest verdrahtet.
- [ ] Vor- und Nachteile sind dokumentiert.
- [ ] Zwei aufeinanderfolgende Testläufe bestehen.

## Freiwillige Zusatzaufgaben
Enum-Variante ausprobieren; Reflexionsangriff demonstrieren; zweite unabhängige Bank im Testfall modellieren.
