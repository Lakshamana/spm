package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.OutOfWorkPeriod;
import br.ufpa.labes.spm.repository.OutOfWorkPeriodRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.OutOfWorkPeriod}. */
@RestController
@RequestMapping("/api")
public class OutOfWorkPeriodResource {

  private final Logger log = LoggerFactory.getLogger(OutOfWorkPeriodResource.class);

  private static final String ENTITY_NAME = "outOfWorkPeriod";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final OutOfWorkPeriodRepository outOfWorkPeriodRepository;

  public OutOfWorkPeriodResource(OutOfWorkPeriodRepository outOfWorkPeriodRepository) {
    this.outOfWorkPeriodRepository = outOfWorkPeriodRepository;
  }

  /**
   * {@code POST /out-of-work-periods} : Create a new outOfWorkPeriod.
   *
   * @param outOfWorkPeriod the outOfWorkPeriod to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     outOfWorkPeriod, or with status {@code 400 (Bad Request)} if the outOfWorkPeriod has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/out-of-work-periods")
  public ResponseEntity<OutOfWorkPeriod> createOutOfWorkPeriod(
      @RequestBody OutOfWorkPeriod outOfWorkPeriod) throws URISyntaxException {
    log.debug("REST request to save OutOfWorkPeriod : {}", outOfWorkPeriod);
    if (outOfWorkPeriod.getId() != null) {
      throw new BadRequestAlertException(
          "A new outOfWorkPeriod cannot already have an ID", ENTITY_NAME, "idexists");
    }
    OutOfWorkPeriod result = outOfWorkPeriodRepository.save(outOfWorkPeriod);
    return ResponseEntity.created(new URI("/api/out-of-work-periods/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /out-of-work-periods} : Updates an existing outOfWorkPeriod.
   *
   * @param outOfWorkPeriod the outOfWorkPeriod to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     outOfWorkPeriod, or with status {@code 400 (Bad Request)} if the outOfWorkPeriod is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the outOfWorkPeriod couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/out-of-work-periods")
  public ResponseEntity<OutOfWorkPeriod> updateOutOfWorkPeriod(
      @RequestBody OutOfWorkPeriod outOfWorkPeriod) throws URISyntaxException {
    log.debug("REST request to update OutOfWorkPeriod : {}", outOfWorkPeriod);
    if (outOfWorkPeriod.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    OutOfWorkPeriod result = outOfWorkPeriodRepository.save(outOfWorkPeriod);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, outOfWorkPeriod.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /out-of-work-periods} : get all the outOfWorkPeriods.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     outOfWorkPeriods in body.
   */
  @GetMapping("/out-of-work-periods")
  public List<OutOfWorkPeriod> getAllOutOfWorkPeriods() {
    log.debug("REST request to get all OutOfWorkPeriods");
    return outOfWorkPeriodRepository.findAll();
  }

  /**
   * {@code GET /out-of-work-periods/:id} : get the "id" outOfWorkPeriod.
   *
   * @param id the id of the outOfWorkPeriod to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     outOfWorkPeriod, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/out-of-work-periods/{id}")
  public ResponseEntity<OutOfWorkPeriod> getOutOfWorkPeriod(@PathVariable Long id) {
    log.debug("REST request to get OutOfWorkPeriod : {}", id);
    Optional<OutOfWorkPeriod> outOfWorkPeriod = outOfWorkPeriodRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(outOfWorkPeriod);
  }

  /**
   * {@code DELETE /out-of-work-periods/:id} : delete the "id" outOfWorkPeriod.
   *
   * @param id the id of the outOfWorkPeriod to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/out-of-work-periods/{id}")
  public ResponseEntity<Void> deleteOutOfWorkPeriod(@PathVariable Long id) {
    log.debug("REST request to delete OutOfWorkPeriod : {}", id);
    outOfWorkPeriodRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
