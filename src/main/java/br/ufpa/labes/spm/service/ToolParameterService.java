package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ToolParameter;
import br.ufpa.labes.spm.repository.ToolParameterRepository;
import br.ufpa.labes.spm.service.dto.ToolParameterDTO;
import br.ufpa.labes.spm.service.mapper.ToolParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ToolParameter}.
 */
@Service
@Transactional
public class ToolParameterService {

    private final Logger log = LoggerFactory.getLogger(ToolParameterService.class);

    private final ToolParameterRepository toolParameterRepository;

    private final ToolParameterMapper toolParameterMapper;

    public ToolParameterService(ToolParameterRepository toolParameterRepository, ToolParameterMapper toolParameterMapper) {
        this.toolParameterRepository = toolParameterRepository;
        this.toolParameterMapper = toolParameterMapper;
    }

    /**
     * Save a toolParameter.
     *
     * @param toolParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public ToolParameterDTO save(ToolParameterDTO toolParameterDTO) {
        log.debug("Request to save ToolParameter : {}", toolParameterDTO);
        ToolParameter toolParameter = toolParameterMapper.toEntity(toolParameterDTO);
        toolParameter = toolParameterRepository.save(toolParameter);
        return toolParameterMapper.toDto(toolParameter);
    }

    /**
     * Get all the toolParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ToolParameterDTO> findAll() {
        log.debug("Request to get all ToolParameters");
        return toolParameterRepository.findAll().stream()
            .map(toolParameterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one toolParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ToolParameterDTO> findOne(Long id) {
        log.debug("Request to get ToolParameter : {}", id);
        return toolParameterRepository.findById(id)
            .map(toolParameterMapper::toDto);
    }

    /**
     * Delete the toolParameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ToolParameter : {}", id);
        toolParameterRepository.deleteById(id);
    }
}
