package ch.bbw;

/**
 * Buchung.
 *
 * @param date   Datum der Transaktion als Unix-Timestamp in Millisekunden.
 * @param amount Transaktionsbetrag in Millirappen.
 * @param text   Beschreibung der Buchung.
 * @author Luigi Cavuoti, lro@gmx.ch
 * @version 2.2
 */
public record Booking(long date, long amount, String text) {

    public Booking {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text darf nicht leer sein");
        }
    }
}