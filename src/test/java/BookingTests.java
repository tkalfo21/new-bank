import ch.bbw.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTests
{
    @Test
    @DisplayName("Construct Simple Booking, should work")
    public void testInitialization()
    {
        Booking booking = new Booking(1000L, 5000L, "Einzahlung");

        assertEquals(1000L, booking.date());
        assertEquals(5000L, booking.amount());
        assertEquals("Einzahlung", booking.text());
    }

    @Test
    @DisplayName("Not a real test, it prints a list of bookings")
    public void testPrint()
    {
        Booking booking1 = new Booking(1000L, 5000L, "Einzahlung");
        Booking booking2 = new Booking(2000L, -2000L, "Auszahlung");

        List<Booking> bookings = List.of(booking1, booking2);

        bookings.forEach(System.out::println);

        assertEquals(2, bookings.size());
    }
}
