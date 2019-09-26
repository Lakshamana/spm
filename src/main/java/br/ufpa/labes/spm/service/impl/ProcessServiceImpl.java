package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.ProcessService;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.repository.ProcessRepository;
import br.ufpa.labes.spm.service.dto.ProcessDTO;
import br.ufpa.labes.spm.service.mapper.ProcessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Process}.
 */
@Service
@Transactional
public class ProcessServiceImpl implements ProcessService {

    private final Logger log = LoggerFactory.getLogger(ProcessServiceImpl.class);

    private final ProcessRepository processRepository;

    private final ProcessMapper processMapper;

    public ProcessServiceImpl(ProcessRepository processRepository, ProcessMapper processMapper) {
        this.processRepository = processRepository;
        this.processMapper = processMapper;
    }

    /**
     * Save a process.
     *
     * @param processDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ProcessDTO save(ProcessDTO processDTO) {
        log.debug("Request to save Process : {}", processDTO);
        Process process = processMapper.toEntity(processDTO);
        process = processRepository.save(process);
        return processMapper.toDto(process);
    }

    /**
     * Get all the processes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProcessDTO> findAll() {
        log.debug("Request to get all Processes");
        return processRepository.findAllWithEagerRelationships().stream()
            .map(processMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the processes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProcessDTO> findAllWithEagerRelationships(Pageable pageable) {
        return processRepository.findAllWithEagerRelationships(pageable).map(processMapper::toDto);
    }
    


    /**
    *  Get all the processes where TheLog is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ProcessDTO> findAllWhereTheLogIsNull() {
        log.debug("Request to get all processes where TheLog is null");
        return StreamSupport
            .stream(processRepository.findAll().spliterator(), false)
            .filter(process -> process.getTheLog() == null)
            .map(processMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one process by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessDTO> findOne(Long id) {
        log.debug("Request to get Process : {}", id);
        return processRepository.findOneWithEagerRelationships(id)
            .map(processMapper::toDto);
    }

    /**
     * Delete the process by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Process : {}", id);
        processRepository.deleteById(id);
    }
}
