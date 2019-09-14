package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentMetric;
import br.ufpa.labes.spm.repository.AgentMetricRepository;
import br.ufpa.labes.spm.service.dto.AgentMetricDTO;
import br.ufpa.labes.spm.service.mapper.AgentMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link AgentMetric}.
 */
@Service
@Transactional
public class AgentMetricService {

    private final Logger log = LoggerFactory.getLogger(AgentMetricService.class);

    private final AgentMetricRepository agentMetricRepository;

    private final AgentMetricMapper agentMetricMapper;

    public AgentMetricService(AgentMetricRepository agentMetricRepository, AgentMetricMapper agentMetricMapper) {
        this.agentMetricRepository = agentMetricRepository;
        this.agentMetricMapper = agentMetricMapper;
    }

    /**
     * Save a agentMetric.
     *
     * @param agentMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentMetricDTO save(AgentMetricDTO agentMetricDTO) {
        log.debug("Request to save AgentMetric : {}", agentMetricDTO);
        AgentMetric agentMetric = agentMetricMapper.toEntity(agentMetricDTO);
        agentMetric = agentMetricRepository.save(agentMetric);
        return agentMetricMapper.toDto(agentMetric);
    }

    /**
     * Get all the agentMetrics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentMetricDTO> findAll() {
        log.debug("Request to get all AgentMetrics");
        return agentMetricRepository.findAll().stream()
            .map(agentMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the agentMetrics where TheMetricSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<AgentMetricDTO> findAllWhereTheMetricSuperIsNull() {
        log.debug("Request to get all agentMetrics where TheMetricSuper is null");
        return StreamSupport
            .stream(agentMetricRepository.findAll().spliterator(), false)
            .filter(agentMetric -> agentMetric.getTheMetricSuper() == null)
            .map(agentMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one agentMetric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentMetricDTO> findOne(Long id) {
        log.debug("Request to get AgentMetric : {}", id);
        return agentMetricRepository.findById(id)
            .map(agentMetricMapper::toDto);
    }

    /**
     * Delete the agentMetric by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgentMetric : {}", id);
        agentMetricRepository.deleteById(id);
    }
}
