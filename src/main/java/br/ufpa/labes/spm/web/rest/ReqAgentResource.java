package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.repository.ReqAgentRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ReqAgent}. */
@RestController
@RequestMapping("/api")
public class ReqAgentResource {

  private final Logger log = LoggerFactory.getLogger(ReqAgentResource.class);

  private static final String ENTITY_NAME = "reqAgent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ReqAgentRepository reqAgentRepository;

  public ReqAgentResource(ReqAgentRepository reqAgentRepository) {
    this.reqAgentRepository = reqAgentRepository;
  }

  /**
   * {@code POST /req-agents} : Create a new reqAgent.
   *
   * @param reqAgent the reqAgent to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     reqAgent, or with status {@code 400 (Bad Request)} if the reqAgent has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/req-agents")
  public ResponseEntity<ReqAgent> createReqAgent(@RequestBody ReqAgent reqAgent)
      throws URISyntaxException {
    log.debug("REST request to save ReqAgent : {}", reqAgent);
    if (reqAgent.getId() != null) {
      throw new BadRequestAlertException(
          "A new reqAgent cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ReqAgent result = reqAgentRepository.save(reqAgent);
    return ResponseEntity.created(new URI("/api/req-agents/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /req-agents} : Updates an existing reqAgent.
   *
   * @param reqAgent the reqAgent to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     reqAgent, or with status {@code 400 (Bad Request)} if the reqAgent is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the reqAgent couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/req-agents")
  public ResponseEntity<ReqAgent> updateReqAgent(@RequestBody ReqAgent reqAgent)
      throws URISyntaxException {
    log.debug("REST request to update ReqAgent : {}", reqAgent);
    if (reqAgent.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ReqAgent result = reqAgentRepository.save(reqAgent);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, reqAgent.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /req-agents} : get all the reqAgents.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reqAgents in
   *     body.
   */
  @GetMapping("/req-agents")
  public List<ReqAgent> getAllReqAgents() {
    log.debug("REST request to get all ReqAgents");
    return reqAgentRepository.findAll();
  }

  /**
   * {@code GET /req-agents/:id} : get the "id" reqAgent.
   *
   * @param id the id of the reqAgent to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reqAgent, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/req-agents/{id}")
  public ResponseEntity<ReqAgent> getReqAgent(@PathVariable Long id) {
    log.debug("REST request to get ReqAgent : {}", id);
    Optional<ReqAgent> reqAgent = reqAgentRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(reqAgent);
  }

  /**
   * {@code DELETE /req-agents/:id} : delete the "id" reqAgent.
   *
   * @param id the id of the reqAgent to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/req-agents/{id}")
  public ResponseEntity<Void> deleteReqAgent(@PathVariable Long id) {
    log.debug("REST request to delete ReqAgent : {}", id);
    reqAgentRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
