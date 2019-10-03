package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.MetricDefinitionUnitService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.MetricDefinitionUnitDTO;

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

    private final MetricDefinitionUnitService metricDefinitionUnitService;

    public MetricDefinitionUnitResource(MetricDefinitionUnitService metricDefinitionUnitService) {
        this.metricDefinitionUnitService = metricDefinitionUnitService;
    }

    /**
     * {@code POST  /metric-definition-units} : Create a new metricDefinitionUnit.
     *
     * @param metricDefinitionUnitDTO the metricDefinitionUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metricDefinitionUnitDTO, or with status {@code 400 (Bad Request)} if the metricDefinitionUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/metric-definition-units")
    public ResponseEntity<MetricDefinitionUnitDTO> createMetricDefinitionUnit(@RequestBody MetricDefinitionUnitDTO metricDefinitionUnitDTO) throws URISyntaxException {
        log.debug("REST request to save MetricDefinitionUnit : {}", metricDefinitionUnitDTO);
        if (metricDefinitionUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new metricDefinitionUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetricDefinitionUnitDTO result = metricDefinitionUnitService.save(metricDefinitionUnitDTO);
        return ResponseEntity.created(new URI("/api/metric-definition-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /metric-definition-units} : Updates an existing metricDefinitionUnit.
     *
     * @param metricDefinitionUnitDTO the metricDefinitionUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metricDefinitionUnitDTO,
     * or with status {@code 400 (Bad Request)} if the metricDefinitionUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metricDefinitionUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/metric-definition-units")
    public ResponseEntity<MetricDefinitionUnitDTO> updateMetricDefinitionUnit(@RequestBody MetricDefinitionUnitDTO metricDefinitionUnitDTO) throws URISyntaxException {
        log.debug("REST request to update MetricDefinitionUnit : {}", metricDefinitionUnitDTO);
        if (metricDefinitionUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MetricDefinitionUnitDTO result = metricDefinitionUnitService.save(metricDefinitionUnitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metricDefinitionUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /metric-definition-units} : get all the metricDefinitionUnits.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metricDefinitionUnits in body.
     */
    @GetMapping("/metric-definition-units")
    public List<MetricDefinitionUnitDTO> getAllMetricDefinitionUnits() {
        log.debug("REST request to get all MetricDefinitionUnits");
        return metricDefinitionUnitService.findAll();
    }

    /**
     * {@code GET  /metric-definition-units/:id} : get the "id" metricDefinitionUnit.
     *
     * @param id the id of the metricDefinitionUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metricDefinitionUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/metric-definition-units/{id}")
    public ResponseEntity<MetricDefinitionUnitDTO> getMetricDefinitionUnit(@PathVariable Long id) {
        log.debug("REST request to get MetricDefinitionUnit : {}", id);
        Optional<MetricDefinitionUnitDTO> metricDefinitionUnitDTO = metricDefinitionUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metricDefinitionUnitDTO);
    }

    /**
     * {@code DELETE  /metric-definition-units/:id} : delete the "id" metricDefinitionUnit.
     *
     * @param id the id of the metricDefinitionUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/metric-definition-units/{id}")
    public ResponseEntity<Void> deleteMetricDefinitionUnit(@PathVariable Long id) {
        log.debug("REST request to delete MetricDefinitionUnit : {}", id);
        metricDefinitionUnitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
