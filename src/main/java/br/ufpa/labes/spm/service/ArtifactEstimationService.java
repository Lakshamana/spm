package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ArtifactEstimation;
import br.ufpa.labes.spm.repository.ArtifactEstimationRepository;
import br.ufpa.labes.spm.service.dto.ArtifactEstimationDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link ArtifactEstimation}. */
@Service
@Transactional
public class ArtifactEstimationService {

  private final Logger log = LoggerFactory.getLogger(ArtifactEstimationService.class);

  private final ArtifactEstimationRepository artifactEstimationRepository;

  private final ArtifactEstimationMapper artifactEstimationMapper;

  public ArtifactEstimationService(
      ArtifactEstimationRepository artifactEstimationRepository,
      ArtifactEstimationMapper artifactEstimationMapper) {
    this.artifactEstimationRepository = artifactEstimationRepository;
    this.artifactEstimationMapper = artifactEstimationMapper;
  }

  /**
   * Save a artifactEstimation.
   *
   * @param artifactEstimationDTO the entity to save.
   * @return the persisted entity.
   */
  public ArtifactEstimationDTO save(ArtifactEstimationDTO artifactEstimationDTO) {
    log.debug("Request to save ArtifactEstimation : {}", artifactEstimationDTO);
    ArtifactEstimation artifactEstimation =
        artifactEstimationMapper.toEntity(artifactEstimationDTO);
    artifactEstimation = artifactEstimationRepository.save(artifactEstimation);
    return artifactEstimationMapper.toDto(artifactEstimation);
  }

  /**
   * Get all the artifactEstimations.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ArtifactEstimationDTO> findAll() {
    log.debug("Request to get all ArtifactEstimations");
    return artifactEstimationRepository.findAll().stream()
        .map(artifactEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the artifactEstimations where TheEstimationSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ArtifactEstimationDTO> findAllWhereTheEstimationSuperIsNull() {
    log.debug("Request to get all artifactEstimations where TheEstimationSuper is null");
    return StreamSupport.stream(artifactEstimationRepository.findAll().spliterator(), false)
        .filter(artifactEstimation -> artifactEstimation.getTheEstimationSuper() == null)
        .map(artifactEstimationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one artifactEstimation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ArtifactEstimationDTO> findOne(Long id) {
    log.debug("Request to get ArtifactEstimation : {}", id);
    return artifactEstimationRepository.findById(id).map(artifactEstimationMapper::toDto);
  }

  /**
   * Delete the artifactEstimation by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ArtifactEstimation : {}", id);
    artifactEstimationRepository.deleteById(id);
  }
}
