package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.LessonLearned;
import br.ufpa.labes.spm.repository.LessonLearnedRepository;
import br.ufpa.labes.spm.service.dto.LessonLearnedDTO;
import br.ufpa.labes.spm.service.mapper.LessonLearnedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link LessonLearned}.
 */
@Service
@Transactional
public class LessonLearnedService {

    private final Logger log = LoggerFactory.getLogger(LessonLearnedService.class);

    private final LessonLearnedRepository lessonLearnedRepository;

    private final LessonLearnedMapper lessonLearnedMapper;

    public LessonLearnedService(LessonLearnedRepository lessonLearnedRepository, LessonLearnedMapper lessonLearnedMapper) {
        this.lessonLearnedRepository = lessonLearnedRepository;
        this.lessonLearnedMapper = lessonLearnedMapper;
    }

    /**
     * Save a lessonLearned.
     *
     * @param lessonLearnedDTO the entity to save.
     * @return the persisted entity.
     */
    public LessonLearnedDTO save(LessonLearnedDTO lessonLearnedDTO) {
        log.debug("Request to save LessonLearned : {}", lessonLearnedDTO);
        LessonLearned lessonLearned = lessonLearnedMapper.toEntity(lessonLearnedDTO);
        lessonLearned = lessonLearnedRepository.save(lessonLearned);
        return lessonLearnedMapper.toDto(lessonLearned);
    }

    /**
     * Get all the lessonLearneds.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LessonLearnedDTO> findAll() {
        log.debug("Request to get all LessonLearneds");
        return lessonLearnedRepository.findAll().stream()
            .map(lessonLearnedMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one lessonLearned by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LessonLearnedDTO> findOne(Long id) {
        log.debug("Request to get LessonLearned : {}", id);
        return lessonLearnedRepository.findById(id)
            .map(lessonLearnedMapper::toDto);
    }

    /**
     * Delete the lessonLearned by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LessonLearned : {}", id);
        lessonLearnedRepository.deleteById(id);
    }
}
