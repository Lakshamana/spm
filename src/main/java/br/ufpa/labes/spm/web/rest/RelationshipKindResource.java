package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.RelationshipKind;
import br.ufpa.labes.spm.repository.RelationshipKindRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.RelationshipKind}. */
@RestController
@RequestMapping("/api")
public class RelationshipKindResource {

  private final Logger log = LoggerFactory.getLogger(RelationshipKindResource.class);

  private static final String ENTITY_NAME = "relationshipKind";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RelationshipKindRepository relationshipKindRepository;

  public RelationshipKindResource(RelationshipKindRepository relationshipKindRepository) {
    this.relationshipKindRepository = relationshipKindRepository;
  }

  /**
   * {@code POST /relationship-kinds} : Create a new relationshipKind.
   *
   * @param relationshipKind the relationshipKind to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     relationshipKind, or with status {@code 400 (Bad Request)} if the relationshipKind has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/relationship-kinds")
  public ResponseEntity<RelationshipKind> createRelationshipKind(
      @RequestBody RelationshipKind relationshipKind) throws URISyntaxException {
    log.debug("REST request to save RelationshipKind : {}", relationshipKind);
    if (relationshipKind.getId() != null) {
      throw new BadRequestAlertException(
          "A new relationshipKind cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RelationshipKind result = relationshipKindRepository.save(relationshipKind);
    return ResponseEntity.created(new URI("/api/relationship-kinds/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /relationship-kinds} : Updates an existing relationshipKind.
   *
   * @param relationshipKind the relationshipKind to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     relationshipKind, or with status {@code 400 (Bad Request)} if the relationshipKind is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the relationshipKind couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/relationship-kinds")
  public ResponseEntity<RelationshipKind> updateRelationshipKind(
      @RequestBody RelationshipKind relationshipKind) throws URISyntaxException {
    log.debug("REST request to update RelationshipKind : {}", relationshipKind);
    if (relationshipKind.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    RelationshipKind result = relationshipKindRepository.save(relationshipKind);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, relationshipKind.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /relationship-kinds} : get all the relationshipKinds.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     relationshipKinds in body.
   */
  @GetMapping("/relationship-kinds")
  public List<RelationshipKind> getAllRelationshipKinds() {
    log.debug("REST request to get all RelationshipKinds");
    return relationshipKindRepository.findAll();
  }

  /**
   * {@code GET /relationship-kinds/:id} : get the "id" relationshipKind.
   *
   * @param id the id of the relationshipKind to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     relationshipKind, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/relationship-kinds/{id}")
  public ResponseEntity<RelationshipKind> getRelationshipKind(@PathVariable Long id) {
    log.debug("REST request to get RelationshipKind : {}", id);
    Optional<RelationshipKind> relationshipKind = relationshipKindRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(relationshipKind);
  }

  /**
   * {@code DELETE /relationship-kinds/:id} : delete the "id" relationshipKind.
   *
   * @param id the id of the relationshipKind to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/relationship-kinds/{id}")
  public ResponseEntity<Void> deleteRelationshipKind(@PathVariable Long id) {
    log.debug("REST request to delete RelationshipKind : {}", id);
    relationshipKindRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
