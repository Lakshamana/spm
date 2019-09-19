package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces..IPersistenceAuditEventDAO;


import br.ufpa.labes.spm.domain.PersistentAuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/** Spring Data JPA repository for the {@link PersistentAuditEvent} entity. */
public interface PersistenceAuditEventRepository extends IPersistenceAuditEventDAO, JpaRepository<PersistentAuditEvent, Long> {

  List<PersistentAuditEvent> findByPrincipal(String principal);

  List<PersistentAuditEvent> findByAuditEventDateAfter(Instant after);

  List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, Instant after);

  List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfterAndAuditEventType(
      String principal, Instant after, String type);

  Page<PersistentAuditEvent> findAllByAuditEventDateBetween(
      Instant fromDate, Instant toDate, Pageable pageable);
}
