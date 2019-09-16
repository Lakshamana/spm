package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.RoleTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.RoleTypeDTO;

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

  private final RoleTypeService roleTypeService;

  public RoleTypeResource(RoleTypeService roleTypeService) {
    this.roleTypeService = roleTypeService;
  }

  /**
   * {@code POST /role-types} : Create a new roleType.
   *
   * @param roleTypeDTO the roleTypeDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     roleTypeDTO, or with status {@code 400 (Bad Request)} if the roleType has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/role-types")
  public ResponseEntity<RoleTypeDTO> createRoleType(@RequestBody RoleTypeDTO roleTypeDTO)
      throws URISyntaxException {
    log.debug("REST request to save RoleType : {}", roleTypeDTO);
    if (roleTypeDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new roleType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RoleTypeDTO result = roleTypeService.save(roleTypeDTO);
    return ResponseEntity.created(new URI("/api/role-types/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /role-types} : Updates an existing roleType.
   *
   * @param roleTypeDTO the roleTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     roleTypeDTO, or with status {@code 400 (Bad Request)} if the roleTypeDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the roleTypeDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/role-types")
  public ResponseEntity<RoleTypeDTO> updateRoleType(@RequestBody RoleTypeDTO roleTypeDTO)
      throws URISyntaxException {
    log.debug("REST request to update RoleType : {}", roleTypeDTO);
    if (roleTypeDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    RoleTypeDTO result = roleTypeService.save(roleTypeDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, roleTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /role-types} : get all the roleTypes.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleTypes in
   *     body.
   */
  @GetMapping("/role-types")
  public List<RoleTypeDTO> getAllRoleTypes(@RequestParam(required = false) String filter) {
    if ("thetypesuper-is-null".equals(filter)) {
      log.debug("REST request to get all RoleTypes where theTypeSuper is null");
      return roleTypeService.findAllWhereTheTypeSuperIsNull();
    }
    log.debug("REST request to get all RoleTypes");
    return roleTypeService.findAll();
  }

  /**
   * {@code GET /role-types/:id} : get the "id" roleType.
   *
   * @param id the id of the roleTypeDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleTypeDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/role-types/{id}")
  public ResponseEntity<RoleTypeDTO> getRoleType(@PathVariable Long id) {
    log.debug("REST request to get RoleType : {}", id);
    Optional<RoleTypeDTO> roleTypeDTO = roleTypeService.findOne(id);
    return ResponseUtil.wrapOrNotFound(roleTypeDTO);
  }

  /**
   * {@code DELETE /role-types/:id} : delete the "id" roleType.
   *
   * @param id the id of the roleTypeDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/role-types/{id}")
  public ResponseEntity<Void> deleteRoleType(@PathVariable Long id) {
    log.debug("REST request to delete RoleType : {}", id);
    roleTypeService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
