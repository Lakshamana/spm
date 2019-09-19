package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.WorkWorkGroupService;
import br.ufpa.labes.spm.domain.WorkWorkGroup;
import br.ufpa.labes.spm.repository.WorkWorkGroupRepository;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupDTO;
import br.ufpa.labes.spm.service.mapper.WorkWorkGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkWorkGroup}.
 */
@Service
@Transactional
public class WorkWorkGroupServiceImpl implements WorkWorkGroupService {

    private final Logger log = LoggerFactory.getLogger(WorkWorkGroupServiceImpl.class);

    private final WorkWorkGroupRepository workWorkGroupRepository;

    private final WorkWorkGroupMapper workWorkGroupMapper;

    public WorkWorkGroupServiceImpl(WorkWorkGroupRepository workWorkGroupRepository, WorkWorkGroupMapper workWorkGroupMapper) {
        this.workWorkGroupRepository = workWorkGroupRepository;
        this.workWorkGroupMapper = workWorkGroupMapper;
    }

    /**
     * Save a workWorkGroup.
     *
     * @param workWorkGroupDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public WorkWorkGroupDTO save(WorkWorkGroupDTO workWorkGroupDTO) {
        log.debug("Request to save WorkWorkGroup : {}", workWorkGroupDTO);
        WorkWorkGroup workWorkGroup = workWorkGroupMapper.toEntity(workWorkGroupDTO);
        workWorkGroup = workWorkGroupRepository.save(workWorkGroup);
        return workWorkGroupMapper.toDto(workWorkGroup);
    }

    /**
     * Get all the workWorkGroups.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkWorkGroupDTO> findAll() {
        log.debug("Request to get all WorkWorkGroups");
        return workWorkGroupRepository.findAll().stream()
            .map(workWorkGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one workWorkGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkWorkGroupDTO> findOne(Long id) {
        log.debug("Request to get WorkWorkGroup : {}", id);
        return workWorkGroupRepository.findById(id)
            .map(workWorkGroupMapper::toDto);
    }

    /**
     * Delete the workWorkGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkWorkGroup : {}", id);
        workWorkGroupRepository.deleteById(id);
    }
}
