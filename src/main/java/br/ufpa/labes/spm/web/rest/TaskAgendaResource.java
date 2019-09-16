package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.TaskAgendaService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.TaskAgendaDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.TaskAgenda}. */
@RestController
@RequestMapping("/api")
public class TaskAgendaResource {

  private final Logger log = LoggerFactory.getLogger(TaskAgendaResource.class);

  private static final String ENTITY_NAME = "taskAgenda";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final TaskAgendaService taskAgendaService;

  public TaskAgendaResource(TaskAgendaService taskAgendaService) {
    this.taskAgendaService = taskAgendaService;
  }

  /**
   * {@code POST /task-agenda} : Create a new taskAgenda.
   *
   * @param taskAgendaDTO the taskAgendaDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     taskAgendaDTO, or with status {@code 400 (Bad Request)} if the taskAgenda has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/task-agenda")
  public ResponseEntity<TaskAgendaDTO> createTaskAgenda(@RequestBody TaskAgendaDTO taskAgendaDTO)
      throws URISyntaxException {
    log.debug("REST request to save TaskAgenda : {}", taskAgendaDTO);
    if (taskAgendaDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new taskAgenda cannot already have an ID", ENTITY_NAME, "idexists");
    }
    TaskAgendaDTO result = taskAgendaService.save(taskAgendaDTO);
    return ResponseEntity.created(new URI("/api/task-agenda/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /task-agenda} : Updates an existing taskAgenda.
   *
   * @param taskAgendaDTO the taskAgendaDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     taskAgendaDTO, or with status {@code 400 (Bad Request)} if the taskAgendaDTO is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the taskAgendaDTO couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/task-agenda")
  public ResponseEntity<TaskAgendaDTO> updateTaskAgenda(@RequestBody TaskAgendaDTO taskAgendaDTO)
      throws URISyntaxException {
    log.debug("REST request to update TaskAgenda : {}", taskAgendaDTO);
    if (taskAgendaDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    TaskAgendaDTO result = taskAgendaService.save(taskAgendaDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, taskAgendaDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /task-agenda} : get all the taskAgenda.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskAgenda in
   *     body.
   */
  @GetMapping("/task-agenda")
  public List<TaskAgendaDTO> getAllTaskAgenda(@RequestParam(required = false) String filter) {
    if ("theagent-is-null".equals(filter)) {
      log.debug("REST request to get all TaskAgendas where theAgent is null");
      return taskAgendaService.findAllWhereTheAgentIsNull();
    }
    log.debug("REST request to get all TaskAgenda");
    return taskAgendaService.findAll();
  }

  /**
   * {@code GET /task-agenda/:id} : get the "id" taskAgenda.
   *
   * @param id the id of the taskAgendaDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     taskAgendaDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/task-agenda/{id}")
  public ResponseEntity<TaskAgendaDTO> getTaskAgenda(@PathVariable Long id) {
    log.debug("REST request to get TaskAgenda : {}", id);
    Optional<TaskAgendaDTO> taskAgendaDTO = taskAgendaService.findOne(id);
    return ResponseUtil.wrapOrNotFound(taskAgendaDTO);
  }

  /**
   * {@code DELETE /task-agenda/:id} : delete the "id" taskAgenda.
   *
   * @param id the id of the taskAgendaDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/task-agenda/{id}")
  public ResponseEntity<Void> deleteTaskAgenda(@PathVariable Long id) {
    log.debug("REST request to delete TaskAgenda : {}", id);
    taskAgendaService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
