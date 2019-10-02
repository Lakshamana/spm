package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.repository.AgendaEventRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgendaEvent}. */
@RestController
@RequestMapping("/api")
public class AgendaEventResource {

  private final Logger log = LoggerFactory.getLogger(AgendaEventResource.class);

  private static final String ENTITY_NAME = "agendaEvent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgendaEventRepository agendaEventRepository;

  public AgendaEventResource(AgendaEventRepository agendaEventRepository) {
    this.agendaEventRepository = agendaEventRepository;
  }

  /**
   * {@code POST /agenda-events} : Create a new agendaEvent.
   *
   * @param agendaEvent the agendaEvent to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agendaEvent, or with status {@code 400 (Bad Request)} if the agendaEvent has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agenda-events")
  public ResponseEntity<AgendaEvent> createAgendaEvent(@RequestBody AgendaEvent agendaEvent)
      throws URISyntaxException {
    log.debug("REST request to save AgendaEvent : {}", agendaEvent);
    if (agendaEvent.getId() != null) {
      throw new BadRequestAlertException(
          "A new agendaEvent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgendaEvent result = agendaEventRepository.save(agendaEvent);
    return ResponseEntity.created(new URI("/api/agenda-events/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agenda-events} : Updates an existing agendaEvent.
   *
   * @param agendaEvent the agendaEvent to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agendaEvent, or with status {@code 400 (Bad Request)} if the agendaEvent is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the agendaEvent couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agenda-events")
  public ResponseEntity<AgendaEvent> updateAgendaEvent(@RequestBody AgendaEvent agendaEvent)
      throws URISyntaxException {
    log.debug("REST request to update AgendaEvent : {}", agendaEvent);
    if (agendaEvent.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgendaEvent result = agendaEventRepository.save(agendaEvent);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agendaEvent.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agenda-events} : get all the agendaEvents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agendaEvents in
   *     body.
   */
  @GetMapping("/agenda-events")
  public List<AgendaEvent> getAllAgendaEvents() {
    log.debug("REST request to get all AgendaEvents");
    return agendaEventRepository.findAll();
  }

  /**
   * {@code GET /agenda-events/:id} : get the "id" agendaEvent.
   *
   * @param id the id of the agendaEvent to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agendaEvent,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agenda-events/{id}")
  public ResponseEntity<AgendaEvent> getAgendaEvent(@PathVariable Long id) {
    log.debug("REST request to get AgendaEvent : {}", id);
    Optional<AgendaEvent> agendaEvent = agendaEventRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(agendaEvent);
  }

  /**
   * {@code DELETE /agenda-events/:id} : delete the "id" agendaEvent.
   *
   * @param id the id of the agendaEvent to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agenda-events/{id}")
  public ResponseEntity<Void> deleteAgendaEvent(@PathVariable Long id) {
    log.debug("REST request to delete AgendaEvent : {}", id);
    agendaEventRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
