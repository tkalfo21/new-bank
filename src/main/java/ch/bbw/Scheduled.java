package ch.bbw;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * Klasse zur Darstellung eines geplanten Datums.
 * @author lro@gmx.ch
 * @version 1.0
 */
public class Scheduled {

    /**
     * Millisekunden bis zum Termin. Negative Werte bedeuten, dass der Termin vergangen ist.
     */
    private long millisecondsUntilDue;
    public Scheduled(long millisecondsUntilDue) {
        this.millisecondsUntilDue = millisecondsUntilDue;
    }

    /**
     * Prüft, ob das Transaktionsdatum vergangen ist.
     * @return Wahr, wenn das Transaktionsdatum bereits vergangen ist.
     */
    public boolean isPastDue() {
        return millisecondsUntilDue < 0;
    }

    /**
     * Erstellt ein geplantes Datum für den aktuellen Zeitpunkt.
     * @return Ein geplantes Datum für den aktuellen Zeitpunkt.
     */
    public static Scheduled now() {
        return new Scheduled(0);
    }

    /**
     * Wandelt das Datum in einen Unix-Zeitstempel um.
     * @return Der Unix-Zeitstempel
     */
    public long toUnix(){
        return toInstant().toEpochMilli();
    }

    /**
     * Wandelt das Datum in einen Instant um.
     * @return Der Zeitpunkt
     */
    public Instant toInstant(){

        return Instant.now().plus(millisecondsUntilDue, ChronoUnit.MILLIS);
    }

    /**
     * Addiert eine Zeitspanne zum geplanten Datum.
     * @param duration Die zu addierende Zeitspanne
     * @param unit Die Zeiteinheit
     * @return Das geplante Datum
     */
    public Scheduled plus(long duration, ChronoUnit unit){
            return switch (unit) {
                case MILLIS    -> plusMillis(duration);
                case SECONDS   -> plusSeconds(duration);
                case MINUTES   -> plusMinutes(duration);
                case HOURS     -> plusHours(duration);
                case DAYS      -> plusDays(duration);
                case WEEKS -> plusWeeks(duration);
                case MONTHS    -> plusMonths(duration);
                case YEARS     -> plusYears(duration);
                default -> throw new UnsupportedTemporalTypeException("Nicht unterstützte Zeiteinheit: " + unit + "\nDiese Zeiteinheit muss noch implementiert werden.");
            };
    }

    public Scheduled plusMillis(long millisToAdd){
        millisecondsUntilDue += millisToAdd;
        return this;
    }

    public Scheduled plusSeconds(long secondsToAdd){
        millisecondsUntilDue += secondsToAdd * 1000;
        return this;
    }

    public Scheduled plusMinutes(long minutesToAdd){
        millisecondsUntilDue += minutesToAdd * 1000 * 60;
        return this;
    }

    public Scheduled plusHours(long hoursToAdd){
        millisecondsUntilDue += hoursToAdd * 1000 * 60 * 60;
        return this;
    }

    public Scheduled plusDays(long daysToAdd){
        millisecondsUntilDue += daysToAdd * 1000 * 60 * 60 * 24;
        return this;
    }

    public Scheduled plusWeeks(long weeksToAdd){
        millisecondsUntilDue += weeksToAdd * 1000 * 60 * 60 * 24 * 7;
        return this;
    }

    public Scheduled plusMonths(long monthsToAdd){
        millisecondsUntilDue += monthsToAdd * 1000 * 60 * 60 * 24 * 30;
        return this;
    }

    public Scheduled plusYears(long yearsToAdd){
        millisecondsUntilDue += yearsToAdd * 1000 * 60 * 60 * 24 * 365;
        return this;
    }

    /**
     * Zieht eine Zeitspanne vom geplanten Datum ab.
     * @param duration Die abzuziehende Zeitspanne
     * @param unit Die Zeiteinheit
     * @return Das geplante Datum
     */
    public Scheduled minus(long duration, ChronoUnit unit){
        return switch (unit) {
            case MILLIS    -> minusMillis(duration);
            case SECONDS   -> minusSeconds(duration);
            case MINUTES   -> minusMinutes(duration);
            case HOURS     -> minusHours(duration);
            case DAYS      -> minusDays(duration);
            case WEEKS -> minusWeeks(duration);
            case MONTHS    -> minusMonths(duration);
            case YEARS     -> minusYears(duration);
            default -> throw new UnsupportedTemporalTypeException("Nicht unterstützte Zeiteinheit: " + unit + "\nDiese Zeiteinheit muss noch implementiert werden.");
        };
    }

    public Scheduled minusMillis(long millisToAdd){
        millisecondsUntilDue -= millisToAdd;
        return this;
    }

    public Scheduled minusSeconds(long secondsToAdd){
        millisecondsUntilDue -= secondsToAdd * 1000;
        return this;
    }

    public Scheduled minusMinutes(long minutesToAdd){
        millisecondsUntilDue -= minutesToAdd * 1000 * 60;
        return this;
    }

    public Scheduled minusHours(long hoursToAdd){
        millisecondsUntilDue -= hoursToAdd * 1000 * 60 * 60;
        return this;
    }

    public Scheduled minusDays(long daysToAdd){
        millisecondsUntilDue -= daysToAdd * 1000 * 60 * 60 * 24;
        return this;
    }

    public Scheduled minusWeeks(long weeksToAdd){
        millisecondsUntilDue -= weeksToAdd * 1000 * 60 * 60 * 24 * 7;
        return this;
    }

    public Scheduled minusMonths(long monthsToAdd){
        millisecondsUntilDue -= monthsToAdd * 1000 * 60 * 60 * 24 * 30;
        return this;
    }

    public Scheduled minusYears(long yearsToAdd){
        millisecondsUntilDue -= yearsToAdd * 1000 * 60 * 60 * 24 * 365;
        return this;
    }

    /**
     * Wandelt das geplante Datum in Text um.
     * @return Textdarstellung des geplanten Datums
     */
    @Override
    public String toString() {
        return toInstant().toString();
    }
}
