package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.log.IEventDAO;


import br.ufpa.labes.spm.domain.Event;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends IEventDAO, JpaRepository<Event, Long> {

}
