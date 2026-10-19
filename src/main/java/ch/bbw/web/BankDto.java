package ch.bbw.web;

import ch.bbw.Booking;
import ch.bbw.accounts.Account;

/** JSON-DTOs; interne Domain-Objekte werden nicht direkt zurückgegeben. */
public final class BankDto {
    private BankDto() {}
    public record IdResponse(String id) {}
    public record AccountResponse(String id, String type, long balance) {
        public static AccountResponse of(Account account) {
            return new AccountResponse(account.getId(), account.getAccountType(), account.getBalance());
        }
    }
    public record BalanceResponse(long balance) {}
    public record SalaryRequest(long creditLimit) {}
    public record TransactionRequest(long amount, String date, String text) {}
    public record BookingResponse(long date, long amount, String text) {
        public static BookingResponse of(Booking booking) {
            return new BookingResponse(booking.date(), booking.amount(), booking.text());
        }
    }
    public record ErrorResponse(String error) {}
}
