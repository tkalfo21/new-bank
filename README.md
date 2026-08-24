# New Bank — m320 OOD Start Point

Start point (*Vorgabe*) for the **m320 Objektorientiertes Design** Bank exercise.

The focus of this project is **use-cases and sprints**: in each sprint one new part of the system is
designed and then implemented. You are not meant to build everything at once — work through
`docs/exercises/` in order.

---

## ⚠️ The build is red on purpose

```
Tests run: 19, Failures: 19, Errors: 0
```

**This is the intended starting condition, not a broken checkout.** Every test in `src/test/` is a
deliberate stub:

```java
@Test
@DisplayName("Simple deposit on SavingsAccount should work")
public void testDeposit() {
    fail("ToDo");
}
```

Your job is to replace each `fail("ToDo")` with a real test. `Failures: 19, Errors: 0` means all 19
stubs were found and ran correctly. If you ever see **`Errors`** instead of `Failures`, *that* is a
real problem — something no longer compiles or the test framework cannot start.

---

## Quick start

**Prerequisites:** JDK 21 or newer. Maven is not required — the project ships the Maven wrapper
(`mvnw`), which downloads what it needs on first run.

```bash
# Compile everything (main + tests)
./mvnw test-compile

# Run the test suite (expect 19 failures — see above)
./mvnw test

# Start the Spring Boot application on http://localhost:8080
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd` instead of `./mvnw`.

The application starts, but currently has **no REST endpoints** — every URL returns `404`. That is
correct: the web layer is built in Sprint 7. A clean startup looks like this:

```
Tomcat started on port 8080 (http) with context path '/'
Started NewBankApplication in 1.1 seconds
```

---

## Project layout

```
src/main/java/ch/bbw/
├── NewBankApplication.java      Spring Boot entry point (@SpringBootApplication)
├── Bank.java                    In-memory registry of accounts (TreeMap, keyed by id)
├── AccountFactory.java          Creates accounts + generates ids (S-…, Y-…, P-…)
├── Booking.java                 record(date, amount) — one transaction
├── Scheduled.java               A transaction date; rejects past-due dates
├── accounts/
│   ├── Account.java             abstract: id, booking list, balance, deposit/withdraw
│   └── SavingsAccount.java      Adds the no-overdraft rule
└── exceptions/
    ├── InvalidAmountException.java
    └── InvalidDateException.java

src/test/java/                   All tests are fail("ToDo") stubs
docs/exercises/                  The sprint briefs — start here
```

### Domain vocabulary

| Term | Meaning |
|---|---|
| **Millirappen** | The unit of all amounts. `100_000` = CHF 1.00 |
| **Buchung** / `Booking` | A single transaction. Withdrawals are stored as **negative** amounts |
| **Saldo** / balance | Current account balance |
| `Scheduled` | A transaction date, expressed as milliseconds-until-due |

---

## The sprints

Read `docs/exercises/start.md` first — it lists the full sequence. The briefs:

| Sprint | Brief | Topic |
|---|---|---|
| 1 | *(done — this repo)* | `Bank` + `Account` baseline |
| 2 | [`02-Exercise-bookings.md`](docs/exercises/02-Exercise-bookings.md) | Bookings; derive the balance from them |
| 3 | [`03-Exercise-specific-accounts.md`](docs/exercises/03-Exercise-specific-accounts.md) | Inheritance: `SalaryAccount`, `PromoYouthSavingsAccount` |
| 4 | [`04-Exercise-factory-pattern.md`](docs/exercises/04-Exercise-factory-pattern.md) | Factory pattern |
| 5 | [`05-Exercise-singleton-pattern.md`](docs/exercises/05-Exercise-singleton-pattern.md) | Singleton pattern + its trade-offs |
| 6–11 | [`01-Exercise-build-the-be.md`](docs/exercises/01-Exercise-build-the-be.md) | Spring Boot REST backend |

> **Note on numbering:** `01-Exercise-build-the-be.md` is the Spring Boot extension and belongs at
> the **end**, after sprints 2–5. It is numbered `01` for historical reasons.

---

## Known gaps (deliberate)

These are not bugs to report — they are the exercises. Each is addressed by the sprint named.

| Gap | Where | Sprint |
|---|---|---|
| `Booking` has no `text`, so a statement cannot say *why* money moved | `Booking.java` | 2 |
| `Account` keeps `balance` as a field *and* a booking list — two sources of truth | `Account.java` | 2 |
| The booking list is write-only; there is no getter | `Account.java` | 2 |
| `createPromoYouthSavingsAccount()` and `createSalaryAccount()` both return a plain `SavingsAccount` | `AccountFactory.java` | 3 + 4 |
| `creditLimit` is accepted and silently discarded | `AccountFactory.java` | 3 + 4 |
| `Bank` is not a singleton | `Bank.java` | 5 |
| No controllers, DTOs or error handling | — | 7 + 10 |

`Bank.getAccount()` also throws `InvalidAmountException` for an unknown id, which is the wrong
exception type. Fixing that is fair game at any point.

---

## Tech stack

| | |
|---|---|
| Java | 21 (toolchain target) |
| Spring Boot | 4.1.0 |
| Build | Maven wrapper 3.6.3 |
| Web | `spring-boot-starter-webmvc` (the Boot 4 name for `starter-web`) |
| Persistence | `spring-boot-starter-data-jpa` + H2 in-memory |
| Testing | `spring-boot-starter-test` — JUnit 5, AssertJ, Mockito, MockMvc |

The database is **in-memory**: it is recreated on every start and gone when you stop the app.

Note that the domain classes (`Bank`, `Account`, …) are deliberately **not** Spring beans. They are
plain objects, so the OO design stands on its own. The service layer that wraps them in Spring
arrives in Sprint 6.
