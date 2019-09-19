package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IAgentMetricDAO;


import br.ufpa.labes.spm.domain.AgentMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AgentMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentMetricRepository extends IAgentMetricDAO, JpaRepository<AgentMetric, Long> {

}
