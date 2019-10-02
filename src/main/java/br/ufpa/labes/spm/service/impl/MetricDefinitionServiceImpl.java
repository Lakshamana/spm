package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.MetricDefinitionService;
import br.ufpa.labes.spm.domain.MetricDefinition;
import br.ufpa.labes.spm.repository.MetricDefinitionRepository;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;
import br.ufpa.labes.spm.service.mapper.MetricDefinitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link MetricDefinition}. */
@Service
@Transactional
public class MetricDefinitionServiceImpl implements MetricDefinitionService {

  private final Logger log = LoggerFactory.getLogger(MetricDefinitionServiceImpl.class);

  private final MetricDefinitionRepository metricDefinitionRepository;

  private final MetricDefinitionMapper metricDefinitionMapper;

  public MetricDefinitionServiceImpl(
      MetricDefinitionRepository metricDefinitionRepository,
      MetricDefinitionMapper metricDefinitionMapper) {
    this.metricDefinitionRepository = metricDefinitionRepository;
    this.metricDefinitionMapper = metricDefinitionMapper;
  }

  /**
   * Save a metricDefinition.
   *
   * @param metricDefinitionDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public MetricDefinitionDTO save(MetricDefinitionDTO metricDefinitionDTO) {
    log.debug("Request to save MetricDefinition : {}", metricDefinitionDTO);
    MetricDefinition metricDefinition = metricDefinitionMapper.toEntity(metricDefinitionDTO);
    metricDefinition = metricDefinitionRepository.save(metricDefinition);
    return metricDefinitionMapper.toDto(metricDefinition);
  }

  /**
   * Get all the metricDefinitions.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<MetricDefinitionDTO> findAll() {
    log.debug("Request to get all MetricDefinitions");
    return metricDefinitionRepository.findAll().stream()
        .map(metricDefinitionMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one metricDefinition by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<MetricDefinitionDTO> findOne(Long id) {
    log.debug("Request to get MetricDefinition : {}", id);
    return metricDefinitionRepository.findById(id).map(metricDefinitionMapper::toDto);
  }

  /**
   * Delete the metricDefinition by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete MetricDefinition : {}", id);
    metricDefinitionRepository.deleteById(id);
  }
}
