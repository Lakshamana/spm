package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentEstimation;
import br.ufpa.labes.spm.repository.AgentEstimationRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentEstimation}. */
@RestController
@RequestMapping("/api")
public class AgentEstimationResource {

  private final Logger log = LoggerFactory.getLogger(AgentEstimationResource.class);

  private static final String ENTITY_NAME = "agentEstimation";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentEstimationRepository agentEstimationRepository;

  public AgentEstimationResource(AgentEstimationRepository agentEstimationRepository) {
    this.agentEstimationRepository = agentEstimationRepository;
  }

  /**
   * {@code POST /agent-estimations} : Create a new agentEstimation.
   *
   * @param agentEstimation the agentEstimation to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentEstimation, or with status {@code 400 (Bad Request)} if the agentEstimation has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-estimations")
  public ResponseEntity<AgentEstimation> createAgentEstimation(
      @RequestBody AgentEstimation agentEstimation) throws URISyntaxException {
    log.debug("REST request to save AgentEstimation : {}", agentEstimation);
    if (agentEstimation.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentEstimation cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentEstimation result = agentEstimationRepository.save(agentEstimation);
    return ResponseEntity.created(new URI("/api/agent-estimations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-estimations} : Updates an existing agentEstimation.
   *
   * @param agentEstimation the agentEstimation to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentEstimation, or with status {@code 400 (Bad Request)} if the agentEstimation is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the agentEstimation couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-estimations")
  public ResponseEntity<AgentEstimation> updateAgentEstimation(
      @RequestBody AgentEstimation agentEstimation) throws URISyntaxException {
    log.debug("REST request to update AgentEstimation : {}", agentEstimation);
    if (agentEstimation.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentEstimation result = agentEstimationRepository.save(agentEstimation);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentEstimation.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-estimations} : get all the agentEstimations.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     agentEstimations in body.
   */
  @GetMapping("/agent-estimations")
  public List<AgentEstimation> getAllAgentEstimations() {
    log.debug("REST request to get all AgentEstimations");
    return agentEstimationRepository.findAll();
  }

  /**
   * {@code GET /agent-estimations/:id} : get the "id" agentEstimation.
   *
   * @param id the id of the agentEstimation to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentEstimation, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-estimations/{id}")
  public ResponseEntity<AgentEstimation> getAgentEstimation(@PathVariable Long id) {
    log.debug("REST request to get AgentEstimation : {}", id);
    Optional<AgentEstimation> agentEstimation = agentEstimationRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(agentEstimation);
  }

  /**
   * {@code DELETE /agent-estimations/:id} : delete the "id" agentEstimation.
   *
   * @param id the id of the agentEstimation to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-estimations/{id}")
  public ResponseEntity<Void> deleteAgentEstimation(@PathVariable Long id) {
    log.debug("REST request to delete AgentEstimation : {}", id);
    agentEstimationRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
