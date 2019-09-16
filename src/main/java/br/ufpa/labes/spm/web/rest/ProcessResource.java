package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ProcessService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ProcessDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Process}. */
@RestController
@RequestMapping("/api")
public class ProcessResource {

  private final Logger log = LoggerFactory.getLogger(ProcessResource.class);

  private static final String ENTITY_NAME = "process";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ProcessService processService;

  public ProcessResource(ProcessService processService) {
    this.processService = processService;
  }

  /**
   * {@code POST /processes} : Create a new process.
   *
   * @param processDTO the processDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     processDTO, or with status {@code 400 (Bad Request)} if the process has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/processes")
  public ResponseEntity<ProcessDTO> createProcess(@RequestBody ProcessDTO processDTO)
      throws URISyntaxException {
    log.debug("REST request to save Process : {}", processDTO);
    if (processDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new process cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ProcessDTO result = processService.save(processDTO);
    return ResponseEntity.created(new URI("/api/processes/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /processes} : Updates an existing process.
   *
   * @param processDTO the processDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     processDTO, or with status {@code 400 (Bad Request)} if the processDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the processDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/processes")
  public ResponseEntity<ProcessDTO> updateProcess(@RequestBody ProcessDTO processDTO)
      throws URISyntaxException {
    log.debug("REST request to update Process : {}", processDTO);
    if (processDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ProcessDTO result = processService.save(processDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, processDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /processes} : get all the processes.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processes in
   *     body.
   */
  @GetMapping("/processes")
  public List<ProcessDTO> getAllProcesses(@RequestParam(required = false) String filter) {
    if ("thelog-is-null".equals(filter)) {
      log.debug("REST request to get all Processs where theLog is null");
      return processService.findAllWhereTheLogIsNull();
    }
    log.debug("REST request to get all Processes");
    return processService.findAll();
  }

  /**
   * {@code GET /processes/:id} : get the "id" process.
   *
   * @param id the id of the processDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/processes/{id}")
  public ResponseEntity<ProcessDTO> getProcess(@PathVariable Long id) {
    log.debug("REST request to get Process : {}", id);
    Optional<ProcessDTO> processDTO = processService.findOne(id);
    return ResponseUtil.wrapOrNotFound(processDTO);
  }

  /**
   * {@code DELETE /processes/:id} : delete the "id" process.
   *
   * @param id the id of the processDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/processes/{id}")
  public ResponseEntity<Void> deleteProcess(@PathVariable Long id) {
    log.debug("REST request to delete Process : {}", id);
    processService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
