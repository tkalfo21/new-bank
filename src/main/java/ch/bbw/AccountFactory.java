package ch.bbw;
import ch.bbw.accounts.*;
import java.util.concurrent.atomic.AtomicLong;

/** Simple Factory: alle konkreten Konten entstehen hier. Ein gemeinsamer AtomicLong-Zähler vergibt eindeutige IDs. */
public class AccountFactory {
    private final AtomicLong nextAccountId;
    public AccountFactory() { this(1000); }
    public AccountFactory(long startingId) { this.nextAccountId = new AtomicLong(startingId); }
    public Account createSavingsAccount() { return create(AccountType.SAVINGS, 0); }
    public Account createPromoYouthSavingsAccount() { return create(AccountType.PROMO_YOUTH, 0); }
    public Account createSalaryAccount(long limit) { return create(AccountType.SALARY, limit); }

    /** Benannte Methoden sind für Bank lesbarer, weil nur Salary eine Kreditlimite nutzt. */
    public Account create(AccountType type, long creditLimit) {
        if (type == null) throw new IllegalArgumentException("Kontotyp fehlt");
        if (type != AccountType.SALARY && creditLimit != 0)
            throw new IllegalArgumentException("Kreditlimite nur für Lohnkonten");
        if (type == AccountType.SALARY && creditLimit > 0)
            throw new IllegalArgumentException("Kreditlimite muss <= 0 sein");
        String id = type.prefix() + "-" + nextAccountId.getAndIncrement();
        return switch (type) {
            case SAVINGS -> new SavingsAccount(id);
            case PROMO_YOUTH -> new PromoYouthSavingsAccount(id);
            case SALARY -> new SalaryAccount(id, creditLimit);
        };
    }
    /** Kompatible ältere String-API. */
    public Account create(String type, long limit) {
        return switch (type) {
            case "SavingsAccount" -> create(AccountType.SAVINGS, limit);
            case "PromoYouthSavingsAccount" -> create(AccountType.PROMO_YOUTH, limit);
            case "SalaryAccount" -> create(AccountType.SALARY, limit);
            default -> throw new IllegalArgumentException("Unbekannter Kontotyp: " + type);
        };
    }
}
