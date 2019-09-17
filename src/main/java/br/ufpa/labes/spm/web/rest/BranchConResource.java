package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.BranchConService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.BranchConDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.BranchCon}. */
@RestController
@RequestMapping("/api")
public class BranchConResource {

  private final Logger log = LoggerFactory.getLogger(BranchConResource.class);

  private static final String ENTITY_NAME = "branchCon";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final BranchConService branchConService;

  public BranchConResource(BranchConService branchConService) {
    this.branchConService = branchConService;
  }

  /**
   * {@code POST /branch-cons} : Create a new branchCon.
   *
   * @param branchConDTO the branchConDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     branchConDTO, or with status {@code 400 (Bad Request)} if the branchCon has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/branch-cons")
  public ResponseEntity<BranchConDTO> createBranchCon(@RequestBody BranchConDTO branchConDTO)
      throws URISyntaxException {
    log.debug("REST request to save BranchCon : {}", branchConDTO);
    if (branchConDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new branchCon cannot already have an ID", ENTITY_NAME, "idexists");
    }
    BranchConDTO result = branchConService.save(branchConDTO);
    return ResponseEntity.created(new URI("/api/branch-cons/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /branch-cons} : Updates an existing branchCon.
   *
   * @param branchConDTO the branchConDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     branchConDTO, or with status {@code 400 (Bad Request)} if the branchConDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the branchConDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/branch-cons")
  public ResponseEntity<BranchConDTO> updateBranchCon(@RequestBody BranchConDTO branchConDTO)
      throws URISyntaxException {
    log.debug("REST request to update BranchCon : {}", branchConDTO);
    if (branchConDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    BranchConDTO result = branchConService.save(branchConDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, branchConDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /branch-cons} : get all the branchCons.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchCons in
   *     body.
   */
  @GetMapping("/branch-cons")
  public List<BranchConDTO> getAllBranchCons() {
    log.debug("REST request to get all BranchCons");
    return branchConService.findAll();
  }

  /**
   * {@code GET /branch-cons/:id} : get the "id" branchCon.
   *
   * @param id the id of the branchConDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchConDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/branch-cons/{id}")
  public ResponseEntity<BranchConDTO> getBranchCon(@PathVariable Long id) {
    log.debug("REST request to get BranchCon : {}", id);
    Optional<BranchConDTO> branchConDTO = branchConService.findOne(id);
    return ResponseUtil.wrapOrNotFound(branchConDTO);
  }

  /**
   * {@code DELETE /branch-cons/:id} : delete the "id" branchCon.
   *
   * @param id the id of the branchConDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/branch-cons/{id}")
  public ResponseEntity<Void> deleteBranchCon(@PathVariable Long id) {
    log.debug("REST request to delete BranchCon : {}", id);
    branchConService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
