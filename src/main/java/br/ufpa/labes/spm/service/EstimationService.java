package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.EstimationDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.Estimation}.
 */
public interface EstimationService {

    /**
     * Save a estimation.
     *
     * @param estimationDTO the entity to save.
     * @return the persisted entity.
     */
    EstimationDTO save(EstimationDTO estimationDTO);

    /**
     * Get all the estimations.
     *
     * @return the list of entities.
     */
    List<EstimationDTO> findAll();


    /**
     * Get the "id" estimation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EstimationDTO> findOne(Long id);

    /**
     * Delete the "id" estimation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
