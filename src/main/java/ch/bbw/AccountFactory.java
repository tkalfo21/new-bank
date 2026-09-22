package ch.bbw;

import ch.bbw.accounts.*;

import java.util.concurrent.atomic.AtomicLong;

public class AccountFactory {
    /**
     * Next account id.
     */
    private AtomicLong nextAccountId;

    public AccountFactory() {
        this.nextAccountId = new AtomicLong(1000);
    }

    public Account createPromoYouthSavingsAccount() {
        String id = "Y-" + nextAccountId.getAndIncrement();

        return new PromoYouthSavingsAccount(id);
    }

    public Account createSalaryAccount(long creditLimit) {
        String id = "P-" + nextAccountId.getAndIncrement();

        return new SalaryAccount(id, creditLimit);
    }

    public Account createSavingsAccount(){
        String id = "S-" + nextAccountId.getAndIncrement();

        return new SavingsAccount(id);
    }

    public Account create(String accountType, long creditLimit){
        return switch (accountType){
            case "SavingsAccount" -> createSavingsAccount();
            case "SalaryAccount" -> createSalaryAccount(creditLimit);
            case "PromoYouthSavingsAccount" -> createPromoYouthSavingsAccount();
            default -> throw new IllegalStateException("Unexpected value: " + accountType);
        };
    }

}
