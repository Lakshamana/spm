package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.GlobalActivityEvent;
import br.ufpa.labes.spm.repository.GlobalActivityEventRepository;
import br.ufpa.labes.spm.service.dto.GlobalActivityEventDTO;
import br.ufpa.labes.spm.service.mapper.GlobalActivityEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link GlobalActivityEvent}. */
@Service
@Transactional
public class GlobalActivityEventService {

  private final Logger log = LoggerFactory.getLogger(GlobalActivityEventService.class);

  private final GlobalActivityEventRepository globalActivityEventRepository;

  private final GlobalActivityEventMapper globalActivityEventMapper;

  public GlobalActivityEventService(
      GlobalActivityEventRepository globalActivityEventRepository,
      GlobalActivityEventMapper globalActivityEventMapper) {
    this.globalActivityEventRepository = globalActivityEventRepository;
    this.globalActivityEventMapper = globalActivityEventMapper;
  }

  /**
   * Save a globalActivityEvent.
   *
   * @param globalActivityEventDTO the entity to save.
   * @return the persisted entity.
   */
  public GlobalActivityEventDTO save(GlobalActivityEventDTO globalActivityEventDTO) {
    log.debug("Request to save GlobalActivityEvent : {}", globalActivityEventDTO);
    GlobalActivityEvent globalActivityEvent =
        globalActivityEventMapper.toEntity(globalActivityEventDTO);
    globalActivityEvent = globalActivityEventRepository.save(globalActivityEvent);
    return globalActivityEventMapper.toDto(globalActivityEvent);
  }

  /**
   * Get all the globalActivityEvents.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<GlobalActivityEventDTO> findAll() {
    log.debug("Request to get all GlobalActivityEvents");
    return globalActivityEventRepository.findAll().stream()
        .map(globalActivityEventMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one globalActivityEvent by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<GlobalActivityEventDTO> findOne(Long id) {
    log.debug("Request to get GlobalActivityEvent : {}", id);
    return globalActivityEventRepository.findById(id).map(globalActivityEventMapper::toDto);
  }

  /**
   * Delete the globalActivityEvent by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete GlobalActivityEvent : {}", id);
    globalActivityEventRepository.deleteById(id);
  }
}
