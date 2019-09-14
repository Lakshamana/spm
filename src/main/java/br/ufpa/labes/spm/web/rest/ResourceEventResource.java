package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ResourceEventService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ResourceEventDTO;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ResourceEvent}.
 */
@RestController
@RequestMapping("/api")
public class ResourceEventResource {

    private final Logger log = LoggerFactory.getLogger(ResourceEventResource.class);

    private static final String ENTITY_NAME = "resourceEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceEventService resourceEventService;

    public ResourceEventResource(ResourceEventService resourceEventService) {
        this.resourceEventService = resourceEventService;
    }

    /**
     * {@code POST  /resource-events} : Create a new resourceEvent.
     *
     * @param resourceEventDTO the resourceEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceEventDTO, or with status {@code 400 (Bad Request)} if the resourceEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-events")
    public ResponseEntity<ResourceEventDTO> createResourceEvent(@RequestBody ResourceEventDTO resourceEventDTO) throws URISyntaxException {
        log.debug("REST request to save ResourceEvent : {}", resourceEventDTO);
        if (resourceEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceEventDTO result = resourceEventService.save(resourceEventDTO);
        return ResponseEntity.created(new URI("/api/resource-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-events} : Updates an existing resourceEvent.
     *
     * @param resourceEventDTO the resourceEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceEventDTO,
     * or with status {@code 400 (Bad Request)} if the resourceEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-events")
    public ResponseEntity<ResourceEventDTO> updateResourceEvent(@RequestBody ResourceEventDTO resourceEventDTO) throws URISyntaxException {
        log.debug("REST request to update ResourceEvent : {}", resourceEventDTO);
        if (resourceEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceEventDTO result = resourceEventService.save(resourceEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-events} : get all the resourceEvents.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceEvents in body.
     */
    @GetMapping("/resource-events")
    public List<ResourceEventDTO> getAllResourceEvents(@RequestParam(required = false) String filter) {
        if ("theeventsuper-is-null".equals(filter)) {
            log.debug("REST request to get all ResourceEvents where theEventSuper is null");
            return resourceEventService.findAllWhereTheEventSuperIsNull();
        }
        log.debug("REST request to get all ResourceEvents");
        return resourceEventService.findAll();
    }

    /**
     * {@code GET  /resource-events/:id} : get the "id" resourceEvent.
     *
     * @param id the id of the resourceEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-events/{id}")
    public ResponseEntity<ResourceEventDTO> getResourceEvent(@PathVariable Long id) {
        log.debug("REST request to get ResourceEvent : {}", id);
        Optional<ResourceEventDTO> resourceEventDTO = resourceEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceEventDTO);
    }

    /**
     * {@code DELETE  /resource-events/:id} : delete the "id" resourceEvent.
     *
     * @param id the id of the resourceEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-events/{id}")
    public ResponseEntity<Void> deleteResourceEvent(@PathVariable Long id) {
        log.debug("REST request to delete ResourceEvent : {}", id);
        resourceEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
