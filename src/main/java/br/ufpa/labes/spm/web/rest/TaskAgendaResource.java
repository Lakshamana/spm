package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.TaskAgenda;
import br.ufpa.labes.spm.repository.TaskAgendaRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.TaskAgenda}.
 */
@RestController
@RequestMapping("/api")
public class TaskAgendaResource {

    private final Logger log = LoggerFactory.getLogger(TaskAgendaResource.class);

    private static final String ENTITY_NAME = "taskAgenda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskAgendaRepository taskAgendaRepository;

    public TaskAgendaResource(TaskAgendaRepository taskAgendaRepository) {
        this.taskAgendaRepository = taskAgendaRepository;
    }

    /**
     * {@code POST  /task-agenda} : Create a new taskAgenda.
     *
     * @param taskAgenda the taskAgenda to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskAgenda, or with status {@code 400 (Bad Request)} if the taskAgenda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-agenda")
    public ResponseEntity<TaskAgenda> createTaskAgenda(@RequestBody TaskAgenda taskAgenda) throws URISyntaxException {
        log.debug("REST request to save TaskAgenda : {}", taskAgenda);
        if (taskAgenda.getId() != null) {
            throw new BadRequestAlertException("A new taskAgenda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskAgenda result = taskAgendaRepository.save(taskAgenda);
        return ResponseEntity.created(new URI("/api/task-agenda/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-agenda} : Updates an existing taskAgenda.
     *
     * @param taskAgenda the taskAgenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskAgenda,
     * or with status {@code 400 (Bad Request)} if the taskAgenda is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskAgenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-agenda")
    public ResponseEntity<TaskAgenda> updateTaskAgenda(@RequestBody TaskAgenda taskAgenda) throws URISyntaxException {
        log.debug("REST request to update TaskAgenda : {}", taskAgenda);
        if (taskAgenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaskAgenda result = taskAgendaRepository.save(taskAgenda);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskAgenda.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /task-agenda} : get all the taskAgenda.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskAgenda in body.
     */
    @GetMapping("/task-agenda")
    public List<TaskAgenda> getAllTaskAgenda(@RequestParam(required = false) String filter) {
        if ("theagent-is-null".equals(filter)) {
            log.debug("REST request to get all TaskAgendas where theAgent is null");
            return StreamSupport
                .stream(taskAgendaRepository.findAll().spliterator(), false)
                .filter(taskAgenda -> taskAgenda.getTheAgent() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all TaskAgenda");
        return taskAgendaRepository.findAll();
    }

    /**
     * {@code GET  /task-agenda/:id} : get the "id" taskAgenda.
     *
     * @param id the id of the taskAgenda to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskAgenda, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-agenda/{id}")
    public ResponseEntity<TaskAgenda> getTaskAgenda(@PathVariable Long id) {
        log.debug("REST request to get TaskAgenda : {}", id);
        Optional<TaskAgenda> taskAgenda = taskAgendaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(taskAgenda);
    }

    /**
     * {@code DELETE  /task-agenda/:id} : delete the "id" taskAgenda.
     *
     * @param id the id of the taskAgenda to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-agenda/{id}")
    public ResponseEntity<Void> deleteTaskAgenda(@PathVariable Long id) {
        log.debug("REST request to delete TaskAgenda : {}", id);
        taskAgendaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
