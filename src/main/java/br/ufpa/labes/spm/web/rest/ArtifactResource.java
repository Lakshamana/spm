package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ArtifactService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ArtifactDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Artifact}. */
@RestController
@RequestMapping("/api")
public class ArtifactResource {

  private final Logger log = LoggerFactory.getLogger(ArtifactResource.class);

  private static final String ENTITY_NAME = "artifact";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ArtifactService artifactService;

  public ArtifactResource(ArtifactService artifactService) {
    this.artifactService = artifactService;
  }

  /**
   * {@code POST /artifacts} : Create a new artifact.
   *
   * @param artifactDTO the artifactDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactDTO, or with status {@code 400 (Bad Request)} if the artifact has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifacts")
  public ResponseEntity<ArtifactDTO> createArtifact(@RequestBody ArtifactDTO artifactDTO)
      throws URISyntaxException {
    log.debug("REST request to save Artifact : {}", artifactDTO);
    if (artifactDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifact cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactDTO result = artifactService.save(artifactDTO);
    return ResponseEntity.created(new URI("/api/artifacts/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifacts} : Updates an existing artifact.
   *
   * @param artifactDTO the artifactDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactDTO, or with status {@code 400 (Bad Request)} if the artifactDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the artifactDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifacts")
  public ResponseEntity<ArtifactDTO> updateArtifact(@RequestBody ArtifactDTO artifactDTO)
      throws URISyntaxException {
    log.debug("REST request to update Artifact : {}", artifactDTO);
    if (artifactDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactDTO result = artifactService.save(artifactDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifacts} : get all the artifacts.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifacts in
   *     body.
   */
  @GetMapping("/artifacts")
  public List<ArtifactDTO> getAllArtifacts() {
    log.debug("REST request to get all Artifacts");
    return artifactService.findAll();
  }

  /**
   * {@code GET /artifacts/:id} : get the "id" artifact.
   *
   * @param id the id of the artifactDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artifactDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifacts/{id}")
  public ResponseEntity<ArtifactDTO> getArtifact(@PathVariable Long id) {
    log.debug("REST request to get Artifact : {}", id);
    Optional<ArtifactDTO> artifactDTO = artifactService.findOne(id);
    return ResponseUtil.wrapOrNotFound(artifactDTO);
  }

  /**
   * {@code DELETE /artifacts/:id} : delete the "id" artifact.
   *
   * @param id the id of the artifactDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifacts/{id}")
  public ResponseEntity<Void> deleteArtifact(@PathVariable Long id) {
    log.debug("REST request to delete Artifact : {}", id);
    artifactService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
