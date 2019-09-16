package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentHasAbilityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentHasAbilityDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentHasAbility}. */
@RestController
@RequestMapping("/api")
public class AgentHasAbilityResource {

  private final Logger log = LoggerFactory.getLogger(AgentHasAbilityResource.class);

  private static final String ENTITY_NAME = "agentHasAbility";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentHasAbilityService agentHasAbilityService;

  public AgentHasAbilityResource(AgentHasAbilityService agentHasAbilityService) {
    this.agentHasAbilityService = agentHasAbilityService;
  }

  /**
   * {@code POST /agent-has-abilities} : Create a new agentHasAbility.
   *
   * @param agentHasAbilityDTO the agentHasAbilityDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentHasAbilityDTO, or with status {@code 400 (Bad Request)} if the agentHasAbility has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-has-abilities")
  public ResponseEntity<AgentHasAbilityDTO> createAgentHasAbility(
      @RequestBody AgentHasAbilityDTO agentHasAbilityDTO) throws URISyntaxException {
    log.debug("REST request to save AgentHasAbility : {}", agentHasAbilityDTO);
    if (agentHasAbilityDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentHasAbility cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentHasAbilityDTO result = agentHasAbilityService.save(agentHasAbilityDTO);
    return ResponseEntity.created(new URI("/api/agent-has-abilities/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-has-abilities} : Updates an existing agentHasAbility.
   *
   * @param agentHasAbilityDTO the agentHasAbilityDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentHasAbilityDTO, or with status {@code 400 (Bad Request)} if the agentHasAbilityDTO is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the agentHasAbilityDTO
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-has-abilities")
  public ResponseEntity<AgentHasAbilityDTO> updateAgentHasAbility(
      @RequestBody AgentHasAbilityDTO agentHasAbilityDTO) throws URISyntaxException {
    log.debug("REST request to update AgentHasAbility : {}", agentHasAbilityDTO);
    if (agentHasAbilityDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentHasAbilityDTO result = agentHasAbilityService.save(agentHasAbilityDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentHasAbilityDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-has-abilities} : get all the agentHasAbilities.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     agentHasAbilities in body.
   */
  @GetMapping("/agent-has-abilities")
  public List<AgentHasAbilityDTO> getAllAgentHasAbilities() {
    log.debug("REST request to get all AgentHasAbilities");
    return agentHasAbilityService.findAll();
  }

  /**
   * {@code GET /agent-has-abilities/:id} : get the "id" agentHasAbility.
   *
   * @param id the id of the agentHasAbilityDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentHasAbilityDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-has-abilities/{id}")
  public ResponseEntity<AgentHasAbilityDTO> getAgentHasAbility(@PathVariable Long id) {
    log.debug("REST request to get AgentHasAbility : {}", id);
    Optional<AgentHasAbilityDTO> agentHasAbilityDTO = agentHasAbilityService.findOne(id);
    return ResponseUtil.wrapOrNotFound(agentHasAbilityDTO);
  }

  /**
   * {@code DELETE /agent-has-abilities/:id} : delete the "id" agentHasAbility.
   *
   * @param id the id of the agentHasAbilityDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-has-abilities/{id}")
  public ResponseEntity<Void> deleteAgentHasAbility(@PathVariable Long id) {
    log.debug("REST request to delete AgentHasAbility : {}", id);
    agentHasAbilityService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
