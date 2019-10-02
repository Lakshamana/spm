package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.repository.RequiredResourceRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.RequiredResource}.
 */
@RestController
@RequestMapping("/api")
public class RequiredResourceResource {

    private final Logger log = LoggerFactory.getLogger(RequiredResourceResource.class);

    private static final String ENTITY_NAME = "requiredResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequiredResourceRepository requiredResourceRepository;

    public RequiredResourceResource(RequiredResourceRepository requiredResourceRepository) {
        this.requiredResourceRepository = requiredResourceRepository;
    }

    /**
     * {@code POST  /required-resources} : Create a new requiredResource.
     *
     * @param requiredResource the requiredResource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requiredResource, or with status {@code 400 (Bad Request)} if the requiredResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/required-resources")
    public ResponseEntity<RequiredResource> createRequiredResource(@RequestBody RequiredResource requiredResource) throws URISyntaxException {
        log.debug("REST request to save RequiredResource : {}", requiredResource);
        if (requiredResource.getId() != null) {
            throw new BadRequestAlertException("A new requiredResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequiredResource result = requiredResourceRepository.save(requiredResource);
        return ResponseEntity.created(new URI("/api/required-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /required-resources} : Updates an existing requiredResource.
     *
     * @param requiredResource the requiredResource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requiredResource,
     * or with status {@code 400 (Bad Request)} if the requiredResource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requiredResource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/required-resources")
    public ResponseEntity<RequiredResource> updateRequiredResource(@RequestBody RequiredResource requiredResource) throws URISyntaxException {
        log.debug("REST request to update RequiredResource : {}", requiredResource);
        if (requiredResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequiredResource result = requiredResourceRepository.save(requiredResource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requiredResource.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /required-resources} : get all the requiredResources.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requiredResources in body.
     */
    @GetMapping("/required-resources")
    public List<RequiredResource> getAllRequiredResources() {
        log.debug("REST request to get all RequiredResources");
        return requiredResourceRepository.findAll();
    }

    /**
     * {@code GET  /required-resources/:id} : get the "id" requiredResource.
     *
     * @param id the id of the requiredResource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requiredResource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/required-resources/{id}")
    public ResponseEntity<RequiredResource> getRequiredResource(@PathVariable Long id) {
        log.debug("REST request to get RequiredResource : {}", id);
        Optional<RequiredResource> requiredResource = requiredResourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requiredResource);
    }

    /**
     * {@code DELETE  /required-resources/:id} : delete the "id" requiredResource.
     *
     * @param id the id of the requiredResource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/required-resources/{id}")
    public ResponseEntity<Void> deleteRequiredResource(@PathVariable Long id) {
        log.debug("REST request to delete RequiredResource : {}", id);
        requiredResourceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
