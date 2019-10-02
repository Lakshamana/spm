package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.EstimationService;
import br.ufpa.labes.spm.domain.Estimation;
import br.ufpa.labes.spm.repository.EstimationRepository;
import br.ufpa.labes.spm.service.dto.EstimationDTO;
import br.ufpa.labes.spm.service.mapper.EstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Estimation}. */
@Service
@Transactional
public class EstimationServiceImpl implements EstimationService {

  private final Logger log = LoggerFactory.getLogger(EstimationServiceImpl.class);

  private final EstimationRepository estimationRepository;

  private final EstimationMapper estimationMapper;

  public EstimationServiceImpl(
      EstimationRepository estimationRepository, EstimationMapper estimationMapper) {
    this.estimationRepository = estimationRepository;
    this.estimationMapper = estimationMapper;
  }

  /**
   * Save a estimation.
   *
   * @param estimationDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public EstimationDTO save(EstimationDTO estimationDTO) {
    log.debug("Request to save Estimation : {}", estimationDTO);
    Estimation estimation = estimationMapper.toEntity(estimationDTO);
    estimation = estimationRepository.save(estimation);
    return estimationMapper.toDto(estimation);
  }

  /**
   * Get all the estimations.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<EstimationDTO> findAll() {
    log.debug("Request to get all Estimations");
    return estimationRepository.findAll().stream()
        .map(estimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one estimation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<EstimationDTO> findOne(Long id) {
    log.debug("Request to get Estimation : {}", id);
    return estimationRepository.findById(id).map(estimationMapper::toDto);
  }

  /**
   * Delete the estimation by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Estimation : {}", id);
    estimationRepository.deleteById(id);
  }
}
