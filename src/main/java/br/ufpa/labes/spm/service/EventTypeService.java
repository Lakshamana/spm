package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.EventType;
import br.ufpa.labes.spm.repository.EventTypeRepository;
import br.ufpa.labes.spm.service.dto.EventTypeDTO;
import br.ufpa.labes.spm.service.mapper.EventTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link EventType}. */
@Service
@Transactional
public class EventTypeService {

  private final Logger log = LoggerFactory.getLogger(EventTypeService.class);

  private final EventTypeRepository eventTypeRepository;

  private final EventTypeMapper eventTypeMapper;

  public EventTypeService(
      EventTypeRepository eventTypeRepository, EventTypeMapper eventTypeMapper) {
    this.eventTypeRepository = eventTypeRepository;
    this.eventTypeMapper = eventTypeMapper;
  }

  /**
   * Save a eventType.
   *
   * @param eventTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public EventTypeDTO save(EventTypeDTO eventTypeDTO) {
    log.debug("Request to save EventType : {}", eventTypeDTO);
    EventType eventType = eventTypeMapper.toEntity(eventTypeDTO);
    eventType = eventTypeRepository.save(eventType);
    return eventTypeMapper.toDto(eventType);
  }

  /**
   * Get all the eventTypes.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<EventTypeDTO> findAll() {
    log.debug("Request to get all EventTypes");
    return eventTypeRepository.findAll().stream()
        .map(eventTypeMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the eventTypes where TheTypeSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<EventTypeDTO> findAllWhereTheTypeSuperIsNull() {
    log.debug("Request to get all eventTypes where TheTypeSuper is null");
    return StreamSupport.stream(eventTypeRepository.findAll().spliterator(), false)
        .filter(eventType -> eventType.getTheTypeSuper() == null)
        .map(eventTypeMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one eventType by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<EventTypeDTO> findOne(Long id) {
    log.debug("Request to get EventType : {}", id);
    return eventTypeRepository.findById(id).map(eventTypeMapper::toDto);
  }

  /**
   * Delete the eventType by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete EventType : {}", id);
    eventTypeRepository.deleteById(id);
  }
}
