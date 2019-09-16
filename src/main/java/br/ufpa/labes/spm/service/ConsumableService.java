package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Consumable;
import br.ufpa.labes.spm.repository.ConsumableRepository;
import br.ufpa.labes.spm.service.dto.ConsumableDTO;
import br.ufpa.labes.spm.service.mapper.ConsumableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link Consumable}. */
@Service
@Transactional
public class ConsumableService {

  private final Logger log = LoggerFactory.getLogger(ConsumableService.class);

  private final ConsumableRepository consumableRepository;

  private final ConsumableMapper consumableMapper;

  public ConsumableService(
      ConsumableRepository consumableRepository, ConsumableMapper consumableMapper) {
    this.consumableRepository = consumableRepository;
    this.consumableMapper = consumableMapper;
  }

  /**
   * Save a consumable.
   *
   * @param consumableDTO the entity to save.
   * @return the persisted entity.
   */
  public ConsumableDTO save(ConsumableDTO consumableDTO) {
    log.debug("Request to save Consumable : {}", consumableDTO);
    Consumable consumable = consumableMapper.toEntity(consumableDTO);
    consumable = consumableRepository.save(consumable);
    return consumableMapper.toDto(consumable);
  }

  /**
   * Get all the consumables.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ConsumableDTO> findAll() {
    log.debug("Request to get all Consumables");
    return consumableRepository.findAll().stream()
        .map(consumableMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the consumables where TheResourceSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ConsumableDTO> findAllWhereTheResourceSuperIsNull() {
    log.debug("Request to get all consumables where TheResourceSuper is null");
    return StreamSupport.stream(consumableRepository.findAll().spliterator(), false)
        .filter(consumable -> consumable.getTheResourceSuper() == null)
        .map(consumableMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one consumable by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ConsumableDTO> findOne(Long id) {
    log.debug("Request to get Consumable : {}", id);
    return consumableRepository.findById(id).map(consumableMapper::toDto);
  }

  /**
   * Delete the consumable by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Consumable : {}", id);
    consumableRepository.deleteById(id);
  }
}
