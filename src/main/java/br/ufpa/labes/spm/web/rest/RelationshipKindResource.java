package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.RelationshipKindService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.RelationshipKindDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.RelationshipKind}. */
@RestController
@RequestMapping("/api")
public class RelationshipKindResource {

  private final Logger log = LoggerFactory.getLogger(RelationshipKindResource.class);

  private static final String ENTITY_NAME = "relationshipKind";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RelationshipKindService relationshipKindService;

  public RelationshipKindResource(RelationshipKindService relationshipKindService) {
    this.relationshipKindService = relationshipKindService;
  }

  /**
   * {@code POST /relationship-kinds} : Create a new relationshipKind.
   *
   * @param relationshipKindDTO the relationshipKindDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     relationshipKindDTO, or with status {@code 400 (Bad Request)} if the relationshipKind has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/relationship-kinds")
  public ResponseEntity<RelationshipKindDTO> createRelationshipKind(
      @RequestBody RelationshipKindDTO relationshipKindDTO) throws URISyntaxException {
    log.debug("REST request to save RelationshipKind : {}", relationshipKindDTO);
    if (relationshipKindDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new relationshipKind cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RelationshipKindDTO result = relationshipKindService.save(relationshipKindDTO);
    return ResponseEntity.created(new URI("/api/relationship-kinds/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /relationship-kinds} : Updates an existing relationshipKind.
   *
   * @param relationshipKindDTO the relationshipKindDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     relationshipKindDTO, or with status {@code 400 (Bad Request)} if the relationshipKindDTO is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the relationshipKindDTO
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/relationship-kinds")
  public ResponseEntity<RelationshipKindDTO> updateRelationshipKind(
      @RequestBody RelationshipKindDTO relationshipKindDTO) throws URISyntaxException {
    log.debug("REST request to update RelationshipKind : {}", relationshipKindDTO);
    if (relationshipKindDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    RelationshipKindDTO result = relationshipKindService.save(relationshipKindDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, relationshipKindDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /relationship-kinds} : get all the relationshipKinds.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     relationshipKinds in body.
   */
  @GetMapping("/relationship-kinds")
  public List<RelationshipKindDTO> getAllRelationshipKinds() {
    log.debug("REST request to get all RelationshipKinds");
    return relationshipKindService.findAll();
  }

  /**
   * {@code GET /relationship-kinds/:id} : get the "id" relationshipKind.
   *
   * @param id the id of the relationshipKindDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     relationshipKindDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/relationship-kinds/{id}")
  public ResponseEntity<RelationshipKindDTO> getRelationshipKind(@PathVariable Long id) {
    log.debug("REST request to get RelationshipKind : {}", id);
    Optional<RelationshipKindDTO> relationshipKindDTO = relationshipKindService.findOne(id);
    return ResponseUtil.wrapOrNotFound(relationshipKindDTO);
  }

  /**
   * {@code DELETE /relationship-kinds/:id} : delete the "id" relationshipKind.
   *
   * @param id the id of the relationshipKindDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/relationship-kinds/{id}")
  public ResponseEntity<Void> deleteRelationshipKind(@PathVariable Long id) {
    log.debug("REST request to delete RelationshipKind : {}", id);
    relationshipKindService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
