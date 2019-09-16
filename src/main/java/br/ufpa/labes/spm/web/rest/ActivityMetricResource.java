package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ActivityMetricService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ActivityMetricDTO;

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

  private final ActivityMetricService activityMetricService;

  public ActivityMetricResource(ActivityMetricService activityMetricService) {
    this.activityMetricService = activityMetricService;
  }

  /**
   * {@code POST /activity-metrics} : Create a new activityMetric.
   *
   * @param activityMetricDTO the activityMetricDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     activityMetricDTO, or with status {@code 400 (Bad Request)} if the activityMetric has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/activity-metrics")
  public ResponseEntity<ActivityMetricDTO> createActivityMetric(
      @RequestBody ActivityMetricDTO activityMetricDTO) throws URISyntaxException {
    log.debug("REST request to save ActivityMetric : {}", activityMetricDTO);
    if (activityMetricDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new activityMetric cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ActivityMetricDTO result = activityMetricService.save(activityMetricDTO);
    return ResponseEntity.created(new URI("/api/activity-metrics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /activity-metrics} : Updates an existing activityMetric.
   *
   * @param activityMetricDTO the activityMetricDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     activityMetricDTO, or with status {@code 400 (Bad Request)} if the activityMetricDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the activityMetricDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/activity-metrics")
  public ResponseEntity<ActivityMetricDTO> updateActivityMetric(
      @RequestBody ActivityMetricDTO activityMetricDTO) throws URISyntaxException {
    log.debug("REST request to update ActivityMetric : {}", activityMetricDTO);
    if (activityMetricDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ActivityMetricDTO result = activityMetricService.save(activityMetricDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, activityMetricDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /activity-metrics} : get all the activityMetrics.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityMetrics
   *     in body.
   */
  @GetMapping("/activity-metrics")
  public List<ActivityMetricDTO> getAllActivityMetrics(
      @RequestParam(required = false) String filter) {
    if ("themetricsuper-is-null".equals(filter)) {
      log.debug("REST request to get all ActivityMetrics where theMetricSuper is null");
      return activityMetricService.findAllWhereTheMetricSuperIsNull();
    }
    log.debug("REST request to get all ActivityMetrics");
    return activityMetricService.findAll();
  }

  /**
   * {@code GET /activity-metrics/:id} : get the "id" activityMetric.
   *
   * @param id the id of the activityMetricDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     activityMetricDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/activity-metrics/{id}")
  public ResponseEntity<ActivityMetricDTO> getActivityMetric(@PathVariable Long id) {
    log.debug("REST request to get ActivityMetric : {}", id);
    Optional<ActivityMetricDTO> activityMetricDTO = activityMetricService.findOne(id);
    return ResponseUtil.wrapOrNotFound(activityMetricDTO);
  }

  /**
   * {@code DELETE /activity-metrics/:id} : delete the "id" activityMetric.
   *
   * @param id the id of the activityMetricDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/activity-metrics/{id}")
  public ResponseEntity<Void> deleteActivityMetric(@PathVariable Long id) {
    log.debug("REST request to delete ActivityMetric : {}", id);
    activityMetricService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
