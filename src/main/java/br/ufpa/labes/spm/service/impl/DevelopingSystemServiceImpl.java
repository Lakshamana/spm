package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.DevelopingSystemService;
import br.ufpa.labes.spm.domain.DevelopingSystem;
import br.ufpa.labes.spm.repository.DevelopingSystemRepository;
import br.ufpa.labes.spm.service.dto.DevelopingSystemDTO;
import br.ufpa.labes.spm.service.mapper.DevelopingSystemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link DevelopingSystem}. */
@Service
@Transactional
public class DevelopingSystemServiceImpl implements DevelopingSystemService {

  private final Logger log = LoggerFactory.getLogger(DevelopingSystemServiceImpl.class);

  private final DevelopingSystemRepository developingSystemRepository;

  private final DevelopingSystemMapper developingSystemMapper;

  public DevelopingSystemServiceImpl(
      DevelopingSystemRepository developingSystemRepository,
      DevelopingSystemMapper developingSystemMapper) {
    this.developingSystemRepository = developingSystemRepository;
    this.developingSystemMapper = developingSystemMapper;
  }

  /**
   * Save a developingSystem.
   *
   * @param developingSystemDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public DevelopingSystemDTO save(DevelopingSystemDTO developingSystemDTO) {
    log.debug("Request to save DevelopingSystem : {}", developingSystemDTO);
    DevelopingSystem developingSystem = developingSystemMapper.toEntity(developingSystemDTO);
    developingSystem = developingSystemRepository.save(developingSystem);
    return developingSystemMapper.toDto(developingSystem);
  }

  /**
   * Get all the developingSystems.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<DevelopingSystemDTO> findAll() {
    log.debug("Request to get all DevelopingSystems");
    return developingSystemRepository.findAll().stream()
        .map(developingSystemMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one developingSystem by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<DevelopingSystemDTO> findOne(Long id) {
    log.debug("Request to get DevelopingSystem : {}", id);
    return developingSystemRepository.findById(id).map(developingSystemMapper::toDto);
  }

  /**
   * Delete the developingSystem by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete DevelopingSystem : {}", id);
    developingSystemRepository.deleteById(id);
  }
}
