package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.DevelopingSystemDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.DevelopingSystem}.
 */
public interface DevelopingSystemService {

    /**
     * Save a developingSystem.
     *
     * @param developingSystemDTO the entity to save.
     * @return the persisted entity.
     */
    DevelopingSystemDTO save(DevelopingSystemDTO developingSystemDTO);

    /**
     * Get all the developingSystems.
     *
     * @return the list of entities.
     */
    List<DevelopingSystemDTO> findAll();


    /**
     * Get the "id" developingSystem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DevelopingSystemDTO> findOne(Long id);

    /**
     * Delete the "id" developingSystem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
