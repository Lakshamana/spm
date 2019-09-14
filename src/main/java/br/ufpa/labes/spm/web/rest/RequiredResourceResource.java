package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.RequiredResourceService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.RequiredResourceDTO;

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

    private final RequiredResourceService requiredResourceService;

    public RequiredResourceResource(RequiredResourceService requiredResourceService) {
        this.requiredResourceService = requiredResourceService;
    }

    /**
     * {@code POST  /required-resources} : Create a new requiredResource.
     *
     * @param requiredResourceDTO the requiredResourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requiredResourceDTO, or with status {@code 400 (Bad Request)} if the requiredResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/required-resources")
    public ResponseEntity<RequiredResourceDTO> createRequiredResource(@RequestBody RequiredResourceDTO requiredResourceDTO) throws URISyntaxException {
        log.debug("REST request to save RequiredResource : {}", requiredResourceDTO);
        if (requiredResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new requiredResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequiredResourceDTO result = requiredResourceService.save(requiredResourceDTO);
        return ResponseEntity.created(new URI("/api/required-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /required-resources} : Updates an existing requiredResource.
     *
     * @param requiredResourceDTO the requiredResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requiredResourceDTO,
     * or with status {@code 400 (Bad Request)} if the requiredResourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requiredResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/required-resources")
    public ResponseEntity<RequiredResourceDTO> updateRequiredResource(@RequestBody RequiredResourceDTO requiredResourceDTO) throws URISyntaxException {
        log.debug("REST request to update RequiredResource : {}", requiredResourceDTO);
        if (requiredResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequiredResourceDTO result = requiredResourceService.save(requiredResourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requiredResourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /required-resources} : get all the requiredResources.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requiredResources in body.
     */
    @GetMapping("/required-resources")
    public List<RequiredResourceDTO> getAllRequiredResources() {
        log.debug("REST request to get all RequiredResources");
        return requiredResourceService.findAll();
    }

    /**
     * {@code GET  /required-resources/:id} : get the "id" requiredResource.
     *
     * @param id the id of the requiredResourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requiredResourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/required-resources/{id}")
    public ResponseEntity<RequiredResourceDTO> getRequiredResource(@PathVariable Long id) {
        log.debug("REST request to get RequiredResource : {}", id);
        Optional<RequiredResourceDTO> requiredResourceDTO = requiredResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requiredResourceDTO);
    }

    /**
     * {@code DELETE  /required-resources/:id} : delete the "id" requiredResource.
     *
     * @param id the id of the requiredResourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/required-resources/{id}")
    public ResponseEntity<Void> deleteRequiredResource(@PathVariable Long id) {
        log.debug("REST request to delete RequiredResource : {}", id);
        requiredResourceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
