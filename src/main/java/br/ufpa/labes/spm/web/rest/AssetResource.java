package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AssetService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AssetDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Asset}. */
@RestController
@RequestMapping("/api")
public class AssetResource {

  private final Logger log = LoggerFactory.getLogger(AssetResource.class);

  private static final String ENTITY_NAME = "asset";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AssetService assetService;

  public AssetResource(AssetService assetService) {
    this.assetService = assetService;
  }

  /**
   * {@code POST /assets} : Create a new asset.
   *
   * @param assetDTO the assetDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     assetDTO, or with status {@code 400 (Bad Request)} if the asset has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/assets")
  public ResponseEntity<AssetDTO> createAsset(@RequestBody AssetDTO assetDTO)
      throws URISyntaxException {
    log.debug("REST request to save Asset : {}", assetDTO);
    if (assetDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new asset cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AssetDTO result = assetService.save(assetDTO);
    return ResponseEntity.created(new URI("/api/assets/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /assets} : Updates an existing asset.
   *
   * @param assetDTO the assetDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     assetDTO, or with status {@code 400 (Bad Request)} if the assetDTO is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the assetDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/assets")
  public ResponseEntity<AssetDTO> updateAsset(@RequestBody AssetDTO assetDTO)
      throws URISyntaxException {
    log.debug("REST request to update Asset : {}", assetDTO);
    if (assetDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AssetDTO result = assetService.save(assetDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, assetDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /assets} : get all the assets.
   *
   * @param eagerload flag to eager load entities from relationships (This is applicable for
   *     many-to-many).
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assets in body.
   */
  @GetMapping("/assets")
  public List<AssetDTO> getAllAssets(
      @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
    log.debug("REST request to get all Assets");
    return assetService.findAll();
  }

  /**
   * {@code GET /assets/:id} : get the "id" asset.
   *
   * @param id the id of the assetDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDTO, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/assets/{id}")
  public ResponseEntity<AssetDTO> getAsset(@PathVariable Long id) {
    log.debug("REST request to get Asset : {}", id);
    Optional<AssetDTO> assetDTO = assetService.findOne(id);
    return ResponseUtil.wrapOrNotFound(assetDTO);
  }

  /**
   * {@code DELETE /assets/:id} : delete the "id" asset.
   *
   * @param id the id of the assetDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/assets/{id}")
  public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
    log.debug("REST request to delete Asset : {}", id);
    assetService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
