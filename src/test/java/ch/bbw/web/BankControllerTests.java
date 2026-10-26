package ch.bbw.web;
import ch.bbw.Bank;
import ch.bbw.web.BankDto.*;
import ch.bbw.exceptions.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

class BankControllerTests {
    private BankController controller;
    @BeforeEach void setUp() {
        Bank.getInstance().resetForTests();
        controller = new BankController(new BankService(Bank.getInstance()));
    }
    @Test void createDepositWithdrawAndGetBalance() throws Exception {
        IdResponse created = controller.createSavings();
        assertTrue(created.id().startsWith("S-"));
        assertEquals(5000, controller.deposit(created.id(),
                new TransactionRequest(5000, null, "Lohn")).balance());
        assertEquals(3000, controller.withdraw(created.id(),
                new TransactionRequest(2000, null, "Einkauf")).balance());
        assertEquals(3000, controller.balance(created.id()).balance());
        assertEquals(2, controller.bookings(created.id()).size());
    }
    @Test void failedWithdrawalBecomesBadRequest() throws Exception {
        String id = controller.createSavings().id();
        assertThrows(InvalidAmountException.class,
                () -> controller.withdraw(id, new TransactionRequest(1, null, "Zu viel")));
        var response = new BankExceptionHandler().amount(new InvalidAmountException("Nicht genug"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, new BankExceptionHandler()
                .amount(new InvalidAmountException("Invalid bank id")).getStatusCode());
    }
}
