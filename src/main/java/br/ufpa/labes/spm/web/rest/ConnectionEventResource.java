package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ConnectionEvent;
import br.ufpa.labes.spm.repository.ConnectionEventRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ConnectionEvent}. */
@RestController
@RequestMapping("/api")
public class ConnectionEventResource {

  private final Logger log = LoggerFactory.getLogger(ConnectionEventResource.class);

  private static final String ENTITY_NAME = "connectionEvent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ConnectionEventRepository connectionEventRepository;

  public ConnectionEventResource(ConnectionEventRepository connectionEventRepository) {
    this.connectionEventRepository = connectionEventRepository;
  }

  /**
   * {@code POST /connection-events} : Create a new connectionEvent.
   *
   * @param connectionEvent the connectionEvent to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     connectionEvent, or with status {@code 400 (Bad Request)} if the connectionEvent has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/connection-events")
  public ResponseEntity<ConnectionEvent> createConnectionEvent(
      @RequestBody ConnectionEvent connectionEvent) throws URISyntaxException {
    log.debug("REST request to save ConnectionEvent : {}", connectionEvent);
    if (connectionEvent.getId() != null) {
      throw new BadRequestAlertException(
          "A new connectionEvent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ConnectionEvent result = connectionEventRepository.save(connectionEvent);
    return ResponseEntity.created(new URI("/api/connection-events/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /connection-events} : Updates an existing connectionEvent.
   *
   * @param connectionEvent the connectionEvent to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     connectionEvent, or with status {@code 400 (Bad Request)} if the connectionEvent is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the connectionEvent couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/connection-events")
  public ResponseEntity<ConnectionEvent> updateConnectionEvent(
      @RequestBody ConnectionEvent connectionEvent) throws URISyntaxException {
    log.debug("REST request to update ConnectionEvent : {}", connectionEvent);
    if (connectionEvent.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ConnectionEvent result = connectionEventRepository.save(connectionEvent);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, connectionEvent.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /connection-events} : get all the connectionEvents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     connectionEvents in body.
   */
  @GetMapping("/connection-events")
  public List<ConnectionEvent> getAllConnectionEvents() {
    log.debug("REST request to get all ConnectionEvents");
    return connectionEventRepository.findAll();
  }

  /**
   * {@code GET /connection-events/:id} : get the "id" connectionEvent.
   *
   * @param id the id of the connectionEvent to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     connectionEvent, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/connection-events/{id}")
  public ResponseEntity<ConnectionEvent> getConnectionEvent(@PathVariable Long id) {
    log.debug("REST request to get ConnectionEvent : {}", id);
    Optional<ConnectionEvent> connectionEvent = connectionEventRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(connectionEvent);
  }

  /**
   * {@code DELETE /connection-events/:id} : delete the "id" connectionEvent.
   *
   * @param id the id of the connectionEvent to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/connection-events/{id}")
  public ResponseEntity<Void> deleteConnectionEvent(@PathVariable Long id) {
    log.debug("REST request to delete ConnectionEvent : {}", id);
    connectionEventRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
