package ch.bbw;
/** Kontotyp und ID-Präfix zusammen definiert. */
public enum AccountType {
    SAVINGS("S"), PROMO_YOUTH("Y"), SALARY("P");
    private final String prefix;
    AccountType(String prefix) { this.prefix = prefix; }
    public String prefix() { return prefix; }
}
