package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.WorkGroupEstimationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.WorkGroupEstimationDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroupEstimation}.
 */
@RestController
@RequestMapping("/api")
public class WorkGroupEstimationResource {

    private final Logger log = LoggerFactory.getLogger(WorkGroupEstimationResource.class);

    private static final String ENTITY_NAME = "workGroupEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkGroupEstimationService workGroupEstimationService;

    public WorkGroupEstimationResource(WorkGroupEstimationService workGroupEstimationService) {
        this.workGroupEstimationService = workGroupEstimationService;
    }

    /**
     * {@code POST  /work-group-estimations} : Create a new workGroupEstimation.
     *
     * @param workGroupEstimationDTO the workGroupEstimationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workGroupEstimationDTO, or with status {@code 400 (Bad Request)} if the workGroupEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-group-estimations")
    public ResponseEntity<WorkGroupEstimationDTO> createWorkGroupEstimation(@RequestBody WorkGroupEstimationDTO workGroupEstimationDTO) throws URISyntaxException {
        log.debug("REST request to save WorkGroupEstimation : {}", workGroupEstimationDTO);
        if (workGroupEstimationDTO.getId() != null) {
            throw new BadRequestAlertException("A new workGroupEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkGroupEstimationDTO result = workGroupEstimationService.save(workGroupEstimationDTO);
        return ResponseEntity.created(new URI("/api/work-group-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-group-estimations} : Updates an existing workGroupEstimation.
     *
     * @param workGroupEstimationDTO the workGroupEstimationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workGroupEstimationDTO,
     * or with status {@code 400 (Bad Request)} if the workGroupEstimationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workGroupEstimationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-group-estimations")
    public ResponseEntity<WorkGroupEstimationDTO> updateWorkGroupEstimation(@RequestBody WorkGroupEstimationDTO workGroupEstimationDTO) throws URISyntaxException {
        log.debug("REST request to update WorkGroupEstimation : {}", workGroupEstimationDTO);
        if (workGroupEstimationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkGroupEstimationDTO result = workGroupEstimationService.save(workGroupEstimationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workGroupEstimationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-group-estimations} : get all the workGroupEstimations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroupEstimations in body.
     */
    @GetMapping("/work-group-estimations")
    public List<WorkGroupEstimationDTO> getAllWorkGroupEstimations() {
        log.debug("REST request to get all WorkGroupEstimations");
        return workGroupEstimationService.findAll();
    }

    /**
     * {@code GET  /work-group-estimations/:id} : get the "id" workGroupEstimation.
     *
     * @param id the id of the workGroupEstimationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroupEstimationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-group-estimations/{id}")
    public ResponseEntity<WorkGroupEstimationDTO> getWorkGroupEstimation(@PathVariable Long id) {
        log.debug("REST request to get WorkGroupEstimation : {}", id);
        Optional<WorkGroupEstimationDTO> workGroupEstimationDTO = workGroupEstimationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workGroupEstimationDTO);
    }

    /**
     * {@code DELETE  /work-group-estimations/:id} : delete the "id" workGroupEstimation.
     *
     * @param id the id of the workGroupEstimationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-group-estimations/{id}")
    public ResponseEntity<Void> deleteWorkGroupEstimation(@PathVariable Long id) {
        log.debug("REST request to delete WorkGroupEstimation : {}", id);
        workGroupEstimationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
