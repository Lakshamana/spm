package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AssetStat;
import br.ufpa.labes.spm.repository.AssetStatRepository;
import br.ufpa.labes.spm.service.dto.AssetStatDTO;
import br.ufpa.labes.spm.service.mapper.AssetStatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link AssetStat}. */
@Service
@Transactional
public class AssetStatService {

  private final Logger log = LoggerFactory.getLogger(AssetStatService.class);

  private final AssetStatRepository assetStatRepository;

  private final AssetStatMapper assetStatMapper;

  public AssetStatService(
      AssetStatRepository assetStatRepository, AssetStatMapper assetStatMapper) {
    this.assetStatRepository = assetStatRepository;
    this.assetStatMapper = assetStatMapper;
  }

  /**
   * Save a assetStat.
   *
   * @param assetStatDTO the entity to save.
   * @return the persisted entity.
   */
  public AssetStatDTO save(AssetStatDTO assetStatDTO) {
    log.debug("Request to save AssetStat : {}", assetStatDTO);
    AssetStat assetStat = assetStatMapper.toEntity(assetStatDTO);
    assetStat = assetStatRepository.save(assetStat);
    return assetStatMapper.toDto(assetStat);
  }

  /**
   * Get all the assetStats.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AssetStatDTO> findAll() {
    log.debug("Request to get all AssetStats");
    return assetStatRepository.findAll().stream()
        .map(assetStatMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the assetStats where TheAsset is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AssetStatDTO> findAllWhereTheAssetIsNull() {
    log.debug("Request to get all assetStats where TheAsset is null");
    return StreamSupport.stream(assetStatRepository.findAll().spliterator(), false)
        .filter(assetStat -> assetStat.getTheAsset() == null)
        .map(assetStatMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one assetStat by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AssetStatDTO> findOne(Long id) {
    log.debug("Request to get AssetStat : {}", id);
    return assetStatRepository.findById(id).map(assetStatMapper::toDto);
  }

  /**
   * Delete the assetStat by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete AssetStat : {}", id);
    assetStatRepository.deleteById(id);
  }
}
