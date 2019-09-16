package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.repository.ModelingActivityEventRepository;
import br.ufpa.labes.spm.service.dto.ModelingActivityEventDTO;
import br.ufpa.labes.spm.service.mapper.ModelingActivityEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link ModelingActivityEvent}. */
@Service
@Transactional
public class ModelingActivityEventService {

  private final Logger log = LoggerFactory.getLogger(ModelingActivityEventService.class);

  private final ModelingActivityEventRepository modelingActivityEventRepository;

  private final ModelingActivityEventMapper modelingActivityEventMapper;

  public ModelingActivityEventService(
      ModelingActivityEventRepository modelingActivityEventRepository,
      ModelingActivityEventMapper modelingActivityEventMapper) {
    this.modelingActivityEventRepository = modelingActivityEventRepository;
    this.modelingActivityEventMapper = modelingActivityEventMapper;
  }

  /**
   * Save a modelingActivityEvent.
   *
   * @param modelingActivityEventDTO the entity to save.
   * @return the persisted entity.
   */
  public ModelingActivityEventDTO save(ModelingActivityEventDTO modelingActivityEventDTO) {
    log.debug("Request to save ModelingActivityEvent : {}", modelingActivityEventDTO);
    ModelingActivityEvent modelingActivityEvent =
        modelingActivityEventMapper.toEntity(modelingActivityEventDTO);
    modelingActivityEvent = modelingActivityEventRepository.save(modelingActivityEvent);
    return modelingActivityEventMapper.toDto(modelingActivityEvent);
  }

  /**
   * Get all the modelingActivityEvents.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ModelingActivityEventDTO> findAll() {
    log.debug("Request to get all ModelingActivityEvents");
    return modelingActivityEventRepository.findAll().stream()
        .map(modelingActivityEventMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the modelingActivityEvents where TheEventSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ModelingActivityEventDTO> findAllWhereTheEventSuperIsNull() {
    log.debug("Request to get all modelingActivityEvents where TheEventSuper is null");
    return StreamSupport.stream(modelingActivityEventRepository.findAll().spliterator(), false)
        .filter(modelingActivityEvent -> modelingActivityEvent.getTheEventSuper() == null)
        .map(modelingActivityEventMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one modelingActivityEvent by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ModelingActivityEventDTO> findOne(Long id) {
    log.debug("Request to get ModelingActivityEvent : {}", id);
    return modelingActivityEventRepository.findById(id).map(modelingActivityEventMapper::toDto);
  }

  /**
   * Delete the modelingActivityEvent by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ModelingActivityEvent : {}", id);
    modelingActivityEventRepository.deleteById(id);
  }
}
