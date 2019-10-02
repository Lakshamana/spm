package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.RoleNeedsAbility;
import br.ufpa.labes.spm.repository.RoleNeedsAbilityRepository;
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

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.RoleNeedsAbility}.
 */
@RestController
@RequestMapping("/api")
public class RoleNeedsAbilityResource {

    private final Logger log = LoggerFactory.getLogger(RoleNeedsAbilityResource.class);

    private static final String ENTITY_NAME = "roleNeedsAbility";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleNeedsAbilityRepository roleNeedsAbilityRepository;

    public RoleNeedsAbilityResource(RoleNeedsAbilityRepository roleNeedsAbilityRepository) {
        this.roleNeedsAbilityRepository = roleNeedsAbilityRepository;
    }

    /**
     * {@code POST  /role-needs-abilities} : Create a new roleNeedsAbility.
     *
     * @param roleNeedsAbility the roleNeedsAbility to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleNeedsAbility, or with status {@code 400 (Bad Request)} if the roleNeedsAbility has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/role-needs-abilities")
    public ResponseEntity<RoleNeedsAbility> createRoleNeedsAbility(@RequestBody RoleNeedsAbility roleNeedsAbility) throws URISyntaxException {
        log.debug("REST request to save RoleNeedsAbility : {}", roleNeedsAbility);
        if (roleNeedsAbility.getId() != null) {
            throw new BadRequestAlertException("A new roleNeedsAbility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoleNeedsAbility result = roleNeedsAbilityRepository.save(roleNeedsAbility);
        return ResponseEntity.created(new URI("/api/role-needs-abilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /role-needs-abilities} : Updates an existing roleNeedsAbility.
     *
     * @param roleNeedsAbility the roleNeedsAbility to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleNeedsAbility,
     * or with status {@code 400 (Bad Request)} if the roleNeedsAbility is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleNeedsAbility couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/role-needs-abilities")
    public ResponseEntity<RoleNeedsAbility> updateRoleNeedsAbility(@RequestBody RoleNeedsAbility roleNeedsAbility) throws URISyntaxException {
        log.debug("REST request to update RoleNeedsAbility : {}", roleNeedsAbility);
        if (roleNeedsAbility.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoleNeedsAbility result = roleNeedsAbilityRepository.save(roleNeedsAbility);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleNeedsAbility.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /role-needs-abilities} : get all the roleNeedsAbilities.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleNeedsAbilities in body.
     */
    @GetMapping("/role-needs-abilities")
    public List<RoleNeedsAbility> getAllRoleNeedsAbilities() {
        log.debug("REST request to get all RoleNeedsAbilities");
        return roleNeedsAbilityRepository.findAll();
    }

    /**
     * {@code GET  /role-needs-abilities/:id} : get the "id" roleNeedsAbility.
     *
     * @param id the id of the roleNeedsAbility to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleNeedsAbility, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role-needs-abilities/{id}")
    public ResponseEntity<RoleNeedsAbility> getRoleNeedsAbility(@PathVariable Long id) {
        log.debug("REST request to get RoleNeedsAbility : {}", id);
        Optional<RoleNeedsAbility> roleNeedsAbility = roleNeedsAbilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(roleNeedsAbility);
    }

    /**
     * {@code DELETE  /role-needs-abilities/:id} : delete the "id" roleNeedsAbility.
     *
     * @param id the id of the roleNeedsAbility to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/role-needs-abilities/{id}")
    public ResponseEntity<Void> deleteRoleNeedsAbility(@PathVariable Long id) {
        log.debug("REST request to delete RoleNeedsAbility : {}", id);
        roleNeedsAbilityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
