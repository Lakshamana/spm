package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ArtifactParamService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ArtifactParamDTO;

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

  private final ArtifactParamService artifactParamService;

  public ArtifactParamResource(ArtifactParamService artifactParamService) {
    this.artifactParamService = artifactParamService;
  }

  /**
   * {@code POST /artifact-params} : Create a new artifactParam.
   *
   * @param artifactParamDTO the artifactParamDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactParamDTO, or with status {@code 400 (Bad Request)} if the artifactParam has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifact-params")
  public ResponseEntity<ArtifactParamDTO> createArtifactParam(
      @RequestBody ArtifactParamDTO artifactParamDTO) throws URISyntaxException {
    log.debug("REST request to save ArtifactParam : {}", artifactParamDTO);
    if (artifactParamDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifactParam cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactParamDTO result = artifactParamService.save(artifactParamDTO);
    return ResponseEntity.created(new URI("/api/artifact-params/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifact-params} : Updates an existing artifactParam.
   *
   * @param artifactParamDTO the artifactParamDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactParamDTO, or with status {@code 400 (Bad Request)} if the artifactParamDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the artifactParamDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifact-params")
  public ResponseEntity<ArtifactParamDTO> updateArtifactParam(
      @RequestBody ArtifactParamDTO artifactParamDTO) throws URISyntaxException {
    log.debug("REST request to update ArtifactParam : {}", artifactParamDTO);
    if (artifactParamDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactParamDTO result = artifactParamService.save(artifactParamDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactParamDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifact-params} : get all the artifactParams.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifactParams
   *     in body.
   */
  @GetMapping("/artifact-params")
  public List<ArtifactParamDTO> getAllArtifactParams() {
    log.debug("REST request to get all ArtifactParams");
    return artifactParamService.findAll();
  }

  /**
   * {@code GET /artifact-params/:id} : get the "id" artifactParam.
   *
   * @param id the id of the artifactParamDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     artifactParamDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifact-params/{id}")
  public ResponseEntity<ArtifactParamDTO> getArtifactParam(@PathVariable Long id) {
    log.debug("REST request to get ArtifactParam : {}", id);
    Optional<ArtifactParamDTO> artifactParamDTO = artifactParamService.findOne(id);
    return ResponseUtil.wrapOrNotFound(artifactParamDTO);
  }

  /**
   * {@code DELETE /artifact-params/:id} : delete the "id" artifactParam.
   *
   * @param id the id of the artifactParamDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifact-params/{id}")
  public ResponseEntity<Void> deleteArtifactParam(@PathVariable Long id) {
    log.debug("REST request to delete ArtifactParam : {}", id);
    artifactParamService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
