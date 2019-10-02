package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.WorkGroupType;
import br.ufpa.labes.spm.repository.WorkGroupTypeRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroupType}.
 */
@RestController
@RequestMapping("/api")
public class WorkGroupTypeResource {

    private final Logger log = LoggerFactory.getLogger(WorkGroupTypeResource.class);

    private static final String ENTITY_NAME = "workGroupType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkGroupTypeRepository workGroupTypeRepository;

    public WorkGroupTypeResource(WorkGroupTypeRepository workGroupTypeRepository) {
        this.workGroupTypeRepository = workGroupTypeRepository;
    }

    /**
     * {@code POST  /work-group-types} : Create a new workGroupType.
     *
     * @param workGroupType the workGroupType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workGroupType, or with status {@code 400 (Bad Request)} if the workGroupType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-group-types")
    public ResponseEntity<WorkGroupType> createWorkGroupType(@RequestBody WorkGroupType workGroupType) throws URISyntaxException {
        log.debug("REST request to save WorkGroupType : {}", workGroupType);
        if (workGroupType.getId() != null) {
            throw new BadRequestAlertException("A new workGroupType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkGroupType result = workGroupTypeRepository.save(workGroupType);
        return ResponseEntity.created(new URI("/api/work-group-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-group-types} : Updates an existing workGroupType.
     *
     * @param workGroupType the workGroupType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workGroupType,
     * or with status {@code 400 (Bad Request)} if the workGroupType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workGroupType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-group-types")
    public ResponseEntity<WorkGroupType> updateWorkGroupType(@RequestBody WorkGroupType workGroupType) throws URISyntaxException {
        log.debug("REST request to update WorkGroupType : {}", workGroupType);
        if (workGroupType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkGroupType result = workGroupTypeRepository.save(workGroupType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workGroupType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-group-types} : get all the workGroupTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroupTypes in body.
     */
    @GetMapping("/work-group-types")
    public List<WorkGroupType> getAllWorkGroupTypes() {
        log.debug("REST request to get all WorkGroupTypes");
        return workGroupTypeRepository.findAll();
    }

    /**
     * {@code GET  /work-group-types/:id} : get the "id" workGroupType.
     *
     * @param id the id of the workGroupType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroupType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-group-types/{id}")
    public ResponseEntity<WorkGroupType> getWorkGroupType(@PathVariable Long id) {
        log.debug("REST request to get WorkGroupType : {}", id);
        Optional<WorkGroupType> workGroupType = workGroupTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workGroupType);
    }

    /**
     * {@code DELETE  /work-group-types/:id} : delete the "id" workGroupType.
     *
     * @param id the id of the workGroupType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-group-types/{id}")
    public ResponseEntity<Void> deleteWorkGroupType(@PathVariable Long id) {
        log.debug("REST request to delete WorkGroupType : {}", id);
        workGroupTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
