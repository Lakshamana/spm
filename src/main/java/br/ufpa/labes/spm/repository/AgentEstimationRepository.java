package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IAgentEstimationDAO;


import br.ufpa.labes.spm.domain.AgentEstimation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AgentEstimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentEstimationRepository extends IAgentEstimationDAO, JpaRepository<AgentEstimation, Long> {

}
