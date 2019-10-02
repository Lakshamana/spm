package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.WorkGroupInstSug;
import br.ufpa.labes.spm.repository.WorkGroupInstSugRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroupInstSug}.
 */
@RestController
@RequestMapping("/api")
public class WorkGroupInstSugResource {

    private final Logger log = LoggerFactory.getLogger(WorkGroupInstSugResource.class);

    private static final String ENTITY_NAME = "workGroupInstSug";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkGroupInstSugRepository workGroupInstSugRepository;

    public WorkGroupInstSugResource(WorkGroupInstSugRepository workGroupInstSugRepository) {
        this.workGroupInstSugRepository = workGroupInstSugRepository;
    }

    /**
     * {@code POST  /work-group-inst-sugs} : Create a new workGroupInstSug.
     *
     * @param workGroupInstSug the workGroupInstSug to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workGroupInstSug, or with status {@code 400 (Bad Request)} if the workGroupInstSug has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-group-inst-sugs")
    public ResponseEntity<WorkGroupInstSug> createWorkGroupInstSug(@RequestBody WorkGroupInstSug workGroupInstSug) throws URISyntaxException {
        log.debug("REST request to save WorkGroupInstSug : {}", workGroupInstSug);
        if (workGroupInstSug.getId() != null) {
            throw new BadRequestAlertException("A new workGroupInstSug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkGroupInstSug result = workGroupInstSugRepository.save(workGroupInstSug);
        return ResponseEntity.created(new URI("/api/work-group-inst-sugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-group-inst-sugs} : Updates an existing workGroupInstSug.
     *
     * @param workGroupInstSug the workGroupInstSug to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workGroupInstSug,
     * or with status {@code 400 (Bad Request)} if the workGroupInstSug is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workGroupInstSug couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-group-inst-sugs")
    public ResponseEntity<WorkGroupInstSug> updateWorkGroupInstSug(@RequestBody WorkGroupInstSug workGroupInstSug) throws URISyntaxException {
        log.debug("REST request to update WorkGroupInstSug : {}", workGroupInstSug);
        if (workGroupInstSug.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkGroupInstSug result = workGroupInstSugRepository.save(workGroupInstSug);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workGroupInstSug.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-group-inst-sugs} : get all the workGroupInstSugs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroupInstSugs in body.
     */
    @GetMapping("/work-group-inst-sugs")
    public List<WorkGroupInstSug> getAllWorkGroupInstSugs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all WorkGroupInstSugs");
        return workGroupInstSugRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /work-group-inst-sugs/:id} : get the "id" workGroupInstSug.
     *
     * @param id the id of the workGroupInstSug to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroupInstSug, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-group-inst-sugs/{id}")
    public ResponseEntity<WorkGroupInstSug> getWorkGroupInstSug(@PathVariable Long id) {
        log.debug("REST request to get WorkGroupInstSug : {}", id);
        Optional<WorkGroupInstSug> workGroupInstSug = workGroupInstSugRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(workGroupInstSug);
    }

    /**
     * {@code DELETE  /work-group-inst-sugs/:id} : delete the "id" workGroupInstSug.
     *
     * @param id the id of the workGroupInstSug to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-group-inst-sugs/{id}")
    public ResponseEntity<Void> deleteWorkGroupInstSug(@PathVariable Long id) {
        log.debug("REST request to delete WorkGroupInstSug : {}", id);
        workGroupInstSugRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
