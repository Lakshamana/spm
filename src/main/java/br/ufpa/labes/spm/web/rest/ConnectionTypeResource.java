package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ConnectionTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ConnectionTypeDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ConnectionType}. */
@RestController
@RequestMapping("/api")
public class ConnectionTypeResource {

  private final Logger log = LoggerFactory.getLogger(ConnectionTypeResource.class);

  private static final String ENTITY_NAME = "connectionType";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ConnectionTypeService connectionTypeService;

  public ConnectionTypeResource(ConnectionTypeService connectionTypeService) {
    this.connectionTypeService = connectionTypeService;
  }

  /**
   * {@code POST /connection-types} : Create a new connectionType.
   *
   * @param connectionTypeDTO the connectionTypeDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     connectionTypeDTO, or with status {@code 400 (Bad Request)} if the connectionType has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/connection-types")
  public ResponseEntity<ConnectionTypeDTO> createConnectionType(
      @RequestBody ConnectionTypeDTO connectionTypeDTO) throws URISyntaxException {
    log.debug("REST request to save ConnectionType : {}", connectionTypeDTO);
    if (connectionTypeDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new connectionType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ConnectionTypeDTO result = connectionTypeService.save(connectionTypeDTO);
    return ResponseEntity.created(new URI("/api/connection-types/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /connection-types} : Updates an existing connectionType.
   *
   * @param connectionTypeDTO the connectionTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     connectionTypeDTO, or with status {@code 400 (Bad Request)} if the connectionTypeDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the connectionTypeDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/connection-types")
  public ResponseEntity<ConnectionTypeDTO> updateConnectionType(
      @RequestBody ConnectionTypeDTO connectionTypeDTO) throws URISyntaxException {
    log.debug("REST request to update ConnectionType : {}", connectionTypeDTO);
    if (connectionTypeDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ConnectionTypeDTO result = connectionTypeService.save(connectionTypeDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, connectionTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /connection-types} : get all the connectionTypes.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of connectionTypes
   *     in body.
   */
  @GetMapping("/connection-types")
  public List<ConnectionTypeDTO> getAllConnectionTypes() {
    log.debug("REST request to get all ConnectionTypes");
    return connectionTypeService.findAll();
  }

  /**
   * {@code GET /connection-types/:id} : get the "id" connectionType.
   *
   * @param id the id of the connectionTypeDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     connectionTypeDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/connection-types/{id}")
  public ResponseEntity<ConnectionTypeDTO> getConnectionType(@PathVariable Long id) {
    log.debug("REST request to get ConnectionType : {}", id);
    Optional<ConnectionTypeDTO> connectionTypeDTO = connectionTypeService.findOne(id);
    return ResponseUtil.wrapOrNotFound(connectionTypeDTO);
  }

  /**
   * {@code DELETE /connection-types/:id} : delete the "id" connectionType.
   *
   * @param id the id of the connectionTypeDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/connection-types/{id}")
  public ResponseEntity<Void> deleteConnectionType(@PathVariable Long id) {
    log.debug("REST request to delete ConnectionType : {}", id);
    connectionTypeService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
