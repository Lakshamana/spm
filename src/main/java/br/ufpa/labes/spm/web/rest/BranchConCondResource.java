package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.BranchConCondService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.BranchConCondDTO;

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

  private final BranchConCondService branchConCondService;

  public BranchConCondResource(BranchConCondService branchConCondService) {
    this.branchConCondService = branchConCondService;
  }

  /**
   * {@code POST /branch-con-conds} : Create a new branchConCond.
   *
   * @param branchConCondDTO the branchConCondDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     branchConCondDTO, or with status {@code 400 (Bad Request)} if the branchConCond has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/branch-con-conds")
  public ResponseEntity<BranchConCondDTO> createBranchConCond(
      @RequestBody BranchConCondDTO branchConCondDTO) throws URISyntaxException {
    log.debug("REST request to save BranchConCond : {}", branchConCondDTO);
    if (branchConCondDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new branchConCond cannot already have an ID", ENTITY_NAME, "idexists");
    }
    BranchConCondDTO result = branchConCondService.save(branchConCondDTO);
    return ResponseEntity.created(new URI("/api/branch-con-conds/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /branch-con-conds} : Updates an existing branchConCond.
   *
   * @param branchConCondDTO the branchConCondDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     branchConCondDTO, or with status {@code 400 (Bad Request)} if the branchConCondDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the branchConCondDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/branch-con-conds")
  public ResponseEntity<BranchConCondDTO> updateBranchConCond(
      @RequestBody BranchConCondDTO branchConCondDTO) throws URISyntaxException {
    log.debug("REST request to update BranchConCond : {}", branchConCondDTO);
    if (branchConCondDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    BranchConCondDTO result = branchConCondService.save(branchConCondDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, branchConCondDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /branch-con-conds} : get all the branchConConds.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchConConds
   *     in body.
   */
  @GetMapping("/branch-con-conds")
  public List<BranchConCondDTO> getAllBranchConConds() {
    log.debug("REST request to get all BranchConConds");
    return branchConCondService.findAll();
  }

  /**
   * {@code GET /branch-con-conds/:id} : get the "id" branchConCond.
   *
   * @param id the id of the branchConCondDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     branchConCondDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/branch-con-conds/{id}")
  public ResponseEntity<BranchConCondDTO> getBranchConCond(@PathVariable Long id) {
    log.debug("REST request to get BranchConCond : {}", id);
    Optional<BranchConCondDTO> branchConCondDTO = branchConCondService.findOne(id);
    return ResponseUtil.wrapOrNotFound(branchConCondDTO);
  }

  /**
   * {@code DELETE /branch-con-conds/:id} : delete the "id" branchConCond.
   *
   * @param id the id of the branchConCondDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/branch-con-conds/{id}")
  public ResponseEntity<Void> deleteBranchConCond(@PathVariable Long id) {
    log.debug("REST request to delete BranchConCond : {}", id);
    branchConCondService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
