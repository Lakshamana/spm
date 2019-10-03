package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.WorkGroupTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.WorkGroupTypeDTO;

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

    private final WorkGroupTypeService workGroupTypeService;

    public WorkGroupTypeResource(WorkGroupTypeService workGroupTypeService) {
        this.workGroupTypeService = workGroupTypeService;
    }

    /**
     * {@code POST  /work-group-types} : Create a new workGroupType.
     *
     * @param workGroupTypeDTO the workGroupTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workGroupTypeDTO, or with status {@code 400 (Bad Request)} if the workGroupType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-group-types")
    public ResponseEntity<WorkGroupTypeDTO> createWorkGroupType(@RequestBody WorkGroupTypeDTO workGroupTypeDTO) throws URISyntaxException {
        log.debug("REST request to save WorkGroupType : {}", workGroupTypeDTO);
        if (workGroupTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new workGroupType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkGroupTypeDTO result = workGroupTypeService.save(workGroupTypeDTO);
        return ResponseEntity.created(new URI("/api/work-group-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-group-types} : Updates an existing workGroupType.
     *
     * @param workGroupTypeDTO the workGroupTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workGroupTypeDTO,
     * or with status {@code 400 (Bad Request)} if the workGroupTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workGroupTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-group-types")
    public ResponseEntity<WorkGroupTypeDTO> updateWorkGroupType(@RequestBody WorkGroupTypeDTO workGroupTypeDTO) throws URISyntaxException {
        log.debug("REST request to update WorkGroupType : {}", workGroupTypeDTO);
        if (workGroupTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkGroupTypeDTO result = workGroupTypeService.save(workGroupTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workGroupTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-group-types} : get all the workGroupTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroupTypes in body.
     */
    @GetMapping("/work-group-types")
    public List<WorkGroupTypeDTO> getAllWorkGroupTypes() {
        log.debug("REST request to get all WorkGroupTypes");
        return workGroupTypeService.findAll();
    }

    /**
     * {@code GET  /work-group-types/:id} : get the "id" workGroupType.
     *
     * @param id the id of the workGroupTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroupTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-group-types/{id}")
    public ResponseEntity<WorkGroupTypeDTO> getWorkGroupType(@PathVariable Long id) {
        log.debug("REST request to get WorkGroupType : {}", id);
        Optional<WorkGroupTypeDTO> workGroupTypeDTO = workGroupTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workGroupTypeDTO);
    }

    /**
     * {@code DELETE  /work-group-types/:id} : delete the "id" workGroupType.
     *
     * @param id the id of the workGroupTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-group-types/{id}")
    public ResponseEntity<Void> deleteWorkGroupType(@PathVariable Long id) {
        log.debug("REST request to delete WorkGroupType : {}", id);
        workGroupTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
