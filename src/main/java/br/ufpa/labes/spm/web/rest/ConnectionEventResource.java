package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ConnectionEventService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ConnectionEventDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ConnectionEvent}.
 */
@RestController
@RequestMapping("/api")
public class ConnectionEventResource {

    private final Logger log = LoggerFactory.getLogger(ConnectionEventResource.class);

    private static final String ENTITY_NAME = "connectionEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConnectionEventService connectionEventService;

    public ConnectionEventResource(ConnectionEventService connectionEventService) {
        this.connectionEventService = connectionEventService;
    }

    /**
     * {@code POST  /connection-events} : Create a new connectionEvent.
     *
     * @param connectionEventDTO the connectionEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new connectionEventDTO, or with status {@code 400 (Bad Request)} if the connectionEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/connection-events")
    public ResponseEntity<ConnectionEventDTO> createConnectionEvent(@RequestBody ConnectionEventDTO connectionEventDTO) throws URISyntaxException {
        log.debug("REST request to save ConnectionEvent : {}", connectionEventDTO);
        if (connectionEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new connectionEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConnectionEventDTO result = connectionEventService.save(connectionEventDTO);
        return ResponseEntity.created(new URI("/api/connection-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /connection-events} : Updates an existing connectionEvent.
     *
     * @param connectionEventDTO the connectionEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated connectionEventDTO,
     * or with status {@code 400 (Bad Request)} if the connectionEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the connectionEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/connection-events")
    public ResponseEntity<ConnectionEventDTO> updateConnectionEvent(@RequestBody ConnectionEventDTO connectionEventDTO) throws URISyntaxException {
        log.debug("REST request to update ConnectionEvent : {}", connectionEventDTO);
        if (connectionEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConnectionEventDTO result = connectionEventService.save(connectionEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, connectionEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /connection-events} : get all the connectionEvents.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of connectionEvents in body.
     */
    @GetMapping("/connection-events")
    public List<ConnectionEventDTO> getAllConnectionEvents(@RequestParam(required = false) String filter) {
        if ("theeventsuper-is-null".equals(filter)) {
            log.debug("REST request to get all ConnectionEvents where theEventSuper is null");
            return connectionEventService.findAllWhereTheEventSuperIsNull();
        }
        log.debug("REST request to get all ConnectionEvents");
        return connectionEventService.findAll();
    }

    /**
     * {@code GET  /connection-events/:id} : get the "id" connectionEvent.
     *
     * @param id the id of the connectionEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the connectionEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/connection-events/{id}")
    public ResponseEntity<ConnectionEventDTO> getConnectionEvent(@PathVariable Long id) {
        log.debug("REST request to get ConnectionEvent : {}", id);
        Optional<ConnectionEventDTO> connectionEventDTO = connectionEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(connectionEventDTO);
    }

    /**
     * {@code DELETE  /connection-events/:id} : delete the "id" connectionEvent.
     *
     * @param id the id of the connectionEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/connection-events/{id}")
    public ResponseEntity<Void> deleteConnectionEvent(@PathVariable Long id) {
        log.debug("REST request to delete ConnectionEvent : {}", id);
        connectionEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
