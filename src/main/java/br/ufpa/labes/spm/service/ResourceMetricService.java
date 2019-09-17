package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ResourceMetric;
import br.ufpa.labes.spm.repository.ResourceMetricRepository;
import br.ufpa.labes.spm.service.dto.ResourceMetricDTO;
import br.ufpa.labes.spm.service.mapper.ResourceMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ResourceMetric}. */
@Service
@Transactional
public class ResourceMetricService {

  private final Logger log = LoggerFactory.getLogger(ResourceMetricService.class);

  private final ResourceMetricRepository resourceMetricRepository;

  private final ResourceMetricMapper resourceMetricMapper;

  public ResourceMetricService(
      ResourceMetricRepository resourceMetricRepository,
      ResourceMetricMapper resourceMetricMapper) {
    this.resourceMetricRepository = resourceMetricRepository;
    this.resourceMetricMapper = resourceMetricMapper;
  }

  /**
   * Save a resourceMetric.
   *
   * @param resourceMetricDTO the entity to save.
   * @return the persisted entity.
   */
  public ResourceMetricDTO save(ResourceMetricDTO resourceMetricDTO) {
    log.debug("Request to save ResourceMetric : {}", resourceMetricDTO);
    ResourceMetric resourceMetric = resourceMetricMapper.toEntity(resourceMetricDTO);
    resourceMetric = resourceMetricRepository.save(resourceMetric);
    return resourceMetricMapper.toDto(resourceMetric);
  }

  /**
   * Get all the resourceMetrics.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ResourceMetricDTO> findAll() {
    log.debug("Request to get all ResourceMetrics");
    return resourceMetricRepository.findAll().stream()
        .map(resourceMetricMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one resourceMetric by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ResourceMetricDTO> findOne(Long id) {
    log.debug("Request to get ResourceMetric : {}", id);
    return resourceMetricRepository.findById(id).map(resourceMetricMapper::toDto);
  }

  /**
   * Delete the resourceMetric by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ResourceMetric : {}", id);
    resourceMetricRepository.deleteById(id);
  }
}
