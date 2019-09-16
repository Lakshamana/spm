package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentEstimationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentEstimationDTO;

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

  private final AgentEstimationService agentEstimationService;

  public AgentEstimationResource(AgentEstimationService agentEstimationService) {
    this.agentEstimationService = agentEstimationService;
  }

  /**
   * {@code POST /agent-estimations} : Create a new agentEstimation.
   *
   * @param agentEstimationDTO the agentEstimationDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentEstimationDTO, or with status {@code 400 (Bad Request)} if the agentEstimation has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-estimations")
  public ResponseEntity<AgentEstimationDTO> createAgentEstimation(
      @RequestBody AgentEstimationDTO agentEstimationDTO) throws URISyntaxException {
    log.debug("REST request to save AgentEstimation : {}", agentEstimationDTO);
    if (agentEstimationDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentEstimation cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentEstimationDTO result = agentEstimationService.save(agentEstimationDTO);
    return ResponseEntity.created(new URI("/api/agent-estimations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-estimations} : Updates an existing agentEstimation.
   *
   * @param agentEstimationDTO the agentEstimationDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentEstimationDTO, or with status {@code 400 (Bad Request)} if the agentEstimationDTO is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the agentEstimationDTO
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-estimations")
  public ResponseEntity<AgentEstimationDTO> updateAgentEstimation(
      @RequestBody AgentEstimationDTO agentEstimationDTO) throws URISyntaxException {
    log.debug("REST request to update AgentEstimation : {}", agentEstimationDTO);
    if (agentEstimationDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentEstimationDTO result = agentEstimationService.save(agentEstimationDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentEstimationDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-estimations} : get all the agentEstimations.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     agentEstimations in body.
   */
  @GetMapping("/agent-estimations")
  public List<AgentEstimationDTO> getAllAgentEstimations(
      @RequestParam(required = false) String filter) {
    if ("theestimationsuper-is-null".equals(filter)) {
      log.debug("REST request to get all AgentEstimations where theEstimationSuper is null");
      return agentEstimationService.findAllWhereTheEstimationSuperIsNull();
    }
    log.debug("REST request to get all AgentEstimations");
    return agentEstimationService.findAll();
  }

  /**
   * {@code GET /agent-estimations/:id} : get the "id" agentEstimation.
   *
   * @param id the id of the agentEstimationDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentEstimationDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-estimations/{id}")
  public ResponseEntity<AgentEstimationDTO> getAgentEstimation(@PathVariable Long id) {
    log.debug("REST request to get AgentEstimation : {}", id);
    Optional<AgentEstimationDTO> agentEstimationDTO = agentEstimationService.findOne(id);
    return ResponseUtil.wrapOrNotFound(agentEstimationDTO);
  }

  /**
   * {@code DELETE /agent-estimations/:id} : delete the "id" agentEstimation.
   *
   * @param id the id of the agentEstimationDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-estimations/{id}")
  public ResponseEntity<Void> deleteAgentEstimation(@PathVariable Long id) {
    log.debug("REST request to delete AgentEstimation : {}", id);
    agentEstimationService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
