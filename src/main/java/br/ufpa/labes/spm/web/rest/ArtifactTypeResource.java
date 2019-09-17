package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ArtifactTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ArtifactTypeDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ArtifactType}. */
@RestController
@RequestMapping("/api")
public class ArtifactTypeResource {

  private final Logger log = LoggerFactory.getLogger(ArtifactTypeResource.class);

  private static final String ENTITY_NAME = "artifactType";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ArtifactTypeService artifactTypeService;

  public ArtifactTypeResource(ArtifactTypeService artifactTypeService) {
    this.artifactTypeService = artifactTypeService;
  }

  /**
   * {@code POST /artifact-types} : Create a new artifactType.
   *
   * @param artifactTypeDTO the artifactTypeDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactTypeDTO, or with status {@code 400 (Bad Request)} if the artifactType has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifact-types")
  public ResponseEntity<ArtifactTypeDTO> createArtifactType(
      @RequestBody ArtifactTypeDTO artifactTypeDTO) throws URISyntaxException {
    log.debug("REST request to save ArtifactType : {}", artifactTypeDTO);
    if (artifactTypeDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifactType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactTypeDTO result = artifactTypeService.save(artifactTypeDTO);
    return ResponseEntity.created(new URI("/api/artifact-types/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifact-types} : Updates an existing artifactType.
   *
   * @param artifactTypeDTO the artifactTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactTypeDTO, or with status {@code 400 (Bad Request)} if the artifactTypeDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the artifactTypeDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifact-types")
  public ResponseEntity<ArtifactTypeDTO> updateArtifactType(
      @RequestBody ArtifactTypeDTO artifactTypeDTO) throws URISyntaxException {
    log.debug("REST request to update ArtifactType : {}", artifactTypeDTO);
    if (artifactTypeDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactTypeDTO result = artifactTypeService.save(artifactTypeDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifact-types} : get all the artifactTypes.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifactTypes
   *     in body.
   */
  @GetMapping("/artifact-types")
  public List<ArtifactTypeDTO> getAllArtifactTypes() {
    log.debug("REST request to get all ArtifactTypes");
    return artifactTypeService.findAll();
  }

  /**
   * {@code GET /artifact-types/:id} : get the "id" artifactType.
   *
   * @param id the id of the artifactTypeDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     artifactTypeDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifact-types/{id}")
  public ResponseEntity<ArtifactTypeDTO> getArtifactType(@PathVariable Long id) {
    log.debug("REST request to get ArtifactType : {}", id);
    Optional<ArtifactTypeDTO> artifactTypeDTO = artifactTypeService.findOne(id);
    return ResponseUtil.wrapOrNotFound(artifactTypeDTO);
  }

  /**
   * {@code DELETE /artifact-types/:id} : delete the "id" artifactType.
   *
   * @param id the id of the artifactTypeDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifact-types/{id}")
  public ResponseEntity<Void> deleteArtifactType(@PathVariable Long id) {
    log.debug("REST request to delete ArtifactType : {}", id);
    artifactTypeService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
