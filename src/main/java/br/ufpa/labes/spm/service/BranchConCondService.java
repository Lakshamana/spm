package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchConCond;
import br.ufpa.labes.spm.repository.BranchConCondRepository;
import br.ufpa.labes.spm.service.dto.BranchConCondDTO;
import br.ufpa.labes.spm.service.mapper.BranchConCondMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link BranchConCond}. */
@Service
@Transactional
public class BranchConCondService {

  private final Logger log = LoggerFactory.getLogger(BranchConCondService.class);

  private final BranchConCondRepository branchConCondRepository;

  private final BranchConCondMapper branchConCondMapper;

  public BranchConCondService(
      BranchConCondRepository branchConCondRepository, BranchConCondMapper branchConCondMapper) {
    this.branchConCondRepository = branchConCondRepository;
    this.branchConCondMapper = branchConCondMapper;
  }

  /**
   * Save a branchConCond.
   *
   * @param branchConCondDTO the entity to save.
   * @return the persisted entity.
   */
  public BranchConCondDTO save(BranchConCondDTO branchConCondDTO) {
    log.debug("Request to save BranchConCond : {}", branchConCondDTO);
    BranchConCond branchConCond = branchConCondMapper.toEntity(branchConCondDTO);
    branchConCond = branchConCondRepository.save(branchConCond);
    return branchConCondMapper.toDto(branchConCond);
  }

  /**
   * Get all the branchConConds.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<BranchConCondDTO> findAll() {
    log.debug("Request to get all BranchConConds");
    return branchConCondRepository.findAll().stream()
        .map(branchConCondMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one branchConCond by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<BranchConCondDTO> findOne(Long id) {
    log.debug("Request to get BranchConCond : {}", id);
    return branchConCondRepository.findById(id).map(branchConCondMapper::toDto);
  }

  /**
   * Delete the branchConCond by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete BranchConCond : {}", id);
    branchConCondRepository.deleteById(id);
  }
}
