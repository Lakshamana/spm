package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.JoinCon;
import br.ufpa.labes.spm.repository.JoinConRepository;
import br.ufpa.labes.spm.service.dto.JoinConDTO;
import br.ufpa.labes.spm.service.mapper.JoinConMapper;
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
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link JoinCon}. */
@Service
@Transactional
public class JoinConService {

  private final Logger log = LoggerFactory.getLogger(JoinConService.class);

  private final JoinConRepository joinConRepository;

  private final JoinConMapper joinConMapper;

  public JoinConService(JoinConRepository joinConRepository, JoinConMapper joinConMapper) {
    this.joinConRepository = joinConRepository;
    this.joinConMapper = joinConMapper;
  }

  /**
   * Save a joinCon.
   *
   * @param joinConDTO the entity to save.
   * @return the persisted entity.
   */
  public JoinConDTO save(JoinConDTO joinConDTO) {
    log.debug("Request to save JoinCon : {}", joinConDTO);
    JoinCon joinCon = joinConMapper.toEntity(joinConDTO);
    joinCon = joinConRepository.save(joinCon);
    return joinConMapper.toDto(joinCon);
  }

  /**
   * Get all the joinCons.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<JoinConDTO> findAll() {
    log.debug("Request to get all JoinCons");
    return joinConRepository.findAllWithEagerRelationships().stream()
        .map(joinConMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the joinCons with eager load of many-to-many relationships.
   *
   * @return the list of entities.
   */
  public Page<JoinConDTO> findAllWithEagerRelationships(Pageable pageable) {
    return joinConRepository.findAllWithEagerRelationships(pageable).map(joinConMapper::toDto);
  }

  /**
   * Get all the joinCons where TheMultipleConSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<JoinConDTO> findAllWhereTheMultipleConSuperIsNull() {
    log.debug("Request to get all joinCons where TheMultipleConSuper is null");
    return StreamSupport.stream(joinConRepository.findAll().spliterator(), false)
        .filter(joinCon -> joinCon.getTheMultipleConSuper() == null)
        .map(joinConMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one joinCon by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<JoinConDTO> findOne(Long id) {
    log.debug("Request to get JoinCon : {}", id);
    return joinConRepository.findOneWithEagerRelationships(id).map(joinConMapper::toDto);
  }

  /**
   * Delete the joinCon by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete JoinCon : {}", id);
    joinConRepository.deleteById(id);
  }
}
