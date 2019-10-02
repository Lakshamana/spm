package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentAffinityAgent;
import br.ufpa.labes.spm.repository.AgentAffinityAgentRepository;
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

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AgentAffinityAgent}.
 */
@RestController
@RequestMapping("/api")
public class AgentAffinityAgentResource {

    private final Logger log = LoggerFactory.getLogger(AgentAffinityAgentResource.class);

    private static final String ENTITY_NAME = "agentAffinityAgent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentAffinityAgentRepository agentAffinityAgentRepository;

    public AgentAffinityAgentResource(AgentAffinityAgentRepository agentAffinityAgentRepository) {
        this.agentAffinityAgentRepository = agentAffinityAgentRepository;
    }

    /**
     * {@code POST  /agent-affinity-agents} : Create a new agentAffinityAgent.
     *
     * @param agentAffinityAgent the agentAffinityAgent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentAffinityAgent, or with status {@code 400 (Bad Request)} if the agentAffinityAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-affinity-agents")
    public ResponseEntity<AgentAffinityAgent> createAgentAffinityAgent(@RequestBody AgentAffinityAgent agentAffinityAgent) throws URISyntaxException {
        log.debug("REST request to save AgentAffinityAgent : {}", agentAffinityAgent);
        if (agentAffinityAgent.getId() != null) {
            throw new BadRequestAlertException("A new agentAffinityAgent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentAffinityAgent result = agentAffinityAgentRepository.save(agentAffinityAgent);
        return ResponseEntity.created(new URI("/api/agent-affinity-agents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-affinity-agents} : Updates an existing agentAffinityAgent.
     *
     * @param agentAffinityAgent the agentAffinityAgent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentAffinityAgent,
     * or with status {@code 400 (Bad Request)} if the agentAffinityAgent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentAffinityAgent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-affinity-agents")
    public ResponseEntity<AgentAffinityAgent> updateAgentAffinityAgent(@RequestBody AgentAffinityAgent agentAffinityAgent) throws URISyntaxException {
        log.debug("REST request to update AgentAffinityAgent : {}", agentAffinityAgent);
        if (agentAffinityAgent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgentAffinityAgent result = agentAffinityAgentRepository.save(agentAffinityAgent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentAffinityAgent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agent-affinity-agents} : get all the agentAffinityAgents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentAffinityAgents in body.
     */
    @GetMapping("/agent-affinity-agents")
    public List<AgentAffinityAgent> getAllAgentAffinityAgents() {
        log.debug("REST request to get all AgentAffinityAgents");
        return agentAffinityAgentRepository.findAll();
    }

    /**
     * {@code GET  /agent-affinity-agents/:id} : get the "id" agentAffinityAgent.
     *
     * @param id the id of the agentAffinityAgent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentAffinityAgent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-affinity-agents/{id}")
    public ResponseEntity<AgentAffinityAgent> getAgentAffinityAgent(@PathVariable Long id) {
        log.debug("REST request to get AgentAffinityAgent : {}", id);
        Optional<AgentAffinityAgent> agentAffinityAgent = agentAffinityAgentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agentAffinityAgent);
    }

    /**
     * {@code DELETE  /agent-affinity-agents/:id} : delete the "id" agentAffinityAgent.
     *
     * @param id the id of the agentAffinityAgent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-affinity-agents/{id}")
    public ResponseEntity<Void> deleteAgentAffinityAgent(@PathVariable Long id) {
        log.debug("REST request to delete AgentAffinityAgent : {}", id);
        agentAffinityAgentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
