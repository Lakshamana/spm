package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ActivityMetric;
import br.ufpa.labes.spm.repository.ActivityMetricRepository;
import br.ufpa.labes.spm.service.dto.ActivityMetricDTO;
import br.ufpa.labes.spm.service.mapper.ActivityMetricMapper;
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
 * Service Implementation for managing {@link ActivityMetric}.
 */
@Service
@Transactional
public class ActivityMetricService {

    private final Logger log = LoggerFactory.getLogger(ActivityMetricService.class);

    private final ActivityMetricRepository activityMetricRepository;

    private final ActivityMetricMapper activityMetricMapper;

    public ActivityMetricService(ActivityMetricRepository activityMetricRepository, ActivityMetricMapper activityMetricMapper) {
        this.activityMetricRepository = activityMetricRepository;
        this.activityMetricMapper = activityMetricMapper;
    }

    /**
     * Save a activityMetric.
     *
     * @param activityMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public ActivityMetricDTO save(ActivityMetricDTO activityMetricDTO) {
        log.debug("Request to save ActivityMetric : {}", activityMetricDTO);
        ActivityMetric activityMetric = activityMetricMapper.toEntity(activityMetricDTO);
        activityMetric = activityMetricRepository.save(activityMetric);
        return activityMetricMapper.toDto(activityMetric);
    }

    /**
     * Get all the activityMetrics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ActivityMetricDTO> findAll() {
        log.debug("Request to get all ActivityMetrics");
        return activityMetricRepository.findAll().stream()
            .map(activityMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the activityMetrics where TheMetricSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ActivityMetricDTO> findAllWhereTheMetricSuperIsNull() {
        log.debug("Request to get all activityMetrics where TheMetricSuper is null");
        return StreamSupport
            .stream(activityMetricRepository.findAll().spliterator(), false)
            .filter(activityMetric -> activityMetric.getTheMetricSuper() == null)
            .map(activityMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one activityMetric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActivityMetricDTO> findOne(Long id) {
        log.debug("Request to get ActivityMetric : {}", id);
        return activityMetricRepository.findById(id)
            .map(activityMetricMapper::toDto);
    }

    /**
     * Delete the activityMetric by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivityMetric : {}", id);
        activityMetricRepository.deleteById(id);
    }
}
