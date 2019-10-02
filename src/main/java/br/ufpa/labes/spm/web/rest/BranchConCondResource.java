package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.BranchConCond;
import br.ufpa.labes.spm.repository.BranchConCondRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.BranchConCond}. */
@RestController
@RequestMapping("/api")
public class BranchConCondResource {

  private final Logger log = LoggerFactory.getLogger(BranchConCondResource.class);

  private static final String ENTITY_NAME = "branchConCond";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final BranchConCondRepository branchConCondRepository;

  public BranchConCondResource(BranchConCondRepository branchConCondRepository) {
    this.branchConCondRepository = branchConCondRepository;
  }

  /**
   * {@code POST /branch-con-conds} : Create a new branchConCond.
   *
   * @param branchConCond the branchConCond to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     branchConCond, or with status {@code 400 (Bad Request)} if the branchConCond has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/branch-con-conds")
  public ResponseEntity<BranchConCond> createBranchConCond(@RequestBody BranchConCond branchConCond)
      throws URISyntaxException {
    log.debug("REST request to save BranchConCond : {}", branchConCond);
    if (branchConCond.getId() != null) {
      throw new BadRequestAlertException(
          "A new branchConCond cannot already have an ID", ENTITY_NAME, "idexists");
    }
    BranchConCond result = branchConCondRepository.save(branchConCond);
    return ResponseEntity.created(new URI("/api/branch-con-conds/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /branch-con-conds} : Updates an existing branchConCond.
   *
   * @param branchConCond the branchConCond to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     branchConCond, or with status {@code 400 (Bad Request)} if the branchConCond is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the branchConCond couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/branch-con-conds")
  public ResponseEntity<BranchConCond> updateBranchConCond(@RequestBody BranchConCond branchConCond)
      throws URISyntaxException {
    log.debug("REST request to update BranchConCond : {}", branchConCond);
    if (branchConCond.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    BranchConCond result = branchConCondRepository.save(branchConCond);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, branchConCond.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /branch-con-conds} : get all the branchConConds.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchConConds
   *     in body.
   */
  @GetMapping("/branch-con-conds")
  public List<BranchConCond> getAllBranchConConds() {
    log.debug("REST request to get all BranchConConds");
    return branchConCondRepository.findAll();
  }

  /**
   * {@code GET /branch-con-conds/:id} : get the "id" branchConCond.
   *
   * @param id the id of the branchConCond to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     branchConCond, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/branch-con-conds/{id}")
  public ResponseEntity<BranchConCond> getBranchConCond(@PathVariable Long id) {
    log.debug("REST request to get BranchConCond : {}", id);
    Optional<BranchConCond> branchConCond = branchConCondRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(branchConCond);
  }

  /**
   * {@code DELETE /branch-con-conds/:id} : delete the "id" branchConCond.
   *
   * @param id the id of the branchConCond to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/branch-con-conds/{id}")
  public ResponseEntity<Void> deleteBranchConCond(@PathVariable Long id) {
    log.debug("REST request to delete BranchConCond : {}", id);
    branchConCondRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
