package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ProcessEstimation;
import br.ufpa.labes.spm.repository.ProcessEstimationRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessEstimation}. */
@RestController
@RequestMapping("/api")
public class ProcessEstimationResource {

  private final Logger log = LoggerFactory.getLogger(ProcessEstimationResource.class);

  private static final String ENTITY_NAME = "processEstimation";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ProcessEstimationRepository processEstimationRepository;

  public ProcessEstimationResource(ProcessEstimationRepository processEstimationRepository) {
    this.processEstimationRepository = processEstimationRepository;
  }

  /**
   * {@code POST /process-estimations} : Create a new processEstimation.
   *
   * @param processEstimation the processEstimation to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     processEstimation, or with status {@code 400 (Bad Request)} if the processEstimation has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/process-estimations")
  public ResponseEntity<ProcessEstimation> createProcessEstimation(
      @RequestBody ProcessEstimation processEstimation) throws URISyntaxException {
    log.debug("REST request to save ProcessEstimation : {}", processEstimation);
    if (processEstimation.getId() != null) {
      throw new BadRequestAlertException(
          "A new processEstimation cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ProcessEstimation result = processEstimationRepository.save(processEstimation);
    return ResponseEntity.created(new URI("/api/process-estimations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /process-estimations} : Updates an existing processEstimation.
   *
   * @param processEstimation the processEstimation to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     processEstimation, or with status {@code 400 (Bad Request)} if the processEstimation is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the processEstimation couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/process-estimations")
  public ResponseEntity<ProcessEstimation> updateProcessEstimation(
      @RequestBody ProcessEstimation processEstimation) throws URISyntaxException {
    log.debug("REST request to update ProcessEstimation : {}", processEstimation);
    if (processEstimation.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ProcessEstimation result = processEstimationRepository.save(processEstimation);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, processEstimation.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /process-estimations} : get all the processEstimations.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     processEstimations in body.
   */
  @GetMapping("/process-estimations")
  public List<ProcessEstimation> getAllProcessEstimations() {
    log.debug("REST request to get all ProcessEstimations");
    return processEstimationRepository.findAll();
  }

  /**
   * {@code GET /process-estimations/:id} : get the "id" processEstimation.
   *
   * @param id the id of the processEstimation to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     processEstimation, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/process-estimations/{id}")
  public ResponseEntity<ProcessEstimation> getProcessEstimation(@PathVariable Long id) {
    log.debug("REST request to get ProcessEstimation : {}", id);
    Optional<ProcessEstimation> processEstimation = processEstimationRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(processEstimation);
  }

  /**
   * {@code DELETE /process-estimations/:id} : delete the "id" processEstimation.
   *
   * @param id the id of the processEstimation to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/process-estimations/{id}")
  public ResponseEntity<Void> deleteProcessEstimation(@PathVariable Long id) {
    log.debug("REST request to delete ProcessEstimation : {}", id);
    processEstimationRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
