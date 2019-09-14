package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.PlainActivity;
import br.ufpa.labes.spm.repository.PlainActivityRepository;
import br.ufpa.labes.spm.service.dto.PlainActivityDTO;
import br.ufpa.labes.spm.service.mapper.PlainActivityMapper;
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
 * Service Implementation for managing {@link PlainActivity}.
 */
@Service
@Transactional
public class PlainActivityService {

    private final Logger log = LoggerFactory.getLogger(PlainActivityService.class);

    private final PlainActivityRepository plainActivityRepository;

    private final PlainActivityMapper plainActivityMapper;

    public PlainActivityService(PlainActivityRepository plainActivityRepository, PlainActivityMapper plainActivityMapper) {
        this.plainActivityRepository = plainActivityRepository;
        this.plainActivityMapper = plainActivityMapper;
    }

    /**
     * Save a plainActivity.
     *
     * @param plainActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public PlainActivityDTO save(PlainActivityDTO plainActivityDTO) {
        log.debug("Request to save PlainActivity : {}", plainActivityDTO);
        PlainActivity plainActivity = plainActivityMapper.toEntity(plainActivityDTO);
        plainActivity = plainActivityRepository.save(plainActivity);
        return plainActivityMapper.toDto(plainActivity);
    }

    /**
     * Get all the plainActivities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PlainActivityDTO> findAll() {
        log.debug("Request to get all PlainActivities");
        return plainActivityRepository.findAll().stream()
            .map(plainActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the plainActivities where TheActivitySuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<PlainActivityDTO> findAllWhereTheActivitySuperIsNull() {
        log.debug("Request to get all plainActivities where TheActivitySuper is null");
        return StreamSupport
            .stream(plainActivityRepository.findAll().spliterator(), false)
            .filter(plainActivity -> plainActivity.getTheActivitySuper() == null)
            .map(plainActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one plainActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlainActivityDTO> findOne(Long id) {
        log.debug("Request to get PlainActivity : {}", id);
        return plainActivityRepository.findById(id)
            .map(plainActivityMapper::toDto);
    }

    /**
     * Delete the plainActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PlainActivity : {}", id);
        plainActivityRepository.deleteById(id);
    }
}
