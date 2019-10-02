package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.repository.ModelingActivityEventRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ModelingActivityEvent}. */
@RestController
@RequestMapping("/api")
public class ModelingActivityEventResource {

  private final Logger log = LoggerFactory.getLogger(ModelingActivityEventResource.class);

  private static final String ENTITY_NAME = "modelingActivityEvent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ModelingActivityEventRepository modelingActivityEventRepository;

  public ModelingActivityEventResource(
      ModelingActivityEventRepository modelingActivityEventRepository) {
    this.modelingActivityEventRepository = modelingActivityEventRepository;
  }

  /**
   * {@code POST /modeling-activity-events} : Create a new modelingActivityEvent.
   *
   * @param modelingActivityEvent the modelingActivityEvent to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     modelingActivityEvent, or with status {@code 400 (Bad Request)} if the
   *     modelingActivityEvent has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/modeling-activity-events")
  public ResponseEntity<ModelingActivityEvent> createModelingActivityEvent(
      @RequestBody ModelingActivityEvent modelingActivityEvent) throws URISyntaxException {
    log.debug("REST request to save ModelingActivityEvent : {}", modelingActivityEvent);
    if (modelingActivityEvent.getId() != null) {
      throw new BadRequestAlertException(
          "A new modelingActivityEvent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ModelingActivityEvent result = modelingActivityEventRepository.save(modelingActivityEvent);
    return ResponseEntity.created(new URI("/api/modeling-activity-events/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /modeling-activity-events} : Updates an existing modelingActivityEvent.
   *
   * @param modelingActivityEvent the modelingActivityEvent to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     modelingActivityEvent, or with status {@code 400 (Bad Request)} if the
   *     modelingActivityEvent is not valid, or with status {@code 500 (Internal Server Error)} if
   *     the modelingActivityEvent couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/modeling-activity-events")
  public ResponseEntity<ModelingActivityEvent> updateModelingActivityEvent(
      @RequestBody ModelingActivityEvent modelingActivityEvent) throws URISyntaxException {
    log.debug("REST request to update ModelingActivityEvent : {}", modelingActivityEvent);
    if (modelingActivityEvent.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ModelingActivityEvent result = modelingActivityEventRepository.save(modelingActivityEvent);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, modelingActivityEvent.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /modeling-activity-events} : get all the modelingActivityEvents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     modelingActivityEvents in body.
   */
  @GetMapping("/modeling-activity-events")
  public List<ModelingActivityEvent> getAllModelingActivityEvents() {
    log.debug("REST request to get all ModelingActivityEvents");
    return modelingActivityEventRepository.findAll();
  }

  /**
   * {@code GET /modeling-activity-events/:id} : get the "id" modelingActivityEvent.
   *
   * @param id the id of the modelingActivityEvent to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     modelingActivityEvent, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/modeling-activity-events/{id}")
  public ResponseEntity<ModelingActivityEvent> getModelingActivityEvent(@PathVariable Long id) {
    log.debug("REST request to get ModelingActivityEvent : {}", id);
    Optional<ModelingActivityEvent> modelingActivityEvent =
        modelingActivityEventRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(modelingActivityEvent);
  }

  /**
   * {@code DELETE /modeling-activity-events/:id} : delete the "id" modelingActivityEvent.
   *
   * @param id the id of the modelingActivityEvent to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/modeling-activity-events/{id}")
  public ResponseEntity<Void> deleteModelingActivityEvent(@PathVariable Long id) {
    log.debug("REST request to delete ModelingActivityEvent : {}", id);
    modelingActivityEventRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
