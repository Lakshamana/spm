package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ResourceMetricService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ResourceMetricDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ResourceMetric}.
 */
@RestController
@RequestMapping("/api")
public class ResourceMetricResource {

    private final Logger log = LoggerFactory.getLogger(ResourceMetricResource.class);

    private static final String ENTITY_NAME = "resourceMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceMetricService resourceMetricService;

    public ResourceMetricResource(ResourceMetricService resourceMetricService) {
        this.resourceMetricService = resourceMetricService;
    }

    /**
     * {@code POST  /resource-metrics} : Create a new resourceMetric.
     *
     * @param resourceMetricDTO the resourceMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceMetricDTO, or with status {@code 400 (Bad Request)} if the resourceMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-metrics")
    public ResponseEntity<ResourceMetricDTO> createResourceMetric(@RequestBody ResourceMetricDTO resourceMetricDTO) throws URISyntaxException {
        log.debug("REST request to save ResourceMetric : {}", resourceMetricDTO);
        if (resourceMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceMetricDTO result = resourceMetricService.save(resourceMetricDTO);
        return ResponseEntity.created(new URI("/api/resource-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-metrics} : Updates an existing resourceMetric.
     *
     * @param resourceMetricDTO the resourceMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceMetricDTO,
     * or with status {@code 400 (Bad Request)} if the resourceMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-metrics")
    public ResponseEntity<ResourceMetricDTO> updateResourceMetric(@RequestBody ResourceMetricDTO resourceMetricDTO) throws URISyntaxException {
        log.debug("REST request to update ResourceMetric : {}", resourceMetricDTO);
        if (resourceMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceMetricDTO result = resourceMetricService.save(resourceMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceMetricDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-metrics} : get all the resourceMetrics.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceMetrics in body.
     */
    @GetMapping("/resource-metrics")
    public List<ResourceMetricDTO> getAllResourceMetrics() {
        log.debug("REST request to get all ResourceMetrics");
        return resourceMetricService.findAll();
    }

    /**
     * {@code GET  /resource-metrics/:id} : get the "id" resourceMetric.
     *
     * @param id the id of the resourceMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-metrics/{id}")
    public ResponseEntity<ResourceMetricDTO> getResourceMetric(@PathVariable Long id) {
        log.debug("REST request to get ResourceMetric : {}", id);
        Optional<ResourceMetricDTO> resourceMetricDTO = resourceMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceMetricDTO);
    }

    /**
     * {@code DELETE  /resource-metrics/:id} : delete the "id" resourceMetric.
     *
     * @param id the id of the resourceMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-metrics/{id}")
    public ResponseEntity<Void> deleteResourceMetric(@PathVariable Long id) {
        log.debug("REST request to delete ResourceMetric : {}", id);
        resourceMetricService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
