package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.WorkGroupInstSugService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.WorkGroupInstSugDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroupInstSug}.
 */
@RestController
@RequestMapping("/api")
public class WorkGroupInstSugResource {

    private final Logger log = LoggerFactory.getLogger(WorkGroupInstSugResource.class);

    private static final String ENTITY_NAME = "workGroupInstSug";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkGroupInstSugService workGroupInstSugService;

    public WorkGroupInstSugResource(WorkGroupInstSugService workGroupInstSugService) {
        this.workGroupInstSugService = workGroupInstSugService;
    }

    /**
     * {@code POST  /work-group-inst-sugs} : Create a new workGroupInstSug.
     *
     * @param workGroupInstSugDTO the workGroupInstSugDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workGroupInstSugDTO, or with status {@code 400 (Bad Request)} if the workGroupInstSug has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-group-inst-sugs")
    public ResponseEntity<WorkGroupInstSugDTO> createWorkGroupInstSug(@RequestBody WorkGroupInstSugDTO workGroupInstSugDTO) throws URISyntaxException {
        log.debug("REST request to save WorkGroupInstSug : {}", workGroupInstSugDTO);
        if (workGroupInstSugDTO.getId() != null) {
            throw new BadRequestAlertException("A new workGroupInstSug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkGroupInstSugDTO result = workGroupInstSugService.save(workGroupInstSugDTO);
        return ResponseEntity.created(new URI("/api/work-group-inst-sugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-group-inst-sugs} : Updates an existing workGroupInstSug.
     *
     * @param workGroupInstSugDTO the workGroupInstSugDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workGroupInstSugDTO,
     * or with status {@code 400 (Bad Request)} if the workGroupInstSugDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workGroupInstSugDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-group-inst-sugs")
    public ResponseEntity<WorkGroupInstSugDTO> updateWorkGroupInstSug(@RequestBody WorkGroupInstSugDTO workGroupInstSugDTO) throws URISyntaxException {
        log.debug("REST request to update WorkGroupInstSug : {}", workGroupInstSugDTO);
        if (workGroupInstSugDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkGroupInstSugDTO result = workGroupInstSugService.save(workGroupInstSugDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workGroupInstSugDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-group-inst-sugs} : get all the workGroupInstSugs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroupInstSugs in body.
     */
    @GetMapping("/work-group-inst-sugs")
    public List<WorkGroupInstSugDTO> getAllWorkGroupInstSugs(@RequestParam(required = false) String filter,@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        if ("thepeopleinstsugsuper-is-null".equals(filter)) {
            log.debug("REST request to get all WorkGroupInstSugs where thePeopleInstSugSuper is null");
            return workGroupInstSugService.findAllWhereThePeopleInstSugSuperIsNull();
        }
        log.debug("REST request to get all WorkGroupInstSugs");
        return workGroupInstSugService.findAll();
    }

    /**
     * {@code GET  /work-group-inst-sugs/:id} : get the "id" workGroupInstSug.
     *
     * @param id the id of the workGroupInstSugDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroupInstSugDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-group-inst-sugs/{id}")
    public ResponseEntity<WorkGroupInstSugDTO> getWorkGroupInstSug(@PathVariable Long id) {
        log.debug("REST request to get WorkGroupInstSug : {}", id);
        Optional<WorkGroupInstSugDTO> workGroupInstSugDTO = workGroupInstSugService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workGroupInstSugDTO);
    }

    /**
     * {@code DELETE  /work-group-inst-sugs/:id} : delete the "id" workGroupInstSug.
     *
     * @param id the id of the workGroupInstSugDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-group-inst-sugs/{id}")
    public ResponseEntity<Void> deleteWorkGroupInstSug(@PathVariable Long id) {
        log.debug("REST request to delete WorkGroupInstSug : {}", id);
        workGroupInstSugService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
