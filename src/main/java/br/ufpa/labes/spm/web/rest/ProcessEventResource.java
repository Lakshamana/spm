package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ProcessEvent;
import br.ufpa.labes.spm.repository.ProcessEventRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessEvent}.
 */
@RestController
@RequestMapping("/api")
public class ProcessEventResource {

    private final Logger log = LoggerFactory.getLogger(ProcessEventResource.class);

    private static final String ENTITY_NAME = "processEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessEventRepository processEventRepository;

    public ProcessEventResource(ProcessEventRepository processEventRepository) {
        this.processEventRepository = processEventRepository;
    }

    /**
     * {@code POST  /process-events} : Create a new processEvent.
     *
     * @param processEvent the processEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processEvent, or with status {@code 400 (Bad Request)} if the processEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-events")
    public ResponseEntity<ProcessEvent> createProcessEvent(@RequestBody ProcessEvent processEvent) throws URISyntaxException {
        log.debug("REST request to save ProcessEvent : {}", processEvent);
        if (processEvent.getId() != null) {
            throw new BadRequestAlertException("A new processEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessEvent result = processEventRepository.save(processEvent);
        return ResponseEntity.created(new URI("/api/process-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-events} : Updates an existing processEvent.
     *
     * @param processEvent the processEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processEvent,
     * or with status {@code 400 (Bad Request)} if the processEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-events")
    public ResponseEntity<ProcessEvent> updateProcessEvent(@RequestBody ProcessEvent processEvent) throws URISyntaxException {
        log.debug("REST request to update ProcessEvent : {}", processEvent);
        if (processEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessEvent result = processEventRepository.save(processEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-events} : get all the processEvents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processEvents in body.
     */
    @GetMapping("/process-events")
    public List<ProcessEvent> getAllProcessEvents() {
        log.debug("REST request to get all ProcessEvents");
        return processEventRepository.findAll();
    }

    /**
     * {@code GET  /process-events/:id} : get the "id" processEvent.
     *
     * @param id the id of the processEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-events/{id}")
    public ResponseEntity<ProcessEvent> getProcessEvent(@PathVariable Long id) {
        log.debug("REST request to get ProcessEvent : {}", id);
        Optional<ProcessEvent> processEvent = processEventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processEvent);
    }

    /**
     * {@code DELETE  /process-events/:id} : delete the "id" processEvent.
     *
     * @param id the id of the processEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-events/{id}")
    public ResponseEntity<Void> deleteProcessEvent(@PathVariable Long id) {
        log.debug("REST request to delete ProcessEvent : {}", id);
        processEventRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
