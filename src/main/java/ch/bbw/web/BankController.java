package ch.bbw.web;

import ch.bbw.Scheduled;
import jakarta.validation.Valid;
import ch.bbw.web.BankDto.*;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

/** REST-API; Beträge sind Millirappen, Zeitstempel ISO-8601. */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class BankController {
    private final BankService service;
    public BankController(BankService service) { this.service = service; }

    @PostMapping("/accounts/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse createSavings() { return new IdResponse(service.savings()); }

    @PostMapping("/accounts/promo-youth")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse createYouth() { return new IdResponse(service.youth()); }

    @PostMapping("/accounts/salary")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse createSalary(@Valid @RequestBody SalaryRequest request) {
        return new IdResponse(service.salary(request.creditLimit()));
    }

    @GetMapping("/accounts/{id}")
    public AccountResponse account(@PathVariable String id) throws InvalidAmountException {
        return AccountResponse.of(service.account(id));
    }

    @GetMapping("/accounts/{id}/balance")
    public BalanceResponse balance(@PathVariable String id) throws InvalidAmountException {
        return new BalanceResponse(service.balance(id));
    }

    @GetMapping("/bank/balance")
    public BalanceResponse totalBalance() { return new BalanceResponse(service.totalBalance()); }

    @GetMapping("/bank/top5/highest")
    public List<AccountResponse> highest() {
        return service.highest().stream().map(AccountResponse::of).toList();
    }

    @GetMapping("/bank/top5/lowest")
    public List<AccountResponse> lowest() {
        return service.lowest().stream().map(AccountResponse::of).toList();
    }

    @GetMapping("/accounts/{id}/bookings")
    public List<BookingResponse> bookings(@PathVariable String id) throws InvalidAmountException {
        return service.bookings(id).stream().map(BookingResponse::of).toList();
    }

    @PostMapping("/accounts/{id}/deposit")
    public BalanceResponse deposit(@PathVariable String id, @Valid @RequestBody TransactionRequest request)
            throws InvalidAmountException, InvalidDateException {
        return new BalanceResponse(service.deposit(id, date(request.date()), request.amount(),
                label(request.text(), "Einzahlung")));
    }

    @PostMapping("/accounts/{id}/withdraw")
    public BalanceResponse withdraw(@PathVariable String id, @Valid @RequestBody TransactionRequest request)
            throws InvalidAmountException, InvalidDateException {
        return new BalanceResponse(service.withdraw(id, date(request.date()), request.amount(),
                label(request.text(), "Auszahlung")));
    }

    private static String label(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static Scheduled date(String value) {
        if (value == null || value.isBlank()) return Scheduled.now();
        try {
            return new Scheduled(Duration.between(Instant.now(), Instant.parse(value)).toMillis());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Datum muss ISO-8601 sein, z.B. 2026-10-19T12:00:00Z");
        }
    }
}
