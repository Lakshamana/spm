package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.WorkGroupService;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.repository.WorkGroupRepository;
import br.ufpa.labes.spm.service.dto.WorkGroupDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link WorkGroup}. */
@Service
@Transactional
public class WorkGroupServiceImpl implements WorkGroupService {

  private final Logger log = LoggerFactory.getLogger(WorkGroupServiceImpl.class);

  private final WorkGroupRepository workGroupRepository;

  private final WorkGroupMapper workGroupMapper;

  public WorkGroupServiceImpl(
      WorkGroupRepository workGroupRepository, WorkGroupMapper workGroupMapper) {
    this.workGroupRepository = workGroupRepository;
    this.workGroupMapper = workGroupMapper;
  }

  /**
   * Save a workGroup.
   *
   * @param workGroupDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public WorkGroupDTO save(WorkGroupDTO workGroupDTO) {
    log.debug("Request to save WorkGroup : {}", workGroupDTO);
    WorkGroup workGroup = workGroupMapper.toEntity(workGroupDTO);
    workGroup = workGroupRepository.save(workGroup);
    return workGroupMapper.toDto(workGroup);
  }

  /**
   * Get all the workGroups.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<WorkGroupDTO> findAll() {
    log.debug("Request to get all WorkGroups");
    return workGroupRepository.findAll().stream()
        .map(workGroupMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one workGroup by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<WorkGroupDTO> findOne(Long id) {
    log.debug("Request to get WorkGroup : {}", id);
    return workGroupRepository.findById(id).map(workGroupMapper::toDto);
  }

  /**
   * Delete the workGroup by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete WorkGroup : {}", id);
    workGroupRepository.deleteById(id);
  }
}
