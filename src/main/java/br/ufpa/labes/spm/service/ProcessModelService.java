package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.repository.ProcessModelRepository;
import br.ufpa.labes.spm.service.dto.ProcessModelDTO;
import br.ufpa.labes.spm.service.mapper.ProcessModelMapper;
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
 * Service Implementation for managing {@link ProcessModel}.
 */
@Service
@Transactional
public class ProcessModelService {

    private final Logger log = LoggerFactory.getLogger(ProcessModelService.class);

    private final ProcessModelRepository processModelRepository;

    private final ProcessModelMapper processModelMapper;

    public ProcessModelService(ProcessModelRepository processModelRepository, ProcessModelMapper processModelMapper) {
        this.processModelRepository = processModelRepository;
        this.processModelMapper = processModelMapper;
    }

    /**
     * Save a processModel.
     *
     * @param processModelDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcessModelDTO save(ProcessModelDTO processModelDTO) {
        log.debug("Request to save ProcessModel : {}", processModelDTO);
        ProcessModel processModel = processModelMapper.toEntity(processModelDTO);
        processModel = processModelRepository.save(processModel);
        return processModelMapper.toDto(processModel);
    }

    /**
     * Get all the processModels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessModelDTO> findAll() {
        log.debug("Request to get all ProcessModels");
        return processModelRepository.findAll().stream()
            .map(processModelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the processModels where TheProcess is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ProcessModelDTO> findAllWhereTheProcessIsNull() {
        log.debug("Request to get all processModels where TheProcess is null");
        return StreamSupport
            .stream(processModelRepository.findAll().spliterator(), false)
            .filter(processModel -> processModel.getTheProcess() == null)
            .map(processModelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one processModel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProcessModelDTO> findOne(Long id) {
        log.debug("Request to get ProcessModel : {}", id);
        return processModelRepository.findById(id)
            .map(processModelMapper::toDto);
    }

    /**
     * Delete the processModel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProcessModel : {}", id);
        processModelRepository.deleteById(id);
    }
}
