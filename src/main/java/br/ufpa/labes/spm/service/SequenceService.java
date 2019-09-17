package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Sequence;
import br.ufpa.labes.spm.repository.SequenceRepository;
import br.ufpa.labes.spm.service.dto.SequenceDTO;
import br.ufpa.labes.spm.service.mapper.SequenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Sequence}. */
@Service
@Transactional
public class SequenceService {

  private final Logger log = LoggerFactory.getLogger(SequenceService.class);

  private final SequenceRepository sequenceRepository;

  private final SequenceMapper sequenceMapper;

  public SequenceService(SequenceRepository sequenceRepository, SequenceMapper sequenceMapper) {
    this.sequenceRepository = sequenceRepository;
    this.sequenceMapper = sequenceMapper;
  }

  /**
   * Save a sequence.
   *
   * @param sequenceDTO the entity to save.
   * @return the persisted entity.
   */
  public SequenceDTO save(SequenceDTO sequenceDTO) {
    log.debug("Request to save Sequence : {}", sequenceDTO);
    Sequence sequence = sequenceMapper.toEntity(sequenceDTO);
    sequence = sequenceRepository.save(sequence);
    return sequenceMapper.toDto(sequence);
  }

  /**
   * Get all the sequences.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<SequenceDTO> findAll() {
    log.debug("Request to get all Sequences");
    return sequenceRepository.findAll().stream()
        .map(sequenceMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one sequence by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<SequenceDTO> findOne(Long id) {
    log.debug("Request to get Sequence : {}", id);
    return sequenceRepository.findById(id).map(sequenceMapper::toDto);
  }

  /**
   * Delete the sequence by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Sequence : {}", id);
    sequenceRepository.deleteById(id);
  }
}
