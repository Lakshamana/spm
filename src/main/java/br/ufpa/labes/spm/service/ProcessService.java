package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.ProcessDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.Process}.
 */
public interface ProcessService {

    /**
     * Save a process.
     *
     * @param processDTO the entity to save.
     * @return the persisted entity.
     */
    ProcessDTO save(ProcessDTO processDTO);

    /**
     * Get all the processes.
     *
     * @return the list of entities.
     */
    List<ProcessDTO> findAll();
    /**
     * Get all the ProcessDTO where TheLog is {@code null}.
     *
     * @return the list of entities.
     */
    List<ProcessDTO> findAllWhereTheLogIsNull();

    /**
     * Get all the processes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ProcessDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" process.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcessDTO> findOne(Long id);

    /**
     * Delete the "id" process.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
