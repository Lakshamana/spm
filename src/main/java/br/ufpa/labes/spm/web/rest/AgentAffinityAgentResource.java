package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentAffinityAgentService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentAffinityAgentDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentAffinityAgent}. */
@RestController
@RequestMapping("/api")
public class AgentAffinityAgentResource {

  private final Logger log = LoggerFactory.getLogger(AgentAffinityAgentResource.class);

  private static final String ENTITY_NAME = "agentAffinityAgent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentAffinityAgentService agentAffinityAgentService;

  public AgentAffinityAgentResource(AgentAffinityAgentService agentAffinityAgentService) {
    this.agentAffinityAgentService = agentAffinityAgentService;
  }

  /**
   * {@code POST /agent-affinity-agents} : Create a new agentAffinityAgent.
   *
   * @param agentAffinityAgentDTO the agentAffinityAgentDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentAffinityAgentDTO, or with status {@code 400 (Bad Request)} if the agentAffinityAgent
   *     has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-affinity-agents")
  public ResponseEntity<AgentAffinityAgentDTO> createAgentAffinityAgent(
      @RequestBody AgentAffinityAgentDTO agentAffinityAgentDTO) throws URISyntaxException {
    log.debug("REST request to save AgentAffinityAgent : {}", agentAffinityAgentDTO);
    if (agentAffinityAgentDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentAffinityAgent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentAffinityAgentDTO result = agentAffinityAgentService.save(agentAffinityAgentDTO);
    return ResponseEntity.created(new URI("/api/agent-affinity-agents/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-affinity-agents} : Updates an existing agentAffinityAgent.
   *
   * @param agentAffinityAgentDTO the agentAffinityAgentDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentAffinityAgentDTO, or with status {@code 400 (Bad Request)} if the
   *     agentAffinityAgentDTO is not valid, or with status {@code 500 (Internal Server Error)} if
   *     the agentAffinityAgentDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-affinity-agents")
  public ResponseEntity<AgentAffinityAgentDTO> updateAgentAffinityAgent(
      @RequestBody AgentAffinityAgentDTO agentAffinityAgentDTO) throws URISyntaxException {
    log.debug("REST request to update AgentAffinityAgent : {}", agentAffinityAgentDTO);
    if (agentAffinityAgentDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentAffinityAgentDTO result = agentAffinityAgentService.save(agentAffinityAgentDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentAffinityAgentDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-affinity-agents} : get all the agentAffinityAgents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     agentAffinityAgents in body.
   */
  @GetMapping("/agent-affinity-agents")
  public List<AgentAffinityAgentDTO> getAllAgentAffinityAgents() {
    log.debug("REST request to get all AgentAffinityAgents");
    return agentAffinityAgentService.findAll();
  }

  /**
   * {@code GET /agent-affinity-agents/:id} : get the "id" agentAffinityAgent.
   *
   * @param id the id of the agentAffinityAgentDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentAffinityAgentDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-affinity-agents/{id}")
  public ResponseEntity<AgentAffinityAgentDTO> getAgentAffinityAgent(@PathVariable Long id) {
    log.debug("REST request to get AgentAffinityAgent : {}", id);
    Optional<AgentAffinityAgentDTO> agentAffinityAgentDTO = agentAffinityAgentService.findOne(id);
    return ResponseUtil.wrapOrNotFound(agentAffinityAgentDTO);
  }

  /**
   * {@code DELETE /agent-affinity-agents/:id} : delete the "id" agentAffinityAgent.
   *
   * @param id the id of the agentAffinityAgentDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-affinity-agents/{id}")
  public ResponseEntity<Void> deleteAgentAffinityAgent(@PathVariable Long id) {
    log.debug("REST request to delete AgentAffinityAgent : {}", id);
    agentAffinityAgentService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
