package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ProcessModelEvent;
import br.ufpa.labes.spm.repository.ProcessModelEventRepository;
import br.ufpa.labes.spm.service.dto.ProcessModelEventDTO;
import br.ufpa.labes.spm.service.mapper.ProcessModelEventMapper;
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
 * Service Implementation for managing {@link ProcessModelEvent}.
 */
@Service
@Transactional
public class ProcessModelEventService {

    private final Logger log = LoggerFactory.getLogger(ProcessModelEventService.class);

    private final ProcessModelEventRepository processModelEventRepository;

    private final ProcessModelEventMapper processModelEventMapper;

    public ProcessModelEventService(ProcessModelEventRepository processModelEventRepository, ProcessModelEventMapper processModelEventMapper) {
        this.processModelEventRepository = processModelEventRepository;
        this.processModelEventMapper = processModelEventMapper;
    }

    /**
     * Save a processModelEvent.
     *
     * @param processModelEventDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcessModelEventDTO save(ProcessModelEventDTO processModelEventDTO) {
        log.debug("Request to save ProcessModelEvent : {}", processModelEventDTO);
        ProcessModelEvent processModelEvent = processModelEventMapper.toEntity(processModelEventDTO);
        processModelEvent = processModelEventRepository.save(processModelEvent);
        return processModelEventMapper.toDto(processModelEvent);
    }

    /**
     * Get all the processModelEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessModelEventDTO> findAll() {
        log.debug("Request to get all ProcessModelEvents");
        return processModelEventRepository.findAll().stream()
            .map(processModelEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the processModelEvents where TheEventSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ProcessModelEventDTO> findAllWhereTheEventSuperIsNull() {
        log.debug("Request to get all processModelEvents where TheEventSuper is null");
        return StreamSupport
            .stream(processModelEventRepository.findAll().spliterator(), false)
            .filter(processModelEvent -> processModelEvent.getTheEventSuper() == null)
            .map(processModelEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one processModelEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProcessModelEventDTO> findOne(Long id) {
        log.debug("Request to get ProcessModelEvent : {}", id);
        return processModelEventRepository.findById(id)
            .map(processModelEventMapper::toDto);
    }

    /**
     * Delete the processModelEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProcessModelEvent : {}", id);
        processModelEventRepository.deleteById(id);
    }
}
