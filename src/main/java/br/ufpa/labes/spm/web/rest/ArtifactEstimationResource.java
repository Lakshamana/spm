package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ArtifactEstimationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ArtifactEstimationDTO;

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

  private final ArtifactEstimationService artifactEstimationService;

  public ArtifactEstimationResource(ArtifactEstimationService artifactEstimationService) {
    this.artifactEstimationService = artifactEstimationService;
  }

  /**
   * {@code POST /artifact-estimations} : Create a new artifactEstimation.
   *
   * @param artifactEstimationDTO the artifactEstimationDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactEstimationDTO, or with status {@code 400 (Bad Request)} if the artifactEstimation
   *     has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifact-estimations")
  public ResponseEntity<ArtifactEstimationDTO> createArtifactEstimation(
      @RequestBody ArtifactEstimationDTO artifactEstimationDTO) throws URISyntaxException {
    log.debug("REST request to save ArtifactEstimation : {}", artifactEstimationDTO);
    if (artifactEstimationDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifactEstimation cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactEstimationDTO result = artifactEstimationService.save(artifactEstimationDTO);
    return ResponseEntity.created(new URI("/api/artifact-estimations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifact-estimations} : Updates an existing artifactEstimation.
   *
   * @param artifactEstimationDTO the artifactEstimationDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactEstimationDTO, or with status {@code 400 (Bad Request)} if the
   *     artifactEstimationDTO is not valid, or with status {@code 500 (Internal Server Error)} if
   *     the artifactEstimationDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifact-estimations")
  public ResponseEntity<ArtifactEstimationDTO> updateArtifactEstimation(
      @RequestBody ArtifactEstimationDTO artifactEstimationDTO) throws URISyntaxException {
    log.debug("REST request to update ArtifactEstimation : {}", artifactEstimationDTO);
    if (artifactEstimationDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactEstimationDTO result = artifactEstimationService.save(artifactEstimationDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactEstimationDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifact-estimations} : get all the artifactEstimations.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     artifactEstimations in body.
   */
  @GetMapping("/artifact-estimations")
  public List<ArtifactEstimationDTO> getAllArtifactEstimations(
      @RequestParam(required = false) String filter) {
    if ("theestimationsuper-is-null".equals(filter)) {
      log.debug("REST request to get all ArtifactEstimations where theEstimationSuper is null");
      return artifactEstimationService.findAllWhereTheEstimationSuperIsNull();
    }
    log.debug("REST request to get all ArtifactEstimations");
    return artifactEstimationService.findAll();
  }

  /**
   * {@code GET /artifact-estimations/:id} : get the "id" artifactEstimation.
   *
   * @param id the id of the artifactEstimationDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     artifactEstimationDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifact-estimations/{id}")
  public ResponseEntity<ArtifactEstimationDTO> getArtifactEstimation(@PathVariable Long id) {
    log.debug("REST request to get ArtifactEstimation : {}", id);
    Optional<ArtifactEstimationDTO> artifactEstimationDTO = artifactEstimationService.findOne(id);
    return ResponseUtil.wrapOrNotFound(artifactEstimationDTO);
  }

  /**
   * {@code DELETE /artifact-estimations/:id} : delete the "id" artifactEstimation.
   *
   * @param id the id of the artifactEstimationDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifact-estimations/{id}")
  public ResponseEntity<Void> deleteArtifactEstimation(@PathVariable Long id) {
    log.debug("REST request to delete ArtifactEstimation : {}", id);
    artifactEstimationService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
