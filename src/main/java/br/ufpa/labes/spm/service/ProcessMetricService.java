package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ProcessMetric;
import br.ufpa.labes.spm.repository.ProcessMetricRepository;
import br.ufpa.labes.spm.service.dto.ProcessMetricDTO;
import br.ufpa.labes.spm.service.mapper.ProcessMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ProcessMetric}. */
@Service
@Transactional
public class ProcessMetricService {

  private final Logger log = LoggerFactory.getLogger(ProcessMetricService.class);

  private final ProcessMetricRepository processMetricRepository;

  private final ProcessMetricMapper processMetricMapper;

  public ProcessMetricService(
      ProcessMetricRepository processMetricRepository, ProcessMetricMapper processMetricMapper) {
    this.processMetricRepository = processMetricRepository;
    this.processMetricMapper = processMetricMapper;
  }

  /**
   * Save a processMetric.
   *
   * @param processMetricDTO the entity to save.
   * @return the persisted entity.
   */
  public ProcessMetricDTO save(ProcessMetricDTO processMetricDTO) {
    log.debug("Request to save ProcessMetric : {}", processMetricDTO);
    ProcessMetric processMetric = processMetricMapper.toEntity(processMetricDTO);
    processMetric = processMetricRepository.save(processMetric);
    return processMetricMapper.toDto(processMetric);
  }

  /**
   * Get all the processMetrics.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ProcessMetricDTO> findAll() {
    log.debug("Request to get all ProcessMetrics");
    return processMetricRepository.findAll().stream()
        .map(processMetricMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one processMetric by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ProcessMetricDTO> findOne(Long id) {
    log.debug("Request to get ProcessMetric : {}", id);
    return processMetricRepository.findById(id).map(processMetricMapper::toDto);
  }

  /**
   * Delete the processMetric by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ProcessMetric : {}", id);
    processMetricRepository.deleteById(id);
  }
}
