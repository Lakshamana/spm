package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.RoleNeedsAbilityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.RoleNeedsAbilityDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.RoleNeedsAbility}. */
@RestController
@RequestMapping("/api")
public class RoleNeedsAbilityResource {

  private final Logger log = LoggerFactory.getLogger(RoleNeedsAbilityResource.class);

  private static final String ENTITY_NAME = "roleNeedsAbility";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RoleNeedsAbilityService roleNeedsAbilityService;

  public RoleNeedsAbilityResource(RoleNeedsAbilityService roleNeedsAbilityService) {
    this.roleNeedsAbilityService = roleNeedsAbilityService;
  }

  /**
   * {@code POST /role-needs-abilities} : Create a new roleNeedsAbility.
   *
   * @param roleNeedsAbilityDTO the roleNeedsAbilityDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     roleNeedsAbilityDTO, or with status {@code 400 (Bad Request)} if the roleNeedsAbility has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/role-needs-abilities")
  public ResponseEntity<RoleNeedsAbilityDTO> createRoleNeedsAbility(
      @RequestBody RoleNeedsAbilityDTO roleNeedsAbilityDTO) throws URISyntaxException {
    log.debug("REST request to save RoleNeedsAbility : {}", roleNeedsAbilityDTO);
    if (roleNeedsAbilityDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new roleNeedsAbility cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RoleNeedsAbilityDTO result = roleNeedsAbilityService.save(roleNeedsAbilityDTO);
    return ResponseEntity.created(new URI("/api/role-needs-abilities/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /role-needs-abilities} : Updates an existing roleNeedsAbility.
   *
   * @param roleNeedsAbilityDTO the roleNeedsAbilityDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     roleNeedsAbilityDTO, or with status {@code 400 (Bad Request)} if the roleNeedsAbilityDTO is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the roleNeedsAbilityDTO
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/role-needs-abilities")
  public ResponseEntity<RoleNeedsAbilityDTO> updateRoleNeedsAbility(
      @RequestBody RoleNeedsAbilityDTO roleNeedsAbilityDTO) throws URISyntaxException {
    log.debug("REST request to update RoleNeedsAbility : {}", roleNeedsAbilityDTO);
    if (roleNeedsAbilityDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    RoleNeedsAbilityDTO result = roleNeedsAbilityService.save(roleNeedsAbilityDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, roleNeedsAbilityDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /role-needs-abilities} : get all the roleNeedsAbilities.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     roleNeedsAbilities in body.
   */
  @GetMapping("/role-needs-abilities")
  public List<RoleNeedsAbilityDTO> getAllRoleNeedsAbilities() {
    log.debug("REST request to get all RoleNeedsAbilities");
    return roleNeedsAbilityService.findAll();
  }

  /**
   * {@code GET /role-needs-abilities/:id} : get the "id" roleNeedsAbility.
   *
   * @param id the id of the roleNeedsAbilityDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     roleNeedsAbilityDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/role-needs-abilities/{id}")
  public ResponseEntity<RoleNeedsAbilityDTO> getRoleNeedsAbility(@PathVariable Long id) {
    log.debug("REST request to get RoleNeedsAbility : {}", id);
    Optional<RoleNeedsAbilityDTO> roleNeedsAbilityDTO = roleNeedsAbilityService.findOne(id);
    return ResponseUtil.wrapOrNotFound(roleNeedsAbilityDTO);
  }

  /**
   * {@code DELETE /role-needs-abilities/:id} : delete the "id" roleNeedsAbility.
   *
   * @param id the id of the roleNeedsAbilityDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/role-needs-abilities/{id}")
  public ResponseEntity<Void> deleteRoleNeedsAbility(@PathVariable Long id) {
    log.debug("REST request to delete RoleNeedsAbility : {}", id);
    roleNeedsAbilityService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
