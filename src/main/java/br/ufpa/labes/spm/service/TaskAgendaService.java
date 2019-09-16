package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.TaskAgenda;
import br.ufpa.labes.spm.repository.TaskAgendaRepository;
import br.ufpa.labes.spm.service.dto.TaskAgendaDTO;
import br.ufpa.labes.spm.service.mapper.TaskAgendaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link TaskAgenda}. */
@Service
@Transactional
public class TaskAgendaService {

  private final Logger log = LoggerFactory.getLogger(TaskAgendaService.class);

  private final TaskAgendaRepository taskAgendaRepository;

  private final TaskAgendaMapper taskAgendaMapper;

  public TaskAgendaService(
      TaskAgendaRepository taskAgendaRepository, TaskAgendaMapper taskAgendaMapper) {
    this.taskAgendaRepository = taskAgendaRepository;
    this.taskAgendaMapper = taskAgendaMapper;
  }

  /**
   * Save a taskAgenda.
   *
   * @param taskAgendaDTO the entity to save.
   * @return the persisted entity.
   */
  public TaskAgendaDTO save(TaskAgendaDTO taskAgendaDTO) {
    log.debug("Request to save TaskAgenda : {}", taskAgendaDTO);
    TaskAgenda taskAgenda = taskAgendaMapper.toEntity(taskAgendaDTO);
    taskAgenda = taskAgendaRepository.save(taskAgenda);
    return taskAgendaMapper.toDto(taskAgenda);
  }

  /**
   * Get all the taskAgenda.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<TaskAgendaDTO> findAll() {
    log.debug("Request to get all TaskAgenda");
    return taskAgendaRepository.findAll().stream()
        .map(taskAgendaMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the taskAgenda where TheAgent is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<TaskAgendaDTO> findAllWhereTheAgentIsNull() {
    log.debug("Request to get all taskAgenda where TheAgent is null");
    return StreamSupport.stream(taskAgendaRepository.findAll().spliterator(), false)
        .filter(taskAgenda -> taskAgenda.getTheAgent() == null)
        .map(taskAgendaMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one taskAgenda by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<TaskAgendaDTO> findOne(Long id) {
    log.debug("Request to get TaskAgenda : {}", id);
    return taskAgendaRepository.findById(id).map(taskAgendaMapper::toDto);
  }

  /**
   * Delete the taskAgenda by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete TaskAgenda : {}", id);
    taskAgendaRepository.deleteById(id);
  }
}
