package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ProcessModelEvent;
import br.ufpa.labes.spm.repository.ProcessModelEventRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessModelEvent}.
 */
@RestController
@RequestMapping("/api")
public class ProcessModelEventResource {

    private final Logger log = LoggerFactory.getLogger(ProcessModelEventResource.class);

    private static final String ENTITY_NAME = "processModelEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessModelEventRepository processModelEventRepository;

    public ProcessModelEventResource(ProcessModelEventRepository processModelEventRepository) {
        this.processModelEventRepository = processModelEventRepository;
    }

    /**
     * {@code POST  /process-model-events} : Create a new processModelEvent.
     *
     * @param processModelEvent the processModelEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processModelEvent, or with status {@code 400 (Bad Request)} if the processModelEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-model-events")
    public ResponseEntity<ProcessModelEvent> createProcessModelEvent(@RequestBody ProcessModelEvent processModelEvent) throws URISyntaxException {
        log.debug("REST request to save ProcessModelEvent : {}", processModelEvent);
        if (processModelEvent.getId() != null) {
            throw new BadRequestAlertException("A new processModelEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessModelEvent result = processModelEventRepository.save(processModelEvent);
        return ResponseEntity.created(new URI("/api/process-model-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-model-events} : Updates an existing processModelEvent.
     *
     * @param processModelEvent the processModelEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processModelEvent,
     * or with status {@code 400 (Bad Request)} if the processModelEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processModelEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-model-events")
    public ResponseEntity<ProcessModelEvent> updateProcessModelEvent(@RequestBody ProcessModelEvent processModelEvent) throws URISyntaxException {
        log.debug("REST request to update ProcessModelEvent : {}", processModelEvent);
        if (processModelEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessModelEvent result = processModelEventRepository.save(processModelEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processModelEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-model-events} : get all the processModelEvents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processModelEvents in body.
     */
    @GetMapping("/process-model-events")
    public List<ProcessModelEvent> getAllProcessModelEvents() {
        log.debug("REST request to get all ProcessModelEvents");
        return processModelEventRepository.findAll();
    }

    /**
     * {@code GET  /process-model-events/:id} : get the "id" processModelEvent.
     *
     * @param id the id of the processModelEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processModelEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-model-events/{id}")
    public ResponseEntity<ProcessModelEvent> getProcessModelEvent(@PathVariable Long id) {
        log.debug("REST request to get ProcessModelEvent : {}", id);
        Optional<ProcessModelEvent> processModelEvent = processModelEventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processModelEvent);
    }

    /**
     * {@code DELETE  /process-model-events/:id} : delete the "id" processModelEvent.
     *
     * @param id the id of the processModelEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-model-events/{id}")
    public ResponseEntity<Void> deleteProcessModelEvent(@PathVariable Long id) {
        log.debug("REST request to delete ProcessModelEvent : {}", id);
        processModelEventRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
