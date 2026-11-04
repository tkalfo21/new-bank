import ch.bbw.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTests {

    @Test
    @DisplayName("Construct Simple Booking, should work")
    public void testInitialization() {

        Booking booking =
                new Booking(
                        1000L,
                        5000L,
                        "Einzahlung"
                );

        assertEquals(1000L, booking.date());
        assertEquals(5000L, booking.amount());
        assertEquals(
                "Einzahlung",
                booking.text()
        );
    }

    @Test
    @DisplayName(
            "Not a real test, it prints a list of bookings"
    )
    public void testPrint() {

        Booking booking1 =
                new Booking(
                        1000L,
                        5000L,
                        "Einzahlung"
                );

        Booking booking2 =
                new Booking(
                        2000L,
                        -2000L,
                        "Auszahlung"
                );

        List<Booking> bookings =
                List.of(booking1, booking2);

        bookings.forEach(System.out::println);

        assertEquals(2, bookings.size());
    }

    @Test @DisplayName("Negative Beträge bleiben als Auszahlung erhalten")
    void negativeBeträge() {
        Booking buchung = new Booking(500L, -250L, "Auszahlung");
        assertEquals(-250L, buchung.amount());
    }

    @Test @DisplayName("Buchungsdatum wird unverändert gespeichert")
    void zeitstempel() {
        assertEquals(1_700_000_000_000L,
                new Booking(1_700_000_000_000L, 100L, "Test").date());
    }

    @Test @DisplayName("Leerer Buchungstext ist ungültig")
    void leererText() {
        assertThrows(IllegalArgumentException.class, () -> new Booking(0L, 1L, ""));
        assertThrows(IllegalArgumentException.class, () -> new Booking(0L, 1L, " "));
    }
}