package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.LessonLearnedService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.LessonLearnedDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.LessonLearned}. */
@RestController
@RequestMapping("/api")
public class LessonLearnedResource {

  private final Logger log = LoggerFactory.getLogger(LessonLearnedResource.class);

  private static final String ENTITY_NAME = "lessonLearned";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final LessonLearnedService lessonLearnedService;

  public LessonLearnedResource(LessonLearnedService lessonLearnedService) {
    this.lessonLearnedService = lessonLearnedService;
  }

  /**
   * {@code POST /lesson-learneds} : Create a new lessonLearned.
   *
   * @param lessonLearnedDTO the lessonLearnedDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     lessonLearnedDTO, or with status {@code 400 (Bad Request)} if the lessonLearned has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/lesson-learneds")
  public ResponseEntity<LessonLearnedDTO> createLessonLearned(
      @RequestBody LessonLearnedDTO lessonLearnedDTO) throws URISyntaxException {
    log.debug("REST request to save LessonLearned : {}", lessonLearnedDTO);
    if (lessonLearnedDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new lessonLearned cannot already have an ID", ENTITY_NAME, "idexists");
    }
    LessonLearnedDTO result = lessonLearnedService.save(lessonLearnedDTO);
    return ResponseEntity.created(new URI("/api/lesson-learneds/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /lesson-learneds} : Updates an existing lessonLearned.
   *
   * @param lessonLearnedDTO the lessonLearnedDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     lessonLearnedDTO, or with status {@code 400 (Bad Request)} if the lessonLearnedDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the lessonLearnedDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/lesson-learneds")
  public ResponseEntity<LessonLearnedDTO> updateLessonLearned(
      @RequestBody LessonLearnedDTO lessonLearnedDTO) throws URISyntaxException {
    log.debug("REST request to update LessonLearned : {}", lessonLearnedDTO);
    if (lessonLearnedDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    LessonLearnedDTO result = lessonLearnedService.save(lessonLearnedDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, lessonLearnedDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /lesson-learneds} : get all the lessonLearneds.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonLearneds
   *     in body.
   */
  @GetMapping("/lesson-learneds")
  public List<LessonLearnedDTO> getAllLessonLearneds() {
    log.debug("REST request to get all LessonLearneds");
    return lessonLearnedService.findAll();
  }

  /**
   * {@code GET /lesson-learneds/:id} : get the "id" lessonLearned.
   *
   * @param id the id of the lessonLearnedDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     lessonLearnedDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/lesson-learneds/{id}")
  public ResponseEntity<LessonLearnedDTO> getLessonLearned(@PathVariable Long id) {
    log.debug("REST request to get LessonLearned : {}", id);
    Optional<LessonLearnedDTO> lessonLearnedDTO = lessonLearnedService.findOne(id);
    return ResponseUtil.wrapOrNotFound(lessonLearnedDTO);
  }

  /**
   * {@code DELETE /lesson-learneds/:id} : delete the "id" lessonLearned.
   *
   * @param id the id of the lessonLearnedDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/lesson-learneds/{id}")
  public ResponseEntity<Void> deleteLessonLearned(@PathVariable Long id) {
    log.debug("REST request to delete LessonLearned : {}", id);
    lessonLearnedService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
