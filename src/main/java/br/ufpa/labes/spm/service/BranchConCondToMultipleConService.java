package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;
import br.ufpa.labes.spm.repository.BranchConCondToMultipleConRepository;
import br.ufpa.labes.spm.service.dto.BranchConCondToMultipleConDTO;
import br.ufpa.labes.spm.service.mapper.BranchConCondToMultipleConMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link BranchConCondToMultipleCon}. */
@Service
@Transactional
public class BranchConCondToMultipleConService {

  private final Logger log = LoggerFactory.getLogger(BranchConCondToMultipleConService.class);

  private final BranchConCondToMultipleConRepository branchConCondToMultipleConRepository;

  private final BranchConCondToMultipleConMapper branchConCondToMultipleConMapper;

  public BranchConCondToMultipleConService(
      BranchConCondToMultipleConRepository branchConCondToMultipleConRepository,
      BranchConCondToMultipleConMapper branchConCondToMultipleConMapper) {
    this.branchConCondToMultipleConRepository = branchConCondToMultipleConRepository;
    this.branchConCondToMultipleConMapper = branchConCondToMultipleConMapper;
  }

  /**
   * Save a branchConCondToMultipleCon.
   *
   * @param branchConCondToMultipleConDTO the entity to save.
   * @return the persisted entity.
   */
  public BranchConCondToMultipleConDTO save(
      BranchConCondToMultipleConDTO branchConCondToMultipleConDTO) {
    log.debug("Request to save BranchConCondToMultipleCon : {}", branchConCondToMultipleConDTO);
    BranchConCondToMultipleCon branchConCondToMultipleCon =
        branchConCondToMultipleConMapper.toEntity(branchConCondToMultipleConDTO);
    branchConCondToMultipleCon =
        branchConCondToMultipleConRepository.save(branchConCondToMultipleCon);
    return branchConCondToMultipleConMapper.toDto(branchConCondToMultipleCon);
  }

  /**
   * Get all the branchConCondToMultipleCons.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<BranchConCondToMultipleConDTO> findAll() {
    log.debug("Request to get all BranchConCondToMultipleCons");
    return branchConCondToMultipleConRepository.findAll().stream()
        .map(branchConCondToMultipleConMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one branchConCondToMultipleCon by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<BranchConCondToMultipleConDTO> findOne(Long id) {
    log.debug("Request to get BranchConCondToMultipleCon : {}", id);
    return branchConCondToMultipleConRepository
        .findById(id)
        .map(branchConCondToMultipleConMapper::toDto);
  }

  /**
   * Delete the branchConCondToMultipleCon by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete BranchConCondToMultipleCon : {}", id);
    branchConCondToMultipleConRepository.deleteById(id);
  }
}
