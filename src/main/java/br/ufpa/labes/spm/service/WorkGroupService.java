package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.WorkWorkGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.WorkWorkGroup}.
 */
public interface WorkWorkGroupService {

    /**
     * Save a workWorkGroup.
     *
     * @param workWorkGroupDTO the entity to save.
     * @return the persisted entity.
     */
    WorkWorkGroupDTO save(WorkWorkGroupDTO workWorkGroupDTO);

    /**
     * Get all the workWorkGroups.
     *
     * @return the list of entities.
     */
    List<WorkWorkGroupDTO> findAll();


    /**
     * Get the "id" workWorkGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkWorkGroupDTO> findOne(Long id);

    /**
     * Delete the "id" workWorkGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
