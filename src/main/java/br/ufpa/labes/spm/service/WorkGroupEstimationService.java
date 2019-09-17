package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkGroupEstimation;
import br.ufpa.labes.spm.repository.WorkGroupEstimationRepository;
import br.ufpa.labes.spm.service.dto.WorkGroupEstimationDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link WorkGroupEstimation}. */
@Service
@Transactional
public class WorkGroupEstimationService {

  private final Logger log = LoggerFactory.getLogger(WorkGroupEstimationService.class);

  private final WorkGroupEstimationRepository workGroupEstimationRepository;

  private final WorkGroupEstimationMapper workGroupEstimationMapper;

  public WorkGroupEstimationService(
      WorkGroupEstimationRepository workGroupEstimationRepository,
      WorkGroupEstimationMapper workGroupEstimationMapper) {
    this.workGroupEstimationRepository = workGroupEstimationRepository;
    this.workGroupEstimationMapper = workGroupEstimationMapper;
  }

  /**
   * Save a workGroupEstimation.
   *
   * @param workGroupEstimationDTO the entity to save.
   * @return the persisted entity.
   */
  public WorkGroupEstimationDTO save(WorkGroupEstimationDTO workGroupEstimationDTO) {
    log.debug("Request to save WorkGroupEstimation : {}", workGroupEstimationDTO);
    WorkGroupEstimation workGroupEstimation =
        workGroupEstimationMapper.toEntity(workGroupEstimationDTO);
    workGroupEstimation = workGroupEstimationRepository.save(workGroupEstimation);
    return workGroupEstimationMapper.toDto(workGroupEstimation);
  }

  /**
   * Get all the workGroupEstimations.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<WorkGroupEstimationDTO> findAll() {
    log.debug("Request to get all WorkGroupEstimations");
    return workGroupEstimationRepository.findAll().stream()
        .map(workGroupEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one workGroupEstimation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<WorkGroupEstimationDTO> findOne(Long id) {
    log.debug("Request to get WorkGroupEstimation : {}", id);
    return workGroupEstimationRepository.findById(id).map(workGroupEstimationMapper::toDto);
  }

  /**
   * Delete the workGroupEstimation by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete WorkGroupEstimation : {}", id);
    workGroupEstimationRepository.deleteById(id);
  }
}
