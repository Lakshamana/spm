package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.MetricDefinitionUnit;
import br.ufpa.labes.spm.repository.MetricDefinitionUnitRepository;
import br.ufpa.labes.spm.service.dto.MetricDefinitionUnitDTO;
import br.ufpa.labes.spm.service.mapper.MetricDefinitionUnitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link MetricDefinitionUnit}. */
@Service
@Transactional
public class MetricDefinitionUnitService {

  private final Logger log = LoggerFactory.getLogger(MetricDefinitionUnitService.class);

  private final MetricDefinitionUnitRepository metricDefinitionUnitRepository;

  private final MetricDefinitionUnitMapper metricDefinitionUnitMapper;

  public MetricDefinitionUnitService(
      MetricDefinitionUnitRepository metricDefinitionUnitRepository,
      MetricDefinitionUnitMapper metricDefinitionUnitMapper) {
    this.metricDefinitionUnitRepository = metricDefinitionUnitRepository;
    this.metricDefinitionUnitMapper = metricDefinitionUnitMapper;
  }

  /**
   * Save a metricDefinitionUnit.
   *
   * @param metricDefinitionUnitDTO the entity to save.
   * @return the persisted entity.
   */
  public MetricDefinitionUnitDTO save(MetricDefinitionUnitDTO metricDefinitionUnitDTO) {
    log.debug("Request to save MetricDefinitionUnit : {}", metricDefinitionUnitDTO);
    MetricDefinitionUnit metricDefinitionUnit =
        metricDefinitionUnitMapper.toEntity(metricDefinitionUnitDTO);
    metricDefinitionUnit = metricDefinitionUnitRepository.save(metricDefinitionUnit);
    return metricDefinitionUnitMapper.toDto(metricDefinitionUnit);
  }

  /**
   * Get all the metricDefinitionUnits.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<MetricDefinitionUnitDTO> findAll() {
    log.debug("Request to get all MetricDefinitionUnits");
    return metricDefinitionUnitRepository.findAll().stream()
        .map(metricDefinitionUnitMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one metricDefinitionUnit by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<MetricDefinitionUnitDTO> findOne(Long id) {
    log.debug("Request to get MetricDefinitionUnit : {}", id);
    return metricDefinitionUnitRepository.findById(id).map(metricDefinitionUnitMapper::toDto);
  }

  /**
   * Delete the metricDefinitionUnit by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete MetricDefinitionUnit : {}", id);
    metricDefinitionUnitRepository.deleteById(id);
  }
}
