package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.TagStats;
import br.ufpa.labes.spm.repository.TagStatRepository;
import br.ufpa.labes.spm.service.dto.TagStatDTO;
import br.ufpa.labes.spm.service.mapper.TagStatMapper;
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
public class TagStatService {

    private final Logger log = LoggerFactory.getLogger(TagStatService.class);

    private final TagStatRepository tagStatRepository;

    private final TagStatMapper tagStatMapper;

    public TagStatService(TagStatRepository tagStatRepository, TagStatMapper tagStatMapper) {
        this.tagStatRepository = tagStatRepository;
        this.tagStatMapper = tagStatMapper;
    }

    /**
     * Save a tagStat.
     *
     * @param tagStatDTO the entity to save.
     * @return the persisted entity.
     */
    public TagStatDTO save(TagStatDTO tagStatDTO) {
        log.debug("Request to save TagStats : {}", tagStatDTO);
        TagStats tagStat = tagStatMapper.toEntity(tagStatDTO);
        tagStat = tagStatRepository.save(tagStat);
        return tagStatMapper.toDto(tagStat);
    }

    /**
     * Get all the tagStats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TagStatDTO> findAll() {
        log.debug("Request to get all TagStats");
        return tagStatRepository.findAll().stream()
            .map(tagStatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one tagStat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TagStatDTO> findOne(Long id) {
        log.debug("Request to get TagStats : {}", id);
        return tagStatRepository.findById(id)
            .map(tagStatMapper::toDto);
    }

    /**
     * Delete the tagStat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TagStats : {}", id);
        tagStatRepository.deleteById(id);
    }
}
