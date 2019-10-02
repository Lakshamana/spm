package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Asset;
import br.ufpa.labes.spm.repository.AssetRepository;
import br.ufpa.labes.spm.service.dto.AssetDTO;
import br.ufpa.labes.spm.service.mapper.AssetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Asset}. */
@Service
@Transactional
public class AssetService {

  private final Logger log = LoggerFactory.getLogger(AssetService.class);

  private final AssetRepository assetRepository;

  private final AssetMapper assetMapper;

  public AssetService(AssetRepository assetRepository, AssetMapper assetMapper) {
    this.assetRepository = assetRepository;
    this.assetMapper = assetMapper;
  }

  /**
   * Save a asset.
   *
   * @param assetDTO the entity to save.
   * @return the persisted entity.
   */
  public AssetDTO save(AssetDTO assetDTO) {
    log.debug("Request to save Asset : {}", assetDTO);
    Asset asset = assetMapper.toEntity(assetDTO);
    asset = assetRepository.save(asset);
    return assetMapper.toDto(asset);
  }

  /**
   * Get all the assets.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AssetDTO> findAll() {
    log.debug("Request to get all Assets");
    return assetRepository.findAllWithEagerRelationships().stream()
        .map(assetMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the assets with eager load of many-to-many relationships.
   *
   * @return the list of entities.
   */
  public Page<AssetDTO> findAllWithEagerRelationships(Pageable pageable) {
    return assetRepository.findAllWithEagerRelationships(pageable).map(assetMapper::toDto);
  }

  /**
   * Get one asset by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AssetDTO> findOne(Long id) {
    log.debug("Request to get Asset : {}", id);
    return assetRepository.findOneWithEagerRelationships(id).map(assetMapper::toDto);
  }

  /**
   * Delete the asset by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Asset : {}", id);
    assetRepository.deleteById(id);
  }
}
