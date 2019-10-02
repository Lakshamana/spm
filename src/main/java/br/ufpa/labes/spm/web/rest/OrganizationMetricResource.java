package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.OrganizationMetric;
import br.ufpa.labes.spm.repository.OrganizationMetricRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.OrganizationMetric}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationMetricResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationMetricResource.class);

    private static final String ENTITY_NAME = "organizationMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationMetricRepository organizationMetricRepository;

    public OrganizationMetricResource(OrganizationMetricRepository organizationMetricRepository) {
        this.organizationMetricRepository = organizationMetricRepository;
    }

    /**
     * {@code POST  /organization-metrics} : Create a new organizationMetric.
     *
     * @param organizationMetric the organizationMetric to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationMetric, or with status {@code 400 (Bad Request)} if the organizationMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-metrics")
    public ResponseEntity<OrganizationMetric> createOrganizationMetric(@RequestBody OrganizationMetric organizationMetric) throws URISyntaxException {
        log.debug("REST request to save OrganizationMetric : {}", organizationMetric);
        if (organizationMetric.getId() != null) {
            throw new BadRequestAlertException("A new organizationMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationMetric result = organizationMetricRepository.save(organizationMetric);
        return ResponseEntity.created(new URI("/api/organization-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-metrics} : Updates an existing organizationMetric.
     *
     * @param organizationMetric the organizationMetric to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationMetric,
     * or with status {@code 400 (Bad Request)} if the organizationMetric is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationMetric couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-metrics")
    public ResponseEntity<OrganizationMetric> updateOrganizationMetric(@RequestBody OrganizationMetric organizationMetric) throws URISyntaxException {
        log.debug("REST request to update OrganizationMetric : {}", organizationMetric);
        if (organizationMetric.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationMetric result = organizationMetricRepository.save(organizationMetric);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationMetric.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organization-metrics} : get all the organizationMetrics.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationMetrics in body.
     */
    @GetMapping("/organization-metrics")
    public List<OrganizationMetric> getAllOrganizationMetrics() {
        log.debug("REST request to get all OrganizationMetrics");
        return organizationMetricRepository.findAll();
    }

    /**
     * {@code GET  /organization-metrics/:id} : get the "id" organizationMetric.
     *
     * @param id the id of the organizationMetric to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationMetric, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-metrics/{id}")
    public ResponseEntity<OrganizationMetric> getOrganizationMetric(@PathVariable Long id) {
        log.debug("REST request to get OrganizationMetric : {}", id);
        Optional<OrganizationMetric> organizationMetric = organizationMetricRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(organizationMetric);
    }

    /**
     * {@code DELETE  /organization-metrics/:id} : delete the "id" organizationMetric.
     *
     * @param id the id of the organizationMetric to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-metrics/{id}")
    public ResponseEntity<Void> deleteOrganizationMetric(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationMetric : {}", id);
        organizationMetricRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
