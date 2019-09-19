package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IWorkGroupEstimationDAO;


import br.ufpa.labes.spm.domain.WorkGroupEstimation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WorkGroupEstimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkGroupEstimationRepository extends IWorkGroupEstimationDAO, JpaRepository<WorkGroupEstimation, Long> {

}
