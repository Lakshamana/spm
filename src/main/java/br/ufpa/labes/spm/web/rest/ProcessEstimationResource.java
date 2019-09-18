package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ProcessEstimationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ProcessEstimationDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessEstimation}.
 */
@RestController
@RequestMapping("/api")
public class ProcessEstimationResource {

    private final Logger log = LoggerFactory.getLogger(ProcessEstimationResource.class);

    private static final String ENTITY_NAME = "processEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessEstimationService processEstimationService;

    public ProcessEstimationResource(ProcessEstimationService processEstimationService) {
        this.processEstimationService = processEstimationService;
    }

    /**
     * {@code POST  /process-estimations} : Create a new processEstimation.
     *
     * @param processEstimationDTO the processEstimationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processEstimationDTO, or with status {@code 400 (Bad Request)} if the processEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-estimations")
    public ResponseEntity<ProcessEstimationDTO> createProcessEstimation(@RequestBody ProcessEstimationDTO processEstimationDTO) throws URISyntaxException {
        log.debug("REST request to save ProcessEstimation : {}", processEstimationDTO);
        if (processEstimationDTO.getId() != null) {
            throw new BadRequestAlertException("A new processEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessEstimationDTO result = processEstimationService.save(processEstimationDTO);
        return ResponseEntity.created(new URI("/api/process-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-estimations} : Updates an existing processEstimation.
     *
     * @param processEstimationDTO the processEstimationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processEstimationDTO,
     * or with status {@code 400 (Bad Request)} if the processEstimationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processEstimationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-estimations")
    public ResponseEntity<ProcessEstimationDTO> updateProcessEstimation(@RequestBody ProcessEstimationDTO processEstimationDTO) throws URISyntaxException {
        log.debug("REST request to update ProcessEstimation : {}", processEstimationDTO);
        if (processEstimationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessEstimationDTO result = processEstimationService.save(processEstimationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processEstimationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-estimations} : get all the processEstimations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processEstimations in body.
     */
    @GetMapping("/process-estimations")
    public List<ProcessEstimationDTO> getAllProcessEstimations() {
        log.debug("REST request to get all ProcessEstimations");
        return processEstimationService.findAll();
    }

    /**
     * {@code GET  /process-estimations/:id} : get the "id" processEstimation.
     *
     * @param id the id of the processEstimationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processEstimationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-estimations/{id}")
    public ResponseEntity<ProcessEstimationDTO> getProcessEstimation(@PathVariable Long id) {
        log.debug("REST request to get ProcessEstimation : {}", id);
        Optional<ProcessEstimationDTO> processEstimationDTO = processEstimationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processEstimationDTO);
    }

    /**
     * {@code DELETE  /process-estimations/:id} : delete the "id" processEstimation.
     *
     * @param id the id of the processEstimationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-estimations/{id}")
    public ResponseEntity<Void> deleteProcessEstimation(@PathVariable Long id) {
        log.debug("REST request to delete ProcessEstimation : {}", id);
        processEstimationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
