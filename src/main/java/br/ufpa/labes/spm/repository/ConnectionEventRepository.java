package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ConnectionEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the ConnectionEvent entity. */
@SuppressWarnings("unused")
@Repository
public interface ConnectionEventRepository extends JpaRepository<ConnectionEvent, Long> {}
