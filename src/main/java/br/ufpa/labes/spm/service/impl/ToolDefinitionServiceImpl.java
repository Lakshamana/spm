package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.ToolDefinitionService;
import br.ufpa.labes.spm.domain.ToolDefinition;
import br.ufpa.labes.spm.repository.ToolDefinitionRepository;
import br.ufpa.labes.spm.service.dto.ToolDefinitionDTO;
import br.ufpa.labes.spm.service.mapper.ToolDefinitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ToolDefinition}.
 */
@Service
@Transactional
public class ToolDefinitionServiceImpl implements ToolDefinitionService {

    private final Logger log = LoggerFactory.getLogger(ToolDefinitionServiceImpl.class);

    private final ToolDefinitionRepository toolDefinitionRepository;

    private final ToolDefinitionMapper toolDefinitionMapper;

    public ToolDefinitionServiceImpl(ToolDefinitionRepository toolDefinitionRepository, ToolDefinitionMapper toolDefinitionMapper) {
        this.toolDefinitionRepository = toolDefinitionRepository;
        this.toolDefinitionMapper = toolDefinitionMapper;
    }

    /**
     * Save a toolDefinition.
     *
     * @param toolDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ToolDefinitionDTO save(ToolDefinitionDTO toolDefinitionDTO) {
        log.debug("Request to save ToolDefinition : {}", toolDefinitionDTO);
        ToolDefinition toolDefinition = toolDefinitionMapper.toEntity(toolDefinitionDTO);
        toolDefinition = toolDefinitionRepository.save(toolDefinition);
        return toolDefinitionMapper.toDto(toolDefinition);
    }

    /**
     * Get all the toolDefinitions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ToolDefinitionDTO> findAll() {
        log.debug("Request to get all ToolDefinitions");
        return toolDefinitionRepository.findAllWithEagerRelationships().stream()
            .map(toolDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the toolDefinitions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ToolDefinitionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return toolDefinitionRepository.findAllWithEagerRelationships(pageable).map(toolDefinitionMapper::toDto);
    }
    

    /**
     * Get one toolDefinition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ToolDefinitionDTO> findOne(Long id) {
        log.debug("Request to get ToolDefinition : {}", id);
        return toolDefinitionRepository.findOneWithEagerRelationships(id)
            .map(toolDefinitionMapper::toDto);
    }

    /**
     * Delete the toolDefinition by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ToolDefinition : {}", id);
        toolDefinitionRepository.deleteById(id);
    }
}
