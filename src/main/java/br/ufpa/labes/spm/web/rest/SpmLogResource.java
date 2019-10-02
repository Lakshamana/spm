package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.SpmLog;
import br.ufpa.labes.spm.repository.SpmLogRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.SpmLog}. */
@RestController
@RequestMapping("/api")
public class SpmLogResource {

  private final Logger log = LoggerFactory.getLogger(SpmLogResource.class);

  private static final String ENTITY_NAME = "spmLog";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SpmLogRepository spmLogRepository;

  public SpmLogResource(SpmLogRepository spmLogRepository) {
    this.spmLogRepository = spmLogRepository;
  }

  /**
   * {@code POST /spm-logs} : Create a new spmLog.
   *
   * @param spmLog the spmLog to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     spmLog, or with status {@code 400 (Bad Request)} if the spmLog has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/spm-logs")
  public ResponseEntity<SpmLog> createSpmLog(@RequestBody SpmLog spmLog) throws URISyntaxException {
    log.debug("REST request to save SpmLog : {}", spmLog);
    if (spmLog.getId() != null) {
      throw new BadRequestAlertException(
          "A new spmLog cannot already have an ID", ENTITY_NAME, "idexists");
    }
    SpmLog result = spmLogRepository.save(spmLog);
    return ResponseEntity.created(new URI("/api/spm-logs/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /spm-logs} : Updates an existing spmLog.
   *
   * @param spmLog the spmLog to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     spmLog, or with status {@code 400 (Bad Request)} if the spmLog is not valid, or with status
   *     {@code 500 (Internal Server Error)} if the spmLog couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/spm-logs")
  public ResponseEntity<SpmLog> updateSpmLog(@RequestBody SpmLog spmLog) throws URISyntaxException {
    log.debug("REST request to update SpmLog : {}", spmLog);
    if (spmLog.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    SpmLog result = spmLogRepository.save(spmLog);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, spmLog.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /spm-logs} : get all the spmLogs.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spmLogs in
   *     body.
   */
  @GetMapping("/spm-logs")
  public List<SpmLog> getAllSpmLogs() {
    log.debug("REST request to get all SpmLogs");
    return spmLogRepository.findAll();
  }

  /**
   * {@code GET /spm-logs/:id} : get the "id" spmLog.
   *
   * @param id the id of the spmLog to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spmLog, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/spm-logs/{id}")
  public ResponseEntity<SpmLog> getSpmLog(@PathVariable Long id) {
    log.debug("REST request to get SpmLog : {}", id);
    Optional<SpmLog> spmLog = spmLogRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(spmLog);
  }

  /**
   * {@code DELETE /spm-logs/:id} : delete the "id" spmLog.
   *
   * @param id the id of the spmLog to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/spm-logs/{id}")
  public ResponseEntity<Void> deleteSpmLog(@PathVariable Long id) {
    log.debug("REST request to delete SpmLog : {}", id);
    spmLogRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
