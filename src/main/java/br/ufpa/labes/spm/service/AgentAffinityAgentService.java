package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentAffinityAgent;
import br.ufpa.labes.spm.repository.AgentAffinityAgentRepository;
import br.ufpa.labes.spm.service.dto.AgentAffinityAgentDTO;
import br.ufpa.labes.spm.service.mapper.AgentAffinityAgentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AgentAffinityAgent}.
 */
@Service
@Transactional
public class AgentAffinityAgentService {

    private final Logger log = LoggerFactory.getLogger(AgentAffinityAgentService.class);

    private final AgentAffinityAgentRepository agentAffinityAgentRepository;

    private final AgentAffinityAgentMapper agentAffinityAgentMapper;

    public AgentAffinityAgentService(AgentAffinityAgentRepository agentAffinityAgentRepository, AgentAffinityAgentMapper agentAffinityAgentMapper) {
        this.agentAffinityAgentRepository = agentAffinityAgentRepository;
        this.agentAffinityAgentMapper = agentAffinityAgentMapper;
    }

    /**
     * Save a agentAffinityAgent.
     *
     * @param agentAffinityAgentDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentAffinityAgentDTO save(AgentAffinityAgentDTO agentAffinityAgentDTO) {
        log.debug("Request to save AgentAffinityAgent : {}", agentAffinityAgentDTO);
        AgentAffinityAgent agentAffinityAgent = agentAffinityAgentMapper.toEntity(agentAffinityAgentDTO);
        agentAffinityAgent = agentAffinityAgentRepository.save(agentAffinityAgent);
        return agentAffinityAgentMapper.toDto(agentAffinityAgent);
    }

    /**
     * Get all the agentAffinityAgents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentAffinityAgentDTO> findAll() {
        log.debug("Request to get all AgentAffinityAgents");
        return agentAffinityAgentRepository.findAll().stream()
            .map(agentAffinityAgentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one agentAffinityAgent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentAffinityAgentDTO> findOne(Long id) {
        log.debug("Request to get AgentAffinityAgent : {}", id);
        return agentAffinityAgentRepository.findById(id)
            .map(agentAffinityAgentMapper::toDto);
    }

    /**
     * Delete the agentAffinityAgent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgentAffinityAgent : {}", id);
        agentAffinityAgentRepository.deleteById(id);
    }
}
