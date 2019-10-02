package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkGroupType;
import br.ufpa.labes.spm.repository.WorkGroupTypeRepository;
import br.ufpa.labes.spm.service.dto.WorkGroupTypeDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link WorkGroupType}. */
@Service
@Transactional
public class WorkGroupTypeService {

  private final Logger log = LoggerFactory.getLogger(WorkGroupTypeService.class);

  private final WorkGroupTypeRepository workGroupTypeRepository;

  private final WorkGroupTypeMapper workGroupTypeMapper;

  public WorkGroupTypeService(
      WorkGroupTypeRepository workGroupTypeRepository, WorkGroupTypeMapper workGroupTypeMapper) {
    this.workGroupTypeRepository = workGroupTypeRepository;
    this.workGroupTypeMapper = workGroupTypeMapper;
  }

  /**
   * Save a workGroupType.
   *
   * @param workGroupTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public WorkGroupTypeDTO save(WorkGroupTypeDTO workGroupTypeDTO) {
    log.debug("Request to save WorkGroupType : {}", workGroupTypeDTO);
    WorkGroupType workGroupType = workGroupTypeMapper.toEntity(workGroupTypeDTO);
    workGroupType = workGroupTypeRepository.save(workGroupType);
    return workGroupTypeMapper.toDto(workGroupType);
  }

  /**
   * Get all the workGroupTypes.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<WorkGroupTypeDTO> findAll() {
    log.debug("Request to get all WorkGroupTypes");
    return workGroupTypeRepository.findAll().stream()
        .map(workGroupTypeMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one workGroupType by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<WorkGroupTypeDTO> findOne(Long id) {
    log.debug("Request to get WorkGroupType : {}", id);
    return workGroupTypeRepository.findById(id).map(workGroupTypeMapper::toDto);
  }

  /**
   * Delete the workGroupType by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete WorkGroupType : {}", id);
    workGroupTypeRepository.deleteById(id);
  }
}
