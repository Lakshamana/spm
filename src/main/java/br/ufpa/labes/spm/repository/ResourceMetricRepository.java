package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IResourceMetricDAO;


import br.ufpa.labes.spm.domain.ResourceMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResourceMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceMetricRepository extends IResourceMetricDAO, JpaRepository<ResourceMetric, Long> {

}
