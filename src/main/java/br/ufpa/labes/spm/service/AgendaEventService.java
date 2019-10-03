package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.repository.AgendaEventRepository;
import br.ufpa.labes.spm.service.dto.AgendaEventDTO;
import br.ufpa.labes.spm.service.mapper.AgendaEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AgendaEvent}.
 */
@Service
@Transactional
public class AgendaEventService {

    private final Logger log = LoggerFactory.getLogger(AgendaEventService.class);

    private final AgendaEventRepository agendaEventRepository;

    private final AgendaEventMapper agendaEventMapper;

    public AgendaEventService(AgendaEventRepository agendaEventRepository, AgendaEventMapper agendaEventMapper) {
        this.agendaEventRepository = agendaEventRepository;
        this.agendaEventMapper = agendaEventMapper;
    }

    /**
     * Save a agendaEvent.
     *
     * @param agendaEventDTO the entity to save.
     * @return the persisted entity.
     */
    public AgendaEventDTO save(AgendaEventDTO agendaEventDTO) {
        log.debug("Request to save AgendaEvent : {}", agendaEventDTO);
        AgendaEvent agendaEvent = agendaEventMapper.toEntity(agendaEventDTO);
        agendaEvent = agendaEventRepository.save(agendaEvent);
        return agendaEventMapper.toDto(agendaEvent);
    }

    /**
     * Get all the agendaEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgendaEventDTO> findAll() {
        log.debug("Request to get all AgendaEvents");
        return agendaEventRepository.findAll().stream()
            .map(agendaEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one agendaEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgendaEventDTO> findOne(Long id) {
        log.debug("Request to get AgendaEvent : {}", id);
        return agendaEventRepository.findById(id)
            .map(agendaEventMapper::toDto);
    }

    /**
     * Delete the agendaEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgendaEvent : {}", id);
        agendaEventRepository.deleteById(id);
    }
}
