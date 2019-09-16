package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ArtifactParam;
import br.ufpa.labes.spm.repository.ArtifactParamRepository;
import br.ufpa.labes.spm.service.dto.ArtifactParamDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link ArtifactParam}. */
@Service
@Transactional
public class ArtifactParamService {

  private final Logger log = LoggerFactory.getLogger(ArtifactParamService.class);

  private final ArtifactParamRepository artifactParamRepository;

  private final ArtifactParamMapper artifactParamMapper;

  public ArtifactParamService(
      ArtifactParamRepository artifactParamRepository, ArtifactParamMapper artifactParamMapper) {
    this.artifactParamRepository = artifactParamRepository;
    this.artifactParamMapper = artifactParamMapper;
  }

  /**
   * Save a artifactParam.
   *
   * @param artifactParamDTO the entity to save.
   * @return the persisted entity.
   */
  public ArtifactParamDTO save(ArtifactParamDTO artifactParamDTO) {
    log.debug("Request to save ArtifactParam : {}", artifactParamDTO);
    ArtifactParam artifactParam = artifactParamMapper.toEntity(artifactParamDTO);
    artifactParam = artifactParamRepository.save(artifactParam);
    return artifactParamMapper.toDto(artifactParam);
  }

  /**
   * Get all the artifactParams.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ArtifactParamDTO> findAll() {
    log.debug("Request to get all ArtifactParams");
    return artifactParamRepository.findAll().stream()
        .map(artifactParamMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the artifactParams where TheParameterSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ArtifactParamDTO> findAllWhereTheParameterSuperIsNull() {
    log.debug("Request to get all artifactParams where TheParameterSuper is null");
    return StreamSupport.stream(artifactParamRepository.findAll().spliterator(), false)
        .filter(artifactParam -> artifactParam.getTheParameterSuper() == null)
        .map(artifactParamMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one artifactParam by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ArtifactParamDTO> findOne(Long id) {
    log.debug("Request to get ArtifactParam : {}", id);
    return artifactParamRepository.findById(id).map(artifactParamMapper::toDto);
  }

  /**
   * Delete the artifactParam by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ArtifactParam : {}", id);
    artifactParamRepository.deleteById(id);
  }
}
