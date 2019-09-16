package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.ToolDefinitionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/** Service Interface for managing {@link br.ufpa.labes.spm.domain.ToolDefinition}. */
public interface ToolDefinitionService {

  /**
   * Save a toolDefinition.
   *
   * @param toolDefinitionDTO the entity to save.
   * @return the persisted entity.
   */
  ToolDefinitionDTO save(ToolDefinitionDTO toolDefinitionDTO);

  /**
   * Get all the toolDefinitions.
   *
   * @return the list of entities.
   */
  List<ToolDefinitionDTO> findAll();

  /**
   * Get all the toolDefinitions with eager load of many-to-many relationships.
   *
   * @return the list of entities.
   */
  Page<ToolDefinitionDTO> findAllWithEagerRelationships(Pageable pageable);

  /**
   * Get the "id" toolDefinition.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<ToolDefinitionDTO> findOne(Long id);

  /**
   * Delete the "id" toolDefinition.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
