package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ModelingActivityEventService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ModelingActivityEventDTO;

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

  private final ModelingActivityEventService modelingActivityEventService;

  public ModelingActivityEventResource(ModelingActivityEventService modelingActivityEventService) {
    this.modelingActivityEventService = modelingActivityEventService;
  }

  /**
   * {@code POST /modeling-activity-events} : Create a new modelingActivityEvent.
   *
   * @param modelingActivityEventDTO the modelingActivityEventDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     modelingActivityEventDTO, or with status {@code 400 (Bad Request)} if the
   *     modelingActivityEvent has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/modeling-activity-events")
  public ResponseEntity<ModelingActivityEventDTO> createModelingActivityEvent(
      @RequestBody ModelingActivityEventDTO modelingActivityEventDTO) throws URISyntaxException {
    log.debug("REST request to save ModelingActivityEvent : {}", modelingActivityEventDTO);
    if (modelingActivityEventDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new modelingActivityEvent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ModelingActivityEventDTO result = modelingActivityEventService.save(modelingActivityEventDTO);
    return ResponseEntity.created(new URI("/api/modeling-activity-events/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /modeling-activity-events} : Updates an existing modelingActivityEvent.
   *
   * @param modelingActivityEventDTO the modelingActivityEventDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     modelingActivityEventDTO, or with status {@code 400 (Bad Request)} if the
   *     modelingActivityEventDTO is not valid, or with status {@code 500 (Internal Server Error)}
   *     if the modelingActivityEventDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/modeling-activity-events")
  public ResponseEntity<ModelingActivityEventDTO> updateModelingActivityEvent(
      @RequestBody ModelingActivityEventDTO modelingActivityEventDTO) throws URISyntaxException {
    log.debug("REST request to update ModelingActivityEvent : {}", modelingActivityEventDTO);
    if (modelingActivityEventDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ModelingActivityEventDTO result = modelingActivityEventService.save(modelingActivityEventDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, modelingActivityEventDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /modeling-activity-events} : get all the modelingActivityEvents.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     modelingActivityEvents in body.
   */
  @GetMapping("/modeling-activity-events")
  public List<ModelingActivityEventDTO> getAllModelingActivityEvents(
      @RequestParam(required = false) String filter) {
    if ("theeventsuper-is-null".equals(filter)) {
      log.debug("REST request to get all ModelingActivityEvents where theEventSuper is null");
      return modelingActivityEventService.findAllWhereTheEventSuperIsNull();
    }
    log.debug("REST request to get all ModelingActivityEvents");
    return modelingActivityEventService.findAll();
  }

  /**
   * {@code GET /modeling-activity-events/:id} : get the "id" modelingActivityEvent.
   *
   * @param id the id of the modelingActivityEventDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     modelingActivityEventDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/modeling-activity-events/{id}")
  public ResponseEntity<ModelingActivityEventDTO> getModelingActivityEvent(@PathVariable Long id) {
    log.debug("REST request to get ModelingActivityEvent : {}", id);
    Optional<ModelingActivityEventDTO> modelingActivityEventDTO =
        modelingActivityEventService.findOne(id);
    return ResponseUtil.wrapOrNotFound(modelingActivityEventDTO);
  }

  /**
   * {@code DELETE /modeling-activity-events/:id} : delete the "id" modelingActivityEvent.
   *
   * @param id the id of the modelingActivityEventDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/modeling-activity-events/{id}")
  public ResponseEntity<Void> deleteModelingActivityEvent(@PathVariable Long id) {
    log.debug("REST request to delete ModelingActivityEvent : {}", id);
    modelingActivityEventService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
