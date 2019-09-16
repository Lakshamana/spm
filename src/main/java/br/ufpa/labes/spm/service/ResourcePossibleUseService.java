package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ResourcePossibleUse;
import br.ufpa.labes.spm.repository.ResourcePossibleUseRepository;
import br.ufpa.labes.spm.service.dto.ResourcePossibleUseDTO;
import br.ufpa.labes.spm.service.mapper.ResourcePossibleUseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ResourcePossibleUse}. */
@Service
@Transactional
public class ResourcePossibleUseService {

  private final Logger log = LoggerFactory.getLogger(ResourcePossibleUseService.class);

  private final ResourcePossibleUseRepository resourcePossibleUseRepository;

  private final ResourcePossibleUseMapper resourcePossibleUseMapper;

  public ResourcePossibleUseService(
      ResourcePossibleUseRepository resourcePossibleUseRepository,
      ResourcePossibleUseMapper resourcePossibleUseMapper) {
    this.resourcePossibleUseRepository = resourcePossibleUseRepository;
    this.resourcePossibleUseMapper = resourcePossibleUseMapper;
  }

  /**
   * Save a resourcePossibleUse.
   *
   * @param resourcePossibleUseDTO the entity to save.
   * @return the persisted entity.
   */
  public ResourcePossibleUseDTO save(ResourcePossibleUseDTO resourcePossibleUseDTO) {
    log.debug("Request to save ResourcePossibleUse : {}", resourcePossibleUseDTO);
    ResourcePossibleUse resourcePossibleUse =
        resourcePossibleUseMapper.toEntity(resourcePossibleUseDTO);
    resourcePossibleUse = resourcePossibleUseRepository.save(resourcePossibleUse);
    return resourcePossibleUseMapper.toDto(resourcePossibleUse);
  }

  /**
   * Get all the resourcePossibleUses.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ResourcePossibleUseDTO> findAll() {
    log.debug("Request to get all ResourcePossibleUses");
    return resourcePossibleUseRepository.findAll().stream()
        .map(resourcePossibleUseMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one resourcePossibleUse by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ResourcePossibleUseDTO> findOne(Long id) {
    log.debug("Request to get ResourcePossibleUse : {}", id);
    return resourcePossibleUseRepository.findById(id).map(resourcePossibleUseMapper::toDto);
  }

  /**
   * Delete the resourcePossibleUse by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ResourcePossibleUse : {}", id);
    resourcePossibleUseRepository.deleteById(id);
  }
}
