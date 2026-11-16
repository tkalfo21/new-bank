package ch.bbw.web;

import ch.bbw.web.BankDto.AccountResponse;
import ch.bbw.web.BankDto.BalanceResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** REST-Endpunkte für bankweite Auswertungen. */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class BankController {
    private final BankService service;
    public BankController(BankService service) { this.service = service; }

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


}
