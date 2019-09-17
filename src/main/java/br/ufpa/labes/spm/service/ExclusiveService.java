package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Exclusive;
import br.ufpa.labes.spm.repository.ExclusiveRepository;
import br.ufpa.labes.spm.service.dto.ExclusiveDTO;
import br.ufpa.labes.spm.service.mapper.ExclusiveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Exclusive}. */
@Service
@Transactional
public class ExclusiveService {

  private final Logger log = LoggerFactory.getLogger(ExclusiveService.class);

  private final ExclusiveRepository exclusiveRepository;

  private final ExclusiveMapper exclusiveMapper;

  public ExclusiveService(
      ExclusiveRepository exclusiveRepository, ExclusiveMapper exclusiveMapper) {
    this.exclusiveRepository = exclusiveRepository;
    this.exclusiveMapper = exclusiveMapper;
  }

  /**
   * Save a exclusive.
   *
   * @param exclusiveDTO the entity to save.
   * @return the persisted entity.
   */
  public ExclusiveDTO save(ExclusiveDTO exclusiveDTO) {
    log.debug("Request to save Exclusive : {}", exclusiveDTO);
    Exclusive exclusive = exclusiveMapper.toEntity(exclusiveDTO);
    exclusive = exclusiveRepository.save(exclusive);
    return exclusiveMapper.toDto(exclusive);
  }

  /**
   * Get all the exclusives.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ExclusiveDTO> findAll() {
    log.debug("Request to get all Exclusives");
    return exclusiveRepository.findAll().stream()
        .map(exclusiveMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one exclusive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ExclusiveDTO> findOne(Long id) {
    log.debug("Request to get Exclusive : {}", id);
    return exclusiveRepository.findById(id).map(exclusiveMapper::toDto);
  }

  /**
   * Delete the exclusive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Exclusive : {}", id);
    exclusiveRepository.deleteById(id);
  }
}
