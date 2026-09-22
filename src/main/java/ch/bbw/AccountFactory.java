package ch.bbw;

import ch.bbw.accounts.*;

public class AccountFactory {
    /**
     * Next account id.
     */
    private long nextAccountId;

    public AccountFactory() {
        this.nextAccountId = 1000;
    }

    public Account createPromoYouthSavingsAccount() {
        String id = "Y-" + nextAccountId++;

        return new PromoYouthSavingsAccount(id);
    }

    public Account createSalaryAccount(long creditLimit) {
        String id = "P-" + nextAccountId++;

        return new SalaryAccount(id, creditLimit);
    }

    public Account createSavingsAccount(){
        String id = "S-" + nextAccountId++;

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
