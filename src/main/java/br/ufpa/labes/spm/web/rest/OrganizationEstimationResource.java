package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.OrganizationEstimationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.OrganizationEstimationDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.OrganizationEstimation}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationEstimationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationEstimationResource.class);

    private static final String ENTITY_NAME = "organizationEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationEstimationService organizationEstimationService;

    public OrganizationEstimationResource(OrganizationEstimationService organizationEstimationService) {
        this.organizationEstimationService = organizationEstimationService;
    }

    /**
     * {@code POST  /organization-estimations} : Create a new organizationEstimation.
     *
     * @param organizationEstimationDTO the organizationEstimationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationEstimationDTO, or with status {@code 400 (Bad Request)} if the organizationEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-estimations")
    public ResponseEntity<OrganizationEstimationDTO> createOrganizationEstimation(@RequestBody OrganizationEstimationDTO organizationEstimationDTO) throws URISyntaxException {
        log.debug("REST request to save OrganizationEstimation : {}", organizationEstimationDTO);
        if (organizationEstimationDTO.getId() != null) {
            throw new BadRequestAlertException("A new organizationEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationEstimationDTO result = organizationEstimationService.save(organizationEstimationDTO);
        return ResponseEntity.created(new URI("/api/organization-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-estimations} : Updates an existing organizationEstimation.
     *
     * @param organizationEstimationDTO the organizationEstimationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationEstimationDTO,
     * or with status {@code 400 (Bad Request)} if the organizationEstimationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationEstimationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-estimations")
    public ResponseEntity<OrganizationEstimationDTO> updateOrganizationEstimation(@RequestBody OrganizationEstimationDTO organizationEstimationDTO) throws URISyntaxException {
        log.debug("REST request to update OrganizationEstimation : {}", organizationEstimationDTO);
        if (organizationEstimationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationEstimationDTO result = organizationEstimationService.save(organizationEstimationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationEstimationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organization-estimations} : get all the organizationEstimations.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationEstimations in body.
     */
    @GetMapping("/organization-estimations")
    public List<OrganizationEstimationDTO> getAllOrganizationEstimations(@RequestParam(required = false) String filter) {
        if ("theestimationsuper-is-null".equals(filter)) {
            log.debug("REST request to get all OrganizationEstimations where theEstimationSuper is null");
            return organizationEstimationService.findAllWhereTheEstimationSuperIsNull();
        }
        log.debug("REST request to get all OrganizationEstimations");
        return organizationEstimationService.findAll();
    }

    /**
     * {@code GET  /organization-estimations/:id} : get the "id" organizationEstimation.
     *
     * @param id the id of the organizationEstimationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationEstimationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-estimations/{id}")
    public ResponseEntity<OrganizationEstimationDTO> getOrganizationEstimation(@PathVariable Long id) {
        log.debug("REST request to get OrganizationEstimation : {}", id);
        Optional<OrganizationEstimationDTO> organizationEstimationDTO = organizationEstimationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationEstimationDTO);
    }

    /**
     * {@code DELETE  /organization-estimations/:id} : delete the "id" organizationEstimation.
     *
     * @param id the id of the organizationEstimationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-estimations/{id}")
    public ResponseEntity<Void> deleteOrganizationEstimation(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationEstimation : {}", id);
        organizationEstimationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
