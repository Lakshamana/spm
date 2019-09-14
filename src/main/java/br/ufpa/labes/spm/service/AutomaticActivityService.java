package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AutomaticActivity;
import br.ufpa.labes.spm.repository.AutomaticActivityRepository;
import br.ufpa.labes.spm.service.dto.AutomaticActivityDTO;
import br.ufpa.labes.spm.service.mapper.AutomaticActivityMapper;
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
 * Service Implementation for managing {@link AutomaticActivity}.
 */
@Service
@Transactional
public class AutomaticActivityService {

    private final Logger log = LoggerFactory.getLogger(AutomaticActivityService.class);

    private final AutomaticActivityRepository automaticActivityRepository;

    private final AutomaticActivityMapper automaticActivityMapper;

    public AutomaticActivityService(AutomaticActivityRepository automaticActivityRepository, AutomaticActivityMapper automaticActivityMapper) {
        this.automaticActivityRepository = automaticActivityRepository;
        this.automaticActivityMapper = automaticActivityMapper;
    }

    /**
     * Save a automaticActivity.
     *
     * @param automaticActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public AutomaticActivityDTO save(AutomaticActivityDTO automaticActivityDTO) {
        log.debug("Request to save AutomaticActivity : {}", automaticActivityDTO);
        AutomaticActivity automaticActivity = automaticActivityMapper.toEntity(automaticActivityDTO);
        automaticActivity = automaticActivityRepository.save(automaticActivity);
        return automaticActivityMapper.toDto(automaticActivity);
    }

    /**
     * Get all the automaticActivities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AutomaticActivityDTO> findAll() {
        log.debug("Request to get all AutomaticActivities");
        return automaticActivityRepository.findAll().stream()
            .map(automaticActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the automaticActivities where TheAutomatic is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<AutomaticActivityDTO> findAllWhereTheAutomaticIsNull() {
        log.debug("Request to get all automaticActivities where TheAutomatic is null");
        return StreamSupport
            .stream(automaticActivityRepository.findAll().spliterator(), false)
            .filter(automaticActivity -> automaticActivity.getTheAutomatic() == null)
            .map(automaticActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one automaticActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutomaticActivityDTO> findOne(Long id) {
        log.debug("Request to get AutomaticActivity : {}", id);
        return automaticActivityRepository.findById(id)
            .map(automaticActivityMapper::toDto);
    }

    /**
     * Delete the automaticActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AutomaticActivity : {}", id);
        automaticActivityRepository.deleteById(id);
    }
}
