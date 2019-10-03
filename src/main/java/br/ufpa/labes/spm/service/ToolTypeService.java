package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ToolType;
import br.ufpa.labes.spm.repository.ToolTypeRepository;
import br.ufpa.labes.spm.service.dto.ToolTypeDTO;
import br.ufpa.labes.spm.service.mapper.ToolTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ToolType}.
 */
@Service
@Transactional
public class ToolTypeService {

    private final Logger log = LoggerFactory.getLogger(ToolTypeService.class);

    private final ToolTypeRepository toolTypeRepository;

    private final ToolTypeMapper toolTypeMapper;

    public ToolTypeService(ToolTypeRepository toolTypeRepository, ToolTypeMapper toolTypeMapper) {
        this.toolTypeRepository = toolTypeRepository;
        this.toolTypeMapper = toolTypeMapper;
    }

    /**
     * Save a toolType.
     *
     * @param toolTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ToolTypeDTO save(ToolTypeDTO toolTypeDTO) {
        log.debug("Request to save ToolType : {}", toolTypeDTO);
        ToolType toolType = toolTypeMapper.toEntity(toolTypeDTO);
        toolType = toolTypeRepository.save(toolType);
        return toolTypeMapper.toDto(toolType);
    }

    /**
     * Get all the toolTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ToolTypeDTO> findAll() {
        log.debug("Request to get all ToolTypes");
        return toolTypeRepository.findAll().stream()
            .map(toolTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one toolType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ToolTypeDTO> findOne(Long id) {
        log.debug("Request to get ToolType : {}", id);
        return toolTypeRepository.findById(id)
            .map(toolTypeMapper::toDto);
    }

    /**
     * Delete the toolType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ToolType : {}", id);
        toolTypeRepository.deleteById(id);
    }
}
