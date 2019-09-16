package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ArtifactMetric;
import br.ufpa.labes.spm.repository.ArtifactMetricRepository;
import br.ufpa.labes.spm.service.dto.ArtifactMetricDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link ArtifactMetric}. */
@Service
@Transactional
public class ArtifactMetricService {

  private final Logger log = LoggerFactory.getLogger(ArtifactMetricService.class);

  private final ArtifactMetricRepository artifactMetricRepository;

  private final ArtifactMetricMapper artifactMetricMapper;

  public ArtifactMetricService(
      ArtifactMetricRepository artifactMetricRepository,
      ArtifactMetricMapper artifactMetricMapper) {
    this.artifactMetricRepository = artifactMetricRepository;
    this.artifactMetricMapper = artifactMetricMapper;
  }

  /**
   * Save a artifactMetric.
   *
   * @param artifactMetricDTO the entity to save.
   * @return the persisted entity.
   */
  public ArtifactMetricDTO save(ArtifactMetricDTO artifactMetricDTO) {
    log.debug("Request to save ArtifactMetric : {}", artifactMetricDTO);
    ArtifactMetric artifactMetric = artifactMetricMapper.toEntity(artifactMetricDTO);
    artifactMetric = artifactMetricRepository.save(artifactMetric);
    return artifactMetricMapper.toDto(artifactMetric);
  }

  /**
   * Get all the artifactMetrics.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ArtifactMetricDTO> findAll() {
    log.debug("Request to get all ArtifactMetrics");
    return artifactMetricRepository.findAll().stream()
        .map(artifactMetricMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the artifactMetrics where TheMetricSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ArtifactMetricDTO> findAllWhereTheMetricSuperIsNull() {
    log.debug("Request to get all artifactMetrics where TheMetricSuper is null");
    return StreamSupport.stream(artifactMetricRepository.findAll().spliterator(), false)
        .filter(artifactMetric -> artifactMetric.getTheMetricSuper() == null)
        .map(artifactMetricMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one artifactMetric by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ArtifactMetricDTO> findOne(Long id) {
    log.debug("Request to get ArtifactMetric : {}", id);
    return artifactMetricRepository.findById(id).map(artifactMetricMapper::toDto);
  }

  /**
   * Delete the artifactMetric by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ArtifactMetric : {}", id);
    artifactMetricRepository.deleteById(id);
  }
}
