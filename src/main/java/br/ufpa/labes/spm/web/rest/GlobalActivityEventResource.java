package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.GlobalActivityEvent;
import br.ufpa.labes.spm.repository.GlobalActivityEventRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.GlobalActivityEvent}. */
@RestController
@RequestMapping("/api")
public class GlobalActivityEventResource {

  private final Logger log = LoggerFactory.getLogger(GlobalActivityEventResource.class);

  private static final String ENTITY_NAME = "globalActivityEvent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final GlobalActivityEventRepository globalActivityEventRepository;

  public GlobalActivityEventResource(GlobalActivityEventRepository globalActivityEventRepository) {
    this.globalActivityEventRepository = globalActivityEventRepository;
  }

  /**
   * {@code POST /global-activity-events} : Create a new globalActivityEvent.
   *
   * @param globalActivityEvent the globalActivityEvent to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     globalActivityEvent, or with status {@code 400 (Bad Request)} if the globalActivityEvent
   *     has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/global-activity-events")
  public ResponseEntity<GlobalActivityEvent> createGlobalActivityEvent(
      @RequestBody GlobalActivityEvent globalActivityEvent) throws URISyntaxException {
    log.debug("REST request to save GlobalActivityEvent : {}", globalActivityEvent);
    if (globalActivityEvent.getId() != null) {
      throw new BadRequestAlertException(
          "A new globalActivityEvent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    GlobalActivityEvent result = globalActivityEventRepository.save(globalActivityEvent);
    return ResponseEntity.created(new URI("/api/global-activity-events/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /global-activity-events} : Updates an existing globalActivityEvent.
   *
   * @param globalActivityEvent the globalActivityEvent to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     globalActivityEvent, or with status {@code 400 (Bad Request)} if the globalActivityEvent is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the globalActivityEvent
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/global-activity-events")
  public ResponseEntity<GlobalActivityEvent> updateGlobalActivityEvent(
      @RequestBody GlobalActivityEvent globalActivityEvent) throws URISyntaxException {
    log.debug("REST request to update GlobalActivityEvent : {}", globalActivityEvent);
    if (globalActivityEvent.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    GlobalActivityEvent result = globalActivityEventRepository.save(globalActivityEvent);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, globalActivityEvent.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /global-activity-events} : get all the globalActivityEvents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     globalActivityEvents in body.
   */
  @GetMapping("/global-activity-events")
  public List<GlobalActivityEvent> getAllGlobalActivityEvents() {
    log.debug("REST request to get all GlobalActivityEvents");
    return globalActivityEventRepository.findAll();
  }

  /**
   * {@code GET /global-activity-events/:id} : get the "id" globalActivityEvent.
   *
   * @param id the id of the globalActivityEvent to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     globalActivityEvent, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/global-activity-events/{id}")
  public ResponseEntity<GlobalActivityEvent> getGlobalActivityEvent(@PathVariable Long id) {
    log.debug("REST request to get GlobalActivityEvent : {}", id);
    Optional<GlobalActivityEvent> globalActivityEvent = globalActivityEventRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(globalActivityEvent);
  }

  /**
   * {@code DELETE /global-activity-events/:id} : delete the "id" globalActivityEvent.
   *
   * @param id the id of the globalActivityEvent to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/global-activity-events/{id}")
  public ResponseEntity<Void> deleteGlobalActivityEvent(@PathVariable Long id) {
    log.debug("REST request to delete GlobalActivityEvent : {}", id);
    globalActivityEventRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
