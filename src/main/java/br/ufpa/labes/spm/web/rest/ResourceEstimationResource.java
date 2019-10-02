package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ResourceEstimation;
import br.ufpa.labes.spm.repository.ResourceEstimationRepository;
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

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ResourceEstimation}.
 */
@RestController
@RequestMapping("/api")
public class ResourceEstimationResource {

    private final Logger log = LoggerFactory.getLogger(ResourceEstimationResource.class);

    private static final String ENTITY_NAME = "resourceEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceEstimationRepository resourceEstimationRepository;

    public ResourceEstimationResource(ResourceEstimationRepository resourceEstimationRepository) {
        this.resourceEstimationRepository = resourceEstimationRepository;
    }

    /**
     * {@code POST  /resource-estimations} : Create a new resourceEstimation.
     *
     * @param resourceEstimation the resourceEstimation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceEstimation, or with status {@code 400 (Bad Request)} if the resourceEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-estimations")
    public ResponseEntity<ResourceEstimation> createResourceEstimation(@RequestBody ResourceEstimation resourceEstimation) throws URISyntaxException {
        log.debug("REST request to save ResourceEstimation : {}", resourceEstimation);
        if (resourceEstimation.getId() != null) {
            throw new BadRequestAlertException("A new resourceEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceEstimation result = resourceEstimationRepository.save(resourceEstimation);
        return ResponseEntity.created(new URI("/api/resource-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-estimations} : Updates an existing resourceEstimation.
     *
     * @param resourceEstimation the resourceEstimation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceEstimation,
     * or with status {@code 400 (Bad Request)} if the resourceEstimation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceEstimation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-estimations")
    public ResponseEntity<ResourceEstimation> updateResourceEstimation(@RequestBody ResourceEstimation resourceEstimation) throws URISyntaxException {
        log.debug("REST request to update ResourceEstimation : {}", resourceEstimation);
        if (resourceEstimation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceEstimation result = resourceEstimationRepository.save(resourceEstimation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceEstimation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-estimations} : get all the resourceEstimations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceEstimations in body.
     */
    @GetMapping("/resource-estimations")
    public List<ResourceEstimation> getAllResourceEstimations() {
        log.debug("REST request to get all ResourceEstimations");
        return resourceEstimationRepository.findAll();
    }

    /**
     * {@code GET  /resource-estimations/:id} : get the "id" resourceEstimation.
     *
     * @param id the id of the resourceEstimation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceEstimation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-estimations/{id}")
    public ResponseEntity<ResourceEstimation> getResourceEstimation(@PathVariable Long id) {
        log.debug("REST request to get ResourceEstimation : {}", id);
        Optional<ResourceEstimation> resourceEstimation = resourceEstimationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resourceEstimation);
    }

    /**
     * {@code DELETE  /resource-estimations/:id} : delete the "id" resourceEstimation.
     *
     * @param id the id of the resourceEstimation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-estimations/{id}")
    public ResponseEntity<Void> deleteResourceEstimation(@PathVariable Long id) {
        log.debug("REST request to delete ResourceEstimation : {}", id);
        resourceEstimationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
