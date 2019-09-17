package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ProcessEvent;
import br.ufpa.labes.spm.repository.ProcessEventRepository;
import br.ufpa.labes.spm.service.dto.ProcessEventDTO;
import br.ufpa.labes.spm.service.mapper.ProcessEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ProcessEvent}. */
@Service
@Transactional
public class ProcessEventService {

  private final Logger log = LoggerFactory.getLogger(ProcessEventService.class);

  private final ProcessEventRepository processEventRepository;

  private final ProcessEventMapper processEventMapper;

  public ProcessEventService(
      ProcessEventRepository processEventRepository, ProcessEventMapper processEventMapper) {
    this.processEventRepository = processEventRepository;
    this.processEventMapper = processEventMapper;
  }

  /**
   * Save a processEvent.
   *
   * @param processEventDTO the entity to save.
   * @return the persisted entity.
   */
  public ProcessEventDTO save(ProcessEventDTO processEventDTO) {
    log.debug("Request to save ProcessEvent : {}", processEventDTO);
    ProcessEvent processEvent = processEventMapper.toEntity(processEventDTO);
    processEvent = processEventRepository.save(processEvent);
    return processEventMapper.toDto(processEvent);
  }

  /**
   * Get all the processEvents.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ProcessEventDTO> findAll() {
    log.debug("Request to get all ProcessEvents");
    return processEventRepository.findAll().stream()
        .map(processEventMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one processEvent by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ProcessEventDTO> findOne(Long id) {
    log.debug("Request to get ProcessEvent : {}", id);
    return processEventRepository.findById(id).map(processEventMapper::toDto);
  }

  /**
   * Delete the processEvent by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ProcessEvent : {}", id);
    processEventRepository.deleteById(id);
  }
}
