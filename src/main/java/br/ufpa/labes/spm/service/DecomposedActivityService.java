package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.DecomposedActivity;
import br.ufpa.labes.spm.repository.DecomposedActivityRepository;
import br.ufpa.labes.spm.service.dto.DecomposedActivityDTO;
import br.ufpa.labes.spm.service.mapper.DecomposedActivityMapper;
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
 * Service Implementation for managing {@link DecomposedActivity}.
 */
@Service
@Transactional
public class DecomposedActivityService {

    private final Logger log = LoggerFactory.getLogger(DecomposedActivityService.class);

    private final DecomposedActivityRepository decomposedActivityRepository;

    private final DecomposedActivityMapper decomposedActivityMapper;

    public DecomposedActivityService(DecomposedActivityRepository decomposedActivityRepository, DecomposedActivityMapper decomposedActivityMapper) {
        this.decomposedActivityRepository = decomposedActivityRepository;
        this.decomposedActivityMapper = decomposedActivityMapper;
    }

    /**
     * Save a decomposedActivity.
     *
     * @param decomposedActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public DecomposedActivityDTO save(DecomposedActivityDTO decomposedActivityDTO) {
        log.debug("Request to save DecomposedActivity : {}", decomposedActivityDTO);
        DecomposedActivity decomposedActivity = decomposedActivityMapper.toEntity(decomposedActivityDTO);
        decomposedActivity = decomposedActivityRepository.save(decomposedActivity);
        return decomposedActivityMapper.toDto(decomposedActivity);
    }

    /**
     * Get all the decomposedActivities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DecomposedActivityDTO> findAll() {
        log.debug("Request to get all DecomposedActivities");
        return decomposedActivityRepository.findAll().stream()
            .map(decomposedActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the decomposedActivities where TheActivitySuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<DecomposedActivityDTO> findAllWhereTheActivitySuperIsNull() {
        log.debug("Request to get all decomposedActivities where TheActivitySuper is null");
        return StreamSupport
            .stream(decomposedActivityRepository.findAll().spliterator(), false)
            .filter(decomposedActivity -> decomposedActivity.getTheActivitySuper() == null)
            .map(decomposedActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one decomposedActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DecomposedActivityDTO> findOne(Long id) {
        log.debug("Request to get DecomposedActivity : {}", id);
        return decomposedActivityRepository.findById(id)
            .map(decomposedActivityMapper::toDto);
    }

    /**
     * Delete the decomposedActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DecomposedActivity : {}", id);
        decomposedActivityRepository.deleteById(id);
    }
}
