package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.WorkGroupDTO;

import java.util.List;
import java.util.Optional;

/** Service Interface for managing {@link br.ufpa.labes.spm.domain.WorkGroup}. */
public interface WorkGroupService {

  /**
   * Save a workGroup.
   *
   * @param workGroupDTO the entity to save.
   * @return the persisted entity.
   */
  WorkGroupDTO save(WorkGroupDTO workGroupDTO);

  /**
   * Get all the workGroups.
   *
   * @return the list of entities.
   */
  List<WorkGroupDTO> findAll();

  /**
   * Get the "id" workGroup.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<WorkGroupDTO> findOne(Long id);

  /**
   * Delete the "id" workGroup.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
