package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentInstSug;
import br.ufpa.labes.spm.repository.AgentInstSugRepository;
import br.ufpa.labes.spm.service.dto.AgentInstSugDTO;
import br.ufpa.labes.spm.service.mapper.AgentInstSugMapper;
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
 * Service Implementation for managing {@link AgentInstSug}.
 */
@Service
@Transactional
public class AgentInstSugService {

    private final Logger log = LoggerFactory.getLogger(AgentInstSugService.class);

    private final AgentInstSugRepository agentInstSugRepository;

    private final AgentInstSugMapper agentInstSugMapper;

    public AgentInstSugService(AgentInstSugRepository agentInstSugRepository, AgentInstSugMapper agentInstSugMapper) {
        this.agentInstSugRepository = agentInstSugRepository;
        this.agentInstSugMapper = agentInstSugMapper;
    }

    /**
     * Save a agentInstSug.
     *
     * @param agentInstSugDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentInstSugDTO save(AgentInstSugDTO agentInstSugDTO) {
        log.debug("Request to save AgentInstSug : {}", agentInstSugDTO);
        AgentInstSug agentInstSug = agentInstSugMapper.toEntity(agentInstSugDTO);
        agentInstSug = agentInstSugRepository.save(agentInstSug);
        return agentInstSugMapper.toDto(agentInstSug);
    }

    /**
     * Get all the agentInstSugs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentInstSugDTO> findAll() {
        log.debug("Request to get all AgentInstSugs");
        return agentInstSugRepository.findAll().stream()
            .map(agentInstSugMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the agentInstSugs where ThePeopleInstSugSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<AgentInstSugDTO> findAllWhereThePeopleInstSugSuperIsNull() {
        log.debug("Request to get all agentInstSugs where ThePeopleInstSugSuper is null");
        return StreamSupport
            .stream(agentInstSugRepository.findAll().spliterator(), false)
            .filter(agentInstSug -> agentInstSug.getThePeopleInstSugSuper() == null)
            .map(agentInstSugMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one agentInstSug by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentInstSugDTO> findOne(Long id) {
        log.debug("Request to get AgentInstSug : {}", id);
        return agentInstSugRepository.findById(id)
            .map(agentInstSugMapper::toDto);
    }

    /**
     * Delete the agentInstSug by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgentInstSug : {}", id);
        agentInstSugRepository.deleteById(id);
    }
}
