package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentEstimation;
import br.ufpa.labes.spm.repository.AgentEstimationRepository;
import br.ufpa.labes.spm.service.dto.AgentEstimationDTO;
import br.ufpa.labes.spm.service.mapper.AgentEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AgentEstimation}.
 */
@Service
@Transactional
public class AgentEstimationService {

    private final Logger log = LoggerFactory.getLogger(AgentEstimationService.class);

    private final AgentEstimationRepository agentEstimationRepository;

    private final AgentEstimationMapper agentEstimationMapper;

    public AgentEstimationService(AgentEstimationRepository agentEstimationRepository, AgentEstimationMapper agentEstimationMapper) {
        this.agentEstimationRepository = agentEstimationRepository;
        this.agentEstimationMapper = agentEstimationMapper;
    }

    /**
     * Save a agentEstimation.
     *
     * @param agentEstimationDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentEstimationDTO save(AgentEstimationDTO agentEstimationDTO) {
        log.debug("Request to save AgentEstimation : {}", agentEstimationDTO);
        AgentEstimation agentEstimation = agentEstimationMapper.toEntity(agentEstimationDTO);
        agentEstimation = agentEstimationRepository.save(agentEstimation);
        return agentEstimationMapper.toDto(agentEstimation);
    }

    /**
     * Get all the agentEstimations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentEstimationDTO> findAll() {
        log.debug("Request to get all AgentEstimations");
        return agentEstimationRepository.findAll().stream()
            .map(agentEstimationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one agentEstimation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentEstimationDTO> findOne(Long id) {
        log.debug("Request to get AgentEstimation : {}", id);
        return agentEstimationRepository.findById(id)
            .map(agentEstimationMapper::toDto);
    }

    /**
     * Delete the agentEstimation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgentEstimation : {}", id);
        agentEstimationRepository.deleteById(id);
    }
}
