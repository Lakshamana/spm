package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.OrganizationMetricService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.OrganizationMetricDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.OrganizationMetric}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationMetricResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationMetricResource.class);

    private static final String ENTITY_NAME = "organizationMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationMetricService organizationMetricService;

    public OrganizationMetricResource(OrganizationMetricService organizationMetricService) {
        this.organizationMetricService = organizationMetricService;
    }

    /**
     * {@code POST  /organization-metrics} : Create a new organizationMetric.
     *
     * @param organizationMetricDTO the organizationMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationMetricDTO, or with status {@code 400 (Bad Request)} if the organizationMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-metrics")
    public ResponseEntity<OrganizationMetricDTO> createOrganizationMetric(@RequestBody OrganizationMetricDTO organizationMetricDTO) throws URISyntaxException {
        log.debug("REST request to save OrganizationMetric : {}", organizationMetricDTO);
        if (organizationMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new organizationMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationMetricDTO result = organizationMetricService.save(organizationMetricDTO);
        return ResponseEntity.created(new URI("/api/organization-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-metrics} : Updates an existing organizationMetric.
     *
     * @param organizationMetricDTO the organizationMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationMetricDTO,
     * or with status {@code 400 (Bad Request)} if the organizationMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-metrics")
    public ResponseEntity<OrganizationMetricDTO> updateOrganizationMetric(@RequestBody OrganizationMetricDTO organizationMetricDTO) throws URISyntaxException {
        log.debug("REST request to update OrganizationMetric : {}", organizationMetricDTO);
        if (organizationMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationMetricDTO result = organizationMetricService.save(organizationMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationMetricDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organization-metrics} : get all the organizationMetrics.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationMetrics in body.
     */
    @GetMapping("/organization-metrics")
    public List<OrganizationMetricDTO> getAllOrganizationMetrics(@RequestParam(required = false) String filter) {
        if ("themetricsuper-is-null".equals(filter)) {
            log.debug("REST request to get all OrganizationMetrics where theMetricSuper is null");
            return organizationMetricService.findAllWhereTheMetricSuperIsNull();
        }
        log.debug("REST request to get all OrganizationMetrics");
        return organizationMetricService.findAll();
    }

    /**
     * {@code GET  /organization-metrics/:id} : get the "id" organizationMetric.
     *
     * @param id the id of the organizationMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-metrics/{id}")
    public ResponseEntity<OrganizationMetricDTO> getOrganizationMetric(@PathVariable Long id) {
        log.debug("REST request to get OrganizationMetric : {}", id);
        Optional<OrganizationMetricDTO> organizationMetricDTO = organizationMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationMetricDTO);
    }

    /**
     * {@code DELETE  /organization-metrics/:id} : delete the "id" organizationMetric.
     *
     * @param id the id of the organizationMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-metrics/{id}")
    public ResponseEntity<Void> deleteOrganizationMetric(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationMetric : {}", id);
        organizationMetricService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
