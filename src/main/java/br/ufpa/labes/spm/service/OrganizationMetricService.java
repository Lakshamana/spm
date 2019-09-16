package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.OrganizationMetric;
import br.ufpa.labes.spm.repository.OrganizationMetricRepository;
import br.ufpa.labes.spm.service.dto.OrganizationMetricDTO;
import br.ufpa.labes.spm.service.mapper.OrganizationMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link OrganizationMetric}. */
@Service
@Transactional
public class OrganizationMetricService {

  private final Logger log = LoggerFactory.getLogger(OrganizationMetricService.class);

  private final OrganizationMetricRepository organizationMetricRepository;

  private final OrganizationMetricMapper organizationMetricMapper;

  public OrganizationMetricService(
      OrganizationMetricRepository organizationMetricRepository,
      OrganizationMetricMapper organizationMetricMapper) {
    this.organizationMetricRepository = organizationMetricRepository;
    this.organizationMetricMapper = organizationMetricMapper;
  }

  /**
   * Save a organizationMetric.
   *
   * @param organizationMetricDTO the entity to save.
   * @return the persisted entity.
   */
  public OrganizationMetricDTO save(OrganizationMetricDTO organizationMetricDTO) {
    log.debug("Request to save OrganizationMetric : {}", organizationMetricDTO);
    OrganizationMetric organizationMetric =
        organizationMetricMapper.toEntity(organizationMetricDTO);
    organizationMetric = organizationMetricRepository.save(organizationMetric);
    return organizationMetricMapper.toDto(organizationMetric);
  }

  /**
   * Get all the organizationMetrics.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<OrganizationMetricDTO> findAll() {
    log.debug("Request to get all OrganizationMetrics");
    return organizationMetricRepository.findAll().stream()
        .map(organizationMetricMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the organizationMetrics where TheMetricSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<OrganizationMetricDTO> findAllWhereTheMetricSuperIsNull() {
    log.debug("Request to get all organizationMetrics where TheMetricSuper is null");
    return StreamSupport.stream(organizationMetricRepository.findAll().spliterator(), false)
        .filter(organizationMetric -> organizationMetric.getTheMetricSuper() == null)
        .map(organizationMetricMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one organizationMetric by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<OrganizationMetricDTO> findOne(Long id) {
    log.debug("Request to get OrganizationMetric : {}", id);
    return organizationMetricRepository.findById(id).map(organizationMetricMapper::toDto);
  }

  /**
   * Delete the organizationMetric by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete OrganizationMetric : {}", id);
    organizationMetricRepository.deleteById(id);
  }
}
