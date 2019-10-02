package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ResourcePossibleUse;
import br.ufpa.labes.spm.repository.ResourcePossibleUseRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ResourcePossibleUse}.
 */
@RestController
@RequestMapping("/api")
public class ResourcePossibleUseResource {

    private final Logger log = LoggerFactory.getLogger(ResourcePossibleUseResource.class);

    private static final String ENTITY_NAME = "resourcePossibleUse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourcePossibleUseRepository resourcePossibleUseRepository;

    public ResourcePossibleUseResource(ResourcePossibleUseRepository resourcePossibleUseRepository) {
        this.resourcePossibleUseRepository = resourcePossibleUseRepository;
    }

    /**
     * {@code POST  /resource-possible-uses} : Create a new resourcePossibleUse.
     *
     * @param resourcePossibleUse the resourcePossibleUse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourcePossibleUse, or with status {@code 400 (Bad Request)} if the resourcePossibleUse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-possible-uses")
    public ResponseEntity<ResourcePossibleUse> createResourcePossibleUse(@RequestBody ResourcePossibleUse resourcePossibleUse) throws URISyntaxException {
        log.debug("REST request to save ResourcePossibleUse : {}", resourcePossibleUse);
        if (resourcePossibleUse.getId() != null) {
            throw new BadRequestAlertException("A new resourcePossibleUse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourcePossibleUse result = resourcePossibleUseRepository.save(resourcePossibleUse);
        return ResponseEntity.created(new URI("/api/resource-possible-uses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-possible-uses} : Updates an existing resourcePossibleUse.
     *
     * @param resourcePossibleUse the resourcePossibleUse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourcePossibleUse,
     * or with status {@code 400 (Bad Request)} if the resourcePossibleUse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourcePossibleUse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-possible-uses")
    public ResponseEntity<ResourcePossibleUse> updateResourcePossibleUse(@RequestBody ResourcePossibleUse resourcePossibleUse) throws URISyntaxException {
        log.debug("REST request to update ResourcePossibleUse : {}", resourcePossibleUse);
        if (resourcePossibleUse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourcePossibleUse result = resourcePossibleUseRepository.save(resourcePossibleUse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourcePossibleUse.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-possible-uses} : get all the resourcePossibleUses.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourcePossibleUses in body.
     */
    @GetMapping("/resource-possible-uses")
    public List<ResourcePossibleUse> getAllResourcePossibleUses() {
        log.debug("REST request to get all ResourcePossibleUses");
        return resourcePossibleUseRepository.findAll();
    }

    /**
     * {@code GET  /resource-possible-uses/:id} : get the "id" resourcePossibleUse.
     *
     * @param id the id of the resourcePossibleUse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourcePossibleUse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-possible-uses/{id}")
    public ResponseEntity<ResourcePossibleUse> getResourcePossibleUse(@PathVariable Long id) {
        log.debug("REST request to get ResourcePossibleUse : {}", id);
        Optional<ResourcePossibleUse> resourcePossibleUse = resourcePossibleUseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resourcePossibleUse);
    }

    /**
     * {@code DELETE  /resource-possible-uses/:id} : delete the "id" resourcePossibleUse.
     *
     * @param id the id of the resourcePossibleUse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-possible-uses/{id}")
    public ResponseEntity<Void> deleteResourcePossibleUse(@PathVariable Long id) {
        log.debug("REST request to delete ResourcePossibleUse : {}", id);
        resourcePossibleUseRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
