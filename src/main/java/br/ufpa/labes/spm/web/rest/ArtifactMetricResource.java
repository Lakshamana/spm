package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ArtifactMetricService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ArtifactMetricDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ArtifactMetric}. */
@RestController
@RequestMapping("/api")
public class ArtifactMetricResource {

  private final Logger log = LoggerFactory.getLogger(ArtifactMetricResource.class);

  private static final String ENTITY_NAME = "artifactMetric";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ArtifactMetricService artifactMetricService;

  public ArtifactMetricResource(ArtifactMetricService artifactMetricService) {
    this.artifactMetricService = artifactMetricService;
  }

  /**
   * {@code POST /artifact-metrics} : Create a new artifactMetric.
   *
   * @param artifactMetricDTO the artifactMetricDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactMetricDTO, or with status {@code 400 (Bad Request)} if the artifactMetric has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifact-metrics")
  public ResponseEntity<ArtifactMetricDTO> createArtifactMetric(
      @RequestBody ArtifactMetricDTO artifactMetricDTO) throws URISyntaxException {
    log.debug("REST request to save ArtifactMetric : {}", artifactMetricDTO);
    if (artifactMetricDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifactMetric cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactMetricDTO result = artifactMetricService.save(artifactMetricDTO);
    return ResponseEntity.created(new URI("/api/artifact-metrics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifact-metrics} : Updates an existing artifactMetric.
   *
   * @param artifactMetricDTO the artifactMetricDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactMetricDTO, or with status {@code 400 (Bad Request)} if the artifactMetricDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the artifactMetricDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifact-metrics")
  public ResponseEntity<ArtifactMetricDTO> updateArtifactMetric(
      @RequestBody ArtifactMetricDTO artifactMetricDTO) throws URISyntaxException {
    log.debug("REST request to update ArtifactMetric : {}", artifactMetricDTO);
    if (artifactMetricDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactMetricDTO result = artifactMetricService.save(artifactMetricDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactMetricDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifact-metrics} : get all the artifactMetrics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifactMetrics
   *     in body.
   */
  @GetMapping("/artifact-metrics")
  public List<ArtifactMetricDTO> getAllArtifactMetrics() {
    log.debug("REST request to get all ArtifactMetrics");
    return artifactMetricService.findAll();
  }

  /**
   * {@code GET /artifact-metrics/:id} : get the "id" artifactMetric.
   *
   * @param id the id of the artifactMetricDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     artifactMetricDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifact-metrics/{id}")
  public ResponseEntity<ArtifactMetricDTO> getArtifactMetric(@PathVariable Long id) {
    log.debug("REST request to get ArtifactMetric : {}", id);
    Optional<ArtifactMetricDTO> artifactMetricDTO = artifactMetricService.findOne(id);
    return ResponseUtil.wrapOrNotFound(artifactMetricDTO);
  }

  /**
   * {@code DELETE /artifact-metrics/:id} : delete the "id" artifactMetric.
   *
   * @param id the id of the artifactMetricDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifact-metrics/{id}")
  public ResponseEntity<Void> deleteArtifactMetric(@PathVariable Long id) {
    log.debug("REST request to delete ArtifactMetric : {}", id);
    artifactMetricService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
