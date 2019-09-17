package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.SimpleCon;
import br.ufpa.labes.spm.repository.SimpleConRepository;
import br.ufpa.labes.spm.service.dto.SimpleConDTO;
import br.ufpa.labes.spm.service.mapper.SimpleConMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link SimpleCon}. */
@Service
@Transactional
public class SimpleConService {

  private final Logger log = LoggerFactory.getLogger(SimpleConService.class);

  private final SimpleConRepository simpleConRepository;

  private final SimpleConMapper simpleConMapper;

  public SimpleConService(
      SimpleConRepository simpleConRepository, SimpleConMapper simpleConMapper) {
    this.simpleConRepository = simpleConRepository;
    this.simpleConMapper = simpleConMapper;
  }

  /**
   * Save a simpleCon.
   *
   * @param simpleConDTO the entity to save.
   * @return the persisted entity.
   */
  public SimpleConDTO save(SimpleConDTO simpleConDTO) {
    log.debug("Request to save SimpleCon : {}", simpleConDTO);
    SimpleCon simpleCon = simpleConMapper.toEntity(simpleConDTO);
    simpleCon = simpleConRepository.save(simpleCon);
    return simpleConMapper.toDto(simpleCon);
  }

  /**
   * Get all the simpleCons.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<SimpleConDTO> findAll() {
    log.debug("Request to get all SimpleCons");
    return simpleConRepository.findAll().stream()
        .map(simpleConMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one simpleCon by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<SimpleConDTO> findOne(Long id) {
    log.debug("Request to get SimpleCon : {}", id);
    return simpleConRepository.findById(id).map(simpleConMapper::toDto);
  }

  /**
   * Delete the simpleCon by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete SimpleCon : {}", id);
    simpleConRepository.deleteById(id);
  }
}
