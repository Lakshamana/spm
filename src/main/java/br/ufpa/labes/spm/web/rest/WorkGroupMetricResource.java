package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.WorkGroupMetric;
import br.ufpa.labes.spm.repository.WorkGroupMetricRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroupMetric}. */
@RestController
@RequestMapping("/api")
public class WorkGroupMetricResource {

  private final Logger log = LoggerFactory.getLogger(WorkGroupMetricResource.class);

  private static final String ENTITY_NAME = "workGroupMetric";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final WorkGroupMetricRepository workGroupMetricRepository;

  public WorkGroupMetricResource(WorkGroupMetricRepository workGroupMetricRepository) {
    this.workGroupMetricRepository = workGroupMetricRepository;
  }

  /**
   * {@code POST /work-group-metrics} : Create a new workGroupMetric.
   *
   * @param workGroupMetric the workGroupMetric to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     workGroupMetric, or with status {@code 400 (Bad Request)} if the workGroupMetric has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/work-group-metrics")
  public ResponseEntity<WorkGroupMetric> createWorkGroupMetric(
      @RequestBody WorkGroupMetric workGroupMetric) throws URISyntaxException {
    log.debug("REST request to save WorkGroupMetric : {}", workGroupMetric);
    if (workGroupMetric.getId() != null) {
      throw new BadRequestAlertException(
          "A new workGroupMetric cannot already have an ID", ENTITY_NAME, "idexists");
    }
    WorkGroupMetric result = workGroupMetricRepository.save(workGroupMetric);
    return ResponseEntity.created(new URI("/api/work-group-metrics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /work-group-metrics} : Updates an existing workGroupMetric.
   *
   * @param workGroupMetric the workGroupMetric to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     workGroupMetric, or with status {@code 400 (Bad Request)} if the workGroupMetric is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the workGroupMetric couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/work-group-metrics")
  public ResponseEntity<WorkGroupMetric> updateWorkGroupMetric(
      @RequestBody WorkGroupMetric workGroupMetric) throws URISyntaxException {
    log.debug("REST request to update WorkGroupMetric : {}", workGroupMetric);
    if (workGroupMetric.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    WorkGroupMetric result = workGroupMetricRepository.save(workGroupMetric);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, workGroupMetric.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /work-group-metrics} : get all the workGroupMetrics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     workGroupMetrics in body.
   */
  @GetMapping("/work-group-metrics")
  public List<WorkGroupMetric> getAllWorkGroupMetrics() {
    log.debug("REST request to get all WorkGroupMetrics");
    return workGroupMetricRepository.findAll();
  }

  /**
   * {@code GET /work-group-metrics/:id} : get the "id" workGroupMetric.
   *
   * @param id the id of the workGroupMetric to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     workGroupMetric, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/work-group-metrics/{id}")
  public ResponseEntity<WorkGroupMetric> getWorkGroupMetric(@PathVariable Long id) {
    log.debug("REST request to get WorkGroupMetric : {}", id);
    Optional<WorkGroupMetric> workGroupMetric = workGroupMetricRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(workGroupMetric);
  }

  /**
   * {@code DELETE /work-group-metrics/:id} : delete the "id" workGroupMetric.
   *
   * @param id the id of the workGroupMetric to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/work-group-metrics/{id}")
  public ResponseEntity<Void> deleteWorkGroupMetric(@PathVariable Long id) {
    log.debug("REST request to delete WorkGroupMetric : {}", id);
    workGroupMetricRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
