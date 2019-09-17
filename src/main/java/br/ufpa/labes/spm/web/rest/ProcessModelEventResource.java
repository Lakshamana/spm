package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ProcessModelEventService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ProcessModelEventDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessModelEvent}. */
@RestController
@RequestMapping("/api")
public class ProcessModelEventResource {

  private final Logger log = LoggerFactory.getLogger(ProcessModelEventResource.class);

  private static final String ENTITY_NAME = "processModelEvent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ProcessModelEventService processModelEventService;

  public ProcessModelEventResource(ProcessModelEventService processModelEventService) {
    this.processModelEventService = processModelEventService;
  }

  /**
   * {@code POST /process-model-events} : Create a new processModelEvent.
   *
   * @param processModelEventDTO the processModelEventDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     processModelEventDTO, or with status {@code 400 (Bad Request)} if the processModelEvent has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/process-model-events")
  public ResponseEntity<ProcessModelEventDTO> createProcessModelEvent(
      @RequestBody ProcessModelEventDTO processModelEventDTO) throws URISyntaxException {
    log.debug("REST request to save ProcessModelEvent : {}", processModelEventDTO);
    if (processModelEventDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new processModelEvent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ProcessModelEventDTO result = processModelEventService.save(processModelEventDTO);
    return ResponseEntity.created(new URI("/api/process-model-events/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /process-model-events} : Updates an existing processModelEvent.
   *
   * @param processModelEventDTO the processModelEventDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     processModelEventDTO, or with status {@code 400 (Bad Request)} if the processModelEventDTO
   *     is not valid, or with status {@code 500 (Internal Server Error)} if the
   *     processModelEventDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/process-model-events")
  public ResponseEntity<ProcessModelEventDTO> updateProcessModelEvent(
      @RequestBody ProcessModelEventDTO processModelEventDTO) throws URISyntaxException {
    log.debug("REST request to update ProcessModelEvent : {}", processModelEventDTO);
    if (processModelEventDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ProcessModelEventDTO result = processModelEventService.save(processModelEventDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, processModelEventDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /process-model-events} : get all the processModelEvents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     processModelEvents in body.
   */
  @GetMapping("/process-model-events")
  public List<ProcessModelEventDTO> getAllProcessModelEvents() {
    log.debug("REST request to get all ProcessModelEvents");
    return processModelEventService.findAll();
  }

  /**
   * {@code GET /process-model-events/:id} : get the "id" processModelEvent.
   *
   * @param id the id of the processModelEventDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     processModelEventDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/process-model-events/{id}")
  public ResponseEntity<ProcessModelEventDTO> getProcessModelEvent(@PathVariable Long id) {
    log.debug("REST request to get ProcessModelEvent : {}", id);
    Optional<ProcessModelEventDTO> processModelEventDTO = processModelEventService.findOne(id);
    return ResponseUtil.wrapOrNotFound(processModelEventDTO);
  }

  /**
   * {@code DELETE /process-model-events/:id} : delete the "id" processModelEvent.
   *
   * @param id the id of the processModelEventDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/process-model-events/{id}")
  public ResponseEntity<Void> deleteProcessModelEvent(@PathVariable Long id) {
    log.debug("REST request to delete ProcessModelEvent : {}", id);
    processModelEventService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
