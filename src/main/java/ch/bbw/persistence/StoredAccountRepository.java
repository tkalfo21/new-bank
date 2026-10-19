package ch.bbw.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data persistence adapter. */
public interface StoredAccountRepository extends JpaRepository<StoredAccount, String> {}
