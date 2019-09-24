package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.WorkGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.WorkGroup}.
 */
public interface WorkGroupService {

    /**
     * Save a WorkGroup.
     *
     * @param WorkGroupDTO the entity to save.
     * @return the persisted entity.
     */
    WorkWorkGroupDTO save(WorkWorkGroupDTO WorkGroupDTO);

    /**
     * Get all the WorkGroups.
     *
     * @return the list of entities.
     */
    List<WorkGroupDTO> findAll();


    /**
     * Get the "id" WorkGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkGroupDTO> findOne(Long id);

    /**
     * Delete the "id" WorkGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
