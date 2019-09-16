package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;

import java.util.List;
import java.util.Optional;

/** Service Interface for managing {@link br.ufpa.labes.spm.domain.SpmConfiguration}. */
public interface SpmConfigurationService {

  /**
   * Save a spmConfiguration.
   *
   * @param spmConfigurationDTO the entity to save.
   * @return the persisted entity.
   */
  SpmConfigurationDTO save(SpmConfigurationDTO spmConfigurationDTO);

  /**
   * Get all the spmConfigurations.
   *
   * @return the list of entities.
   */
  List<SpmConfigurationDTO> findAll();
  /**
   * Get all the SpmConfigurationDTO where TheAgent is {@code null}.
   *
   * @return the list of entities.
   */
  List<SpmConfigurationDTO> findAllWhereTheAgentIsNull();

  /**
   * Get the "id" spmConfiguration.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<SpmConfigurationDTO> findOne(Long id);

  /**
   * Delete the "id" spmConfiguration.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
