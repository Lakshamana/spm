package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ProcessMetric;
import br.ufpa.labes.spm.repository.ProcessMetricRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessMetric}. */
@RestController
@RequestMapping("/api")
public class ProcessMetricResource {

  private final Logger log = LoggerFactory.getLogger(ProcessMetricResource.class);

  private static final String ENTITY_NAME = "processMetric";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ProcessMetricRepository processMetricRepository;

  public ProcessMetricResource(ProcessMetricRepository processMetricRepository) {
    this.processMetricRepository = processMetricRepository;
  }

  /**
   * {@code POST /process-metrics} : Create a new processMetric.
   *
   * @param processMetric the processMetric to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     processMetric, or with status {@code 400 (Bad Request)} if the processMetric has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/process-metrics")
  public ResponseEntity<ProcessMetric> createProcessMetric(@RequestBody ProcessMetric processMetric)
      throws URISyntaxException {
    log.debug("REST request to save ProcessMetric : {}", processMetric);
    if (processMetric.getId() != null) {
      throw new BadRequestAlertException(
          "A new processMetric cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ProcessMetric result = processMetricRepository.save(processMetric);
    return ResponseEntity.created(new URI("/api/process-metrics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /process-metrics} : Updates an existing processMetric.
   *
   * @param processMetric the processMetric to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     processMetric, or with status {@code 400 (Bad Request)} if the processMetric is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the processMetric couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/process-metrics")
  public ResponseEntity<ProcessMetric> updateProcessMetric(@RequestBody ProcessMetric processMetric)
      throws URISyntaxException {
    log.debug("REST request to update ProcessMetric : {}", processMetric);
    if (processMetric.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ProcessMetric result = processMetricRepository.save(processMetric);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, processMetric.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /process-metrics} : get all the processMetrics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processMetrics
   *     in body.
   */
  @GetMapping("/process-metrics")
  public List<ProcessMetric> getAllProcessMetrics() {
    log.debug("REST request to get all ProcessMetrics");
    return processMetricRepository.findAll();
  }

  /**
   * {@code GET /process-metrics/:id} : get the "id" processMetric.
   *
   * @param id the id of the processMetric to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     processMetric, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/process-metrics/{id}")
  public ResponseEntity<ProcessMetric> getProcessMetric(@PathVariable Long id) {
    log.debug("REST request to get ProcessMetric : {}", id);
    Optional<ProcessMetric> processMetric = processMetricRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(processMetric);
  }

  /**
   * {@code DELETE /process-metrics/:id} : delete the "id" processMetric.
   *
   * @param id the id of the processMetric to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/process-metrics/{id}")
  public ResponseEntity<Void> deleteProcessMetric(@PathVariable Long id) {
    log.debug("REST request to delete ProcessMetric : {}", id);
    processMetricRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
