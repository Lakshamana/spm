package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.CatalogEvent;
import br.ufpa.labes.spm.repository.CatalogEventRepository;
import br.ufpa.labes.spm.service.dto.CatalogEventDTO;
import br.ufpa.labes.spm.service.mapper.CatalogEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link CatalogEvent}. */
@Service
@Transactional
public class CatalogEventService {

  private final Logger log = LoggerFactory.getLogger(CatalogEventService.class);

  private final CatalogEventRepository catalogEventRepository;

  private final CatalogEventMapper catalogEventMapper;

  public CatalogEventService(
      CatalogEventRepository catalogEventRepository, CatalogEventMapper catalogEventMapper) {
    this.catalogEventRepository = catalogEventRepository;
    this.catalogEventMapper = catalogEventMapper;
  }

  /**
   * Save a catalogEvent.
   *
   * @param catalogEventDTO the entity to save.
   * @return the persisted entity.
   */
  public CatalogEventDTO save(CatalogEventDTO catalogEventDTO) {
    log.debug("Request to save CatalogEvent : {}", catalogEventDTO);
    CatalogEvent catalogEvent = catalogEventMapper.toEntity(catalogEventDTO);
    catalogEvent = catalogEventRepository.save(catalogEvent);
    return catalogEventMapper.toDto(catalogEvent);
  }

  /**
   * Get all the catalogEvents.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<CatalogEventDTO> findAll() {
    log.debug("Request to get all CatalogEvents");
    return catalogEventRepository.findAll().stream()
        .map(catalogEventMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one catalogEvent by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<CatalogEventDTO> findOne(Long id) {
    log.debug("Request to get CatalogEvent : {}", id);
    return catalogEventRepository.findById(id).map(catalogEventMapper::toDto);
  }

  /**
   * Delete the catalogEvent by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete CatalogEvent : {}", id);
    catalogEventRepository.deleteById(id);
  }
}
