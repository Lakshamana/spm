package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ResourceEstimation;
import br.ufpa.labes.spm.repository.ResourceEstimationRepository;
import br.ufpa.labes.spm.service.dto.ResourceEstimationDTO;
import br.ufpa.labes.spm.service.mapper.ResourceEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ResourceEstimation}. */
@Service
@Transactional
public class ResourceEstimationService {

  private final Logger log = LoggerFactory.getLogger(ResourceEstimationService.class);

  private final ResourceEstimationRepository resourceEstimationRepository;

  private final ResourceEstimationMapper resourceEstimationMapper;

  public ResourceEstimationService(
      ResourceEstimationRepository resourceEstimationRepository,
      ResourceEstimationMapper resourceEstimationMapper) {
    this.resourceEstimationRepository = resourceEstimationRepository;
    this.resourceEstimationMapper = resourceEstimationMapper;
  }

  /**
   * Save a resourceEstimation.
   *
   * @param resourceEstimationDTO the entity to save.
   * @return the persisted entity.
   */
  public ResourceEstimationDTO save(ResourceEstimationDTO resourceEstimationDTO) {
    log.debug("Request to save ResourceEstimation : {}", resourceEstimationDTO);
    ResourceEstimation resourceEstimation =
        resourceEstimationMapper.toEntity(resourceEstimationDTO);
    resourceEstimation = resourceEstimationRepository.save(resourceEstimation);
    return resourceEstimationMapper.toDto(resourceEstimation);
  }

  /**
   * Get all the resourceEstimations.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ResourceEstimationDTO> findAll() {
    log.debug("Request to get all ResourceEstimations");
    return resourceEstimationRepository.findAll().stream()
        .map(resourceEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one resourceEstimation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ResourceEstimationDTO> findOne(Long id) {
    log.debug("Request to get ResourceEstimation : {}", id);
    return resourceEstimationRepository.findById(id).map(resourceEstimationMapper::toDto);
  }

  /**
   * Delete the resourceEstimation by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ResourceEstimation : {}", id);
    resourceEstimationRepository.deleteById(id);
  }
}
