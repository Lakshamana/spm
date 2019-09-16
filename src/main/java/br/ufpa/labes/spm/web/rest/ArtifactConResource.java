package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ArtifactConService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ArtifactConDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ArtifactCon}. */
@RestController
@RequestMapping("/api")
public class ArtifactConResource {

  private final Logger log = LoggerFactory.getLogger(ArtifactConResource.class);

  private static final String ENTITY_NAME = "artifactCon";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ArtifactConService artifactConService;

  public ArtifactConResource(ArtifactConService artifactConService) {
    this.artifactConService = artifactConService;
  }

  /**
   * {@code POST /artifact-cons} : Create a new artifactCon.
   *
   * @param artifactConDTO the artifactConDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     artifactConDTO, or with status {@code 400 (Bad Request)} if the artifactCon has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/artifact-cons")
  public ResponseEntity<ArtifactConDTO> createArtifactCon(
      @RequestBody ArtifactConDTO artifactConDTO) throws URISyntaxException {
    log.debug("REST request to save ArtifactCon : {}", artifactConDTO);
    if (artifactConDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new artifactCon cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ArtifactConDTO result = artifactConService.save(artifactConDTO);
    return ResponseEntity.created(new URI("/api/artifact-cons/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /artifact-cons} : Updates an existing artifactCon.
   *
   * @param artifactConDTO the artifactConDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     artifactConDTO, or with status {@code 400 (Bad Request)} if the artifactConDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the artifactConDTO couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/artifact-cons")
  public ResponseEntity<ArtifactConDTO> updateArtifactCon(
      @RequestBody ArtifactConDTO artifactConDTO) throws URISyntaxException {
    log.debug("REST request to update ArtifactCon : {}", artifactConDTO);
    if (artifactConDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ArtifactConDTO result = artifactConService.save(artifactConDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, artifactConDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /artifact-cons} : get all the artifactCons.
   *
   * @param eagerload flag to eager load entities from relationships (This is applicable for
   *     many-to-many).
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifactCons in
   *     body.
   */
  @GetMapping("/artifact-cons")
  public List<ArtifactConDTO> getAllArtifactCons(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
    if ("theconnectionsuper-is-null".equals(filter)) {
      log.debug("REST request to get all ArtifactCons where theConnectionSuper is null");
      return artifactConService.findAllWhereTheConnectionSuperIsNull();
    }
    log.debug("REST request to get all ArtifactCons");
    return artifactConService.findAll();
  }

  /**
   * {@code GET /artifact-cons/:id} : get the "id" artifactCon.
   *
   * @param id the id of the artifactConDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     artifactConDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/artifact-cons/{id}")
  public ResponseEntity<ArtifactConDTO> getArtifactCon(@PathVariable Long id) {
    log.debug("REST request to get ArtifactCon : {}", id);
    Optional<ArtifactConDTO> artifactConDTO = artifactConService.findOne(id);
    return ResponseUtil.wrapOrNotFound(artifactConDTO);
  }

  /**
   * {@code DELETE /artifact-cons/:id} : delete the "id" artifactCon.
   *
   * @param id the id of the artifactConDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/artifact-cons/{id}")
  public ResponseEntity<Void> deleteArtifactCon(@PathVariable Long id) {
    log.debug("REST request to delete ArtifactCon : {}", id);
    artifactConService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
