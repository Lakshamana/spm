package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ResourceInstSug;
import br.ufpa.labes.spm.repository.ResourceInstSugRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ResourceInstSug}.
 */
@RestController
@RequestMapping("/api")
public class ResourceInstSugResource {

    private final Logger log = LoggerFactory.getLogger(ResourceInstSugResource.class);

    private static final String ENTITY_NAME = "resourceInstSug";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceInstSugRepository resourceInstSugRepository;

    public ResourceInstSugResource(ResourceInstSugRepository resourceInstSugRepository) {
        this.resourceInstSugRepository = resourceInstSugRepository;
    }

    /**
     * {@code POST  /resource-inst-sugs} : Create a new resourceInstSug.
     *
     * @param resourceInstSug the resourceInstSug to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceInstSug, or with status {@code 400 (Bad Request)} if the resourceInstSug has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-inst-sugs")
    public ResponseEntity<ResourceInstSug> createResourceInstSug(@RequestBody ResourceInstSug resourceInstSug) throws URISyntaxException {
        log.debug("REST request to save ResourceInstSug : {}", resourceInstSug);
        if (resourceInstSug.getId() != null) {
            throw new BadRequestAlertException("A new resourceInstSug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceInstSug result = resourceInstSugRepository.save(resourceInstSug);
        return ResponseEntity.created(new URI("/api/resource-inst-sugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-inst-sugs} : Updates an existing resourceInstSug.
     *
     * @param resourceInstSug the resourceInstSug to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceInstSug,
     * or with status {@code 400 (Bad Request)} if the resourceInstSug is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceInstSug couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-inst-sugs")
    public ResponseEntity<ResourceInstSug> updateResourceInstSug(@RequestBody ResourceInstSug resourceInstSug) throws URISyntaxException {
        log.debug("REST request to update ResourceInstSug : {}", resourceInstSug);
        if (resourceInstSug.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceInstSug result = resourceInstSugRepository.save(resourceInstSug);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceInstSug.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-inst-sugs} : get all the resourceInstSugs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceInstSugs in body.
     */
    @GetMapping("/resource-inst-sugs")
    public List<ResourceInstSug> getAllResourceInstSugs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ResourceInstSugs");
        return resourceInstSugRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /resource-inst-sugs/:id} : get the "id" resourceInstSug.
     *
     * @param id the id of the resourceInstSug to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceInstSug, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-inst-sugs/{id}")
    public ResponseEntity<ResourceInstSug> getResourceInstSug(@PathVariable Long id) {
        log.debug("REST request to get ResourceInstSug : {}", id);
        Optional<ResourceInstSug> resourceInstSug = resourceInstSugRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(resourceInstSug);
    }

    /**
     * {@code DELETE  /resource-inst-sugs/:id} : delete the "id" resourceInstSug.
     *
     * @param id the id of the resourceInstSug to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-inst-sugs/{id}")
    public ResponseEntity<Void> deleteResourceInstSug(@PathVariable Long id) {
        log.debug("REST request to delete ResourceInstSug : {}", id);
        resourceInstSugRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
