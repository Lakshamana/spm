package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.LessonLearned;
import br.ufpa.labes.spm.repository.LessonLearnedRepository;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.LessonLearned}.
 */
@RestController
@RequestMapping("/api")
public class LessonLearnedResource {

    private final Logger log = LoggerFactory.getLogger(LessonLearnedResource.class);

    private static final String ENTITY_NAME = "lessonLearned";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonLearnedRepository lessonLearnedRepository;

    public LessonLearnedResource(LessonLearnedRepository lessonLearnedRepository) {
        this.lessonLearnedRepository = lessonLearnedRepository;
    }

    /**
     * {@code POST  /lesson-learneds} : Create a new lessonLearned.
     *
     * @param lessonLearned the lessonLearned to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonLearned, or with status {@code 400 (Bad Request)} if the lessonLearned has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lesson-learneds")
    public ResponseEntity<LessonLearned> createLessonLearned(@RequestBody LessonLearned lessonLearned) throws URISyntaxException {
        log.debug("REST request to save LessonLearned : {}", lessonLearned);
        if (lessonLearned.getId() != null) {
            throw new BadRequestAlertException("A new lessonLearned cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonLearned result = lessonLearnedRepository.save(lessonLearned);
        return ResponseEntity.created(new URI("/api/lesson-learneds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lesson-learneds} : Updates an existing lessonLearned.
     *
     * @param lessonLearned the lessonLearned to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonLearned,
     * or with status {@code 400 (Bad Request)} if the lessonLearned is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonLearned couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lesson-learneds")
    public ResponseEntity<LessonLearned> updateLessonLearned(@RequestBody LessonLearned lessonLearned) throws URISyntaxException {
        log.debug("REST request to update LessonLearned : {}", lessonLearned);
        if (lessonLearned.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LessonLearned result = lessonLearnedRepository.save(lessonLearned);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lessonLearned.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lesson-learneds} : get all the lessonLearneds.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonLearneds in body.
     */
    @GetMapping("/lesson-learneds")
    public List<LessonLearned> getAllLessonLearneds() {
        log.debug("REST request to get all LessonLearneds");
        return lessonLearnedRepository.findAll();
    }

    /**
     * {@code GET  /lesson-learneds/:id} : get the "id" lessonLearned.
     *
     * @param id the id of the lessonLearned to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonLearned, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lesson-learneds/{id}")
    public ResponseEntity<LessonLearned> getLessonLearned(@PathVariable Long id) {
        log.debug("REST request to get LessonLearned : {}", id);
        Optional<LessonLearned> lessonLearned = lessonLearnedRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lessonLearned);
    }

    /**
     * {@code DELETE  /lesson-learneds/:id} : delete the "id" lessonLearned.
     *
     * @param id the id of the lessonLearned to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lesson-learneds/{id}")
    public ResponseEntity<Void> deleteLessonLearned(@PathVariable Long id) {
        log.debug("REST request to delete LessonLearned : {}", id);
        lessonLearnedRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
