package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.InstantiationSuggestion;
import br.ufpa.labes.spm.repository.InstantiationSuggestionRepository;
import br.ufpa.labes.spm.service.dto.InstantiationSuggestionDTO;
import br.ufpa.labes.spm.service.mapper.InstantiationSuggestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link InstantiationSuggestion}.
 */
@Service
@Transactional
public class InstantiationSuggestionService {

    private final Logger log = LoggerFactory.getLogger(InstantiationSuggestionService.class);

    private final InstantiationSuggestionRepository instantiationSuggestionRepository;

    private final InstantiationSuggestionMapper instantiationSuggestionMapper;

    public InstantiationSuggestionService(InstantiationSuggestionRepository instantiationSuggestionRepository, InstantiationSuggestionMapper instantiationSuggestionMapper) {
        this.instantiationSuggestionRepository = instantiationSuggestionRepository;
        this.instantiationSuggestionMapper = instantiationSuggestionMapper;
    }

    /**
     * Save a instantiationSuggestion.
     *
     * @param instantiationSuggestionDTO the entity to save.
     * @return the persisted entity.
     */
    public InstantiationSuggestionDTO save(InstantiationSuggestionDTO instantiationSuggestionDTO) {
        log.debug("Request to save InstantiationSuggestion : {}", instantiationSuggestionDTO);
        InstantiationSuggestion instantiationSuggestion = instantiationSuggestionMapper.toEntity(instantiationSuggestionDTO);
        instantiationSuggestion = instantiationSuggestionRepository.save(instantiationSuggestion);
        return instantiationSuggestionMapper.toDto(instantiationSuggestion);
    }

    /**
     * Get all the instantiationSuggestions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InstantiationSuggestionDTO> findAll() {
        log.debug("Request to get all InstantiationSuggestions");
        return instantiationSuggestionRepository.findAll().stream()
            .map(instantiationSuggestionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one instantiationSuggestion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InstantiationSuggestionDTO> findOne(Long id) {
        log.debug("Request to get InstantiationSuggestion : {}", id);
        return instantiationSuggestionRepository.findById(id)
            .map(instantiationSuggestionMapper::toDto);
    }

    /**
     * Delete the instantiationSuggestion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InstantiationSuggestion : {}", id);
        instantiationSuggestionRepository.deleteById(id);
    }
}
