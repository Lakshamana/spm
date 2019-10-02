package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.OrganizationEstimation;
import br.ufpa.labes.spm.repository.OrganizationEstimationRepository;
import br.ufpa.labes.spm.service.dto.OrganizationEstimationDTO;
import br.ufpa.labes.spm.service.mapper.OrganizationEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link OrganizationEstimation}. */
@Service
@Transactional
public class OrganizationEstimationService {

  private final Logger log = LoggerFactory.getLogger(OrganizationEstimationService.class);

  private final OrganizationEstimationRepository organizationEstimationRepository;

  private final OrganizationEstimationMapper organizationEstimationMapper;

  public OrganizationEstimationService(
      OrganizationEstimationRepository organizationEstimationRepository,
      OrganizationEstimationMapper organizationEstimationMapper) {
    this.organizationEstimationRepository = organizationEstimationRepository;
    this.organizationEstimationMapper = organizationEstimationMapper;
  }

  /**
   * Save a organizationEstimation.
   *
   * @param organizationEstimationDTO the entity to save.
   * @return the persisted entity.
   */
  public OrganizationEstimationDTO save(OrganizationEstimationDTO organizationEstimationDTO) {
    log.debug("Request to save OrganizationEstimation : {}", organizationEstimationDTO);
    OrganizationEstimation organizationEstimation =
        organizationEstimationMapper.toEntity(organizationEstimationDTO);
    organizationEstimation = organizationEstimationRepository.save(organizationEstimation);
    return organizationEstimationMapper.toDto(organizationEstimation);
  }

  /**
   * Get all the organizationEstimations.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<OrganizationEstimationDTO> findAll() {
    log.debug("Request to get all OrganizationEstimations");
    return organizationEstimationRepository.findAll().stream()
        .map(organizationEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one organizationEstimation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<OrganizationEstimationDTO> findOne(Long id) {
    log.debug("Request to get OrganizationEstimation : {}", id);
    return organizationEstimationRepository.findById(id).map(organizationEstimationMapper::toDto);
  }

  /**
   * Delete the organizationEstimation by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete OrganizationEstimation : {}", id);
    organizationEstimationRepository.deleteById(id);
  }
}
