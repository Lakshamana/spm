package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.BranchConCondToActivity;
import br.ufpa.labes.spm.repository.BranchConCondToActivityRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.BranchConCondToActivity}. */
@RestController
@RequestMapping("/api")
public class BranchConCondToActivityResource {

  private final Logger log = LoggerFactory.getLogger(BranchConCondToActivityResource.class);

  private static final String ENTITY_NAME = "branchConCondToActivity";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final BranchConCondToActivityRepository branchConCondToActivityRepository;

  public BranchConCondToActivityResource(
      BranchConCondToActivityRepository branchConCondToActivityRepository) {
    this.branchConCondToActivityRepository = branchConCondToActivityRepository;
  }

  /**
   * {@code POST /branch-con-cond-to-activities} : Create a new branchConCondToActivity.
   *
   * @param branchConCondToActivity the branchConCondToActivity to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     branchConCondToActivity, or with status {@code 400 (Bad Request)} if the
   *     branchConCondToActivity has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/branch-con-cond-to-activities")
  public ResponseEntity<BranchConCondToActivity> createBranchConCondToActivity(
      @RequestBody BranchConCondToActivity branchConCondToActivity) throws URISyntaxException {
    log.debug("REST request to save BranchConCondToActivity : {}", branchConCondToActivity);
    if (branchConCondToActivity.getId() != null) {
      throw new BadRequestAlertException(
          "A new branchConCondToActivity cannot already have an ID", ENTITY_NAME, "idexists");
    }
    BranchConCondToActivity result =
        branchConCondToActivityRepository.save(branchConCondToActivity);
    return ResponseEntity.created(new URI("/api/branch-con-cond-to-activities/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /branch-con-cond-to-activities} : Updates an existing branchConCondToActivity.
   *
   * @param branchConCondToActivity the branchConCondToActivity to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     branchConCondToActivity, or with status {@code 400 (Bad Request)} if the
   *     branchConCondToActivity is not valid, or with status {@code 500 (Internal Server Error)} if
   *     the branchConCondToActivity couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/branch-con-cond-to-activities")
  public ResponseEntity<BranchConCondToActivity> updateBranchConCondToActivity(
      @RequestBody BranchConCondToActivity branchConCondToActivity) throws URISyntaxException {
    log.debug("REST request to update BranchConCondToActivity : {}", branchConCondToActivity);
    if (branchConCondToActivity.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    BranchConCondToActivity result =
        branchConCondToActivityRepository.save(branchConCondToActivity);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, branchConCondToActivity.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /branch-con-cond-to-activities} : get all the branchConCondToActivities.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     branchConCondToActivities in body.
   */
  @GetMapping("/branch-con-cond-to-activities")
  public List<BranchConCondToActivity> getAllBranchConCondToActivities() {
    log.debug("REST request to get all BranchConCondToActivities");
    return branchConCondToActivityRepository.findAll();
  }

  /**
   * {@code GET /branch-con-cond-to-activities/:id} : get the "id" branchConCondToActivity.
   *
   * @param id the id of the branchConCondToActivity to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     branchConCondToActivity, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/branch-con-cond-to-activities/{id}")
  public ResponseEntity<BranchConCondToActivity> getBranchConCondToActivity(@PathVariable Long id) {
    log.debug("REST request to get BranchConCondToActivity : {}", id);
    Optional<BranchConCondToActivity> branchConCondToActivity =
        branchConCondToActivityRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(branchConCondToActivity);
  }

  /**
   * {@code DELETE /branch-con-cond-to-activities/:id} : delete the "id" branchConCondToActivity.
   *
   * @param id the id of the branchConCondToActivity to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/branch-con-cond-to-activities/{id}")
  public ResponseEntity<Void> deleteBranchConCondToActivity(@PathVariable Long id) {
    log.debug("REST request to delete BranchConCondToActivity : {}", id);
    branchConCondToActivityRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
