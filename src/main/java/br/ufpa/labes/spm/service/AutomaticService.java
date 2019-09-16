package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.repository.AutomaticRepository;
import br.ufpa.labes.spm.service.dto.AutomaticDTO;
import br.ufpa.labes.spm.service.mapper.AutomaticMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link Automatic}. */
@Service
@Transactional
public class AutomaticService {

  private final Logger log = LoggerFactory.getLogger(AutomaticService.class);

  private final AutomaticRepository automaticRepository;

  private final AutomaticMapper automaticMapper;

  public AutomaticService(
      AutomaticRepository automaticRepository, AutomaticMapper automaticMapper) {
    this.automaticRepository = automaticRepository;
    this.automaticMapper = automaticMapper;
  }

  /**
   * Save a automatic.
   *
   * @param automaticDTO the entity to save.
   * @return the persisted entity.
   */
  public AutomaticDTO save(AutomaticDTO automaticDTO) {
    log.debug("Request to save Automatic : {}", automaticDTO);
    Automatic automatic = automaticMapper.toEntity(automaticDTO);
    automatic = automaticRepository.save(automatic);
    return automaticMapper.toDto(automatic);
  }

  /**
   * Get all the automatics.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AutomaticDTO> findAll() {
    log.debug("Request to get all Automatics");
    return automaticRepository.findAll().stream()
        .map(automaticMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the automatics where ThePlainSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AutomaticDTO> findAllWhereThePlainSuperIsNull() {
    log.debug("Request to get all automatics where ThePlainSuper is null");
    return StreamSupport.stream(automaticRepository.findAll().spliterator(), false)
        .filter(automatic -> automatic.getThePlainSuper() == null)
        .map(automaticMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one automatic by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AutomaticDTO> findOne(Long id) {
    log.debug("Request to get Automatic : {}", id);
    return automaticRepository.findById(id).map(automaticMapper::toDto);
  }

  /**
   * Delete the automatic by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Automatic : {}", id);
    automaticRepository.deleteById(id);
  }
}
