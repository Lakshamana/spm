package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ActivityMetric;
import br.ufpa.labes.spm.repository.ActivityMetricRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ActivityMetric}. */
@RestController
@RequestMapping("/api")
public class ActivityMetricResource {

  private final Logger log = LoggerFactory.getLogger(ActivityMetricResource.class);

  private static final String ENTITY_NAME = "activityMetric";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ActivityMetricRepository activityMetricRepository;

  public ActivityMetricResource(ActivityMetricRepository activityMetricRepository) {
    this.activityMetricRepository = activityMetricRepository;
  }

  /**
   * {@code POST /activity-metrics} : Create a new activityMetric.
   *
   * @param activityMetric the activityMetric to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     activityMetric, or with status {@code 400 (Bad Request)} if the activityMetric has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/activity-metrics")
  public ResponseEntity<ActivityMetric> createActivityMetric(
      @RequestBody ActivityMetric activityMetric) throws URISyntaxException {
    log.debug("REST request to save ActivityMetric : {}", activityMetric);
    if (activityMetric.getId() != null) {
      throw new BadRequestAlertException(
          "A new activityMetric cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ActivityMetric result = activityMetricRepository.save(activityMetric);
    return ResponseEntity.created(new URI("/api/activity-metrics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /activity-metrics} : Updates an existing activityMetric.
   *
   * @param activityMetric the activityMetric to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     activityMetric, or with status {@code 400 (Bad Request)} if the activityMetric is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the activityMetric couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/activity-metrics")
  public ResponseEntity<ActivityMetric> updateActivityMetric(
      @RequestBody ActivityMetric activityMetric) throws URISyntaxException {
    log.debug("REST request to update ActivityMetric : {}", activityMetric);
    if (activityMetric.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ActivityMetric result = activityMetricRepository.save(activityMetric);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, activityMetric.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /activity-metrics} : get all the activityMetrics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityMetrics
   *     in body.
   */
  @GetMapping("/activity-metrics")
  public List<ActivityMetric> getAllActivityMetrics() {
    log.debug("REST request to get all ActivityMetrics");
    return activityMetricRepository.findAll();
  }

  /**
   * {@code GET /activity-metrics/:id} : get the "id" activityMetric.
   *
   * @param id the id of the activityMetric to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     activityMetric, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/activity-metrics/{id}")
  public ResponseEntity<ActivityMetric> getActivityMetric(@PathVariable Long id) {
    log.debug("REST request to get ActivityMetric : {}", id);
    Optional<ActivityMetric> activityMetric = activityMetricRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(activityMetric);
  }

  /**
   * {@code DELETE /activity-metrics/:id} : delete the "id" activityMetric.
   *
   * @param id the id of the activityMetric to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/activity-metrics/{id}")
  public ResponseEntity<Void> deleteActivityMetric(@PathVariable Long id) {
    log.debug("REST request to delete ActivityMetric : {}", id);
    activityMetricRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
