package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AssetRelationship;
import br.ufpa.labes.spm.repository.AssetRelationshipRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AssetRelationship}. */
@RestController
@RequestMapping("/api")
public class AssetRelationshipResource {

  private final Logger log = LoggerFactory.getLogger(AssetRelationshipResource.class);

  private static final String ENTITY_NAME = "assetRelationship";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AssetRelationshipRepository assetRelationshipRepository;

  public AssetRelationshipResource(AssetRelationshipRepository assetRelationshipRepository) {
    this.assetRelationshipRepository = assetRelationshipRepository;
  }

  /**
   * {@code POST /asset-relationships} : Create a new assetRelationship.
   *
   * @param assetRelationship the assetRelationship to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     assetRelationship, or with status {@code 400 (Bad Request)} if the assetRelationship has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/asset-relationships")
  public ResponseEntity<AssetRelationship> createAssetRelationship(
      @RequestBody AssetRelationship assetRelationship) throws URISyntaxException {
    log.debug("REST request to save AssetRelationship : {}", assetRelationship);
    if (assetRelationship.getId() != null) {
      throw new BadRequestAlertException(
          "A new assetRelationship cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AssetRelationship result = assetRelationshipRepository.save(assetRelationship);
    return ResponseEntity.created(new URI("/api/asset-relationships/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /asset-relationships} : Updates an existing assetRelationship.
   *
   * @param assetRelationship the assetRelationship to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     assetRelationship, or with status {@code 400 (Bad Request)} if the assetRelationship is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the assetRelationship couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/asset-relationships")
  public ResponseEntity<AssetRelationship> updateAssetRelationship(
      @RequestBody AssetRelationship assetRelationship) throws URISyntaxException {
    log.debug("REST request to update AssetRelationship : {}", assetRelationship);
    if (assetRelationship.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AssetRelationship result = assetRelationshipRepository.save(assetRelationship);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, assetRelationship.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /asset-relationships} : get all the assetRelationships.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     assetRelationships in body.
   */
  @GetMapping("/asset-relationships")
  public List<AssetRelationship> getAllAssetRelationships() {
    log.debug("REST request to get all AssetRelationships");
    return assetRelationshipRepository.findAll();
  }

  /**
   * {@code GET /asset-relationships/:id} : get the "id" assetRelationship.
   *
   * @param id the id of the assetRelationship to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     assetRelationship, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/asset-relationships/{id}")
  public ResponseEntity<AssetRelationship> getAssetRelationship(@PathVariable Long id) {
    log.debug("REST request to get AssetRelationship : {}", id);
    Optional<AssetRelationship> assetRelationship = assetRelationshipRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(assetRelationship);
  }

  /**
   * {@code DELETE /asset-relationships/:id} : delete the "id" assetRelationship.
   *
   * @param id the id of the assetRelationship to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/asset-relationships/{id}")
  public ResponseEntity<Void> deleteAssetRelationship(@PathVariable Long id) {
    log.debug("REST request to delete AssetRelationship : {}", id);
    assetRelationshipRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
