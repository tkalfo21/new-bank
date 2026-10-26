package ch.bbw;
import ch.bbw.accounts.*;
import java.util.concurrent.atomic.AtomicLong;

/** Einfache Factory: Alle konkreten Konten entstehen hier. Ein gemeinsamer AtomicLong-Zähler vergibt eindeutige IDs. */
public class AccountFactory {
    private final AtomicLong nextAccountId;
    public AccountFactory() { this(1000); }
    public AccountFactory(long startingId) { this.nextAccountId = new AtomicLong(startingId); }
    public Account createSavingsAccount() { return create(AccountType.SAVINGS, 0); }
    public Account createPromoYouthSavingsAccount() { return create(AccountType.PROMO_YOUTH, 0); }
    public Account createSalaryAccount(long limit) { return create(AccountType.SALARY, limit); }

    /** Benannte Methoden sind für Bank lesbarer, weil nur das Lohnkonto eine Kreditlimite nutzt. */
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
    /** Reconstruct an account with its persisted ID without allocating a new one. */
    public Account restore(String id, long creditLimit) {
        if (id == null || !id.matches("[SYP]-[0-9]+")) throw new IllegalArgumentException("Ungültige Konto-ID");
        String prefix = id.substring(0, 1);
        long sequence = Long.parseLong(id.substring(2));
        if (sequence == Long.MAX_VALUE) throw new IllegalArgumentException("Konto-ID zu gross");
        nextAccountId.updateAndGet(current -> Math.max(current, sequence + 1));
        return switch (prefix) {
            case "S" -> new SavingsAccount(id);
            case "Y" -> new PromoYouthSavingsAccount(id);
            case "P" -> new SalaryAccount(id, creditLimit);
            default -> throw new IllegalArgumentException("Unbekannter Kontotyp");
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
