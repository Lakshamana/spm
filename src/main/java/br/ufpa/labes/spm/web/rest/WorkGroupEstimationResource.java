package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.WorkGroupEstimation;
import br.ufpa.labes.spm.repository.WorkGroupEstimationRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroupEstimation}.
 */
@RestController
@RequestMapping("/api")
public class WorkGroupEstimationResource {

    private final Logger log = LoggerFactory.getLogger(WorkGroupEstimationResource.class);

    private static final String ENTITY_NAME = "workGroupEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkGroupEstimationRepository workGroupEstimationRepository;

    public WorkGroupEstimationResource(WorkGroupEstimationRepository workGroupEstimationRepository) {
        this.workGroupEstimationRepository = workGroupEstimationRepository;
    }

    /**
     * {@code POST  /work-group-estimations} : Create a new workGroupEstimation.
     *
     * @param workGroupEstimation the workGroupEstimation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workGroupEstimation, or with status {@code 400 (Bad Request)} if the workGroupEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-group-estimations")
    public ResponseEntity<WorkGroupEstimation> createWorkGroupEstimation(@RequestBody WorkGroupEstimation workGroupEstimation) throws URISyntaxException {
        log.debug("REST request to save WorkGroupEstimation : {}", workGroupEstimation);
        if (workGroupEstimation.getId() != null) {
            throw new BadRequestAlertException("A new workGroupEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkGroupEstimation result = workGroupEstimationRepository.save(workGroupEstimation);
        return ResponseEntity.created(new URI("/api/work-group-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-group-estimations} : Updates an existing workGroupEstimation.
     *
     * @param workGroupEstimation the workGroupEstimation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workGroupEstimation,
     * or with status {@code 400 (Bad Request)} if the workGroupEstimation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workGroupEstimation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-group-estimations")
    public ResponseEntity<WorkGroupEstimation> updateWorkGroupEstimation(@RequestBody WorkGroupEstimation workGroupEstimation) throws URISyntaxException {
        log.debug("REST request to update WorkGroupEstimation : {}", workGroupEstimation);
        if (workGroupEstimation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkGroupEstimation result = workGroupEstimationRepository.save(workGroupEstimation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workGroupEstimation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-group-estimations} : get all the workGroupEstimations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroupEstimations in body.
     */
    @GetMapping("/work-group-estimations")
    public List<WorkGroupEstimation> getAllWorkGroupEstimations() {
        log.debug("REST request to get all WorkGroupEstimations");
        return workGroupEstimationRepository.findAll();
    }

    /**
     * {@code GET  /work-group-estimations/:id} : get the "id" workGroupEstimation.
     *
     * @param id the id of the workGroupEstimation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroupEstimation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-group-estimations/{id}")
    public ResponseEntity<WorkGroupEstimation> getWorkGroupEstimation(@PathVariable Long id) {
        log.debug("REST request to get WorkGroupEstimation : {}", id);
        Optional<WorkGroupEstimation> workGroupEstimation = workGroupEstimationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workGroupEstimation);
    }

    /**
     * {@code DELETE  /work-group-estimations/:id} : delete the "id" workGroupEstimation.
     *
     * @param id the id of the workGroupEstimation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-group-estimations/{id}")
    public ResponseEntity<Void> deleteWorkGroupEstimation(@PathVariable Long id) {
        log.debug("REST request to delete WorkGroupEstimation : {}", id);
        workGroupEstimationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
