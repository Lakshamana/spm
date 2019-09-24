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

/**
 * Service Implementation for managing {@link WorkGroup}.
 */
@Service
@Transactional
public class WorkWorkGroupServiceImpl implements WorkGroupService {

    private final Logger log = LoggerFactory.getLogger(WorkGroupServiceImpl.class);

    private final WorkWorkGroupRepository WorkGroupRepository;

    private final WorkWorkGroupMapper WorkGroupMapper;

    public WorkWorkGroupServiceImpl(WorkWorkGroupRepository workWorkGroupRepository, WorkWorkGroupMapper WorkGroupMapper) {
        this.workWorkGroupRepository = WorkGroupRepository;
        this.workWorkGroupMapper = WorkGroupMapper;
    }

    /**
     * Save a WorkGroup.
     *
     * @param WorkGroupDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public WorkWorkGroupDTO save(WorkWorkGroupDTO WorkGroupDTO) {
        log.debug("Request to save WorkWorkGroup : {}", WorkGroupDTO);
        WorkWorkGroup workWorkGroup = workWorkGroupMapper.toEntity(WorkGroupDTO);
        workWorkGroup = workWorkGroupRepository.save(WorkGroup);
        return workWorkGroupMapper.toDto(WorkGroup);
    }

    /**
     * Get all the WorkGroups.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkGroupDTO> findAll() {
        log.debug("Request to get all WorkGroups");
        return WorkGroupRepository.findAll().stream()
            .map(WorkGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one WorkGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkGroupDTO> findOne(Long id) {
        log.debug("Request to get WorkGroup : {}", id);
        return WorkGroupRepository.findById(id)
            .map(WorkGroupMapper::toDto);
    }

    /**
     * Delete the WorkGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkGroup : {}", id);
        WorkGroupRepository.deleteById(id);
    }
}
