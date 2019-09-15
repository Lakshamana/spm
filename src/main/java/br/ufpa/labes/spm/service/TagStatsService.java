package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.TagStats;
import br.ufpa.labes.spm.repository.TagStatsRepository;
import br.ufpa.labes.spm.service.dto.TagStatsDTO;
import br.ufpa.labes.spm.service.mapper.TagStatsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TagStats}.
 */
@Service
@Transactional
public class TagStatsService {

    private final Logger log = LoggerFactory.getLogger(TagStatsService.class);

    private final TagStatsRepository tagStatsRepository;

    private final TagStatsMapper tagStatsMapper;

    public TagStatsService(TagStatsRepository tagStatsRepository, TagStatsMapper tagStatsMapper) {
        this.tagStatsRepository = tagStatsRepository;
        this.tagStatsMapper = tagStatsMapper;
    }

    /**
     * Save a tagStats.
     *
     * @param tagStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public TagStatsDTO save(TagStatsDTO tagStatsDTO) {
        log.debug("Request to save TagStats : {}", tagStatsDTO);
        TagStats tagStats = tagStatsMapper.toEntity(tagStatsDTO);
        tagStats = tagStatsRepository.save(tagStats);
        return tagStatsMapper.toDto(tagStats);
    }

    /**
     * Get all the tagStats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TagStatsDTO> findAll() {
        log.debug("Request to get all TagStats");
        return tagStatsRepository.findAll().stream()
            .map(tagStatsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one tagStats by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TagStatsDTO> findOne(Long id) {
        log.debug("Request to get TagStats : {}", id);
        return tagStatsRepository.findById(id)
            .map(tagStatsMapper::toDto);
    }

    /**
     * Delete the tagStats by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TagStats : {}", id);
        tagStatsRepository.deleteById(id);
    }
}
