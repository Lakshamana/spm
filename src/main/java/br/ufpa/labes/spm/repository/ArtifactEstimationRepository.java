package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IArtifactEstimationDAO;


import br.ufpa.labes.spm.domain.ArtifactEstimation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ArtifactEstimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtifactEstimationRepository extends IArtifactEstimationDAO, JpaRepository<ArtifactEstimation, Long> {

}
