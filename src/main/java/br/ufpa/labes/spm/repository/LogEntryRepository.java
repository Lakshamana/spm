package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.LogEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LogEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    @Query("select logEntry from LogEntry logEntry where logEntry.user.login = ?#{principal.username}")
    List<LogEntry> findByUserIsCurrentUser();

}
