package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.ArtifactService;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.repository.ArtifactRepository;
import br.ufpa.labes.spm.service.dto.ArtifactDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Artifact}. */
@Service
@Transactional
public class ArtifactServiceImpl implements ArtifactService {

  private final Logger log = LoggerFactory.getLogger(ArtifactServiceImpl.class);

  private final ArtifactRepository artifactRepository;

  private final ArtifactMapper artifactMapper;

  public ArtifactServiceImpl(ArtifactRepository artifactRepository, ArtifactMapper artifactMapper) {
    this.artifactRepository = artifactRepository;
    this.artifactMapper = artifactMapper;
  }

  /**
   * Save a artifact.
   *
   * @param artifactDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public ArtifactDTO save(ArtifactDTO artifactDTO) {
    log.debug("Request to save Artifact : {}", artifactDTO);
    Artifact artifact = artifactMapper.toEntity(artifactDTO);
    artifact = artifactRepository.save(artifact);
    return artifactMapper.toDto(artifact);
  }

  /**
   * Get all the artifacts.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<ArtifactDTO> findAll() {
    log.debug("Request to get all Artifacts");
    return artifactRepository.findAll().stream()
        .map(artifactMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one artifact by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<ArtifactDTO> findOne(Long id) {
    log.debug("Request to get Artifact : {}", id);
    return artifactRepository.findById(id).map(artifactMapper::toDto);
  }

  /**
   * Delete the artifact by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Artifact : {}", id);
    artifactRepository.deleteById(id);
  }
}
