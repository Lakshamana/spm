package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ActivityInstantiated;
import br.ufpa.labes.spm.repository.ActivityInstantiatedRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ActivityInstantiated}. */
@RestController
@RequestMapping("/api")
public class ActivityInstantiatedResource {

  private final Logger log = LoggerFactory.getLogger(ActivityInstantiatedResource.class);

  private static final String ENTITY_NAME = "activityInstantiated";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ActivityInstantiatedRepository activityInstantiatedRepository;

  public ActivityInstantiatedResource(
      ActivityInstantiatedRepository activityInstantiatedRepository) {
    this.activityInstantiatedRepository = activityInstantiatedRepository;
  }

  /**
   * {@code POST /activity-instantiateds} : Create a new activityInstantiated.
   *
   * @param activityInstantiated the activityInstantiated to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     activityInstantiated, or with status {@code 400 (Bad Request)} if the activityInstantiated
   *     has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/activity-instantiateds")
  public ResponseEntity<ActivityInstantiated> createActivityInstantiated(
      @RequestBody ActivityInstantiated activityInstantiated) throws URISyntaxException {
    log.debug("REST request to save ActivityInstantiated : {}", activityInstantiated);
    if (activityInstantiated.getId() != null) {
      throw new BadRequestAlertException(
          "A new activityInstantiated cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ActivityInstantiated result = activityInstantiatedRepository.save(activityInstantiated);
    return ResponseEntity.created(new URI("/api/activity-instantiateds/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /activity-instantiateds} : Updates an existing activityInstantiated.
   *
   * @param activityInstantiated the activityInstantiated to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     activityInstantiated, or with status {@code 400 (Bad Request)} if the activityInstantiated
   *     is not valid, or with status {@code 500 (Internal Server Error)} if the
   *     activityInstantiated couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/activity-instantiateds")
  public ResponseEntity<ActivityInstantiated> updateActivityInstantiated(
      @RequestBody ActivityInstantiated activityInstantiated) throws URISyntaxException {
    log.debug("REST request to update ActivityInstantiated : {}", activityInstantiated);
    if (activityInstantiated.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ActivityInstantiated result = activityInstantiatedRepository.save(activityInstantiated);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, activityInstantiated.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /activity-instantiateds} : get all the activityInstantiateds.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     activityInstantiateds in body.
   */
  @GetMapping("/activity-instantiateds")
  public List<ActivityInstantiated> getAllActivityInstantiateds() {
    log.debug("REST request to get all ActivityInstantiateds");
    return activityInstantiatedRepository.findAll();
  }

  /**
   * {@code GET /activity-instantiateds/:id} : get the "id" activityInstantiated.
   *
   * @param id the id of the activityInstantiated to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     activityInstantiated, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/activity-instantiateds/{id}")
  public ResponseEntity<ActivityInstantiated> getActivityInstantiated(@PathVariable Long id) {
    log.debug("REST request to get ActivityInstantiated : {}", id);
    Optional<ActivityInstantiated> activityInstantiated =
        activityInstantiatedRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(activityInstantiated);
  }

  /**
   * {@code DELETE /activity-instantiateds/:id} : delete the "id" activityInstantiated.
   *
   * @param id the id of the activityInstantiated to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/activity-instantiateds/{id}")
  public ResponseEntity<Void> deleteActivityInstantiated(@PathVariable Long id) {
    log.debug("REST request to delete ActivityInstantiated : {}", id);
    activityInstantiatedRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
