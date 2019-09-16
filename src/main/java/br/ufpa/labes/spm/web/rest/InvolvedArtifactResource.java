package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.InvolvedArtifactService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.InvolvedArtifactDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.InvolvedArtifact}. */
@RestController
@RequestMapping("/api")
public class InvolvedArtifactResource {

  private final Logger log = LoggerFactory.getLogger(InvolvedArtifactResource.class);

  private static final String ENTITY_NAME = "involvedArtifact";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final InvolvedArtifactService involvedArtifactService;

  public InvolvedArtifactResource(InvolvedArtifactService involvedArtifactService) {
    this.involvedArtifactService = involvedArtifactService;
  }

  /**
   * {@code POST /involved-artifacts} : Create a new involvedArtifact.
   *
   * @param involvedArtifactDTO the involvedArtifactDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     involvedArtifactDTO, or with status {@code 400 (Bad Request)} if the involvedArtifact has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/involved-artifacts")
  public ResponseEntity<InvolvedArtifactDTO> createInvolvedArtifact(
      @RequestBody InvolvedArtifactDTO involvedArtifactDTO) throws URISyntaxException {
    log.debug("REST request to save InvolvedArtifact : {}", involvedArtifactDTO);
    if (involvedArtifactDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new involvedArtifact cannot already have an ID", ENTITY_NAME, "idexists");
    }
    InvolvedArtifactDTO result = involvedArtifactService.save(involvedArtifactDTO);
    return ResponseEntity.created(new URI("/api/involved-artifacts/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /involved-artifacts} : Updates an existing involvedArtifact.
   *
   * @param involvedArtifactDTO the involvedArtifactDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     involvedArtifactDTO, or with status {@code 400 (Bad Request)} if the involvedArtifactDTO is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the involvedArtifactDTO
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/involved-artifacts")
  public ResponseEntity<InvolvedArtifactDTO> updateInvolvedArtifact(
      @RequestBody InvolvedArtifactDTO involvedArtifactDTO) throws URISyntaxException {
    log.debug("REST request to update InvolvedArtifact : {}", involvedArtifactDTO);
    if (involvedArtifactDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    InvolvedArtifactDTO result = involvedArtifactService.save(involvedArtifactDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, involvedArtifactDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /involved-artifacts} : get all the involvedArtifacts.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     involvedArtifacts in body.
   */
  @GetMapping("/involved-artifacts")
  public List<InvolvedArtifactDTO> getAllInvolvedArtifacts() {
    log.debug("REST request to get all InvolvedArtifact");
    return involvedArtifactService.findAll();
  }

  /**
   * {@code GET /involved-artifacts/:id} : get the "id" involvedArtifact.
   *
   * @param id the id of the involvedArtifactDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     involvedArtifactDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/involved-artifacts/{id}")
  public ResponseEntity<InvolvedArtifactDTO> getInvolvedArtifact(@PathVariable Long id) {
    log.debug("REST request to get InvolvedArtifact : {}", id);
    Optional<InvolvedArtifactDTO> involvedArtifactDTO = involvedArtifactService.findOne(id);
    return ResponseUtil.wrapOrNotFound(involvedArtifactDTO);
  }

  /**
   * {@code DELETE /involved-artifacts/:id} : delete the "id" involvedArtifact.
   *
   * @param id the id of the involvedArtifactDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/involved-artifacts/{id}")
  public ResponseEntity<Void> deleteInvolvedArtifact(@PathVariable Long id) {
    log.debug("REST request to delete InvolvedArtifact : {}", id);
    involvedArtifactService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
