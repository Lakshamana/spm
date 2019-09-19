package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.taskagenda.IOcurrenceDAO;


import br.ufpa.labes.spm.domain.Ocurrence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ocurrence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OcurrenceRepository extends IOcurrenceDAO, JpaRepository<Ocurrence, Long> {

}
