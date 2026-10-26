package ch.bbw.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring-Data-Schnittstelle für die Datenbank. */
public interface StoredAccountRepository extends JpaRepository<StoredAccount, String> {}
