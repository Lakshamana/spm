package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ResourceInstSug;
import br.ufpa.labes.spm.repository.ResourceInstSugRepository;
import br.ufpa.labes.spm.service.dto.ResourceInstSugDTO;
import br.ufpa.labes.spm.service.mapper.ResourceInstSugMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ResourceInstSug}.
 */
@Service
@Transactional
public class ResourceInstSugService {

    private final Logger log = LoggerFactory.getLogger(ResourceInstSugService.class);

    private final ResourceInstSugRepository resourceInstSugRepository;

    private final ResourceInstSugMapper resourceInstSugMapper;

    public ResourceInstSugService(ResourceInstSugRepository resourceInstSugRepository, ResourceInstSugMapper resourceInstSugMapper) {
        this.resourceInstSugRepository = resourceInstSugRepository;
        this.resourceInstSugMapper = resourceInstSugMapper;
    }

    /**
     * Save a resourceInstSug.
     *
     * @param resourceInstSugDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceInstSugDTO save(ResourceInstSugDTO resourceInstSugDTO) {
        log.debug("Request to save ResourceInstSug : {}", resourceInstSugDTO);
        ResourceInstSug resourceInstSug = resourceInstSugMapper.toEntity(resourceInstSugDTO);
        resourceInstSug = resourceInstSugRepository.save(resourceInstSug);
        return resourceInstSugMapper.toDto(resourceInstSug);
    }

    /**
     * Get all the resourceInstSugs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResourceInstSugDTO> findAll() {
        log.debug("Request to get all ResourceInstSugs");
        return resourceInstSugRepository.findAll().stream()
            .map(resourceInstSugMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one resourceInstSug by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResourceInstSugDTO> findOne(Long id) {
        log.debug("Request to get ResourceInstSug : {}", id);
        return resourceInstSugRepository.findById(id)
            .map(resourceInstSugMapper::toDto);
    }

    /**
     * Delete the resourceInstSug by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourceInstSug : {}", id);
        resourceInstSugRepository.deleteById(id);
    }
}
