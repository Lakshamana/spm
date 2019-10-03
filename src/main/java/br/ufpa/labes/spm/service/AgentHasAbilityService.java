package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentHasAbility;
import br.ufpa.labes.spm.repository.AgentHasAbilityRepository;
import br.ufpa.labes.spm.service.dto.AgentHasAbilityDTO;
import br.ufpa.labes.spm.service.mapper.AgentHasAbilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AgentHasAbility}.
 */
@Service
@Transactional
public class AgentHasAbilityService {

    private final Logger log = LoggerFactory.getLogger(AgentHasAbilityService.class);

    private final AgentHasAbilityRepository agentHasAbilityRepository;

    private final AgentHasAbilityMapper agentHasAbilityMapper;

    public AgentHasAbilityService(AgentHasAbilityRepository agentHasAbilityRepository, AgentHasAbilityMapper agentHasAbilityMapper) {
        this.agentHasAbilityRepository = agentHasAbilityRepository;
        this.agentHasAbilityMapper = agentHasAbilityMapper;
    }

    /**
     * Save a agentHasAbility.
     *
     * @param agentHasAbilityDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentHasAbilityDTO save(AgentHasAbilityDTO agentHasAbilityDTO) {
        log.debug("Request to save AgentHasAbility : {}", agentHasAbilityDTO);
        AgentHasAbility agentHasAbility = agentHasAbilityMapper.toEntity(agentHasAbilityDTO);
        agentHasAbility = agentHasAbilityRepository.save(agentHasAbility);
        return agentHasAbilityMapper.toDto(agentHasAbility);
    }

    /**
     * Get all the agentHasAbilities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentHasAbilityDTO> findAll() {
        log.debug("Request to get all AgentHasAbilities");
        return agentHasAbilityRepository.findAll().stream()
            .map(agentHasAbilityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one agentHasAbility by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentHasAbilityDTO> findOne(Long id) {
        log.debug("Request to get AgentHasAbility : {}", id);
        return agentHasAbilityRepository.findById(id)
            .map(agentHasAbilityMapper::toDto);
    }

    /**
     * Delete the agentHasAbility by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgentHasAbility : {}", id);
        agentHasAbilityRepository.deleteById(id);
    }
}
