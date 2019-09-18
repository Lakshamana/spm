package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentInstSuggestionToAgent;
import br.ufpa.labes.spm.repository.AgentInstSuggestionToAgentRepository;
import br.ufpa.labes.spm.service.dto.AgentInstSuggestionToAgentDTO;
import br.ufpa.labes.spm.service.mapper.AgentInstSuggestionToAgentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AgentInstSuggestionToAgent}.
 */
@Service
@Transactional
public class AgentInstSuggestionToAgentService {

    private final Logger log = LoggerFactory.getLogger(AgentInstSuggestionToAgentService.class);

    private final AgentInstSuggestionToAgentRepository agentInstSuggestionToAgentRepository;

    private final AgentInstSuggestionToAgentMapper agentInstSuggestionToAgentMapper;

    public AgentInstSuggestionToAgentService(AgentInstSuggestionToAgentRepository agentInstSuggestionToAgentRepository, AgentInstSuggestionToAgentMapper agentInstSuggestionToAgentMapper) {
        this.agentInstSuggestionToAgentRepository = agentInstSuggestionToAgentRepository;
        this.agentInstSuggestionToAgentMapper = agentInstSuggestionToAgentMapper;
    }

    /**
     * Save a agentInstSuggestionToAgent.
     *
     * @param agentInstSuggestionToAgentDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentInstSuggestionToAgentDTO save(AgentInstSuggestionToAgentDTO agentInstSuggestionToAgentDTO) {
        log.debug("Request to save AgentInstSuggestionToAgent : {}", agentInstSuggestionToAgentDTO);
        AgentInstSuggestionToAgent agentInstSuggestionToAgent = agentInstSuggestionToAgentMapper.toEntity(agentInstSuggestionToAgentDTO);
        agentInstSuggestionToAgent = agentInstSuggestionToAgentRepository.save(agentInstSuggestionToAgent);
        return agentInstSuggestionToAgentMapper.toDto(agentInstSuggestionToAgent);
    }

    /**
     * Get all the agentInstSuggestionToAgents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentInstSuggestionToAgentDTO> findAll() {
        log.debug("Request to get all AgentInstSuggestionToAgents");
        return agentInstSuggestionToAgentRepository.findAll().stream()
            .map(agentInstSuggestionToAgentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one agentInstSuggestionToAgent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentInstSuggestionToAgentDTO> findOne(Long id) {
        log.debug("Request to get AgentInstSuggestionToAgent : {}", id);
        return agentInstSuggestionToAgentRepository.findById(id)
            .map(agentInstSuggestionToAgentMapper::toDto);
    }

    /**
     * Delete the agentInstSuggestionToAgent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgentInstSuggestionToAgent : {}", id);
        agentInstSuggestionToAgentRepository.deleteById(id);
    }
}
