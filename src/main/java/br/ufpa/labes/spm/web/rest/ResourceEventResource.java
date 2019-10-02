package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ResourceEvent;
import br.ufpa.labes.spm.repository.ResourceEventRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ResourceEvent}. */
@RestController
@RequestMapping("/api")
public class ResourceEventResource {

  private final Logger log = LoggerFactory.getLogger(ResourceEventResource.class);

  private static final String ENTITY_NAME = "resourceEvent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ResourceEventRepository resourceEventRepository;

  public ResourceEventResource(ResourceEventRepository resourceEventRepository) {
    this.resourceEventRepository = resourceEventRepository;
  }

  /**
   * {@code POST /resource-events} : Create a new resourceEvent.
   *
   * @param resourceEvent the resourceEvent to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     resourceEvent, or with status {@code 400 (Bad Request)} if the resourceEvent has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/resource-events")
  public ResponseEntity<ResourceEvent> createResourceEvent(@RequestBody ResourceEvent resourceEvent)
      throws URISyntaxException {
    log.debug("REST request to save ResourceEvent : {}", resourceEvent);
    if (resourceEvent.getId() != null) {
      throw new BadRequestAlertException(
          "A new resourceEvent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ResourceEvent result = resourceEventRepository.save(resourceEvent);
    return ResponseEntity.created(new URI("/api/resource-events/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /resource-events} : Updates an existing resourceEvent.
   *
   * @param resourceEvent the resourceEvent to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     resourceEvent, or with status {@code 400 (Bad Request)} if the resourceEvent is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the resourceEvent couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/resource-events")
  public ResponseEntity<ResourceEvent> updateResourceEvent(@RequestBody ResourceEvent resourceEvent)
      throws URISyntaxException {
    log.debug("REST request to update ResourceEvent : {}", resourceEvent);
    if (resourceEvent.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ResourceEvent result = resourceEventRepository.save(resourceEvent);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, resourceEvent.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /resource-events} : get all the resourceEvents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceEvents
   *     in body.
   */
  @GetMapping("/resource-events")
  public List<ResourceEvent> getAllResourceEvents() {
    log.debug("REST request to get all ResourceEvents");
    return resourceEventRepository.findAll();
  }

  /**
   * {@code GET /resource-events/:id} : get the "id" resourceEvent.
   *
   * @param id the id of the resourceEvent to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     resourceEvent, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/resource-events/{id}")
  public ResponseEntity<ResourceEvent> getResourceEvent(@PathVariable Long id) {
    log.debug("REST request to get ResourceEvent : {}", id);
    Optional<ResourceEvent> resourceEvent = resourceEventRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(resourceEvent);
  }

  /**
   * {@code DELETE /resource-events/:id} : delete the "id" resourceEvent.
   *
   * @param id the id of the resourceEvent to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/resource-events/{id}")
  public ResponseEntity<Void> deleteResourceEvent(@PathVariable Long id) {
    log.debug("REST request to delete ResourceEvent : {}", id);
    resourceEventRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
