package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.OrganizationEstimation;
import br.ufpa.labes.spm.repository.OrganizationEstimationRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.OrganizationEstimation}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationEstimationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationEstimationResource.class);

    private static final String ENTITY_NAME = "organizationEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationEstimationRepository organizationEstimationRepository;

    public OrganizationEstimationResource(OrganizationEstimationRepository organizationEstimationRepository) {
        this.organizationEstimationRepository = organizationEstimationRepository;
    }

    /**
     * {@code POST  /organization-estimations} : Create a new organizationEstimation.
     *
     * @param organizationEstimation the organizationEstimation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationEstimation, or with status {@code 400 (Bad Request)} if the organizationEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-estimations")
    public ResponseEntity<OrganizationEstimation> createOrganizationEstimation(@RequestBody OrganizationEstimation organizationEstimation) throws URISyntaxException {
        log.debug("REST request to save OrganizationEstimation : {}", organizationEstimation);
        if (organizationEstimation.getId() != null) {
            throw new BadRequestAlertException("A new organizationEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationEstimation result = organizationEstimationRepository.save(organizationEstimation);
        return ResponseEntity.created(new URI("/api/organization-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-estimations} : Updates an existing organizationEstimation.
     *
     * @param organizationEstimation the organizationEstimation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationEstimation,
     * or with status {@code 400 (Bad Request)} if the organizationEstimation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationEstimation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-estimations")
    public ResponseEntity<OrganizationEstimation> updateOrganizationEstimation(@RequestBody OrganizationEstimation organizationEstimation) throws URISyntaxException {
        log.debug("REST request to update OrganizationEstimation : {}", organizationEstimation);
        if (organizationEstimation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationEstimation result = organizationEstimationRepository.save(organizationEstimation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationEstimation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organization-estimations} : get all the organizationEstimations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationEstimations in body.
     */
    @GetMapping("/organization-estimations")
    public List<OrganizationEstimation> getAllOrganizationEstimations() {
        log.debug("REST request to get all OrganizationEstimations");
        return organizationEstimationRepository.findAll();
    }

    /**
     * {@code GET  /organization-estimations/:id} : get the "id" organizationEstimation.
     *
     * @param id the id of the organizationEstimation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationEstimation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-estimations/{id}")
    public ResponseEntity<OrganizationEstimation> getOrganizationEstimation(@PathVariable Long id) {
        log.debug("REST request to get OrganizationEstimation : {}", id);
        Optional<OrganizationEstimation> organizationEstimation = organizationEstimationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(organizationEstimation);
    }

    /**
     * {@code DELETE  /organization-estimations/:id} : delete the "id" organizationEstimation.
     *
     * @param id the id of the organizationEstimation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-estimations/{id}")
    public ResponseEntity<Void> deleteOrganizationEstimation(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationEstimation : {}", id);
        organizationEstimationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
