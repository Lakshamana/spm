package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Script;
import br.ufpa.labes.spm.repository.ScriptRepository;
import br.ufpa.labes.spm.service.dto.ScriptDTO;
import br.ufpa.labes.spm.service.mapper.ScriptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Script}.
 */
@Service
@Transactional
public class ScriptService {

    private final Logger log = LoggerFactory.getLogger(ScriptService.class);

    private final ScriptRepository scriptRepository;

    private final ScriptMapper scriptMapper;

    public ScriptService(ScriptRepository scriptRepository, ScriptMapper scriptMapper) {
        this.scriptRepository = scriptRepository;
        this.scriptMapper = scriptMapper;
    }

    /**
     * Save a script.
     *
     * @param scriptDTO the entity to save.
     * @return the persisted entity.
     */
    public ScriptDTO save(ScriptDTO scriptDTO) {
        log.debug("Request to save Script : {}", scriptDTO);
        Script script = scriptMapper.toEntity(scriptDTO);
        script = scriptRepository.save(script);
        return scriptMapper.toDto(script);
    }

    /**
     * Get all the scripts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ScriptDTO> findAll() {
        log.debug("Request to get all Scripts");
        return scriptRepository.findAll().stream()
            .map(scriptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the scripts where TheSubroutineSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ScriptDTO> findAllWhereTheSubroutineSuperIsNull() {
        log.debug("Request to get all scripts where TheSubroutineSuper is null");
        return StreamSupport
            .stream(scriptRepository.findAll().spliterator(), false)
            .filter(script -> script.getTheSubroutineSuper() == null)
            .map(scriptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one script by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScriptDTO> findOne(Long id) {
        log.debug("Request to get Script : {}", id);
        return scriptRepository.findById(id)
            .map(scriptMapper::toDto);
    }

    /**
     * Delete the script by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Script : {}", id);
        scriptRepository.deleteById(id);
    }
}
