package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ProcessEventService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ProcessEventDTO;

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

    private final ProcessEventService processEventService;

    public ProcessEventResource(ProcessEventService processEventService) {
        this.processEventService = processEventService;
    }

    /**
     * {@code POST  /process-events} : Create a new processEvent.
     *
     * @param processEventDTO the processEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processEventDTO, or with status {@code 400 (Bad Request)} if the processEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-events")
    public ResponseEntity<ProcessEventDTO> createProcessEvent(@RequestBody ProcessEventDTO processEventDTO) throws URISyntaxException {
        log.debug("REST request to save ProcessEvent : {}", processEventDTO);
        if (processEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new processEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessEventDTO result = processEventService.save(processEventDTO);
        return ResponseEntity.created(new URI("/api/process-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-events} : Updates an existing processEvent.
     *
     * @param processEventDTO the processEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processEventDTO,
     * or with status {@code 400 (Bad Request)} if the processEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-events")
    public ResponseEntity<ProcessEventDTO> updateProcessEvent(@RequestBody ProcessEventDTO processEventDTO) throws URISyntaxException {
        log.debug("REST request to update ProcessEvent : {}", processEventDTO);
        if (processEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessEventDTO result = processEventService.save(processEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-events} : get all the processEvents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processEvents in body.
     */
    @GetMapping("/process-events")
    public List<ProcessEventDTO> getAllProcessEvents() {
        log.debug("REST request to get all ProcessEvents");
        return processEventService.findAll();
    }

    /**
     * {@code GET  /process-events/:id} : get the "id" processEvent.
     *
     * @param id the id of the processEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-events/{id}")
    public ResponseEntity<ProcessEventDTO> getProcessEvent(@PathVariable Long id) {
        log.debug("REST request to get ProcessEvent : {}", id);
        Optional<ProcessEventDTO> processEventDTO = processEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processEventDTO);
    }

    /**
     * {@code DELETE  /process-events/:id} : delete the "id" processEvent.
     *
     * @param id the id of the processEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-events/{id}")
    public ResponseEntity<Void> deleteProcessEvent(@PathVariable Long id) {
        log.debug("REST request to delete ProcessEvent : {}", id);
        processEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
