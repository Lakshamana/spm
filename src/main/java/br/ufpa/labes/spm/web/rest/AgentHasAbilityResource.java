package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentHasAbility;
import br.ufpa.labes.spm.repository.AgentHasAbilityRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentHasAbility}. */
@RestController
@RequestMapping("/api")
public class AgentHasAbilityResource {

  private final Logger log = LoggerFactory.getLogger(AgentHasAbilityResource.class);

  private static final String ENTITY_NAME = "agentHasAbility";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentHasAbilityRepository agentHasAbilityRepository;

  public AgentHasAbilityResource(AgentHasAbilityRepository agentHasAbilityRepository) {
    this.agentHasAbilityRepository = agentHasAbilityRepository;
  }

  /**
   * {@code POST /agent-has-abilities} : Create a new agentHasAbility.
   *
   * @param agentHasAbility the agentHasAbility to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentHasAbility, or with status {@code 400 (Bad Request)} if the agentHasAbility has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-has-abilities")
  public ResponseEntity<AgentHasAbility> createAgentHasAbility(
      @RequestBody AgentHasAbility agentHasAbility) throws URISyntaxException {
    log.debug("REST request to save AgentHasAbility : {}", agentHasAbility);
    if (agentHasAbility.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentHasAbility cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentHasAbility result = agentHasAbilityRepository.save(agentHasAbility);
    return ResponseEntity.created(new URI("/api/agent-has-abilities/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-has-abilities} : Updates an existing agentHasAbility.
   *
   * @param agentHasAbility the agentHasAbility to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentHasAbility, or with status {@code 400 (Bad Request)} if the agentHasAbility is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the agentHasAbility couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-has-abilities")
  public ResponseEntity<AgentHasAbility> updateAgentHasAbility(
      @RequestBody AgentHasAbility agentHasAbility) throws URISyntaxException {
    log.debug("REST request to update AgentHasAbility : {}", agentHasAbility);
    if (agentHasAbility.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentHasAbility result = agentHasAbilityRepository.save(agentHasAbility);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentHasAbility.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-has-abilities} : get all the agentHasAbilities.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     agentHasAbilities in body.
   */
  @GetMapping("/agent-has-abilities")
  public List<AgentHasAbility> getAllAgentHasAbilities() {
    log.debug("REST request to get all AgentHasAbilities");
    return agentHasAbilityRepository.findAll();
  }

  /**
   * {@code GET /agent-has-abilities/:id} : get the "id" agentHasAbility.
   *
   * @param id the id of the agentHasAbility to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentHasAbility, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-has-abilities/{id}")
  public ResponseEntity<AgentHasAbility> getAgentHasAbility(@PathVariable Long id) {
    log.debug("REST request to get AgentHasAbility : {}", id);
    Optional<AgentHasAbility> agentHasAbility = agentHasAbilityRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(agentHasAbility);
  }

  /**
   * {@code DELETE /agent-has-abilities/:id} : delete the "id" agentHasAbility.
   *
   * @param id the id of the agentHasAbility to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-has-abilities/{id}")
  public ResponseEntity<Void> deleteAgentHasAbility(@PathVariable Long id) {
    log.debug("REST request to delete AgentHasAbility : {}", id);
    agentHasAbilityRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
