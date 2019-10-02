package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Ocurrence;
import br.ufpa.labes.spm.repository.OcurrenceRepository;
import br.ufpa.labes.spm.service.dto.OcurrenceDTO;
import br.ufpa.labes.spm.service.mapper.OcurrenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Ocurrence}. */
@Service
@Transactional
public class OcurrenceService {

  private final Logger log = LoggerFactory.getLogger(OcurrenceService.class);

  private final OcurrenceRepository ocurrenceRepository;

  private final OcurrenceMapper ocurrenceMapper;

  public OcurrenceService(
      OcurrenceRepository ocurrenceRepository, OcurrenceMapper ocurrenceMapper) {
    this.ocurrenceRepository = ocurrenceRepository;
    this.ocurrenceMapper = ocurrenceMapper;
  }

  /**
   * Save a ocurrence.
   *
   * @param ocurrenceDTO the entity to save.
   * @return the persisted entity.
   */
  public OcurrenceDTO save(OcurrenceDTO ocurrenceDTO) {
    log.debug("Request to save Ocurrence : {}", ocurrenceDTO);
    Ocurrence ocurrence = ocurrenceMapper.toEntity(ocurrenceDTO);
    ocurrence = ocurrenceRepository.save(ocurrence);
    return ocurrenceMapper.toDto(ocurrence);
  }

  /**
   * Get all the ocurrences.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<OcurrenceDTO> findAll() {
    log.debug("Request to get all Ocurrences");
    return ocurrenceRepository.findAll().stream()
        .map(ocurrenceMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one ocurrence by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<OcurrenceDTO> findOne(Long id) {
    log.debug("Request to get Ocurrence : {}", id);
    return ocurrenceRepository.findById(id).map(ocurrenceMapper::toDto);
  }

  /**
   * Delete the ocurrence by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Ocurrence : {}", id);
    ocurrenceRepository.deleteById(id);
  }
}
