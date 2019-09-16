package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ProcessEstimation;
import br.ufpa.labes.spm.repository.ProcessEstimationRepository;
import br.ufpa.labes.spm.service.dto.ProcessEstimationDTO;
import br.ufpa.labes.spm.service.mapper.ProcessEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link ProcessEstimation}. */
@Service
@Transactional
public class ProcessEstimationService {

  private final Logger log = LoggerFactory.getLogger(ProcessEstimationService.class);

  private final ProcessEstimationRepository processEstimationRepository;

  private final ProcessEstimationMapper processEstimationMapper;

  public ProcessEstimationService(
      ProcessEstimationRepository processEstimationRepository,
      ProcessEstimationMapper processEstimationMapper) {
    this.processEstimationRepository = processEstimationRepository;
    this.processEstimationMapper = processEstimationMapper;
  }

  /**
   * Save a processEstimation.
   *
   * @param processEstimationDTO the entity to save.
   * @return the persisted entity.
   */
  public ProcessEstimationDTO save(ProcessEstimationDTO processEstimationDTO) {
    log.debug("Request to save ProcessEstimation : {}", processEstimationDTO);
    ProcessEstimation processEstimation = processEstimationMapper.toEntity(processEstimationDTO);
    processEstimation = processEstimationRepository.save(processEstimation);
    return processEstimationMapper.toDto(processEstimation);
  }

  /**
   * Get all the processEstimations.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ProcessEstimationDTO> findAll() {
    log.debug("Request to get all ProcessEstimations");
    return processEstimationRepository.findAll().stream()
        .map(processEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the processEstimations where TheEstimationSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ProcessEstimationDTO> findAllWhereTheEstimationSuperIsNull() {
    log.debug("Request to get all processEstimations where TheEstimationSuper is null");
    return StreamSupport.stream(processEstimationRepository.findAll().spliterator(), false)
        .filter(processEstimation -> processEstimation.getTheEstimationSuper() == null)
        .map(processEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one processEstimation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ProcessEstimationDTO> findOne(Long id) {
    log.debug("Request to get ProcessEstimation : {}", id);
    return processEstimationRepository.findById(id).map(processEstimationMapper::toDto);
  }

  /**
   * Delete the processEstimation by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ProcessEstimation : {}", id);
    processEstimationRepository.deleteById(id);
  }
}
