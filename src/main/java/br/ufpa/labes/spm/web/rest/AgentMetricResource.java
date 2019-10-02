package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentMetric;
import br.ufpa.labes.spm.repository.AgentMetricRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentMetric}. */
@RestController
@RequestMapping("/api")
public class AgentMetricResource {

  private final Logger log = LoggerFactory.getLogger(AgentMetricResource.class);

  private static final String ENTITY_NAME = "agentMetric";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentMetricRepository agentMetricRepository;

  public AgentMetricResource(AgentMetricRepository agentMetricRepository) {
    this.agentMetricRepository = agentMetricRepository;
  }

  /**
   * {@code POST /agent-metrics} : Create a new agentMetric.
   *
   * @param agentMetric the agentMetric to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentMetric, or with status {@code 400 (Bad Request)} if the agentMetric has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-metrics")
  public ResponseEntity<AgentMetric> createAgentMetric(@RequestBody AgentMetric agentMetric)
      throws URISyntaxException {
    log.debug("REST request to save AgentMetric : {}", agentMetric);
    if (agentMetric.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentMetric cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentMetric result = agentMetricRepository.save(agentMetric);
    return ResponseEntity.created(new URI("/api/agent-metrics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-metrics} : Updates an existing agentMetric.
   *
   * @param agentMetric the agentMetric to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentMetric, or with status {@code 400 (Bad Request)} if the agentMetric is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the agentMetric couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-metrics")
  public ResponseEntity<AgentMetric> updateAgentMetric(@RequestBody AgentMetric agentMetric)
      throws URISyntaxException {
    log.debug("REST request to update AgentMetric : {}", agentMetric);
    if (agentMetric.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentMetric result = agentMetricRepository.save(agentMetric);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentMetric.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-metrics} : get all the agentMetrics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentMetrics in
   *     body.
   */
  @GetMapping("/agent-metrics")
  public List<AgentMetric> getAllAgentMetrics() {
    log.debug("REST request to get all AgentMetrics");
    return agentMetricRepository.findAll();
  }

  /**
   * {@code GET /agent-metrics/:id} : get the "id" agentMetric.
   *
   * @param id the id of the agentMetric to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentMetric,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-metrics/{id}")
  public ResponseEntity<AgentMetric> getAgentMetric(@PathVariable Long id) {
    log.debug("REST request to get AgentMetric : {}", id);
    Optional<AgentMetric> agentMetric = agentMetricRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(agentMetric);
  }

  /**
   * {@code DELETE /agent-metrics/:id} : delete the "id" agentMetric.
   *
   * @param id the id of the agentMetric to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-metrics/{id}")
  public ResponseEntity<Void> deleteAgentMetric(@PathVariable Long id) {
    log.debug("REST request to delete AgentMetric : {}", id);
    agentMetricRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
