package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.LogEntry;
import br.ufpa.labes.spm.repository.LogEntryRepository;
import br.ufpa.labes.spm.service.dto.LogEntryDTO;
import br.ufpa.labes.spm.service.mapper.LogEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link LogEntry}. */
@Service
@Transactional
public class LogEntryService {

  private final Logger log = LoggerFactory.getLogger(LogEntryService.class);

  private final LogEntryRepository logEntryRepository;

  private final LogEntryMapper logEntryMapper;

  public LogEntryService(LogEntryRepository logEntryRepository, LogEntryMapper logEntryMapper) {
    this.logEntryRepository = logEntryRepository;
    this.logEntryMapper = logEntryMapper;
  }

  /**
   * Save a logEntry.
   *
   * @param logEntryDTO the entity to save.
   * @return the persisted entity.
   */
  public LogEntryDTO save(LogEntryDTO logEntryDTO) {
    log.debug("Request to save LogEntry : {}", logEntryDTO);
    LogEntry logEntry = logEntryMapper.toEntity(logEntryDTO);
    logEntry = logEntryRepository.save(logEntry);
    return logEntryMapper.toDto(logEntry);
  }

  /**
   * Get all the logEntries.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<LogEntryDTO> findAll() {
    log.debug("Request to get all LogEntries");
    return logEntryRepository.findAll().stream()
        .map(logEntryMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one logEntry by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<LogEntryDTO> findOne(Long id) {
    log.debug("Request to get LogEntry : {}", id);
    return logEntryRepository.findById(id).map(logEntryMapper::toDto);
  }

  /**
   * Delete the logEntry by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete LogEntry : {}", id);
    logEntryRepository.deleteById(id);
  }
}
