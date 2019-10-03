package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.VCSRepositoryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.VCSRepository}.
 */
public interface VCSRepositoryService {

    /**
     * Save a vCSRepository.
     *
     * @param vCSRepositoryDTO the entity to save.
     * @return the persisted entity.
     */
    VCSRepositoryDTO save(VCSRepositoryDTO vCSRepositoryDTO);

    /**
     * Get all the vCSRepositories.
     *
     * @return the list of entities.
     */
    List<VCSRepositoryDTO> findAll();


    /**
     * Get the "id" vCSRepository.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VCSRepositoryDTO> findOne(Long id);

    /**
     * Delete the "id" vCSRepository.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
