package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ConnectionService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ConnectionDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Connection}.
 */
@RestController
@RequestMapping("/api")
public class ConnectionResource {

    private final Logger log = LoggerFactory.getLogger(ConnectionResource.class);

    private static final String ENTITY_NAME = "connection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConnectionService connectionService;

    public ConnectionResource(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    /**
     * {@code POST  /connections} : Create a new connection.
     *
     * @param connectionDTO the connectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new connectionDTO, or with status {@code 400 (Bad Request)} if the connection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/connections")
    public ResponseEntity<ConnectionDTO> createConnection(@RequestBody ConnectionDTO connectionDTO) throws URISyntaxException {
        log.debug("REST request to save Connection : {}", connectionDTO);
        if (connectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new connection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConnectionDTO result = connectionService.save(connectionDTO);
        return ResponseEntity.created(new URI("/api/connections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /connections} : Updates an existing connection.
     *
     * @param connectionDTO the connectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated connectionDTO,
     * or with status {@code 400 (Bad Request)} if the connectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the connectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/connections")
    public ResponseEntity<ConnectionDTO> updateConnection(@RequestBody ConnectionDTO connectionDTO) throws URISyntaxException {
        log.debug("REST request to update Connection : {}", connectionDTO);
        if (connectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConnectionDTO result = connectionService.save(connectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, connectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /connections} : get all the connections.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of connections in body.
     */
    @GetMapping("/connections")
    public List<ConnectionDTO> getAllConnections() {
        log.debug("REST request to get all Connections");
        return connectionService.findAll();
    }

    /**
     * {@code GET  /connections/:id} : get the "id" connection.
     *
     * @param id the id of the connectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the connectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/connections/{id}")
    public ResponseEntity<ConnectionDTO> getConnection(@PathVariable Long id) {
        log.debug("REST request to get Connection : {}", id);
        Optional<ConnectionDTO> connectionDTO = connectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(connectionDTO);
    }

    /**
     * {@code DELETE  /connections/:id} : delete the "id" connection.
     *
     * @param id the id of the connectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/connections/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        log.debug("REST request to delete Connection : {}", id);
        connectionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
