package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AssetRelationshipService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AssetRelationshipDTO;

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

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AssetRelationship}.
 */
@RestController
@RequestMapping("/api")
public class AssetRelationshipResource {

    private final Logger log = LoggerFactory.getLogger(AssetRelationshipResource.class);

    private static final String ENTITY_NAME = "assetRelationship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetRelationshipService assetRelationshipService;

    public AssetRelationshipResource(AssetRelationshipService assetRelationshipService) {
        this.assetRelationshipService = assetRelationshipService;
    }

    /**
     * {@code POST  /asset-relationships} : Create a new assetRelationship.
     *
     * @param assetRelationshipDTO the assetRelationshipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetRelationshipDTO, or with status {@code 400 (Bad Request)} if the assetRelationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-relationships")
    public ResponseEntity<AssetRelationshipDTO> createAssetRelationship(@RequestBody AssetRelationshipDTO assetRelationshipDTO) throws URISyntaxException {
        log.debug("REST request to save AssetRelationship : {}", assetRelationshipDTO);
        if (assetRelationshipDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetRelationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetRelationshipDTO result = assetRelationshipService.save(assetRelationshipDTO);
        return ResponseEntity.created(new URI("/api/asset-relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-relationships} : Updates an existing assetRelationship.
     *
     * @param assetRelationshipDTO the assetRelationshipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetRelationshipDTO,
     * or with status {@code 400 (Bad Request)} if the assetRelationshipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetRelationshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-relationships")
    public ResponseEntity<AssetRelationshipDTO> updateAssetRelationship(@RequestBody AssetRelationshipDTO assetRelationshipDTO) throws URISyntaxException {
        log.debug("REST request to update AssetRelationship : {}", assetRelationshipDTO);
        if (assetRelationshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AssetRelationshipDTO result = assetRelationshipService.save(assetRelationshipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetRelationshipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /asset-relationships} : get all the assetRelationships.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetRelationships in body.
     */
    @GetMapping("/asset-relationships")
    public List<AssetRelationshipDTO> getAllAssetRelationships() {
        log.debug("REST request to get all AssetRelationships");
        return assetRelationshipService.findAll();
    }

    /**
     * {@code GET  /asset-relationships/:id} : get the "id" assetRelationship.
     *
     * @param id the id of the assetRelationshipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetRelationshipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-relationships/{id}")
    public ResponseEntity<AssetRelationshipDTO> getAssetRelationship(@PathVariable Long id) {
        log.debug("REST request to get AssetRelationship : {}", id);
        Optional<AssetRelationshipDTO> assetRelationshipDTO = assetRelationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetRelationshipDTO);
    }

    /**
     * {@code DELETE  /asset-relationships/:id} : delete the "id" assetRelationship.
     *
     * @param id the id of the assetRelationshipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-relationships/{id}")
    public ResponseEntity<Void> deleteAssetRelationship(@PathVariable Long id) {
        log.debug("REST request to delete AssetRelationship : {}", id);
        assetRelationshipService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
