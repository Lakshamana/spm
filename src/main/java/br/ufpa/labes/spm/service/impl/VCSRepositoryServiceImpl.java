package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.VCSRepositoryService;
import br.ufpa.labes.spm.domain.VCSRepository;
import br.ufpa.labes.spm.repository.VCSRepositoryRepository;
import br.ufpa.labes.spm.service.dto.VCSRepositoryDTO;
import br.ufpa.labes.spm.service.mapper.VCSRepositoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link VCSRepository}. */
@Service
@Transactional
public class VCSRepositoryServiceImpl implements VCSRepositoryService {

  private final Logger log = LoggerFactory.getLogger(VCSRepositoryServiceImpl.class);

  private final VCSRepositoryRepository vCSRepositoryRepository;

  private final VCSRepositoryMapper vCSRepositoryMapper;

  public VCSRepositoryServiceImpl(
      VCSRepositoryRepository vCSRepositoryRepository, VCSRepositoryMapper vCSRepositoryMapper) {
    this.vCSRepositoryRepository = vCSRepositoryRepository;
    this.vCSRepositoryMapper = vCSRepositoryMapper;
  }

  /**
   * Save a vCSRepository.
   *
   * @param vCSRepositoryDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public VCSRepositoryDTO save(VCSRepositoryDTO vCSRepositoryDTO) {
    log.debug("Request to save VCSRepository : {}", vCSRepositoryDTO);
    VCSRepository vCSRepository = vCSRepositoryMapper.toEntity(vCSRepositoryDTO);
    vCSRepository = vCSRepositoryRepository.save(vCSRepository);
    return vCSRepositoryMapper.toDto(vCSRepository);
  }

  /**
   * Get all the vCSRepositories.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<VCSRepositoryDTO> findAll() {
    log.debug("Request to get all VCSRepositories");
    return vCSRepositoryRepository.findAll().stream()
        .map(vCSRepositoryMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one vCSRepository by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<VCSRepositoryDTO> findOne(Long id) {
    log.debug("Request to get VCSRepository : {}", id);
    return vCSRepositoryRepository.findById(id).map(vCSRepositoryMapper::toDto);
  }

  /**
   * Delete the vCSRepository by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete VCSRepository : {}", id);
    vCSRepositoryRepository.deleteById(id);
  }
}
