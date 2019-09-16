package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.TemplateDTO;

import java.util.List;
import java.util.Optional;

/** Service Interface for managing {@link br.ufpa.labes.spm.domain.Template}. */
public interface TemplateService {

  /**
   * Save a template.
   *
   * @param templateDTO the entity to save.
   * @return the persisted entity.
   */
  TemplateDTO save(TemplateDTO templateDTO);

  /**
   * Get all the templates.
   *
   * @return the list of entities.
   */
  List<TemplateDTO> findAll();
  /**
   * Get all the TemplateDTO where TheProcessSuper is {@code null}.
   *
   * @return the list of entities.
   */
  List<TemplateDTO> findAllWhereTheProcessSuperIsNull();

  /**
   * Get the "id" template.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<TemplateDTO> findOne(Long id);

  /**
   * Delete the "id" template.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
