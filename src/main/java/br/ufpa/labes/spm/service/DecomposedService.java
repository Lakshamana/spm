package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.repository.DecomposedRepository;
import br.ufpa.labes.spm.service.dto.DecomposedDTO;
import br.ufpa.labes.spm.service.mapper.DecomposedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link Decomposed}. */
@Service
@Transactional
public class DecomposedService {

  private final Logger log = LoggerFactory.getLogger(DecomposedService.class);

  private final DecomposedRepository decomposedRepository;

  private final DecomposedMapper decomposedMapper;

  public DecomposedService(
      DecomposedRepository decomposedRepository, DecomposedMapper decomposedMapper) {
    this.decomposedRepository = decomposedRepository;
    this.decomposedMapper = decomposedMapper;
  }

  /**
   * Save a decomposed.
   *
   * @param decomposedDTO the entity to save.
   * @return the persisted entity.
   */
  public DecomposedDTO save(DecomposedDTO decomposedDTO) {
    log.debug("Request to save Decomposed : {}", decomposedDTO);
    Decomposed decomposed = decomposedMapper.toEntity(decomposedDTO);
    decomposed = decomposedRepository.save(decomposed);
    return decomposedMapper.toDto(decomposed);
  }

  /**
   * Get all the decomposedActivities.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<DecomposedDTO> findAll() {
    log.debug("Request to get all DecomposedActivities");
    return decomposedRepository.findAll().stream()
        .map(decomposedMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the decomposedActivities where TheActivitySuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<DecomposedDTO> findAllWhereTheActivitySuperIsNull() {
    log.debug("Request to get all decomposedActivities where TheActivitySuper is null");
    return StreamSupport.stream(decomposedRepository.findAll().spliterator(), false)
        .filter(decomposed -> decomposed.getTheActivitySuper() == null)
        .map(decomposedMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one decomposed by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<DecomposedDTO> findOne(Long id) {
    log.debug("Request to get Decomposed : {}", id);
    return decomposedRepository.findById(id).map(decomposedMapper::toDto);
  }

  /**
   * Delete the decomposed by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Decomposed : {}", id);
    decomposedRepository.deleteById(id);
  }
}
