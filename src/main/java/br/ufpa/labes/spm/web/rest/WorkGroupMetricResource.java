package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.WorkGroupMetricService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.WorkGroupMetricDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroupMetric}.
 */
@RestController
@RequestMapping("/api")
public class WorkGroupMetricResource {

    private final Logger log = LoggerFactory.getLogger(WorkGroupMetricResource.class);

    private static final String ENTITY_NAME = "workGroupMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkGroupMetricService workGroupMetricService;

    public WorkGroupMetricResource(WorkGroupMetricService workGroupMetricService) {
        this.workGroupMetricService = workGroupMetricService;
    }

    /**
     * {@code POST  /work-group-metrics} : Create a new workGroupMetric.
     *
     * @param workGroupMetricDTO the workGroupMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workGroupMetricDTO, or with status {@code 400 (Bad Request)} if the workGroupMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-group-metrics")
    public ResponseEntity<WorkGroupMetricDTO> createWorkGroupMetric(@RequestBody WorkGroupMetricDTO workGroupMetricDTO) throws URISyntaxException {
        log.debug("REST request to save WorkGroupMetric : {}", workGroupMetricDTO);
        if (workGroupMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new workGroupMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkGroupMetricDTO result = workGroupMetricService.save(workGroupMetricDTO);
        return ResponseEntity.created(new URI("/api/work-group-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-group-metrics} : Updates an existing workGroupMetric.
     *
     * @param workGroupMetricDTO the workGroupMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workGroupMetricDTO,
     * or with status {@code 400 (Bad Request)} if the workGroupMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workGroupMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-group-metrics")
    public ResponseEntity<WorkGroupMetricDTO> updateWorkGroupMetric(@RequestBody WorkGroupMetricDTO workGroupMetricDTO) throws URISyntaxException {
        log.debug("REST request to update WorkGroupMetric : {}", workGroupMetricDTO);
        if (workGroupMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkGroupMetricDTO result = workGroupMetricService.save(workGroupMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workGroupMetricDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-group-metrics} : get all the workGroupMetrics.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroupMetrics in body.
     */
    @GetMapping("/work-group-metrics")
    public List<WorkGroupMetricDTO> getAllWorkGroupMetrics(@RequestParam(required = false) String filter) {
        if ("themetricsuper-is-null".equals(filter)) {
            log.debug("REST request to get all WorkGroupMetrics where theMetricSuper is null");
            return workGroupMetricService.findAllWhereTheMetricSuperIsNull();
        }
        log.debug("REST request to get all WorkGroupMetrics");
        return workGroupMetricService.findAll();
    }

    /**
     * {@code GET  /work-group-metrics/:id} : get the "id" workGroupMetric.
     *
     * @param id the id of the workGroupMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroupMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-group-metrics/{id}")
    public ResponseEntity<WorkGroupMetricDTO> getWorkGroupMetric(@PathVariable Long id) {
        log.debug("REST request to get WorkGroupMetric : {}", id);
        Optional<WorkGroupMetricDTO> workGroupMetricDTO = workGroupMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workGroupMetricDTO);
    }

    /**
     * {@code DELETE  /work-group-metrics/:id} : delete the "id" workGroupMetric.
     *
     * @param id the id of the workGroupMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-group-metrics/{id}")
    public ResponseEntity<Void> deleteWorkGroupMetric(@PathVariable Long id) {
        log.debug("REST request to delete WorkGroupMetric : {}", id);
        workGroupMetricService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
