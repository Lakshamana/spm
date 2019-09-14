package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.AgentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.Agent}.
 */
public interface AgentService {

    /**
     * Save a agent.
     *
     * @param agentDTO the entity to save.
     * @return the persisted entity.
     */
    AgentDTO save(AgentDTO agentDTO);

    /**
     * Get all the agents.
     *
     * @return the list of entities.
     */
    List<AgentDTO> findAll();
    /**
     * Get all the AgentDTO where TheChatMessage is {@code null}.
     *
     * @return the list of entities.
     */
    List<AgentDTO> findAllWhereTheChatMessageIsNull();

    /**
     * Get all the agents with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<AgentDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" agent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgentDTO> findOne(Long id);

    /**
     * Delete the "id" agent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
