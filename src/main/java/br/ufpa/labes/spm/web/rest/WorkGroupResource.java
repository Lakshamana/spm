package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.repository.WorkGroupRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.WorkGroup}. */
@RestController
@RequestMapping("/api")
public class WorkGroupResource {

  private final Logger log = LoggerFactory.getLogger(WorkGroupResource.class);

  private static final String ENTITY_NAME = "workGroup";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final WorkGroupRepository workGroupRepository;

  public WorkGroupResource(WorkGroupRepository workGroupRepository) {
    this.workGroupRepository = workGroupRepository;
  }

  /**
   * {@code POST /work-groups} : Create a new workGroup.
   *
   * @param workGroup the workGroup to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     workGroup, or with status {@code 400 (Bad Request)} if the workGroup has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/work-groups")
  public ResponseEntity<WorkGroup> createWorkGroup(@RequestBody WorkGroup workGroup)
      throws URISyntaxException {
    log.debug("REST request to save WorkGroup : {}", workGroup);
    if (workGroup.getId() != null) {
      throw new BadRequestAlertException(
          "A new workGroup cannot already have an ID", ENTITY_NAME, "idexists");
    }
    WorkGroup result = workGroupRepository.save(workGroup);
    return ResponseEntity.created(new URI("/api/work-groups/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /work-groups} : Updates an existing workGroup.
   *
   * @param workGroup the workGroup to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     workGroup, or with status {@code 400 (Bad Request)} if the workGroup is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the workGroup couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/work-groups")
  public ResponseEntity<WorkGroup> updateWorkGroup(@RequestBody WorkGroup workGroup)
      throws URISyntaxException {
    log.debug("REST request to update WorkGroup : {}", workGroup);
    if (workGroup.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    WorkGroup result = workGroupRepository.save(workGroup);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, workGroup.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /work-groups} : get all the workGroups.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workGroups in
   *     body.
   */
  @GetMapping("/work-groups")
  public List<WorkGroup> getAllWorkGroups() {
    log.debug("REST request to get all WorkGroups");
    return workGroupRepository.findAll();
  }

  /**
   * {@code GET /work-groups/:id} : get the "id" workGroup.
   *
   * @param id the id of the workGroup to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workGroup, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/work-groups/{id}")
  public ResponseEntity<WorkGroup> getWorkGroup(@PathVariable Long id) {
    log.debug("REST request to get WorkGroup : {}", id);
    Optional<WorkGroup> workGroup = workGroupRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(workGroup);
  }

  /**
   * {@code DELETE /work-groups/:id} : delete the "id" workGroup.
   *
   * @param id the id of the workGroup to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/work-groups/{id}")
  public ResponseEntity<Void> deleteWorkGroup(@PathVariable Long id) {
    log.debug("REST request to delete WorkGroup : {}", id);
    workGroupRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
