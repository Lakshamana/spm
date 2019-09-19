package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.log.IAgendaEventDAO;


import br.ufpa.labes.spm.domain.AgendaEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AgendaEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgendaEventRepository extends IAgendaEventDAO, JpaRepository<AgendaEvent, Long> {

}
