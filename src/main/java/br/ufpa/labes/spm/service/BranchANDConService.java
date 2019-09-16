package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchANDCon;
import br.ufpa.labes.spm.repository.BranchANDConRepository;
import br.ufpa.labes.spm.service.dto.BranchANDConDTO;
import br.ufpa.labes.spm.service.mapper.BranchANDConMapper;
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

/** Service Implementation for managing {@link BranchANDCon}. */
@Service
@Transactional
public class BranchANDConService {

  private final Logger log = LoggerFactory.getLogger(BranchANDConService.class);

  private final BranchANDConRepository branchANDConRepository;

  private final BranchANDConMapper branchANDConMapper;

  public BranchANDConService(
      BranchANDConRepository branchANDConRepository, BranchANDConMapper branchANDConMapper) {
    this.branchANDConRepository = branchANDConRepository;
    this.branchANDConMapper = branchANDConMapper;
  }

  /**
   * Save a branchANDCon.
   *
   * @param branchANDConDTO the entity to save.
   * @return the persisted entity.
   */
  public BranchANDConDTO save(BranchANDConDTO branchANDConDTO) {
    log.debug("Request to save BranchANDCon : {}", branchANDConDTO);
    BranchANDCon branchANDCon = branchANDConMapper.toEntity(branchANDConDTO);
    branchANDCon = branchANDConRepository.save(branchANDCon);
    return branchANDConMapper.toDto(branchANDCon);
  }

  /**
   * Get all the branchANDCons.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<BranchANDConDTO> findAll() {
    log.debug("Request to get all BranchANDCons");
    return branchANDConRepository.findAllWithEagerRelationships().stream()
        .map(branchANDConMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the branchANDCons with eager load of many-to-many relationships.
   *
   * @return the list of entities.
   */
  public Page<BranchANDConDTO> findAllWithEagerRelationships(Pageable pageable) {
    return branchANDConRepository
        .findAllWithEagerRelationships(pageable)
        .map(branchANDConMapper::toDto);
  }

  /**
   * Get all the branchANDCons where TheBranchConSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<BranchANDConDTO> findAllWhereTheBranchConSuperIsNull() {
    log.debug("Request to get all branchANDCons where TheBranchConSuper is null");
    return StreamSupport.stream(branchANDConRepository.findAll().spliterator(), false)
        .filter(branchANDCon -> branchANDCon.getTheBranchConSuper() == null)
        .map(branchANDConMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one branchANDCon by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<BranchANDConDTO> findOne(Long id) {
    log.debug("Request to get BranchANDCon : {}", id);
    return branchANDConRepository.findOneWithEagerRelationships(id).map(branchANDConMapper::toDto);
  }

  /**
   * Delete the branchANDCon by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete BranchANDCon : {}", id);
    branchANDConRepository.deleteById(id);
  }
}
