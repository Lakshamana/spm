package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;

import java.util.List;
import java.util.Optional;

/** Service Interface for managing {@link br.ufpa.labes.spm.domain.MetricDefinition}. */
public interface MetricDefinitionService {

  /**
   * Save a metricDefinition.
   *
   * @param metricDefinitionDTO the entity to save.
   * @return the persisted entity.
   */
  MetricDefinitionDTO save(MetricDefinitionDTO metricDefinitionDTO);

  /**
   * Get all the metricDefinitions.
   *
   * @return the list of entities.
   */
  List<MetricDefinitionDTO> findAll();

  /**
   * Get the "id" metricDefinition.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<MetricDefinitionDTO> findOne(Long id);

  /**
   * Delete the "id" metricDefinition.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
