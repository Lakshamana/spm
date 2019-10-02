package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.ArtifactDTO;

import java.util.List;
import java.util.Optional;

/** Service Interface for managing {@link br.ufpa.labes.spm.domain.Artifact}. */
public interface ArtifactService {

  /**
   * Save a artifact.
   *
   * @param artifactDTO the entity to save.
   * @return the persisted entity.
   */
  ArtifactDTO save(ArtifactDTO artifactDTO);

  /**
   * Get all the artifacts.
   *
   * @return the list of entities.
   */
  List<ArtifactDTO> findAll();

  /**
   * Get the "id" artifact.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<ArtifactDTO> findOne(Long id);

  /**
   * Delete the "id" artifact.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
