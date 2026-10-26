package ch.bbw.persistence;

import ch.bbw.Booking;
import jakarta.persistence.*;

/** Eine Buchung gehört genau zu einem gespeicherten Konto. */
@Entity
@Table(name = "bank_bookings")
public class StoredBooking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private StoredAccount account;
    @Column(nullable = false)
    private long bookedAt;
    @Column(nullable = false)
    private long amount;
    @Column(nullable = false)
    private String description;

    protected StoredBooking() {}
    public StoredBooking(StoredAccount account, Booking booking) {
        this.account = account;
        bookedAt = booking.date();
        amount = booking.amount();
        description = booking.text();
    }
    public Booking toBooking() { return new Booking(bookedAt, amount, description); }
}
