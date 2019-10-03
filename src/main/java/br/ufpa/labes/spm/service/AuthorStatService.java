package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AuthorStat;
import br.ufpa.labes.spm.repository.AuthorStatRepository;
import br.ufpa.labes.spm.service.dto.AuthorStatDTO;
import br.ufpa.labes.spm.service.mapper.AuthorStatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AuthorStat}.
 */
@Service
@Transactional
public class AuthorStatService {

    private final Logger log = LoggerFactory.getLogger(AuthorStatService.class);

    private final AuthorStatRepository authorStatRepository;

    private final AuthorStatMapper authorStatMapper;

    public AuthorStatService(AuthorStatRepository authorStatRepository, AuthorStatMapper authorStatMapper) {
        this.authorStatRepository = authorStatRepository;
        this.authorStatMapper = authorStatMapper;
    }

    /**
     * Save a authorStat.
     *
     * @param authorStatDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorStatDTO save(AuthorStatDTO authorStatDTO) {
        log.debug("Request to save AuthorStat : {}", authorStatDTO);
        AuthorStat authorStat = authorStatMapper.toEntity(authorStatDTO);
        authorStat = authorStatRepository.save(authorStat);
        return authorStatMapper.toDto(authorStat);
    }

    /**
     * Get all the authorStats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorStatDTO> findAll() {
        log.debug("Request to get all AuthorStats");
        return authorStatRepository.findAll().stream()
            .map(authorStatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one authorStat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthorStatDTO> findOne(Long id) {
        log.debug("Request to get AuthorStat : {}", id);
        return authorStatRepository.findById(id)
            .map(authorStatMapper::toDto);
    }

    /**
     * Delete the authorStat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuthorStat : {}", id);
        authorStatRepository.deleteById(id);
    }
}
