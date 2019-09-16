package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.repository.ActivityRepository;
import br.ufpa.labes.spm.service.dto.ActivityDTO;
import br.ufpa.labes.spm.service.mapper.ActivityMapper;
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

/** Service Implementation for managing {@link Activity}. */
@Service
@Transactional
public class ActivityService {

  private final Logger log = LoggerFactory.getLogger(ActivityService.class);

  private final ActivityRepository activityRepository;

  private final ActivityMapper activityMapper;

  public ActivityService(ActivityRepository activityRepository, ActivityMapper activityMapper) {
    this.activityRepository = activityRepository;
    this.activityMapper = activityMapper;
  }

  /**
   * Save a activity.
   *
   * @param activityDTO the entity to save.
   * @return the persisted entity.
   */
  public ActivityDTO save(ActivityDTO activityDTO) {
    log.debug("Request to save Activity : {}", activityDTO);
    Activity activity = activityMapper.toEntity(activityDTO);
    activity = activityRepository.save(activity);
    return activityMapper.toDto(activity);
  }

  /**
   * Get all the activities.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ActivityDTO> findAll() {
    log.debug("Request to get all Activities");
    return activityRepository.findAllWithEagerRelationships().stream()
        .map(activityMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the activities with eager load of many-to-many relationships.
   *
   * @return the list of entities.
   */
  public Page<ActivityDTO> findAllWithEagerRelationships(Pageable pageable) {
    return activityRepository.findAllWithEagerRelationships(pageable).map(activityMapper::toDto);
  }

  /**
   * Get one activity by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ActivityDTO> findOne(Long id) {
    log.debug("Request to get Activity : {}", id);
    return activityRepository.findOneWithEagerRelationships(id).map(activityMapper::toDto);
  }

  /**
   * Delete the activity by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Activity : {}", id);
    activityRepository.deleteById(id);
  }
}
