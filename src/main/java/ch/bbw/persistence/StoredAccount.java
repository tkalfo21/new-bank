package ch.bbw.persistence;

import ch.bbw.Booking;
import ch.bbw.accounts.Account;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** Persisted account and its ordered booking history. */
@Entity
@Table(name = "bank_accounts")
public class StoredAccount {
    @Id
    private String id;
    @Column(nullable = false)
    private long creditLimit;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderColumn(name = "booking_order")
    private List<StoredBooking> bookings = new ArrayList<>();

    protected StoredAccount() {}
    public StoredAccount(String id) { this.id = id; }
    public String getId() { return id; }
    public long getCreditLimit() { return creditLimit; }
    public List<Booking> toBookings() { return bookings.stream().map(StoredBooking::toBooking).toList(); }

    public void replaceWith(Account account, long limit) {
        creditLimit = limit;
        bookings.clear();
        for (Booking booking : account.getBookings()) {
            bookings.add(new StoredBooking(this, booking));
        }
    }
}
