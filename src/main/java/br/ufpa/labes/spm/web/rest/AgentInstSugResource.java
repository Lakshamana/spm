package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentInstSugService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentInstSugDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AgentInstSug}. */
@RestController
@RequestMapping("/api")
public class AgentInstSugResource {

  private final Logger log = LoggerFactory.getLogger(AgentInstSugResource.class);

  private static final String ENTITY_NAME = "agentInstSug";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AgentInstSugService agentInstSugService;

  public AgentInstSugResource(AgentInstSugService agentInstSugService) {
    this.agentInstSugService = agentInstSugService;
  }

  /**
   * {@code POST /agent-inst-sugs} : Create a new agentInstSug.
   *
   * @param agentInstSugDTO the agentInstSugDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     agentInstSugDTO, or with status {@code 400 (Bad Request)} if the agentInstSug has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/agent-inst-sugs")
  public ResponseEntity<AgentInstSugDTO> createAgentInstSug(
      @RequestBody AgentInstSugDTO agentInstSugDTO) throws URISyntaxException {
    log.debug("REST request to save AgentInstSug : {}", agentInstSugDTO);
    if (agentInstSugDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new agentInstSug cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AgentInstSugDTO result = agentInstSugService.save(agentInstSugDTO);
    return ResponseEntity.created(new URI("/api/agent-inst-sugs/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /agent-inst-sugs} : Updates an existing agentInstSug.
   *
   * @param agentInstSugDTO the agentInstSugDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     agentInstSugDTO, or with status {@code 400 (Bad Request)} if the agentInstSugDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the agentInstSugDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/agent-inst-sugs")
  public ResponseEntity<AgentInstSugDTO> updateAgentInstSug(
      @RequestBody AgentInstSugDTO agentInstSugDTO) throws URISyntaxException {
    log.debug("REST request to update AgentInstSug : {}", agentInstSugDTO);
    if (agentInstSugDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AgentInstSugDTO result = agentInstSugService.save(agentInstSugDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, agentInstSugDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /agent-inst-sugs} : get all the agentInstSugs.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentInstSugs
   *     in body.
   */
  @GetMapping("/agent-inst-sugs")
  public List<AgentInstSugDTO> getAllAgentInstSugs(@RequestParam(required = false) String filter) {
    if ("thepeopleinstsugsuper-is-null".equals(filter)) {
      log.debug("REST request to get all AgentInstSugs where thePeopleInstSugSuper is null");
      return agentInstSugService.findAllWhereThePeopleInstSugSuperIsNull();
    }
    log.debug("REST request to get all AgentInstSugs");
    return agentInstSugService.findAll();
  }

  /**
   * {@code GET /agent-inst-sugs/:id} : get the "id" agentInstSug.
   *
   * @param id the id of the agentInstSugDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     agentInstSugDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/agent-inst-sugs/{id}")
  public ResponseEntity<AgentInstSugDTO> getAgentInstSug(@PathVariable Long id) {
    log.debug("REST request to get AgentInstSug : {}", id);
    Optional<AgentInstSugDTO> agentInstSugDTO = agentInstSugService.findOne(id);
    return ResponseUtil.wrapOrNotFound(agentInstSugDTO);
  }

  /**
   * {@code DELETE /agent-inst-sugs/:id} : delete the "id" agentInstSug.
   *
   * @param id the id of the agentInstSugDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/agent-inst-sugs/{id}")
  public ResponseEntity<Void> deleteAgentInstSug(@PathVariable Long id) {
    log.debug("REST request to delete AgentInstSug : {}", id);
    agentInstSugService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
