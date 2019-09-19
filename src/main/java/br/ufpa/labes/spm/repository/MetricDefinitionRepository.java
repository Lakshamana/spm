package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IMetricDefinitionDAO;


import br.ufpa.labes.spm.domain.MetricDefinition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MetricDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetricDefinitionRepository extends IMetricDefinitionDAO, JpaRepository<MetricDefinition, Long> {

}
