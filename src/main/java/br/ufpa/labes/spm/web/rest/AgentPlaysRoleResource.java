package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.repository.AgentPlaysRoleRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AgentPlaysRole}.
 */
@RestController
@RequestMapping("/api")
public class AgentPlaysRoleResource {

    private final Logger log = LoggerFactory.getLogger(AgentPlaysRoleResource.class);

    private static final String ENTITY_NAME = "agentPlaysRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentPlaysRoleRepository agentPlaysRoleRepository;

    public AgentPlaysRoleResource(AgentPlaysRoleRepository agentPlaysRoleRepository) {
        this.agentPlaysRoleRepository = agentPlaysRoleRepository;
    }

    /**
     * {@code POST  /agent-plays-roles} : Create a new agentPlaysRole.
     *
     * @param agentPlaysRole the agentPlaysRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentPlaysRole, or with status {@code 400 (Bad Request)} if the agentPlaysRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-plays-roles")
    public ResponseEntity<AgentPlaysRole> createAgentPlaysRole(@RequestBody AgentPlaysRole agentPlaysRole) throws URISyntaxException {
        log.debug("REST request to save AgentPlaysRole : {}", agentPlaysRole);
        if (agentPlaysRole.getId() != null) {
            throw new BadRequestAlertException("A new agentPlaysRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentPlaysRole result = agentPlaysRoleRepository.save(agentPlaysRole);
        return ResponseEntity.created(new URI("/api/agent-plays-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-plays-roles} : Updates an existing agentPlaysRole.
     *
     * @param agentPlaysRole the agentPlaysRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentPlaysRole,
     * or with status {@code 400 (Bad Request)} if the agentPlaysRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentPlaysRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-plays-roles")
    public ResponseEntity<AgentPlaysRole> updateAgentPlaysRole(@RequestBody AgentPlaysRole agentPlaysRole) throws URISyntaxException {
        log.debug("REST request to update AgentPlaysRole : {}", agentPlaysRole);
        if (agentPlaysRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgentPlaysRole result = agentPlaysRoleRepository.save(agentPlaysRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentPlaysRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agent-plays-roles} : get all the agentPlaysRoles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentPlaysRoles in body.
     */
    @GetMapping("/agent-plays-roles")
    public List<AgentPlaysRole> getAllAgentPlaysRoles() {
        log.debug("REST request to get all AgentPlaysRoles");
        return agentPlaysRoleRepository.findAll();
    }

    /**
     * {@code GET  /agent-plays-roles/:id} : get the "id" agentPlaysRole.
     *
     * @param id the id of the agentPlaysRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentPlaysRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-plays-roles/{id}")
    public ResponseEntity<AgentPlaysRole> getAgentPlaysRole(@PathVariable Long id) {
        log.debug("REST request to get AgentPlaysRole : {}", id);
        Optional<AgentPlaysRole> agentPlaysRole = agentPlaysRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agentPlaysRole);
    }

    /**
     * {@code DELETE  /agent-plays-roles/:id} : delete the "id" agentPlaysRole.
     *
     * @param id the id of the agentPlaysRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-plays-roles/{id}")
    public ResponseEntity<Void> deleteAgentPlaysRole(@PathVariable Long id) {
        log.debug("REST request to delete AgentPlaysRole : {}", id);
        agentPlaysRoleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
