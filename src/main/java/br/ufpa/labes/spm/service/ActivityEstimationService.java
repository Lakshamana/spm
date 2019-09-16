package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ActivityEstimation;
import br.ufpa.labes.spm.repository.ActivityEstimationRepository;
import br.ufpa.labes.spm.service.dto.ActivityEstimationDTO;
import br.ufpa.labes.spm.service.mapper.ActivityEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link ActivityEstimation}. */
@Service
@Transactional
public class ActivityEstimationService {

  private final Logger log = LoggerFactory.getLogger(ActivityEstimationService.class);

  private final ActivityEstimationRepository activityEstimationRepository;

  private final ActivityEstimationMapper activityEstimationMapper;

  public ActivityEstimationService(
      ActivityEstimationRepository activityEstimationRepository,
      ActivityEstimationMapper activityEstimationMapper) {
    this.activityEstimationRepository = activityEstimationRepository;
    this.activityEstimationMapper = activityEstimationMapper;
  }

  /**
   * Save a activityEstimation.
   *
   * @param activityEstimationDTO the entity to save.
   * @return the persisted entity.
   */
  public ActivityEstimationDTO save(ActivityEstimationDTO activityEstimationDTO) {
    log.debug("Request to save ActivityEstimation : {}", activityEstimationDTO);
    ActivityEstimation activityEstimation =
        activityEstimationMapper.toEntity(activityEstimationDTO);
    activityEstimation = activityEstimationRepository.save(activityEstimation);
    return activityEstimationMapper.toDto(activityEstimation);
  }

  /**
   * Get all the activityEstimations.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ActivityEstimationDTO> findAll() {
    log.debug("Request to get all ActivityEstimations");
    return activityEstimationRepository.findAll().stream()
        .map(activityEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the activityEstimations where TheEstimationSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ActivityEstimationDTO> findAllWhereTheEstimationSuperIsNull() {
    log.debug("Request to get all activityEstimations where TheEstimationSuper is null");
    return StreamSupport.stream(activityEstimationRepository.findAll().spliterator(), false)
        .filter(activityEstimation -> activityEstimation.getTheEstimationSuper() == null)
        .map(activityEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one activityEstimation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ActivityEstimationDTO> findOne(Long id) {
    log.debug("Request to get ActivityEstimation : {}", id);
    return activityEstimationRepository.findById(id).map(activityEstimationMapper::toDto);
  }

  /**
   * Delete the activityEstimation by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ActivityEstimation : {}", id);
    activityEstimationRepository.deleteById(id);
  }
}
