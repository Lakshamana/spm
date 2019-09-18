package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.CatalogEventService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.CatalogEventDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.CatalogEvent}.
 */
@RestController
@RequestMapping("/api")
public class CatalogEventResource {

    private final Logger log = LoggerFactory.getLogger(CatalogEventResource.class);

    private static final String ENTITY_NAME = "catalogEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogEventService catalogEventService;

    public CatalogEventResource(CatalogEventService catalogEventService) {
        this.catalogEventService = catalogEventService;
    }

    /**
     * {@code POST  /catalog-events} : Create a new catalogEvent.
     *
     * @param catalogEventDTO the catalogEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogEventDTO, or with status {@code 400 (Bad Request)} if the catalogEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-events")
    public ResponseEntity<CatalogEventDTO> createCatalogEvent(@RequestBody CatalogEventDTO catalogEventDTO) throws URISyntaxException {
        log.debug("REST request to save CatalogEvent : {}", catalogEventDTO);
        if (catalogEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogEventDTO result = catalogEventService.save(catalogEventDTO);
        return ResponseEntity.created(new URI("/api/catalog-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-events} : Updates an existing catalogEvent.
     *
     * @param catalogEventDTO the catalogEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogEventDTO,
     * or with status {@code 400 (Bad Request)} if the catalogEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-events")
    public ResponseEntity<CatalogEventDTO> updateCatalogEvent(@RequestBody CatalogEventDTO catalogEventDTO) throws URISyntaxException {
        log.debug("REST request to update CatalogEvent : {}", catalogEventDTO);
        if (catalogEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CatalogEventDTO result = catalogEventService.save(catalogEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /catalog-events} : get all the catalogEvents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogEvents in body.
     */
    @GetMapping("/catalog-events")
    public List<CatalogEventDTO> getAllCatalogEvents() {
        log.debug("REST request to get all CatalogEvents");
        return catalogEventService.findAll();
    }

    /**
     * {@code GET  /catalog-events/:id} : get the "id" catalogEvent.
     *
     * @param id the id of the catalogEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-events/{id}")
    public ResponseEntity<CatalogEventDTO> getCatalogEvent(@PathVariable Long id) {
        log.debug("REST request to get CatalogEvent : {}", id);
        Optional<CatalogEventDTO> catalogEventDTO = catalogEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogEventDTO);
    }

    /**
     * {@code DELETE  /catalog-events/:id} : delete the "id" catalogEvent.
     *
     * @param id the id of the catalogEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-events/{id}")
    public ResponseEntity<Void> deleteCatalogEvent(@PathVariable Long id) {
        log.debug("REST request to delete CatalogEvent : {}", id);
        catalogEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
