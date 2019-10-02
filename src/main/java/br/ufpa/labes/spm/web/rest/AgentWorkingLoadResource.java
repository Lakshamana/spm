package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentWorkingLoad;
import br.ufpa.labes.spm.repository.AgentWorkingLoadRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentWorkingLoad}. */
@RestController
@RequestMapping("/api")
public class AgentWorkingLoadResource {

  private final Logger log = LoggerFactory.getLogger(AgentWorkingLoadResource.class);

  private static final String ENTITY_NAME = "agentWorkingLoad";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentWorkingLoadRepository agentWorkingLoadRepository;

  public AgentWorkingLoadResource(AgentWorkingLoadRepository agentWorkingLoadRepository) {
    this.agentWorkingLoadRepository = agentWorkingLoadRepository;
  }

  /**
   * {@code POST /agent-working-loads} : Create a new agentWorkingLoad.
   *
   * @param agentWorkingLoad the agentWorkingLoad to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentWorkingLoad, or with status {@code 400 (Bad Request)} if the agentWorkingLoad has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-working-loads")
  public ResponseEntity<AgentWorkingLoad> createAgentWorkingLoad(
      @RequestBody AgentWorkingLoad agentWorkingLoad) throws URISyntaxException {
    log.debug("REST request to save AgentWorkingLoad : {}", agentWorkingLoad);
    if (agentWorkingLoad.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentWorkingLoad cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentWorkingLoad result = agentWorkingLoadRepository.save(agentWorkingLoad);
    return ResponseEntity.created(new URI("/api/agent-working-loads/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-working-loads} : Updates an existing agentWorkingLoad.
   *
   * @param agentWorkingLoad the agentWorkingLoad to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentWorkingLoad, or with status {@code 400 (Bad Request)} if the agentWorkingLoad is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the agentWorkingLoad couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-working-loads")
  public ResponseEntity<AgentWorkingLoad> updateAgentWorkingLoad(
      @RequestBody AgentWorkingLoad agentWorkingLoad) throws URISyntaxException {
    log.debug("REST request to update AgentWorkingLoad : {}", agentWorkingLoad);
    if (agentWorkingLoad.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentWorkingLoad result = agentWorkingLoadRepository.save(agentWorkingLoad);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentWorkingLoad.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-working-loads} : get all the agentWorkingLoads.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     agentWorkingLoads in body.
   */
  @GetMapping("/agent-working-loads")
  public List<AgentWorkingLoad> getAllAgentWorkingLoads() {
    log.debug("REST request to get all AgentWorkingLoads");
    return agentWorkingLoadRepository.findAll();
  }

  /**
   * {@code GET /agent-working-loads/:id} : get the "id" agentWorkingLoad.
   *
   * @param id the id of the agentWorkingLoad to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentWorkingLoad, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-working-loads/{id}")
  public ResponseEntity<AgentWorkingLoad> getAgentWorkingLoad(@PathVariable Long id) {
    log.debug("REST request to get AgentWorkingLoad : {}", id);
    Optional<AgentWorkingLoad> agentWorkingLoad = agentWorkingLoadRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(agentWorkingLoad);
  }

  /**
   * {@code DELETE /agent-working-loads/:id} : delete the "id" agentWorkingLoad.
   *
   * @param id the id of the agentWorkingLoad to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-working-loads/{id}")
  public ResponseEntity<Void> deleteAgentWorkingLoad(@PathVariable Long id) {
    log.debug("REST request to delete AgentWorkingLoad : {}", id);
    agentWorkingLoadRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
