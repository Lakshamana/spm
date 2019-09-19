package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IArtifactMetricDAO;


import br.ufpa.labes.spm.domain.ArtifactMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ArtifactMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtifactMetricRepository extends IArtifactMetricDAO, JpaRepository<ArtifactMetric, Long> {

}
