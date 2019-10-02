package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ArtifactParam;
import br.ufpa.labes.spm.repository.ArtifactParamRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ArtifactParam}. */
@RestController
@RequestMapping("/api")
public class ArtifactParamResource {

  private final Logger log = LoggerFactory.getLogger(ArtifactParamResource.class);

  private static final String ENTITY_NAME = "artifactParam";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ArtifactParamRepository artifactParamRepository;

  public ArtifactParamResource(ArtifactParamRepository artifactParamRepository) {
    this.artifactParamRepository = artifactParamRepository;
  }

  /**
   * {@code POST /artifact-params} : Create a new artifactParam.
   *
   * @param artifactParam the artifactParam to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactParam, or with status {@code 400 (Bad Request)} if the artifactParam has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifact-params")
  public ResponseEntity<ArtifactParam> createArtifactParam(@RequestBody ArtifactParam artifactParam)
      throws URISyntaxException {
    log.debug("REST request to save ArtifactParam : {}", artifactParam);
    if (artifactParam.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifactParam cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactParam result = artifactParamRepository.save(artifactParam);
    return ResponseEntity.created(new URI("/api/artifact-params/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifact-params} : Updates an existing artifactParam.
   *
   * @param artifactParam the artifactParam to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactParam, or with status {@code 400 (Bad Request)} if the artifactParam is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the artifactParam couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifact-params")
  public ResponseEntity<ArtifactParam> updateArtifactParam(@RequestBody ArtifactParam artifactParam)
      throws URISyntaxException {
    log.debug("REST request to update ArtifactParam : {}", artifactParam);
    if (artifactParam.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactParam result = artifactParamRepository.save(artifactParam);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactParam.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifact-params} : get all the artifactParams.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifactParams
   *     in body.
   */
  @GetMapping("/artifact-params")
  public List<ArtifactParam> getAllArtifactParams() {
    log.debug("REST request to get all ArtifactParams");
    return artifactParamRepository.findAll();
  }

  /**
   * {@code GET /artifact-params/:id} : get the "id" artifactParam.
   *
   * @param id the id of the artifactParam to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     artifactParam, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifact-params/{id}")
  public ResponseEntity<ArtifactParam> getArtifactParam(@PathVariable Long id) {
    log.debug("REST request to get ArtifactParam : {}", id);
    Optional<ArtifactParam> artifactParam = artifactParamRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(artifactParam);
  }

  /**
   * {@code DELETE /artifact-params/:id} : delete the "id" artifactParam.
   *
   * @param id the id of the artifactParam to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifact-params/{id}")
  public ResponseEntity<Void> deleteArtifactParam(@PathVariable Long id) {
    log.debug("REST request to delete ArtifactParam : {}", id);
    artifactParamRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
