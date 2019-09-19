package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkWorkGroupType;
import br.ufpa.labes.spm.repository.WorkWorkGroupTypeRepository;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupTypeDTO;
import br.ufpa.labes.spm.service.mapper.WorkWorkGroupTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkWorkGroupType}.
 */
@Service
@Transactional
public class WorkWorkGroupTypeService {

    private final Logger log = LoggerFactory.getLogger(WorkWorkGroupTypeService.class);

    private final WorkWorkGroupTypeRepository workWorkGroupTypeRepository;

    private final WorkWorkGroupTypeMapper workWorkGroupTypeMapper;

    public WorkWorkGroupTypeService(WorkWorkGroupTypeRepository workWorkGroupTypeRepository, WorkWorkGroupTypeMapper workWorkGroupTypeMapper) {
        this.workWorkGroupTypeRepository = workWorkGroupTypeRepository;
        this.workWorkGroupTypeMapper = workWorkGroupTypeMapper;
    }

    /**
     * Save a workWorkGroupType.
     *
     * @param workWorkGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupTypeDTO save(WorkWorkGroupTypeDTO workWorkGroupTypeDTO) {
        log.debug("Request to save WorkWorkGroupType : {}", workWorkGroupTypeDTO);
        WorkWorkGroupType workWorkGroupType = workWorkGroupTypeMapper.toEntity(workWorkGroupTypeDTO);
        workWorkGroupType = workWorkGroupTypeRepository.save(workWorkGroupType);
        return workWorkGroupTypeMapper.toDto(workWorkGroupType);
    }

    /**
     * Get all the workWorkGroupTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkWorkGroupTypeDTO> findAll() {
        log.debug("Request to get all WorkWorkGroupTypes");
        return workWorkGroupTypeRepository.findAll().stream()
            .map(workWorkGroupTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one workWorkGroupType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkWorkGroupTypeDTO> findOne(Long id) {
        log.debug("Request to get WorkWorkGroupType : {}", id);
        return workWorkGroupTypeRepository.findById(id)
            .map(workWorkGroupTypeMapper::toDto);
    }

    /**
     * Delete the workWorkGroupType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkWorkGroupType : {}", id);
        workWorkGroupTypeRepository.deleteById(id);
    }
}
