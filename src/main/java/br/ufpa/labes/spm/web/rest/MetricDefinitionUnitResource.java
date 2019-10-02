package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.MetricDefinitionUnit;
import br.ufpa.labes.spm.repository.MetricDefinitionUnitRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.MetricDefinitionUnit}.
 */
@RestController
@RequestMapping("/api")
public class MetricDefinitionUnitResource {

    private final Logger log = LoggerFactory.getLogger(MetricDefinitionUnitResource.class);

    private static final String ENTITY_NAME = "metricDefinitionUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetricDefinitionUnitRepository metricDefinitionUnitRepository;

    public MetricDefinitionUnitResource(MetricDefinitionUnitRepository metricDefinitionUnitRepository) {
        this.metricDefinitionUnitRepository = metricDefinitionUnitRepository;
    }

    /**
     * {@code POST  /metric-definition-units} : Create a new metricDefinitionUnit.
     *
     * @param metricDefinitionUnit the metricDefinitionUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metricDefinitionUnit, or with status {@code 400 (Bad Request)} if the metricDefinitionUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/metric-definition-units")
    public ResponseEntity<MetricDefinitionUnit> createMetricDefinitionUnit(@RequestBody MetricDefinitionUnit metricDefinitionUnit) throws URISyntaxException {
        log.debug("REST request to save MetricDefinitionUnit : {}", metricDefinitionUnit);
        if (metricDefinitionUnit.getId() != null) {
            throw new BadRequestAlertException("A new metricDefinitionUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetricDefinitionUnit result = metricDefinitionUnitRepository.save(metricDefinitionUnit);
        return ResponseEntity.created(new URI("/api/metric-definition-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /metric-definition-units} : Updates an existing metricDefinitionUnit.
     *
     * @param metricDefinitionUnit the metricDefinitionUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metricDefinitionUnit,
     * or with status {@code 400 (Bad Request)} if the metricDefinitionUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metricDefinitionUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/metric-definition-units")
    public ResponseEntity<MetricDefinitionUnit> updateMetricDefinitionUnit(@RequestBody MetricDefinitionUnit metricDefinitionUnit) throws URISyntaxException {
        log.debug("REST request to update MetricDefinitionUnit : {}", metricDefinitionUnit);
        if (metricDefinitionUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MetricDefinitionUnit result = metricDefinitionUnitRepository.save(metricDefinitionUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metricDefinitionUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /metric-definition-units} : get all the metricDefinitionUnits.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metricDefinitionUnits in body.
     */
    @GetMapping("/metric-definition-units")
    public List<MetricDefinitionUnit> getAllMetricDefinitionUnits() {
        log.debug("REST request to get all MetricDefinitionUnits");
        return metricDefinitionUnitRepository.findAll();
    }

    /**
     * {@code GET  /metric-definition-units/:id} : get the "id" metricDefinitionUnit.
     *
     * @param id the id of the metricDefinitionUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metricDefinitionUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/metric-definition-units/{id}")
    public ResponseEntity<MetricDefinitionUnit> getMetricDefinitionUnit(@PathVariable Long id) {
        log.debug("REST request to get MetricDefinitionUnit : {}", id);
        Optional<MetricDefinitionUnit> metricDefinitionUnit = metricDefinitionUnitRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(metricDefinitionUnit);
    }

    /**
     * {@code DELETE  /metric-definition-units/:id} : delete the "id" metricDefinitionUnit.
     *
     * @param id the id of the metricDefinitionUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/metric-definition-units/{id}")
    public ResponseEntity<Void> deleteMetricDefinitionUnit(@PathVariable Long id) {
        log.debug("REST request to delete MetricDefinitionUnit : {}", id);
        metricDefinitionUnitRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
