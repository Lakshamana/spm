package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.CompanyDTO;

import java.util.List;
import java.util.Optional;

/** Service Interface for managing {@link br.ufpa.labes.spm.domain.Company}. */
public interface CompanyService {

  /**
   * Save a company.
   *
   * @param companyDTO the entity to save.
   * @return the persisted entity.
   */
  CompanyDTO save(CompanyDTO companyDTO);

  /**
   * Get all the companies.
   *
   * @return the list of entities.
   */
  List<CompanyDTO> findAll();
  /**
   * Get all the CompanyDTO where TheDriver is {@code null}.
   *
   * @return the list of entities.
   */
  List<CompanyDTO> findAllWhereTheDriverIsNull();

  /**
   * Get the "id" company.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<CompanyDTO> findOne(Long id);

  /**
   * Delete the "id" company.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
