package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkGroupInstSug;
import br.ufpa.labes.spm.repository.WorkGroupInstSugRepository;
import br.ufpa.labes.spm.service.dto.WorkGroupInstSugDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupInstSugMapper;
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
 * Service Implementation for managing {@link WorkGroupInstSug}.
 */
@Service
@Transactional
public class WorkGroupInstSugService {

    private final Logger log = LoggerFactory.getLogger(WorkGroupInstSugService.class);

    private final WorkGroupInstSugRepository workGroupInstSugRepository;

    private final WorkGroupInstSugMapper workGroupInstSugMapper;

    public WorkGroupInstSugService(WorkGroupInstSugRepository workGroupInstSugRepository, WorkGroupInstSugMapper workGroupInstSugMapper) {
        this.workGroupInstSugRepository = workGroupInstSugRepository;
        this.workGroupInstSugMapper = workGroupInstSugMapper;
    }

    /**
     * Save a workGroupInstSug.
     *
     * @param workGroupInstSugDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkGroupInstSugDTO save(WorkGroupInstSugDTO workGroupInstSugDTO) {
        log.debug("Request to save WorkGroupInstSug : {}", workGroupInstSugDTO);
        WorkGroupInstSug workGroupInstSug = workGroupInstSugMapper.toEntity(workGroupInstSugDTO);
        workGroupInstSug = workGroupInstSugRepository.save(workGroupInstSug);
        return workGroupInstSugMapper.toDto(workGroupInstSug);
    }

    /**
     * Get all the workGroupInstSugs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkGroupInstSugDTO> findAll() {
        log.debug("Request to get all WorkGroupInstSugs");
        return workGroupInstSugRepository.findAllWithEagerRelationships().stream()
            .map(workGroupInstSugMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the workGroupInstSugs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WorkGroupInstSugDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workGroupInstSugRepository.findAllWithEagerRelationships(pageable).map(workGroupInstSugMapper::toDto);
    }
    


    /**
    *  Get all the workGroupInstSugs where ThePeopleInstSugSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<WorkGroupInstSugDTO> findAllWhereThePeopleInstSugSuperIsNull() {
        log.debug("Request to get all workGroupInstSugs where ThePeopleInstSugSuper is null");
        return StreamSupport
            .stream(workGroupInstSugRepository.findAll().spliterator(), false)
            .filter(workGroupInstSug -> workGroupInstSug.getThePeopleInstSugSuper() == null)
            .map(workGroupInstSugMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one workGroupInstSug by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkGroupInstSugDTO> findOne(Long id) {
        log.debug("Request to get WorkGroupInstSug : {}", id);
        return workGroupInstSugRepository.findOneWithEagerRelationships(id)
            .map(workGroupInstSugMapper::toDto);
    }

    /**
     * Delete the workGroupInstSug by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkGroupInstSug : {}", id);
        workGroupInstSugRepository.deleteById(id);
    }
}
