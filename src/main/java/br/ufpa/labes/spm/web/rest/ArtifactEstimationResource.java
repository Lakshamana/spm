package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ArtifactEstimation;
import br.ufpa.labes.spm.repository.ArtifactEstimationRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ArtifactEstimation}. */
@RestController
@RequestMapping("/api")
public class ArtifactEstimationResource {

  private final Logger log = LoggerFactory.getLogger(ArtifactEstimationResource.class);

  private static final String ENTITY_NAME = "artifactEstimation";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ArtifactEstimationRepository artifactEstimationRepository;

  public ArtifactEstimationResource(ArtifactEstimationRepository artifactEstimationRepository) {
    this.artifactEstimationRepository = artifactEstimationRepository;
  }

  /**
   * {@code POST /artifact-estimations} : Create a new artifactEstimation.
   *
   * @param artifactEstimation the artifactEstimation to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactEstimation, or with status {@code 400 (Bad Request)} if the artifactEstimation has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifact-estimations")
  public ResponseEntity<ArtifactEstimation> createArtifactEstimation(
      @RequestBody ArtifactEstimation artifactEstimation) throws URISyntaxException {
    log.debug("REST request to save ArtifactEstimation : {}", artifactEstimation);
    if (artifactEstimation.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifactEstimation cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactEstimation result = artifactEstimationRepository.save(artifactEstimation);
    return ResponseEntity.created(new URI("/api/artifact-estimations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifact-estimations} : Updates an existing artifactEstimation.
   *
   * @param artifactEstimation the artifactEstimation to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactEstimation, or with status {@code 400 (Bad Request)} if the artifactEstimation is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the artifactEstimation
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifact-estimations")
  public ResponseEntity<ArtifactEstimation> updateArtifactEstimation(
      @RequestBody ArtifactEstimation artifactEstimation) throws URISyntaxException {
    log.debug("REST request to update ArtifactEstimation : {}", artifactEstimation);
    if (artifactEstimation.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactEstimation result = artifactEstimationRepository.save(artifactEstimation);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactEstimation.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifact-estimations} : get all the artifactEstimations.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     artifactEstimations in body.
   */
  @GetMapping("/artifact-estimations")
  public List<ArtifactEstimation> getAllArtifactEstimations() {
    log.debug("REST request to get all ArtifactEstimations");
    return artifactEstimationRepository.findAll();
  }

  /**
   * {@code GET /artifact-estimations/:id} : get the "id" artifactEstimation.
   *
   * @param id the id of the artifactEstimation to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     artifactEstimation, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifact-estimations/{id}")
  public ResponseEntity<ArtifactEstimation> getArtifactEstimation(@PathVariable Long id) {
    log.debug("REST request to get ArtifactEstimation : {}", id);
    Optional<ArtifactEstimation> artifactEstimation = artifactEstimationRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(artifactEstimation);
  }

  /**
   * {@code DELETE /artifact-estimations/:id} : delete the "id" artifactEstimation.
   *
   * @param id the id of the artifactEstimation to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifact-estimations/{id}")
  public ResponseEntity<Void> deleteArtifactEstimation(@PathVariable Long id) {
    log.debug("REST request to delete ArtifactEstimation : {}", id);
    artifactEstimationRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
