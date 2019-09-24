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

/**
 * Service Implementation for managing {@link WorkGroupType}.
 */
@Service
@Transactional
public class WorkGroupTypeService {

    private final Logger log = LoggerFactory.getLogger(WorkGroupTypeService.class);

    private final WorkWorkGroupTypeRepository WorkGroupTypeRepository;

    private final WorkWorkGroupTypeMapper WorkGroupTypeMapper;

    public WorkWorkGroupTypeService(WorkWorkGroupTypeRepository workWorkGroupTypeRepository, WorkWorkGroupTypeMapper WorkGroupTypeMapper) {
        this.workWorkGroupTypeRepository = WorkGroupTypeRepository;
        this.workWorkGroupTypeMapper = WorkGroupTypeMapper;
    }

    /**
     * Save a WorkGroupType.
     *
     * @param WorkGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupTypeDTO save(WorkWorkGroupTypeDTO WorkGroupTypeDTO) {
        log.debug("Request to save WorkWorkGroupType : {}", WorkGroupTypeDTO);
        WorkWorkGroupType workWorkGroupType = workWorkGroupTypeMapper.toEntity(WorkGroupTypeDTO);
        workWorkGroupType = workWorkGroupTypeRepository.save(WorkGroupType);
        return workWorkGroupTypeMapper.toDto(WorkGroupType);
    }

    /**
     * Get all the WorkGroupTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkGroupTypeDTO> findAll() {
        log.debug("Request to get all WorkGroupTypes");
        return WorkGroupTypeRepository.findAll().stream()
            .map(WorkGroupTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one WorkGroupType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkGroupTypeDTO> findOne(Long id) {
        log.debug("Request to get WorkGroupType : {}", id);
        return WorkGroupTypeRepository.findById(id)
            .map(WorkGroupTypeMapper::toDto);
    }

    /**
     * Delete the WorkGroupType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkGroupType : {}", id);
        WorkGroupTypeRepository.deleteById(id);
    }
}
