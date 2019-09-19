package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.taskagenda.IProcessAgendaDAO;


import br.ufpa.labes.spm.domain.ProcessAgenda;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProcessAgenda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessAgendaRepository extends IProcessAgendaDAO, JpaRepository<ProcessAgenda, Long> {

}
