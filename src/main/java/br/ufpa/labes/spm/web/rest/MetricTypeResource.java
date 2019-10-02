package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.MetricType;
import br.ufpa.labes.spm.repository.MetricTypeRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.MetricType}.
 */
@RestController
@RequestMapping("/api")
public class MetricTypeResource {

    private final Logger log = LoggerFactory.getLogger(MetricTypeResource.class);

    private static final String ENTITY_NAME = "metricType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetricTypeRepository metricTypeRepository;

    public MetricTypeResource(MetricTypeRepository metricTypeRepository) {
        this.metricTypeRepository = metricTypeRepository;
    }

    /**
     * {@code POST  /metric-types} : Create a new metricType.
     *
     * @param metricType the metricType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metricType, or with status {@code 400 (Bad Request)} if the metricType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/metric-types")
    public ResponseEntity<MetricType> createMetricType(@RequestBody MetricType metricType) throws URISyntaxException {
        log.debug("REST request to save MetricType : {}", metricType);
        if (metricType.getId() != null) {
            throw new BadRequestAlertException("A new metricType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetricType result = metricTypeRepository.save(metricType);
        return ResponseEntity.created(new URI("/api/metric-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /metric-types} : Updates an existing metricType.
     *
     * @param metricType the metricType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metricType,
     * or with status {@code 400 (Bad Request)} if the metricType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metricType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/metric-types")
    public ResponseEntity<MetricType> updateMetricType(@RequestBody MetricType metricType) throws URISyntaxException {
        log.debug("REST request to update MetricType : {}", metricType);
        if (metricType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MetricType result = metricTypeRepository.save(metricType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metricType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /metric-types} : get all the metricTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metricTypes in body.
     */
    @GetMapping("/metric-types")
    public List<MetricType> getAllMetricTypes() {
        log.debug("REST request to get all MetricTypes");
        return metricTypeRepository.findAll();
    }

    /**
     * {@code GET  /metric-types/:id} : get the "id" metricType.
     *
     * @param id the id of the metricType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metricType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/metric-types/{id}")
    public ResponseEntity<MetricType> getMetricType(@PathVariable Long id) {
        log.debug("REST request to get MetricType : {}", id);
        Optional<MetricType> metricType = metricTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(metricType);
    }

    /**
     * {@code DELETE  /metric-types/:id} : delete the "id" metricType.
     *
     * @param id the id of the metricType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/metric-types/{id}")
    public ResponseEntity<Void> deleteMetricType(@PathVariable Long id) {
        log.debug("REST request to delete MetricType : {}", id);
        metricTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
