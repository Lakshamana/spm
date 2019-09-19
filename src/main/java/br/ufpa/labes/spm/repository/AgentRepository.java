package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;


import br.ufpa.labes.spm.domain.Agent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Agent entity.
 */
@Repository
public interface AgentRepository extends IAgentDAO, JpaRepository<Agent, Long> {

    @Query(value = "select distinct agent from Agent agent left join fetch agent.theProcesses left join fetch agent.theWorkGroups left join fetch agent.theOrgUnits",
        countQuery = "select count(distinct agent) from Agent agent")
    Page<Agent> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct agent from Agent agent left join fetch agent.theProcesses left join fetch agent.theWorkGroups left join fetch agent.theOrgUnits")
    List<Agent> findAllWithEagerRelationships();

    @Query("select agent from Agent agent left join fetch agent.theProcesses left join fetch agent.theWorkGroups left join fetch agent.theOrgUnits where agent.id =:id")
    Optional<Agent> findOneWithEagerRelationships(@Param("id") Long id);

}
