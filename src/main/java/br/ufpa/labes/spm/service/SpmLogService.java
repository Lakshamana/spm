package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.SpmLog;
import br.ufpa.labes.spm.repository.SpmLogRepository;
import br.ufpa.labes.spm.service.dto.SpmLogDTO;
import br.ufpa.labes.spm.service.mapper.SpmLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link SpmLog}. */
@Service
@Transactional
public class SpmLogService {

  private final Logger log = LoggerFactory.getLogger(SpmLogService.class);

  private final SpmLogRepository spmLogRepository;

  private final SpmLogMapper spmLogMapper;

  public SpmLogService(SpmLogRepository spmLogRepository, SpmLogMapper spmLogMapper) {
    this.spmLogRepository = spmLogRepository;
    this.spmLogMapper = spmLogMapper;
  }

  /**
   * Save a spmLog.
   *
   * @param spmLogDTO the entity to save.
   * @return the persisted entity.
   */
  public SpmLogDTO save(SpmLogDTO spmLogDTO) {
    log.debug("Request to save SpmLog : {}", spmLogDTO);
    SpmLog spmLog = spmLogMapper.toEntity(spmLogDTO);
    spmLog = spmLogRepository.save(spmLog);
    return spmLogMapper.toDto(spmLog);
  }

  /**
   * Get all the spmLogs.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<SpmLogDTO> findAll() {
    log.debug("Request to get all SpmLogs");
    return spmLogRepository.findAll().stream()
        .map(spmLogMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one spmLog by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<SpmLogDTO> findOne(Long id) {
    log.debug("Request to get SpmLog : {}", id);
    return spmLogRepository.findById(id).map(spmLogMapper::toDto);
  }

  /**
   * Delete the spmLog by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete SpmLog : {}", id);
    spmLogRepository.deleteById(id);
  }
}
