package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Estimation;
import br.ufpa.labes.spm.repository.EstimationRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Estimation}. */
@RestController
@RequestMapping("/api")
public class EstimationResource {

  private final Logger log = LoggerFactory.getLogger(EstimationResource.class);

  private static final String ENTITY_NAME = "estimation";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EstimationRepository estimationRepository;

  public EstimationResource(EstimationRepository estimationRepository) {
    this.estimationRepository = estimationRepository;
  }

  /**
   * {@code POST /estimations} : Create a new estimation.
   *
   * @param estimation the estimation to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     estimation, or with status {@code 400 (Bad Request)} if the estimation has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/estimations")
  public ResponseEntity<Estimation> createEstimation(@RequestBody Estimation estimation)
      throws URISyntaxException {
    log.debug("REST request to save Estimation : {}", estimation);
    if (estimation.getId() != null) {
      throw new BadRequestAlertException(
          "A new estimation cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Estimation result = estimationRepository.save(estimation);
    return ResponseEntity.created(new URI("/api/estimations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /estimations} : Updates an existing estimation.
   *
   * @param estimation the estimation to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     estimation, or with status {@code 400 (Bad Request)} if the estimation is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the estimation couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/estimations")
  public ResponseEntity<Estimation> updateEstimation(@RequestBody Estimation estimation)
      throws URISyntaxException {
    log.debug("REST request to update Estimation : {}", estimation);
    if (estimation.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Estimation result = estimationRepository.save(estimation);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, estimation.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /estimations} : get all the estimations.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estimations in
   *     body.
   */
  @GetMapping("/estimations")
  public List<Estimation> getAllEstimations() {
    log.debug("REST request to get all Estimations");
    return estimationRepository.findAll();
  }

  /**
   * {@code GET /estimations/:id} : get the "id" estimation.
   *
   * @param id the id of the estimation to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estimation,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/estimations/{id}")
  public ResponseEntity<Estimation> getEstimation(@PathVariable Long id) {
    log.debug("REST request to get Estimation : {}", id);
    Optional<Estimation> estimation = estimationRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(estimation);
  }

  /**
   * {@code DELETE /estimations/:id} : delete the "id" estimation.
   *
   * @param id the id of the estimation to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/estimations/{id}")
  public ResponseEntity<Void> deleteEstimation(@PathVariable Long id) {
    log.debug("REST request to delete Estimation : {}", id);
    estimationRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
