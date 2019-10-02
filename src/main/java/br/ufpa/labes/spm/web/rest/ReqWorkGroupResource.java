package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ReqWorkGroup;
import br.ufpa.labes.spm.repository.ReqWorkGroupRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ReqWorkGroup}. */
@RestController
@RequestMapping("/api")
public class ReqWorkGroupResource {

  private final Logger log = LoggerFactory.getLogger(ReqWorkGroupResource.class);

  private static final String ENTITY_NAME = "reqWorkGroup";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ReqWorkGroupRepository reqWorkGroupRepository;

  public ReqWorkGroupResource(ReqWorkGroupRepository reqWorkGroupRepository) {
    this.reqWorkGroupRepository = reqWorkGroupRepository;
  }

  /**
   * {@code POST /req-work-groups} : Create a new reqWorkGroup.
   *
   * @param reqWorkGroup the reqWorkGroup to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     reqWorkGroup, or with status {@code 400 (Bad Request)} if the reqWorkGroup has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/req-work-groups")
  public ResponseEntity<ReqWorkGroup> createReqWorkGroup(@RequestBody ReqWorkGroup reqWorkGroup)
      throws URISyntaxException {
    log.debug("REST request to save ReqWorkGroup : {}", reqWorkGroup);
    if (reqWorkGroup.getId() != null) {
      throw new BadRequestAlertException(
          "A new reqWorkGroup cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ReqWorkGroup result = reqWorkGroupRepository.save(reqWorkGroup);
    return ResponseEntity.created(new URI("/api/req-work-groups/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /req-work-groups} : Updates an existing reqWorkGroup.
   *
   * @param reqWorkGroup the reqWorkGroup to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     reqWorkGroup, or with status {@code 400 (Bad Request)} if the reqWorkGroup is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the reqWorkGroup couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/req-work-groups")
  public ResponseEntity<ReqWorkGroup> updateReqWorkGroup(@RequestBody ReqWorkGroup reqWorkGroup)
      throws URISyntaxException {
    log.debug("REST request to update ReqWorkGroup : {}", reqWorkGroup);
    if (reqWorkGroup.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ReqWorkGroup result = reqWorkGroupRepository.save(reqWorkGroup);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, reqWorkGroup.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /req-work-groups} : get all the reqWorkGroups.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reqWorkGroups
   *     in body.
   */
  @GetMapping("/req-work-groups")
  public List<ReqWorkGroup> getAllReqWorkGroups() {
    log.debug("REST request to get all ReqWorkGroups");
    return reqWorkGroupRepository.findAll();
  }

  /**
   * {@code GET /req-work-groups/:id} : get the "id" reqWorkGroup.
   *
   * @param id the id of the reqWorkGroup to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reqWorkGroup,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/req-work-groups/{id}")
  public ResponseEntity<ReqWorkGroup> getReqWorkGroup(@PathVariable Long id) {
    log.debug("REST request to get ReqWorkGroup : {}", id);
    Optional<ReqWorkGroup> reqWorkGroup = reqWorkGroupRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(reqWorkGroup);
  }

  /**
   * {@code DELETE /req-work-groups/:id} : delete the "id" reqWorkGroup.
   *
   * @param id the id of the reqWorkGroup to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/req-work-groups/{id}")
  public ResponseEntity<Void> deleteReqWorkGroup(@PathVariable Long id) {
    log.debug("REST request to delete ReqWorkGroup : {}", id);
    reqWorkGroupRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
