package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ResourcePossibleUseService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ResourcePossibleUseDTO;

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

    private final ResourcePossibleUseService resourcePossibleUseService;

    public ResourcePossibleUseResource(ResourcePossibleUseService resourcePossibleUseService) {
        this.resourcePossibleUseService = resourcePossibleUseService;
    }

    /**
     * {@code POST  /resource-possible-uses} : Create a new resourcePossibleUse.
     *
     * @param resourcePossibleUseDTO the resourcePossibleUseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourcePossibleUseDTO, or with status {@code 400 (Bad Request)} if the resourcePossibleUse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-possible-uses")
    public ResponseEntity<ResourcePossibleUseDTO> createResourcePossibleUse(@RequestBody ResourcePossibleUseDTO resourcePossibleUseDTO) throws URISyntaxException {
        log.debug("REST request to save ResourcePossibleUse : {}", resourcePossibleUseDTO);
        if (resourcePossibleUseDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourcePossibleUse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourcePossibleUseDTO result = resourcePossibleUseService.save(resourcePossibleUseDTO);
        return ResponseEntity.created(new URI("/api/resource-possible-uses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-possible-uses} : Updates an existing resourcePossibleUse.
     *
     * @param resourcePossibleUseDTO the resourcePossibleUseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourcePossibleUseDTO,
     * or with status {@code 400 (Bad Request)} if the resourcePossibleUseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourcePossibleUseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-possible-uses")
    public ResponseEntity<ResourcePossibleUseDTO> updateResourcePossibleUse(@RequestBody ResourcePossibleUseDTO resourcePossibleUseDTO) throws URISyntaxException {
        log.debug("REST request to update ResourcePossibleUse : {}", resourcePossibleUseDTO);
        if (resourcePossibleUseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourcePossibleUseDTO result = resourcePossibleUseService.save(resourcePossibleUseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourcePossibleUseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-possible-uses} : get all the resourcePossibleUses.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourcePossibleUses in body.
     */
    @GetMapping("/resource-possible-uses")
    public List<ResourcePossibleUseDTO> getAllResourcePossibleUses() {
        log.debug("REST request to get all ResourcePossibleUses");
        return resourcePossibleUseService.findAll();
    }

    /**
     * {@code GET  /resource-possible-uses/:id} : get the "id" resourcePossibleUse.
     *
     * @param id the id of the resourcePossibleUseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourcePossibleUseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-possible-uses/{id}")
    public ResponseEntity<ResourcePossibleUseDTO> getResourcePossibleUse(@PathVariable Long id) {
        log.debug("REST request to get ResourcePossibleUse : {}", id);
        Optional<ResourcePossibleUseDTO> resourcePossibleUseDTO = resourcePossibleUseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourcePossibleUseDTO);
    }

    /**
     * {@code DELETE  /resource-possible-uses/:id} : delete the "id" resourcePossibleUse.
     *
     * @param id the id of the resourcePossibleUseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-possible-uses/{id}")
    public ResponseEntity<Void> deleteResourcePossibleUse(@PathVariable Long id) {
        log.debug("REST request to delete ResourcePossibleUse : {}", id);
        resourcePossibleUseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
