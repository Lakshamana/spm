package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.LogEntryService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.LogEntryDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.LogEntry}.
 */
@RestController
@RequestMapping("/api")
public class LogEntryResource {

    private final Logger log = LoggerFactory.getLogger(LogEntryResource.class);

    private static final String ENTITY_NAME = "logEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LogEntryService logEntryService;

    public LogEntryResource(LogEntryService logEntryService) {
        this.logEntryService = logEntryService;
    }

    /**
     * {@code POST  /log-entries} : Create a new logEntry.
     *
     * @param logEntryDTO the logEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new logEntryDTO, or with status {@code 400 (Bad Request)} if the logEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/log-entries")
    public ResponseEntity<LogEntryDTO> createLogEntry(@RequestBody LogEntryDTO logEntryDTO) throws URISyntaxException {
        log.debug("REST request to save LogEntry : {}", logEntryDTO);
        if (logEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new logEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LogEntryDTO result = logEntryService.save(logEntryDTO);
        return ResponseEntity.created(new URI("/api/log-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /log-entries} : Updates an existing logEntry.
     *
     * @param logEntryDTO the logEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated logEntryDTO,
     * or with status {@code 400 (Bad Request)} if the logEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the logEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/log-entries")
    public ResponseEntity<LogEntryDTO> updateLogEntry(@RequestBody LogEntryDTO logEntryDTO) throws URISyntaxException {
        log.debug("REST request to update LogEntry : {}", logEntryDTO);
        if (logEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LogEntryDTO result = logEntryService.save(logEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, logEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /log-entries} : get all the logEntries.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of logEntries in body.
     */
    @GetMapping("/log-entries")
    public List<LogEntryDTO> getAllLogEntries() {
        log.debug("REST request to get all LogEntries");
        return logEntryService.findAll();
    }

    /**
     * {@code GET  /log-entries/:id} : get the "id" logEntry.
     *
     * @param id the id of the logEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the logEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/log-entries/{id}")
    public ResponseEntity<LogEntryDTO> getLogEntry(@PathVariable Long id) {
        log.debug("REST request to get LogEntry : {}", id);
        Optional<LogEntryDTO> logEntryDTO = logEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(logEntryDTO);
    }

    /**
     * {@code DELETE  /log-entries/:id} : delete the "id" logEntry.
     *
     * @param id the id of the logEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/log-entries/{id}")
    public ResponseEntity<Void> deleteLogEntry(@PathVariable Long id) {
        log.debug("REST request to delete LogEntry : {}", id);
        logEntryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
