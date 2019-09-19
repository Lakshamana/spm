package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IActivityEstimationDAO;


import br.ufpa.labes.spm.domain.ActivityEstimation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActivityEstimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityEstimationRepository extends IActivityEstimationDAO, JpaRepository<ActivityEstimation, Long> {

}
