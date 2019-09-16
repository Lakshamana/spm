package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ActivityInstantiated;
import br.ufpa.labes.spm.repository.ActivityInstantiatedRepository;
import br.ufpa.labes.spm.service.dto.ActivityInstantiatedDTO;
import br.ufpa.labes.spm.service.mapper.ActivityInstantiatedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ActivityInstantiated}. */
@Service
@Transactional
public class ActivityInstantiatedService {

  private final Logger log = LoggerFactory.getLogger(ActivityInstantiatedService.class);

  private final ActivityInstantiatedRepository activityInstantiatedRepository;

  private final ActivityInstantiatedMapper activityInstantiatedMapper;

  public ActivityInstantiatedService(
      ActivityInstantiatedRepository activityInstantiatedRepository,
      ActivityInstantiatedMapper activityInstantiatedMapper) {
    this.activityInstantiatedRepository = activityInstantiatedRepository;
    this.activityInstantiatedMapper = activityInstantiatedMapper;
  }

  /**
   * Save a activityInstantiated.
   *
   * @param activityInstantiatedDTO the entity to save.
   * @return the persisted entity.
   */
  public ActivityInstantiatedDTO save(ActivityInstantiatedDTO activityInstantiatedDTO) {
    log.debug("Request to save ActivityInstantiated : {}", activityInstantiatedDTO);
    ActivityInstantiated activityInstantiated =
        activityInstantiatedMapper.toEntity(activityInstantiatedDTO);
    activityInstantiated = activityInstantiatedRepository.save(activityInstantiated);
    return activityInstantiatedMapper.toDto(activityInstantiated);
  }

  /**
   * Get all the activityInstantiateds.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ActivityInstantiatedDTO> findAll() {
    log.debug("Request to get all ActivityInstantiateds");
    return activityInstantiatedRepository.findAll().stream()
        .map(activityInstantiatedMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one activityInstantiated by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ActivityInstantiatedDTO> findOne(Long id) {
    log.debug("Request to get ActivityInstantiated : {}", id);
    return activityInstantiatedRepository.findById(id).map(activityInstantiatedMapper::toDto);
  }

  /**
   * Delete the activityInstantiated by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ActivityInstantiated : {}", id);
    activityInstantiatedRepository.deleteById(id);
  }
}
