package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ProcessMetricService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ProcessMetricDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessMetric}.
 */
@RestController
@RequestMapping("/api")
public class ProcessMetricResource {

    private final Logger log = LoggerFactory.getLogger(ProcessMetricResource.class);

    private static final String ENTITY_NAME = "processMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessMetricService processMetricService;

    public ProcessMetricResource(ProcessMetricService processMetricService) {
        this.processMetricService = processMetricService;
    }

    /**
     * {@code POST  /process-metrics} : Create a new processMetric.
     *
     * @param processMetricDTO the processMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processMetricDTO, or with status {@code 400 (Bad Request)} if the processMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-metrics")
    public ResponseEntity<ProcessMetricDTO> createProcessMetric(@RequestBody ProcessMetricDTO processMetricDTO) throws URISyntaxException {
        log.debug("REST request to save ProcessMetric : {}", processMetricDTO);
        if (processMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new processMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessMetricDTO result = processMetricService.save(processMetricDTO);
        return ResponseEntity.created(new URI("/api/process-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-metrics} : Updates an existing processMetric.
     *
     * @param processMetricDTO the processMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processMetricDTO,
     * or with status {@code 400 (Bad Request)} if the processMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-metrics")
    public ResponseEntity<ProcessMetricDTO> updateProcessMetric(@RequestBody ProcessMetricDTO processMetricDTO) throws URISyntaxException {
        log.debug("REST request to update ProcessMetric : {}", processMetricDTO);
        if (processMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessMetricDTO result = processMetricService.save(processMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processMetricDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-metrics} : get all the processMetrics.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processMetrics in body.
     */
    @GetMapping("/process-metrics")
    public List<ProcessMetricDTO> getAllProcessMetrics() {
        log.debug("REST request to get all ProcessMetrics");
        return processMetricService.findAll();
    }

    /**
     * {@code GET  /process-metrics/:id} : get the "id" processMetric.
     *
     * @param id the id of the processMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-metrics/{id}")
    public ResponseEntity<ProcessMetricDTO> getProcessMetric(@PathVariable Long id) {
        log.debug("REST request to get ProcessMetric : {}", id);
        Optional<ProcessMetricDTO> processMetricDTO = processMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processMetricDTO);
    }

    /**
     * {@code DELETE  /process-metrics/:id} : delete the "id" processMetric.
     *
     * @param id the id of the processMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-metrics/{id}")
    public ResponseEntity<Void> deleteProcessMetric(@PathVariable Long id) {
        log.debug("REST request to delete ProcessMetric : {}", id);
        processMetricService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
