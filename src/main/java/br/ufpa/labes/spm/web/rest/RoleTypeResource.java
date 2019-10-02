package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.RoleType;
import br.ufpa.labes.spm.repository.RoleTypeRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.RoleType}. */
@RestController
@RequestMapping("/api")
public class RoleTypeResource {

  private final Logger log = LoggerFactory.getLogger(RoleTypeResource.class);

  private static final String ENTITY_NAME = "roleType";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RoleTypeRepository roleTypeRepository;

  public RoleTypeResource(RoleTypeRepository roleTypeRepository) {
    this.roleTypeRepository = roleTypeRepository;
  }

  /**
   * {@code POST /role-types} : Create a new roleType.
   *
   * @param roleType the roleType to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     roleType, or with status {@code 400 (Bad Request)} if the roleType has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/role-types")
  public ResponseEntity<RoleType> createRoleType(@RequestBody RoleType roleType)
      throws URISyntaxException {
    log.debug("REST request to save RoleType : {}", roleType);
    if (roleType.getId() != null) {
      throw new BadRequestAlertException(
          "A new roleType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RoleType result = roleTypeRepository.save(roleType);
    return ResponseEntity.created(new URI("/api/role-types/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /role-types} : Updates an existing roleType.
   *
   * @param roleType the roleType to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     roleType, or with status {@code 400 (Bad Request)} if the roleType is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the roleType couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/role-types")
  public ResponseEntity<RoleType> updateRoleType(@RequestBody RoleType roleType)
      throws URISyntaxException {
    log.debug("REST request to update RoleType : {}", roleType);
    if (roleType.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    RoleType result = roleTypeRepository.save(roleType);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, roleType.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /role-types} : get all the roleTypes.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleTypes in
   *     body.
   */
  @GetMapping("/role-types")
  public List<RoleType> getAllRoleTypes() {
    log.debug("REST request to get all RoleTypes");
    return roleTypeRepository.findAll();
  }

  /**
   * {@code GET /role-types/:id} : get the "id" roleType.
   *
   * @param id the id of the roleType to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleType, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/role-types/{id}")
  public ResponseEntity<RoleType> getRoleType(@PathVariable Long id) {
    log.debug("REST request to get RoleType : {}", id);
    Optional<RoleType> roleType = roleTypeRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(roleType);
  }

  /**
   * {@code DELETE /role-types/:id} : delete the "id" roleType.
   *
   * @param id the id of the roleType to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/role-types/{id}")
  public ResponseEntity<Void> deleteRoleType(@PathVariable Long id) {
    log.debug("REST request to delete RoleType : {}", id);
    roleTypeRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
