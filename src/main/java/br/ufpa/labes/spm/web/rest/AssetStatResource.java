package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AssetStatService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AssetStatDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AssetStat}. */
@RestController
@RequestMapping("/api")
public class AssetStatResource {

  private final Logger log = LoggerFactory.getLogger(AssetStatResource.class);

  private static final String ENTITY_NAME = "assetStat";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AssetStatService assetStatService;

  public AssetStatResource(AssetStatService assetStatService) {
    this.assetStatService = assetStatService;
  }

  /**
   * {@code POST /asset-stats} : Create a new assetStat.
   *
   * @param assetStatDTO the assetStatDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     assetStatDTO, or with status {@code 400 (Bad Request)} if the assetStat has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/asset-stats")
  public ResponseEntity<AssetStatDTO> createAssetStat(@RequestBody AssetStatDTO assetStatDTO)
      throws URISyntaxException {
    log.debug("REST request to save AssetStat : {}", assetStatDTO);
    if (assetStatDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new assetStat cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AssetStatDTO result = assetStatService.save(assetStatDTO);
    return ResponseEntity.created(new URI("/api/asset-stats/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /asset-stats} : Updates an existing assetStat.
   *
   * @param assetStatDTO the assetStatDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     assetStatDTO, or with status {@code 400 (Bad Request)} if the assetStatDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the assetStatDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/asset-stats")
  public ResponseEntity<AssetStatDTO> updateAssetStat(@RequestBody AssetStatDTO assetStatDTO)
      throws URISyntaxException {
    log.debug("REST request to update AssetStat : {}", assetStatDTO);
    if (assetStatDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AssetStatDTO result = assetStatService.save(assetStatDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, assetStatDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /asset-stats} : get all the assetStats.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetStats in
   *     body.
   */
  @GetMapping("/asset-stats")
  public List<AssetStatDTO> getAllAssetStats(@RequestParam(required = false) String filter) {
    if ("theasset-is-null".equals(filter)) {
      log.debug("REST request to get all AssetStats where theAsset is null");
      return assetStatService.findAllWhereTheAssetIsNull();
    }
    log.debug("REST request to get all AssetStats");
    return assetStatService.findAll();
  }

  /**
   * {@code GET /asset-stats/:id} : get the "id" assetStat.
   *
   * @param id the id of the assetStatDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetStatDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/asset-stats/{id}")
  public ResponseEntity<AssetStatDTO> getAssetStat(@PathVariable Long id) {
    log.debug("REST request to get AssetStat : {}", id);
    Optional<AssetStatDTO> assetStatDTO = assetStatService.findOne(id);
    return ResponseUtil.wrapOrNotFound(assetStatDTO);
  }

  /**
   * {@code DELETE /asset-stats/:id} : delete the "id" assetStat.
   *
   * @param id the id of the assetStatDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/asset-stats/{id}")
  public ResponseEntity<Void> deleteAssetStat(@PathVariable Long id) {
    log.debug("REST request to delete AssetStat : {}", id);
    assetStatService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
