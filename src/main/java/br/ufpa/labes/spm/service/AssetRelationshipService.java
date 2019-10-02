package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AssetRelationship;
import br.ufpa.labes.spm.repository.AssetRelationshipRepository;
import br.ufpa.labes.spm.service.dto.AssetRelationshipDTO;
import br.ufpa.labes.spm.service.mapper.AssetRelationshipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link AssetRelationship}. */
@Service
@Transactional
public class AssetRelationshipService {

  private final Logger log = LoggerFactory.getLogger(AssetRelationshipService.class);

  private final AssetRelationshipRepository assetRelationshipRepository;

  private final AssetRelationshipMapper assetRelationshipMapper;

  public AssetRelationshipService(
      AssetRelationshipRepository assetRelationshipRepository,
      AssetRelationshipMapper assetRelationshipMapper) {
    this.assetRelationshipRepository = assetRelationshipRepository;
    this.assetRelationshipMapper = assetRelationshipMapper;
  }

  /**
   * Save a assetRelationship.
   *
   * @param assetRelationshipDTO the entity to save.
   * @return the persisted entity.
   */
  public AssetRelationshipDTO save(AssetRelationshipDTO assetRelationshipDTO) {
    log.debug("Request to save AssetRelationship : {}", assetRelationshipDTO);
    AssetRelationship assetRelationship = assetRelationshipMapper.toEntity(assetRelationshipDTO);
    assetRelationship = assetRelationshipRepository.save(assetRelationship);
    return assetRelationshipMapper.toDto(assetRelationship);
  }

  /**
   * Get all the assetRelationships.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AssetRelationshipDTO> findAll() {
    log.debug("Request to get all AssetRelationships");
    return assetRelationshipRepository.findAll().stream()
        .map(assetRelationshipMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one assetRelationship by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AssetRelationshipDTO> findOne(Long id) {
    log.debug("Request to get AssetRelationship : {}", id);
    return assetRelationshipRepository.findById(id).map(assetRelationshipMapper::toDto);
  }

  /**
   * Delete the assetRelationship by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete AssetRelationship : {}", id);
    assetRelationshipRepository.deleteById(id);
  }
}
