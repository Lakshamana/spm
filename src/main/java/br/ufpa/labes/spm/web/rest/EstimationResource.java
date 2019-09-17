package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.EstimationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.EstimationDTO;

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

  private final EstimationService estimationService;

  public EstimationResource(EstimationService estimationService) {
    this.estimationService = estimationService;
  }

  /**
   * {@code POST /estimations} : Create a new estimation.
   *
   * @param estimationDTO the estimationDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     estimationDTO, or with status {@code 400 (Bad Request)} if the estimation has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/estimations")
  public ResponseEntity<EstimationDTO> createEstimation(@RequestBody EstimationDTO estimationDTO)
      throws URISyntaxException {
    log.debug("REST request to save Estimation : {}", estimationDTO);
    if (estimationDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new estimation cannot already have an ID", ENTITY_NAME, "idexists");
    }
    EstimationDTO result = estimationService.save(estimationDTO);
    return ResponseEntity.created(new URI("/api/estimations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /estimations} : Updates an existing estimation.
   *
   * @param estimationDTO the estimationDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     estimationDTO, or with status {@code 400 (Bad Request)} if the estimationDTO is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the estimationDTO couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/estimations")
  public ResponseEntity<EstimationDTO> updateEstimation(@RequestBody EstimationDTO estimationDTO)
      throws URISyntaxException {
    log.debug("REST request to update Estimation : {}", estimationDTO);
    if (estimationDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    EstimationDTO result = estimationService.save(estimationDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, estimationDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /estimations} : get all the estimations.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estimations in
   *     body.
   */
  @GetMapping("/estimations")
  public List<EstimationDTO> getAllEstimations() {
    log.debug("REST request to get all Estimations");
    return estimationService.findAll();
  }

  /**
   * {@code GET /estimations/:id} : get the "id" estimation.
   *
   * @param id the id of the estimationDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     estimationDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/estimations/{id}")
  public ResponseEntity<EstimationDTO> getEstimation(@PathVariable Long id) {
    log.debug("REST request to get Estimation : {}", id);
    Optional<EstimationDTO> estimationDTO = estimationService.findOne(id);
    return ResponseUtil.wrapOrNotFound(estimationDTO);
  }

  /**
   * {@code DELETE /estimations/:id} : delete the "id" estimation.
   *
   * @param id the id of the estimationDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/estimations/{id}")
  public ResponseEntity<Void> deleteEstimation(@PathVariable Long id) {
    log.debug("REST request to delete Estimation : {}", id);
    estimationService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
