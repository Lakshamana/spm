package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentPlaysRoleService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentPlaysRoleDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentPlaysRole}. */
@RestController
@RequestMapping("/api")
public class AgentPlaysRoleResource {

  private final Logger log = LoggerFactory.getLogger(AgentPlaysRoleResource.class);

  private static final String ENTITY_NAME = "agentPlaysRole";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentPlaysRoleService agentPlaysRoleService;

  public AgentPlaysRoleResource(AgentPlaysRoleService agentPlaysRoleService) {
    this.agentPlaysRoleService = agentPlaysRoleService;
  }

  /**
   * {@code POST /agent-plays-roles} : Create a new agentPlaysRole.
   *
   * @param agentPlaysRoleDTO the agentPlaysRoleDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentPlaysRoleDTO, or with status {@code 400 (Bad Request)} if the agentPlaysRole has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-plays-roles")
  public ResponseEntity<AgentPlaysRoleDTO> createAgentPlaysRole(
      @RequestBody AgentPlaysRoleDTO agentPlaysRoleDTO) throws URISyntaxException {
    log.debug("REST request to save AgentPlaysRole : {}", agentPlaysRoleDTO);
    if (agentPlaysRoleDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentPlaysRole cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentPlaysRoleDTO result = agentPlaysRoleService.save(agentPlaysRoleDTO);
    return ResponseEntity.created(new URI("/api/agent-plays-roles/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-plays-roles} : Updates an existing agentPlaysRole.
   *
   * @param agentPlaysRoleDTO the agentPlaysRoleDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentPlaysRoleDTO, or with status {@code 400 (Bad Request)} if the agentPlaysRoleDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the agentPlaysRoleDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-plays-roles")
  public ResponseEntity<AgentPlaysRoleDTO> updateAgentPlaysRole(
      @RequestBody AgentPlaysRoleDTO agentPlaysRoleDTO) throws URISyntaxException {
    log.debug("REST request to update AgentPlaysRole : {}", agentPlaysRoleDTO);
    if (agentPlaysRoleDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentPlaysRoleDTO result = agentPlaysRoleService.save(agentPlaysRoleDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentPlaysRoleDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-plays-roles} : get all the agentPlaysRoles.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentPlaysRoles
   *     in body.
   */
  @GetMapping("/agent-plays-roles")
  public List<AgentPlaysRoleDTO> getAllAgentPlaysRoles() {
    log.debug("REST request to get all AgentPlaysRoles");
    return agentPlaysRoleService.findAll();
  }

  /**
   * {@code GET /agent-plays-roles/:id} : get the "id" agentPlaysRole.
   *
   * @param id the id of the agentPlaysRoleDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentPlaysRoleDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-plays-roles/{id}")
  public ResponseEntity<AgentPlaysRoleDTO> getAgentPlaysRole(@PathVariable Long id) {
    log.debug("REST request to get AgentPlaysRole : {}", id);
    Optional<AgentPlaysRoleDTO> agentPlaysRoleDTO = agentPlaysRoleService.findOne(id);
    return ResponseUtil.wrapOrNotFound(agentPlaysRoleDTO);
  }

  /**
   * {@code DELETE /agent-plays-roles/:id} : delete the "id" agentPlaysRole.
   *
   * @param id the id of the agentPlaysRoleDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-plays-roles/{id}")
  public ResponseEntity<Void> deleteAgentPlaysRole(@PathVariable Long id) {
    log.debug("REST request to delete AgentPlaysRole : {}", id);
    agentPlaysRoleService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
