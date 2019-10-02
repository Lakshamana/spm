package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ResourceEvent;
import br.ufpa.labes.spm.repository.ResourceEventRepository;
import br.ufpa.labes.spm.service.dto.ResourceEventDTO;
import br.ufpa.labes.spm.service.mapper.ResourceEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ResourceEvent}. */
@Service
@Transactional
public class ResourceEventService {

  private final Logger log = LoggerFactory.getLogger(ResourceEventService.class);

  private final ResourceEventRepository resourceEventRepository;

  private final ResourceEventMapper resourceEventMapper;

  public ResourceEventService(
      ResourceEventRepository resourceEventRepository, ResourceEventMapper resourceEventMapper) {
    this.resourceEventRepository = resourceEventRepository;
    this.resourceEventMapper = resourceEventMapper;
  }

  /**
   * Save a resourceEvent.
   *
   * @param resourceEventDTO the entity to save.
   * @return the persisted entity.
   */
  public ResourceEventDTO save(ResourceEventDTO resourceEventDTO) {
    log.debug("Request to save ResourceEvent : {}", resourceEventDTO);
    ResourceEvent resourceEvent = resourceEventMapper.toEntity(resourceEventDTO);
    resourceEvent = resourceEventRepository.save(resourceEvent);
    return resourceEventMapper.toDto(resourceEvent);
  }

  /**
   * Get all the resourceEvents.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ResourceEventDTO> findAll() {
    log.debug("Request to get all ResourceEvents");
    return resourceEventRepository.findAll().stream()
        .map(resourceEventMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one resourceEvent by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ResourceEventDTO> findOne(Long id) {
    log.debug("Request to get ResourceEvent : {}", id);
    return resourceEventRepository.findById(id).map(resourceEventMapper::toDto);
  }

  /**
   * Delete the resourceEvent by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ResourceEvent : {}", id);
    resourceEventRepository.deleteById(id);
  }
}
